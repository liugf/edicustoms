package com.gavin.ediCustoms.server.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.contract.ContractMaterial;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("contractMaterialDao")
public class ContractMaterialDao extends BaseDaoHibernate<ContractMaterial> {
	private ContractConsumeDao consumeDao;

	@Autowired
	public void setConsumeDao(ContractConsumeDao consumeDao) {
		this.consumeDao = consumeDao;
	}

	@Override
	public Long save(ContractMaterial contractMaterial) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("contractHeadId", contractMaterial.getContractHeadId());
		map.put("no", contractMaterial.getNo());
		if (findAnd(map).size() != 0) {
			return new Long(0);
		}
		return super.save(contractMaterial);
	}

	@Override
	public void delete(Long id) {
		consumeDao.delete("contractMaterialId", id);
		super.delete(id);
	}

}
