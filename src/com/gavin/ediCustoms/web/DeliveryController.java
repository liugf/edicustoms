package com.gavin.ediCustoms.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationGood;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.core.DeliveryRecord;
import com.gavin.ediCustoms.entity.edi.dictionary.Currency;
import com.gavin.ediCustoms.entity.edi.dictionary.Unit;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationGoodDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationHeadDao;
import com.gavin.ediCustoms.server.dao.DeliveryRecordDao;
import com.gavin.ediCustoms.server.dao.EnterpriseDao;
import com.gavin.ediCustoms.server.dao.dictionary.CurrencyDao;
import com.gavin.ediCustoms.server.dao.dictionary.UnitDao;
import com.gavin.ediCustoms.server.utils.DeliveryBill;
import com.gavin.ediCustoms.server.utils.GoodItem;


@Controller
@RequestMapping("/delivery.do")
public class DeliveryController {

	@SuppressWarnings("unchecked")
	@RequestMapping
	public String delivery(Long id, ModelMap map)
			throws Exception {
		CustomsDeclarationHead customsDeclarationHead = customsDeclarationHeadDao
				.get(id);
		List<DeliveryRecord> deliveryRecords = deliveryRecordDao.find(
				"customsDeclarationHeadId", id);
		Enterprise enterprise=enterpriseDao.get(customsDeclarationHead.getOwnerId());
		String manualNo=customsDeclarationHead.getContractNo();

		Map<Long, DeliveryBill> deliveryMap = new HashMap<Long, DeliveryBill>();
		for (DeliveryRecord deliveryRecord : deliveryRecords) {
			DeliveryBill deliveryBill = deliveryMap.get(deliveryRecord
					.getDestinationEnterpriseId());
			if (deliveryBill == null) {
				deliveryBill = new DeliveryBill();
				deliveryBill.setDestinationEnterprise(enterpriseDao
						.get(deliveryRecord.getDestinationEnterpriseId()));
				deliveryMap.put(deliveryRecord.getDestinationEnterpriseId(),
						deliveryBill);
			}
			GoodItem goodItem = new GoodItem();
			goodItem.setCustomsDeclarationGood(customsDeclarationGoodDao
					.get(deliveryRecord.getCustomsDeclarationGoodId()));
			getCustomsDeclarationGoodDetail(goodItem
					.getCustomsDeclarationGood());
			goodItem.setDeliveryRecord(deliveryRecord);
			deliveryBill.getList().add(goodItem);
		}
		List<DeliveryBill> deliveryBills=new ArrayList<DeliveryBill>();
		for(Object o : deliveryMap.keySet()){			
			deliveryBills.add(deliveryMap.get(o));
		}	
		
		map.put("customsDeclarationHead", customsDeclarationHead);
		map.put("enterprise", enterprise);
		map.put("manualNo", manualNo);
		map.put("deliveryBills", deliveryBills);
		return "delivery";
	}

	private void getCustomsDeclarationGoodDetail(
			CustomsDeclarationGood customsDeclarationGood) {
		Unit unit = unitDao.get(customsDeclarationGood.getDeclareUnit());
		if (unit != null) {
			customsDeclarationGood.setDeclareUnit(unit.getName());
		}
		Currency currency = currencyDao.get(customsDeclarationGood
				.getCurrency());
		if (currency != null) {
			customsDeclarationGood.setCurrency(currency.getShortName());
		}
	}

	@Autowired
	private CustomsDeclarationGoodDao customsDeclarationGoodDao;
	@Autowired
	private DeliveryRecordDao deliveryRecordDao;
	@Autowired
	private CustomsDeclarationHeadDao customsDeclarationHeadDao;
	@Autowired
	private EnterpriseDao enterpriseDao;

	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private UnitDao unitDao;

}
