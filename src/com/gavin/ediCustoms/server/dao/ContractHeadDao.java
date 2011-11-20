package com.gavin.ediCustoms.server.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.contract.ContractHead;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("contractHeadDao")
public class ContractHeadDao extends BaseDaoHibernate<ContractHead>{
	private ContractProductDao contractProductDao;
	private ContractMaterialDao contractMaterialDao;
	@Autowired
	public void setContractProductDao(ContractProductDao contractProductDao) {
		this.contractProductDao = contractProductDao;
	}
	@Autowired
	public void setContractMaterialDao(ContractMaterialDao contractMaterialDao) {
		this.contractMaterialDao = contractMaterialDao;
	}

	@Override
	public Long save(ContractHead contractHead) {
		if (find("manualNo", contractHead.getManualNo()).size() != 0) {
			return new Long(0);
		}
		return super.save(contractHead);
	}
	
	@Override
	public void delete(Long id) {
		List<Long> productList=contractProductDao.findIds("contractHeadId", id);
		for(Long productId:productList){
			contractProductDao.delete(productId);
		}
		List<Long> materialList=contractMaterialDao.findIds("contractHeadId", id);
		for(Long materialId:materialList){
			contractMaterialDao.delete(materialId);
		}
		super.delete(id);
	}
}
