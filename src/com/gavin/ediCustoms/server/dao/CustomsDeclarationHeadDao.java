package com.gavin.ediCustoms.server.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("customsDeclarationHeadDao")
public class CustomsDeclarationHeadDao extends BaseDaoHibernate<CustomsDeclarationHead>{
	private CustomsDeclarationGoodDao customsDeclarationGoodDao;
	@Autowired
	public void setCustomsDeclarationGoodDao(
			CustomsDeclarationGoodDao customsDeclarationGoodDao) {
		this.customsDeclarationGoodDao = customsDeclarationGoodDao;
	}

	@Override
	public Long save(CustomsDeclarationHead customsDeclarationHead) {
		if (find("preEntryId", customsDeclarationHead.getPreEntryId()).size() != 0) {
			return new Long(0);
		}
		return super.save(customsDeclarationHead);
	}
	@Override
	public void delete(Long id) {
		List<Long> productList=customsDeclarationGoodDao.findIds("customsDeclarationHeadId", id);
		for(Long productId:productList){
			customsDeclarationGoodDao.delete(productId);
		}
		super.delete(id);
	}
	
	

}
