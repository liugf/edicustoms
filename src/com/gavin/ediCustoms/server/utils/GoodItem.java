package com.gavin.ediCustoms.server.utils;

import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationGood;
import com.gavin.ediCustoms.entity.edi.core.DeliveryRecord;

public class GoodItem {
	CustomsDeclarationGood customsDeclarationGood;
	DeliveryRecord deliveryRecord;

	public CustomsDeclarationGood getCustomsDeclarationGood() {
		return customsDeclarationGood;
	}

	public void setCustomsDeclarationGood(
			CustomsDeclarationGood customsDeclarationGood) {
		this.customsDeclarationGood = customsDeclarationGood;
	}

	public DeliveryRecord getDeliveryRecord() {
		return deliveryRecord;
	}

	public void setDeliveryRecord(DeliveryRecord deliveryRecord) {
		this.deliveryRecord = deliveryRecord;
	}

}