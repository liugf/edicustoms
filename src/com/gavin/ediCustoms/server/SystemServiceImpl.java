package com.gavin.ediCustoms.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gavin.ediCustoms.client.service.SystemService;
import com.gavin.ediCustoms.entity.edi.AccountRecord;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.ForeignEnterprise;
import com.gavin.ediCustoms.entity.edi.Permission;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.entity.edi.RunRelation;
import com.gavin.ediCustoms.entity.edi.SendRelation;
import com.gavin.ediCustoms.entity.edi.admin.Administrator;
import com.gavin.ediCustoms.entity.edi.admin.CompanyMessage;
import com.gavin.ediCustoms.entity.share.User;
import com.gavin.ediCustoms.server.config.ApplicationConfiguration;
import com.gavin.ediCustoms.server.dao.AccountRecordDao;
import com.gavin.ediCustoms.server.dao.PermissionDao;
import com.gavin.ediCustoms.server.dao.RunRelationDao;
import com.gavin.ediCustoms.server.dao.SendRelationDao;
import com.gavin.ediCustoms.server.dao.base.BaseDao;
import com.gavin.ediCustoms.server.utils.Encryptor;
import com.gavin.ediCustoms.server.utils.MoneyManager;
import com.gavin.ediCustoms.shared.UserState;
import com.gavin.ediCustoms.shared.UserState.UserType;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SystemServiceImpl extends RemoteServiceServlet implements
		SystemService {

	private WebApplicationContext ctx;
	private ApplicationConfiguration applicationConfiguration;
	private BaseDao<CompanyMessage> companyMessageDao;
	private BaseDao<Administrator> administratorDao;
	private BaseDao<Enterprise> enterpriseDao;
	private BaseDao<ForeignEnterprise> foreignEnterpriseDao;
	private BaseDao<User> userDao;
	private PermissionDao permissionDao;
	private SendRelationDao sendRelationDao;
	private RunRelationDao runRelationDao;
	private AccountRecordDao accountRecordDao;
	
	private MoneyManager moneyManager;
	private Encryptor encryptor;

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws ServletException {
		super.init();
		ctx = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
		if (ctx == null) {
			throw new RuntimeException(
					"Check Your Web.Xml Setting, No Spring Context Configured");
		}
		
		applicationConfiguration=(ApplicationConfiguration) ctx.getBean("applicationConfiguration");
		
		companyMessageDao = (BaseDao<CompanyMessage>) ctx
				.getBean("companyMessageDao");
		administratorDao = (BaseDao<Administrator>) ctx
				.getBean("administratorDao");
		enterpriseDao = (BaseDao<Enterprise>) ctx.getBean("enterpriseDao");
		foreignEnterpriseDao = (BaseDao<ForeignEnterprise>) ctx
				.getBean("foreignEnterpriseDao");
		userDao = (BaseDao<User>) ctx.getBean("userDao");
		permissionDao = (PermissionDao) ctx.getBean("permissionDao");
		sendRelationDao=(SendRelationDao)ctx.getBean("sendRelationDao");
		runRelationDao=(RunRelationDao)ctx.getBean("runRelationDao");
		accountRecordDao=(AccountRecordDao)ctx.getBean("accountRecordDao");
		
		moneyManager = (MoneyManager)ctx.getBean("moneyManager");
		encryptor = (Encryptor) ctx.getBean("encryptor");
	}

	private void setUserState(UserType userType, Long id) {
		this.getThreadLocalRequest().getSession().setAttribute("userState",
				new UserState(userType, id));
		/*request.getSession().setAttribute("userState",
				new UserState(userType, id));*/
	}

	private UserState getUserState() {
		return (UserState)this.getThreadLocalRequest().getSession().getAttribute("userState");
		//return (UserState) request.getSession().getAttribute("userState");
	}

	@Override
	public CompanyMessage getCompanyMessage() {
		if (getUserState() == null) {
			return null;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return null;
		}
		
		return companyMessageDao.list().get(0);
	}

	@Override
	public void updateCompanyMessage(CompanyMessage companyMessage) {
		if (getUserState() == null) {
			return;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return;
		}

		companyMessageDao.update(companyMessage);
	}

	@Override
	public Administrator getAdministrator() {
		if (getUserState() == null) {
			return null;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return null;
		}

		Administrator administrator = getAdmin();
		administrator.setPassword("");
		return administrator;
	}

	private Administrator getAdmin() {
		Administrator administrator;
		List<Administrator> list = administratorDao.list();
		if (list.size() != 0) {
			administrator = administratorDao.list().get(0);
		} else {
			administrator = new Administrator();
			administrator.setAdminName("master");
			administrator.setPassword(encryptor.encrypt("master"));
			administrator.setId(administratorDao.save(administrator));
		}
		return administrator;
	}

	@Override
	public void updateAdministrator(Administrator administrator) {
		if (getUserState() == null) {
			return;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return;
		}

		if (!administrator.getPassword().trim().equals("")) {
			administrator.setPassword(encryptor.encrypt(administrator
					.getPassword()));
			administratorDao.update(administrator);
		}
	}
	
	@Override
	public Enterprise getEnterprise(Long id) {
		if (getUserState() == null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), id);
		if (permission==null) {
			return null;
		}	
		return enterpriseDao.get(id);
	}

	@Override
	public List<Enterprise> listEnterprise() {
		if (getUserState() == null) {
			return null;
		}	
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return null;
		}
		return enterpriseDao.list();
	}

	@Override
	public Long saveEnterprise(Enterprise enterprise) {
		if (getUserState() == null) {
			return null;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return null;
		}

		return (Long) enterpriseDao.save(enterprise);
	}

	@Override
	public void deleteEnterprise(List<Long> ids) {
		if (getUserState() == null) {
			return;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return;
		}

		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			enterpriseDao.delete(id);
		}
	}

	@Override
	public void updateEnterprise(List<Enterprise> enterprises) {
		if (getUserState() == null) {
			return;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return;
		}

		for (Iterator<Enterprise> iterator = enterprises.iterator(); iterator
				.hasNext();) {
			Enterprise enterprise = (Enterprise) iterator.next();
			enterpriseDao.update(enterprise);
		}
	}

	
	@Override
	public ForeignEnterprise getForeignEnterprise(Long id) {
		if (getUserState() == null) {
			return null;
		}
		return foreignEnterpriseDao.get(id);
	}
	
	@Override
	public List<ForeignEnterprise> listForeignEnterprise() {
		if (getUserState() == null) {
			return null;
		}

		return foreignEnterpriseDao.list();
	}

	@Override
	public Long saveForeignEnterprise(ForeignEnterprise foreignEnterprise) {
		if (getUserState() == null) {
			return null;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return null;
		}

		return (Long) foreignEnterpriseDao.save(foreignEnterprise);
	}

	@Override
	public void deleteForeignEnterprise(List<Long> ids) {
		if (getUserState() == null) {
			return;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return;
		}

		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			foreignEnterpriseDao.delete(id);
		}
	}

	@Override
	public void updateForeignEnterprise(List<ForeignEnterprise> enterprises) {
		if (getUserState() == null) {
			return;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return;
		}

		for (Iterator<ForeignEnterprise> iterator = enterprises.iterator(); iterator
				.hasNext();) {
			ForeignEnterprise foreignEnterprise = (ForeignEnterprise) iterator
					.next();
			foreignEnterpriseDao.update(foreignEnterprise);
		}
	}

	@Override
	public List<User> listUser() {
		if (getUserState() == null) {
			return null;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return null;
		}

		List<User> list = userDao.list();
		for (Iterator<User> iterator = list.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			user.setPassword("");
		}
		return list;
	}

	@Override
	public Long saveUser(User user, List<Permission> permissions) {
		if (getUserState() == null) {
			return null;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return null;
		}

		user.setPassword(encryptor.encrypt(user.getPassword()));
		Long userId = userDao.save(user);
		for (Iterator<Permission> iterator = permissions.iterator(); iterator
				.hasNext();) {
			Permission permission = (Permission) iterator.next();
			permission.setUserId(userId);
			permissionDao.save(permission);
		}
		return userId;
	}

	@Override
	public void deleteUser(List<Long> ids) {
		if (getUserState() == null) {
			return;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return;
		}

		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			userDao.delete(id);
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("userId", id);
			permissionDao.deleteAnd(map);
		}
	}

	public String updatePassword(String password){
		User user = userDao.get(getUserState().getId());
		if (user == null) {
			return "修改密码失败";
		}
		user.setPassword(encryptor.encrypt(password));
		userDao.update(user);
		return "修改成功";
	}
	
	@Override
	public void updateUser(User user, List<Permission> permissions) {
		if (getUserState() == null) {
			return;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return;
		}

		if (user.getPassword() == null) {
			user.setPassword(userDao.find("id", user.getId()).get(0)
					.getPassword());
		} else if (user.getPassword().trim().equals("")) {
			user.setPassword(userDao.find("id", user.getId()).get(0)
					.getPassword());
		} else {
			user.setPassword(encryptor.encrypt(user.getPassword()));
		}
		userDao.update(user);

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("userId", user.getId());
		permissionDao.deleteAnd(map);

		for (Iterator<Permission> permissionIterator = permissions.iterator(); permissionIterator
				.hasNext();) {
			Permission permission = (Permission) permissionIterator.next();
			permission.setUserId(user.getId());
			permissionDao.save(permission);
		}
	}
	

	@Override
	public List<Permission> listPermission() {
		if (getUserState() == null) {
			return null;
		}
		if (getUserState().getUserType() == UserState.UserType.ADMIN) {
			return permissionDao.list();
		} else {
			return permissionDao.find("userId", getUserState().getId());
		}
	}

	@Override
	public Permission getPermission(Long userId, Long enterpriseId) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("userId", userId);
		map.put("enterpriseId", enterpriseId);
		permissionDao.findAnd(map).get(0);
		List<Permission> list = permissionDao.findAnd(map);
		if (list.size() != 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public String validateUser(String userName, String password,
			String checkCode) {
		if (userName == null) {
			return "用户名不能为空";
		}
		if (password == null) {
			return "密码不能为空";
		}
		
		if (!applicationConfiguration.isDebug()) {
			String expectedCheckCode = (String) this.getThreadLocalRequest().getSession().getAttribute(
					com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
			if (!checkCode.trim().equals(expectedCheckCode)) {
				return "验证码错误！";
			}
		}
		
		 
		List<User> list = userDao.find("userName", userName);
		if (list.size() == 0) {
			return "用户不存在！";
		}
		if (!encryptor.encrypt(password.trim()).equals(
				list.get(0).getPassword())) {
			return "密码错误";
		}
		setUserState(UserState.UserType.USER, list.get(0).getId());
		
		return null;

	}

	@Override
	public String validateAdmin(String adminName, String password,
			String checkCode) {
		if (adminName == null) {
			return "管理员不能为空";
		}
		if (password == null) {
			return "密码不能为空";
		}
		
		if (!applicationConfiguration.isDebug()) {
			String expectedCheckCode = (String) this.getThreadLocalRequest().getSession().getAttribute(
					com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);

			if (!checkCode.trim().equals(expectedCheckCode)) {
				return "验证码错误！";
			}
		}
		
		
		Administrator administrator = getAdmin();
		if (!administrator.getAdminName().equals(adminName.trim())) {
			return "管理员不存在！";
		}
		if (!encryptor.encrypt(password.trim()).equals(
				administrator.getPassword())) {
			return "密码错误";
		}
		setUserState(UserState.UserType.ADMIN, administrator.getId());
		return null;
	}

	@Override
	public List<PermitedEnterprise> listPermitedEnterprise() {
		if (getUserState() == null) {
			return null;
		}	
		List<Enterprise> list = enterpriseDao.list();
		List<Permission> permissions = permissionDao.find("userId", getUserState().getId());
		List<PermitedEnterprise> result = new ArrayList<PermitedEnterprise>();
		PermitedEnterprise permitedEnterprise = null;
		for (Iterator<Enterprise> iterator = list.iterator(); iterator
				.hasNext();) {
			Enterprise enterprise = (Enterprise) iterator.next();
			boolean isContain = false;			
			for (Permission permission : permissions) {
				if (permission.getEnterpriseId().equals(enterprise.getId())){
					permitedEnterprise=new  PermitedEnterprise(enterprise,permission);
					isContain = true;
				}
			}
			if (isContain){
				result.add(permitedEnterprise);
			}
				
		}
		return result;
		
	}

	@Override
	public UserState getState() {
		UserState userState=getUserState();
		if (userState.getUserType()==UserState.UserType.USER) {
			User user=userDao.get(userState.getId());
			if (user==null) {
				return null;
			}
			userState.setName(user.getName());
		}
		return userState;
	}
	
	@Override
	public List<Enterprise> listSendEnterprise(Long enterpriseId) {
		if (getUserState() == null) {
			return null;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			Permission permission=getPermission(getUserState().getId(), enterpriseId);
			if (permission==null) {
				return null;
			}
		}	
		List<SendRelation> sendRelations=sendRelationDao.find("receiveEnterpriseId", enterpriseId);
		List<Enterprise> result=new ArrayList<Enterprise>();
		for(SendRelation sendRelation:sendRelations){
			result.add(enterpriseDao.get(sendRelation.getSendEnterpriseId()));			
		}
		return result;
	}
	
	@Override
	public List<Enterprise> listReceiveEnterprise(Long enterpriseId) {
		if (getUserState() == null) {
			return null;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			Permission permission=getPermission(getUserState().getId(), enterpriseId);
			if (permission==null) {
				return null;
			}
		}	
		List<SendRelation> sendRelations=sendRelationDao.find("sendEnterpriseId", enterpriseId);
		List<Enterprise> result=new ArrayList<Enterprise>();
		for(SendRelation sendRelation:sendRelations){
			result.add(enterpriseDao.get(sendRelation.getReceiveEnterpriseId()));			
		}
		return result;
	}
	
	@Override
	public List<Enterprise> listRunEnterprise(Long enterpriseId) {
		if (getUserState() == null) {
			return null;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			Permission permission=getPermission(getUserState().getId(), enterpriseId);
			if (permission==null) {
				return null;
			}
		}	
		List<RunRelation> runRelations=runRelationDao.find("manufactureEnterpriseId", enterpriseId);
		List<Enterprise> result=new ArrayList<Enterprise>();
		for(RunRelation runRelation:runRelations){
			result.add(enterpriseDao.get(runRelation.getRunEnterpriseId()));			
		}
		return result;
	}
	
	@Override
	public List<Enterprise> listDeclareEnterprise() {
		if (getUserState() == null) {
			return null;
		}		
		return enterpriseDao.find("isDeclare", true);
	}
	
	@Override
	public void updateRelation(Long enterpriseId,
			List<SendRelation> sendRelations, List<RunRelation> runRelations) {
		if (getUserState() == null) {
			return ;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			Permission permission=getPermission(getUserState().getId(), enterpriseId);
			if (permission==null) {
				return ;
			}
		}
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("receiveEnterpriseId", enterpriseId);
		map.put("sendEnterpriseId", enterpriseId);
		sendRelationDao.deleteOr(map);
		for(SendRelation sendRelation:sendRelations){
			sendRelationDao.save(sendRelation);
		}
		
		runRelationDao.delete("manufactureEnterpriseId", enterpriseId);
		for(RunRelation runRelation:runRelations){
			runRelationDao.save(runRelation);
		}		
	}
	
	/**
	 * 充值，如果成功返回余额，失败返回null
	 */
	@Override
	public Double charge(Long enterpriseId, double increase,String note) {
		if (getUserState() == null) {
			return null;
		}
		if (getUserState().getUserType() != UserState.UserType.ADMIN) {
			return null;
		}
		return moneyManager.alter(enterpriseId, getUserState().getId(), increase, note, "admin");
	}

	@Override
	public List<AccountRecord> listAccountRecords(Long enterpriseId) {
		return accountRecordDao.find("enterpriseId", enterpriseId);
	}

	@Override
	public List<AccountRecord> listAccountRecordsByPeriod(Long enterpriseId,
			Date start, Date end) {
		return accountRecordDao.findByPeriod(enterpriseId, start, end);
	}

	

	
	

}
