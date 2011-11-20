package com.gavin.ediCustoms.server.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.core.Container;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("containerDao")
public class ContainerDao extends BaseDaoHibernate<Container>{
	@Override
	public Long save(Container container) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("customsDeclarationHeadId", container.getCustomsDeclarationHeadId());
		map.put("containerNo", container.getContainerNo());
		if (findAnd(map).size() != 0) {
			return new Long(0);
		}
		return super.save(container);
	}
}
