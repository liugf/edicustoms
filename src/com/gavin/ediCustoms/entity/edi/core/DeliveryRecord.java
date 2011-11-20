package com.gavin.ediCustoms.entity.edi.core;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class DeliveryRecord implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private Long customsDeclarationHeadId;
	
	private Long destinationEnterpriseId;
	private Long customsDeclarationGoodId;
	private double quantity;
	private Double volume;
	private Double grossWeight;
	private Double netWeight;
	private int packNo; 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustomsDeclarationHeadId() {
		return customsDeclarationHeadId;
	}
	public void setCustomsDeclarationHeadId(Long customsDeclarationHeadId) {
		this.customsDeclarationHeadId = customsDeclarationHeadId;
	}
	public Long getDestinationEnterpriseId() {
		return destinationEnterpriseId;
	}
	public void setDestinationEnterpriseId(Long destinationEnterpriseId) {
		this.destinationEnterpriseId = destinationEnterpriseId;
	}
	public Long getCustomsDeclarationGoodId() {
		return customsDeclarationGoodId;
	}
	public void setCustomsDeclarationGoodId(Long customsDeclarationGoodId) {
		this.customsDeclarationGoodId = customsDeclarationGoodId;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public Double getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}
	public Double getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}
	public int getPackNo() {
		return packNo;
	}
	public void setPackNo(int packNo) {
		this.packNo = packNo;
	}
	
	
}
