package com.gavin.ediCustoms.server.dao;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.ForeignEnterprise;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("foreignEnterpriseDao")
public class ForeignEnterpriseDao extends BaseDaoHibernate<ForeignEnterprise>{
	@Override
	public Long save(ForeignEnterprise foreignEnterprise) {
		if (find("tradeCode", foreignEnterprise.getTradeCode()).size() != 0) {
			return new Long(0);
		}
		return super.save(foreignEnterprise);
	}
}
