package com.gavin.ediCustoms.server.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationGood;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("customsDeclarationGoodDao")
public class CustomsDeclarationGoodDao extends BaseDaoHibernate<CustomsDeclarationGood> {
	
	@Override
	public Long save(CustomsDeclarationGood customsDeclarationGood) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("customsDeclarationHeadId", customsDeclarationGood.getCustomsDeclarationHeadId());
		map.put("no", customsDeclarationGood.getNo());
		if (findAnd(map).size() != 0) {
			return new Long(0);
		}
		return super.save(customsDeclarationGood);
	}


}
