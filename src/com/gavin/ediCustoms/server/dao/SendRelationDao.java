package com.gavin.ediCustoms.server.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.SendRelation;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("sendRelationDao")
public class SendRelationDao extends BaseDaoHibernate<SendRelation>{
	@Override
	public Long save(SendRelation sendRelation) {
		Map<String, Object> map=new LinkedHashMap<String, Object>();
		map.put("sendEnterpriseId", sendRelation.getSendEnterpriseId());
		map.put("receiveEnterpriseId", sendRelation.getReceiveEnterpriseId());
		if (findAnd(map).size() != 0) {
			return new Long(0);
		}
		return super.save(sendRelation);
	}
	
}
