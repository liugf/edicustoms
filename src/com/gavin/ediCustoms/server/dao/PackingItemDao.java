package com.gavin.ediCustoms.server.dao;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.core.PackingItem;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("packingItemDao")
public class PackingItemDao extends BaseDaoHibernate<PackingItem>{

}
