package com.gavin.ediCustoms.server.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.core.TotalBillList;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("totalBillListDao")
public class TotalBillListDao extends BaseDaoHibernate<TotalBillList> {
	
	@Override
	public Long save(TotalBillList totalBillList) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("totalBillHeadId", totalBillList.getTotalBillHeadId());
		map.put("no", totalBillList.getNo());
		if (findAnd(map).size() != 0) {
			return new Long(0);
		}
		return super.save(totalBillList);
	}


}
