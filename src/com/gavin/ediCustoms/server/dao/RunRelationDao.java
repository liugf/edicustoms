package com.gavin.ediCustoms.server.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.RunRelation;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("runRelationDao")
public class RunRelationDao extends BaseDaoHibernate<RunRelation>{
	@Override
	public Long save(RunRelation runRelation) {
		Map<String, Object> map=new LinkedHashMap<String, Object>();
		map.put("runEnterpriseId", runRelation.getRunEnterpriseId());
		map.put("manufactureEnterpriseId", runRelation.getManufactureEnterpriseId());
		if (findAnd(map).size() != 0) {
			return new Long(0);
		}
		return super.save(runRelation);
	}	
}
