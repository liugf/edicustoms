package com.gavin.ediCustoms.server.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.Permission;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("permissionDao")
public class PermissionDao extends BaseDaoHibernate<Permission>{
	@Override
	public Long save(Permission permission) {
		Map<String, Object> map=new LinkedHashMap<String, Object>();
		map.put("userId", permission.getUserId());
		map.put("enterpriseId", permission.getEnterpriseId());
		if (findAnd(map).size() != 0) {
			return new Long(0);
		}
		return super.save(permission);
	}
	
}
