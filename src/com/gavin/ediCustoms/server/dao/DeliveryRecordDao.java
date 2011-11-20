package com.gavin.ediCustoms.server.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.core.DeliveryRecord;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("deliveryRecordDao")
public class DeliveryRecordDao extends BaseDaoHibernate<DeliveryRecord>{
	@Override
	public Long save(DeliveryRecord deliveryRecord) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("customsDeclarationHeadId", deliveryRecord.getCustomsDeclarationHeadId());
		map.put("destinationEnterpriseId", deliveryRecord.getDestinationEnterpriseId());
		map.put("customsDeclarationGoodId", deliveryRecord.getCustomsDeclarationGoodId());
		if (findAnd(map).size() != 0) {
			return new Long(0);
		}
		return super.save(deliveryRecord);
	}
}
