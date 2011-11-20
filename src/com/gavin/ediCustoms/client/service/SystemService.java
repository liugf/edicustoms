package com.gavin.ediCustoms.client.service;


import java.util.Date;
import java.util.List;

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
import com.gavin.ediCustoms.shared.UserState;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("system")
public interface SystemService extends RemoteService {
	//CompanyMessage
	CompanyMessage getCompanyMessage();
	void updateCompanyMessage(CompanyMessage companyMessage);
	
	//Administrator
	Administrator getAdministrator();
	void updateAdministrator(Administrator administrator);
	
	//Enterprise
	Enterprise getEnterprise(Long id);
	List<Enterprise> listEnterprise();
	Long saveEnterprise(Enterprise enterprise);
	void deleteEnterprise(List<Long> ids);
	void updateEnterprise(List<Enterprise> enterprises);	
	
	//ForeignEnterprise
	ForeignEnterprise getForeignEnterprise(Long id);
	List<ForeignEnterprise> listForeignEnterprise();
	Long saveForeignEnterprise(ForeignEnterprise foreignEnterprise);
	void deleteForeignEnterprise(List<Long> ids);
	void updateForeignEnterprise(List<ForeignEnterprise> foreignEnterprises);
	
	//User
	List<User> listUser();
	Long saveUser(User user,List<Permission> permissions);
	void deleteUser(List<Long> ids);
	void updateUser(User user,List<Permission> permissions);
	String updatePassword(String password);
	
	
	//Permission
	List<Permission> listPermission();
	Permission getPermission(Long userId,Long enterpriseId);
	
	//Login
	String validateUser(String userName,String password,String checkCode);
	String validateAdmin(String adminName,String password,String checkCode);
	
	//PermitedEnterprise
	List<PermitedEnterprise> listPermitedEnterprise();
	
	//userState
	UserState getState();
	
	//sendRelation runRelation declareEnterprise
	List<Enterprise> listSendEnterprise(Long enterpriseId);
	List<Enterprise> listReceiveEnterprise(Long enterpriseId);
	List<Enterprise> listRunEnterprise(Long enterpriseId);
	List<Enterprise> listDeclareEnterprise();	
	void updateRelation(Long enterpriseId,List<SendRelation> sendRelations,List<RunRelation> runRelations);
	
	//充值
	Double charge(Long enterpriseId,double increase,String note);
	List<AccountRecord> listAccountRecords(Long enterpriseId);
	List<AccountRecord> listAccountRecordsByPeriod(Long enterpriseId,Date start,Date end);
	
}
