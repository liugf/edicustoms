package com.gavin.ediCustoms.server.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.core.TotalBillHead;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("totalBillHeadDao")
public class TotalBillHeadDao extends BaseDaoHibernate<TotalBillHead>{
	private TotalBillListDao totalBillListDao;
	@Autowired
	public void setTotalBillListDao(TotalBillListDao totalBillListDao) {
		this.totalBillListDao = totalBillListDao;
	}

	@Override
	public Long save(TotalBillHead totalBillHead) {
		if (find("billNo", totalBillHead.getBillNo()).size() != 0) {
			return new Long(0);
		}
		return super.save(totalBillHead);
	}
	
	@Override
	public void delete(Long id) {
		List<Long> productList=totalBillListDao.findIds("totalBillHeadId", id);
		for(Long productId:productList){
			totalBillListDao.delete(productId);
		}
		super.delete(id);
	}

}
