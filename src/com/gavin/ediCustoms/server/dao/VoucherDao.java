package com.gavin.ediCustoms.server.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.Voucher;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("voucherDao")
public class VoucherDao extends BaseDaoHibernate<Voucher>{
	@Override
	public Long save(Voucher voucher) {
		Map<String, Object> map=new LinkedHashMap<String, Object>();
		map.put("ownerId", voucher.getOwnerId());
		map.put("no", voucher.getNo());
		if (findAnd(map).size() != 0) {
			return new Long(0);
		}
		return super.save(voucher);
	}
}
