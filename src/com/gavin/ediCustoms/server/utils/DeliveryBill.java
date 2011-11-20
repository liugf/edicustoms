package com.gavin.ediCustoms.server.utils;

import java.util.ArrayList;
import java.util.List;

import com.gavin.ediCustoms.entity.edi.Enterprise;

public class DeliveryBill {
	public DeliveryBill() {
		list = new ArrayList<GoodItem>();
	}

	Enterprise destinationEnterprise;
	List<GoodItem> list;

	public Enterprise getDestinationEnterprise() {
		return destinationEnterprise;
	}

	public void setDestinationEnterprise(Enterprise destinationEnterprise) {
		this.destinationEnterprise = destinationEnterprise;
	}

	public List<GoodItem> getList() {
		return list;
	}

	public void setList(List<GoodItem> list) {
		this.list = list;
	}

}