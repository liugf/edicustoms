package com.gavin.ediCustoms.server.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("enterpriseDao")
public class EnterpriseDao extends BaseDaoHibernate<Enterprise>{
	private VoucherDao voucherDao;
	private ContractHeadDao contractHeadDao;
	private TotalBillHeadDao totalBillHeadDao;
	private CustomsDeclarationHeadDao customsDeclarationHeadDao;
	private RunRelationDao runRelationDao;
	private SendRelationDao sendRelationDao;
	private PermissionDao permissionDao;
	
	public void test() {
		System.out.println(getHibernateTemplate());
	}
	
	@Autowired
	public void setVoucherDao(VoucherDao voucherDao) {
		this.voucherDao = voucherDao;
	}
	@Autowired
	public void setContractHeadDao(ContractHeadDao contractHeadDao) {
		this.contractHeadDao = contractHeadDao;
	}
	@Autowired
	public void setCustomsDeclarationHeadDao(
			CustomsDeclarationHeadDao customsDeclarationHeadDao) {
		this.customsDeclarationHeadDao = customsDeclarationHeadDao;
	}
	@Autowired
	public void setTotalBillHeadDao(TotalBillHeadDao totalBillHeadDao) {
		this.totalBillHeadDao = totalBillHeadDao;
	}	
	@Autowired
	public void setRunRelationDao(RunRelationDao runRelationDao) {
		this.runRelationDao = runRelationDao;
	}
	@Autowired
	public void setSendRelationDao(SendRelationDao sendRelationDao) {
		this.sendRelationDao = sendRelationDao;
	}
	@Autowired
	public void setPermissionDao(PermissionDao permissionDao) {
		this.permissionDao = permissionDao;
	}
	
	
	@Override
	public Long save(Enterprise enterprise) {
		if (find("ownerCode", enterprise.getOwnerCode()).size() != 0) {
			return new Long(0);
		}
		if (find("ediCode", enterprise.getEdiCode()).size() != 0) {
			return new Long(0);
		}
		return super.save(enterprise);
	}	

	@Override
	public void delete(Long id) {
		List<Long> list=voucherDao.findIds("ownerId", id);
		for(Long idLong:list){
			voucherDao.delete(idLong);
		}
		list=contractHeadDao.findIds("ownerId", id);
		for(Long idLong:list){
			contractHeadDao.delete(idLong);
		}
		list=totalBillHeadDao.findIds("ownerId", id);
		for(Long idLong:list){
			totalBillHeadDao.delete(idLong);
		}
		list=customsDeclarationHeadDao.findIds("ownerId", id);
		for(Long idLong:list){
			customsDeclarationHeadDao.delete(idLong);
		}
		list=permissionDao.findIds("enterpriseId", id);
		for(Long idLong:list){
			permissionDao.delete(idLong);
		}
		
		Map<String, Object> map=new LinkedHashMap<String, Object>();
		map.put("runEnterpriseId", id);
		map.put("manufactureEnterpriseId", id);
		list=runRelationDao.findIdsOr(map);
		for(Long idLong:list){
			runRelationDao.delete(idLong);
		}
		
		map.clear();
		map.put("sendEnterpriseId", id);
		map.put("receiveEnterpriseId", id);
		list=sendRelationDao.findIdsOr(map);
		for(Long idLong:list){
			sendRelationDao.delete(idLong);
		}
		
		
		super.delete(id);
	}
}
