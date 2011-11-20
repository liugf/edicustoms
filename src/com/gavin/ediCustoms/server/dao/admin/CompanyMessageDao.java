package com.gavin.ediCustoms.server.dao.admin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.admin.CompanyMessage;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("companyMessageDao")
public class CompanyMessageDao extends BaseDaoHibernate<CompanyMessage>{
	@Override
	public List<CompanyMessage> list() {
		List<CompanyMessage> list = super.list();
		if (list.size() != 0) {
			return list;
		} else {
			CompanyMessage companyMessage;
			companyMessage = new CompanyMessage();
			companyMessage.setEdiPlatformCode("9992");
			companyMessage.setCompanyName("新泽进出口有限公司");
			companyMessage.setId(save(companyMessage));
			list.add(companyMessage);
		}
		return list;
	}
}
