package com.gavin.ediCustoms.server.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.contract.ContractConsume;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("contractConsumeDao")
public class ContractConsumeDao extends BaseDaoHibernate<ContractConsume>{
	@Override
	public Long save(ContractConsume contractConsume) {
		Map<String, Object> map=new LinkedHashMap<String, Object>();
		map.put("contractProductId", contractConsume.getContractProductId());
		map.put("contractMaterialId", contractConsume.getContractMaterialId());
		if (findAnd(map).size() != 0) {
			return new Long(0);
		}
		return super.save(contractConsume);
	}
}
