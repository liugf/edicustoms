package com.gavin.ediCustoms.server.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.contract.ContractProduct;
import com.gavin.ediCustoms.entity.edi.dictionary.GoodClassification;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;
import com.gavin.ediCustoms.server.dao.dictionary.GoodClassificationDao;

@Repository("contractProductDao")
public class ContractProductDao extends BaseDaoHibernate<ContractProduct> {
	@Autowired
	private ContractConsumeDao consumeDao;
	@Autowired
	private GoodClassificationDao goodClassificationDao;

	@Override
	public Long save(ContractProduct contractProduct) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("contractHeadId", contractProduct.getContractHeadId());
		map.put("no", contractProduct.getNo());
		if (findAnd(map).size() != 0) {
			return new Long(0);
		}
		return super.save(contractProduct);
	}

	@Override
	public void delete(Long id) {
		consumeDao.delete("contractProductId", id);
		super.delete(id);
	}
	
	public List<ContractProduct> list(Long contractHeadId) {
		List<ContractProduct> list=find("contractHeadId", contractHeadId);
		for(ContractProduct contractProduct:list){
			if (contractProduct.getGoodClassificationId()!=null) {
				GoodClassification goodClassification=goodClassificationDao.get(contractProduct.getGoodClassificationId());
				if (goodClassification!=null) {
					contractProduct.setUnit1(goodClassification.getUnit1());
					contractProduct.setUnit2(goodClassification.getUnit2());
				}
			}			
		}
		return list;
	}
	
	

}
