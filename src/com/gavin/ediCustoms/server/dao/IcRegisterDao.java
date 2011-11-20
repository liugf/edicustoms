package com.gavin.ediCustoms.server.dao;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.logistics.IcRegister;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("icRegisterDao")
public class IcRegisterDao extends BaseDaoHibernate<IcRegister>{
	@Override
	public Long save(IcRegister icRegister) {	
		if (find("cardNo",icRegister.getCardNo()).size() != 0) {
			return new Long(0);
		}
		return super.save(icRegister);
	}
}
