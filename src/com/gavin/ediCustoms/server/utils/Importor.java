package com.gavin.ediCustoms.server.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationGood;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.core.DeliveryRecord;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationGoodDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationHeadDao;
import com.gavin.ediCustoms.server.dao.DeliveryRecordDao;
import com.gavin.ediCustoms.server.dao.dictionary.LoadPortDao;

@Service("importor")
public class Importor {
	@Autowired
	private CustomsDeclarationGoodDao customsDeclarationGoodDao;
	@Autowired
	private CustomsDeclarationHeadDao customsDeclarationHeadDao;	
	@Autowired
	private CodeGenerator codeGenerator;
	@Autowired
	private DeliveryRecordDao deliveryRecordDao;
	@Autowired LoadPortDao loadPortDao;	
	
	
	/*public String importPackingItem(Long customsDeclarationHeadId){
		CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(customsDeclarationHeadId);
		if (customsDeclarationHead==null) {
			return "报关单不存在";
		}
		List<CustomsDeclarationGood> customsDeclarationGoods=customsDeclarationGoodDao.find("customsDeclarationHeadId", customsDeclarationHeadId);
		packingItemDao.delete("customsDeclarationHeadId", customsDeclarationHeadId);
		int no=0;
		for(CustomsDeclarationGood customsDeclarationGood:customsDeclarationGoods){
			no++;
			PackingItem packingItem=new PackingItem();
			packingItem.setNo(no);
			packingItem.setCustomsDeclarationHeadId(customsDeclarationHeadId);
			packingItem.setName(customsDeclarationGood.getName());
			packingItem.setGoodModel(customsDeclarationGood.getShenbaoguifan());
			packingItem.setQuantity(customsDeclarationGood.getQuantity());
			packingItem.setDeclareUnit(customsDeclarationGood.getDeclareUnit());
			packingItem.setGrossWeight(customsDeclarationGood.getGrossWeight());
			packingItem.setPackNo(customsDeclarationGood.getPackNo());
			packingItem.setNetWeight(customsDeclarationGood.getNetWeight());
			packingItemDao.save(packingItem);
		}		
		return null;
	}*/

	
	public String importCustomsDeclaration(Long enterpriseId, Long id) {		
		CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(id);
		customsDeclarationHead.setOwnerId(enterpriseId);
		customsDeclarationHead.setIsExport(false);
		customsDeclarationHead.setIsDeclared(false);		
		customsDeclarationHead.setElectronicPort(null);
		customsDeclarationHead.setRunEnterpriseId(null);
		customsDeclarationHead.setDeclarantId(null);
		customsDeclarationHead.setContractHeadId(null);
		customsDeclarationHead.setPackNo(0);
		customsDeclarationHead.setGrossWeight(0);
		customsDeclarationHead.setNetWeight(0);		
		String customsCode=loadPortDao.get(customsDeclarationHead.getLoadPort()).getCustomsCode();
		customsDeclarationHead.setPreEntryId(codeGenerator.getPreEntryId(
				enterpriseId, customsCode, false));
		customsDeclarationHead.setId(customsDeclarationHeadDao.save(customsDeclarationHead));
		
		
		List<DeliveryRecord> deliveryRecords = deliveryRecordDao.find("customsDeclarationHeadId", id);
		for(DeliveryRecord deliveryRecord:deliveryRecords){
			if (deliveryRecord.getDestinationEnterpriseId().equals(enterpriseId)) {
				CustomsDeclarationGood customsDeclarationGood=customsDeclarationGoodDao
				.get(deliveryRecord.getCustomsDeclarationGoodId());
				customsDeclarationGood.setCustomsDeclarationHeadId(customsDeclarationHead.getId());
				customsDeclarationGood.setNetWeight(deliveryRecord.getNetWeight());
				customsDeclarationGood.setGrossWeight(null);
				customsDeclarationGood.setVolume(null);
				customsDeclarationGood.setQuantity(deliveryRecord.getQuantity());
				customsDeclarationGood.setQuantity1(0);
				customsDeclarationGood.setQuantity2(0);
				customsDeclarationGood.setUnit1(null);
				customsDeclarationGood.setUnit2(null);
				customsDeclarationGood.setTotalPrice(customsDeclarationGood.getQuantity()*customsDeclarationGood.getDeclarePrice());				
				
				customsDeclarationGoodDao.save(customsDeclarationGood);
			}			
		}
		
	
		return null;
	}
	

}
