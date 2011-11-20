package com.gavin.ediCustoms.entity.edi.core;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class PackingItem implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private Long customsDeclarationHeadId;
	
	private int no;
	
	//托/卷/扎或箱号
	private Integer wrapNo;
	
	//总箱数
	private Integer packNo; 
	
	private String name="";	
	
	//客户编号
	private String clientNo;
	
	//数量
	private Double quantity;
	//申报单位 
	private String declareUnit="";
	
	private Double grossWeightPerPack;
	private Double netWeightPerPack;
	
	private Double grossWeight;
	private Double netWeight;
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
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	
	public Integer getPackNo() {
		return packNo;
	}
	public void setPackNo(Integer packNo) {
		this.packNo = packNo;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClientNo() {
		return clientNo;
	}
	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public String getDeclareUnit() {
		return declareUnit;
	}
	public void setDeclareUnit(String declareUnit) {
		this.declareUnit = declareUnit;
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
	public Double getGrossWeightPerPack() {
		return grossWeightPerPack;
	}
	public void setGrossWeightPerPack(Double grossWeightPerPack) {
		this.grossWeightPerPack = grossWeightPerPack;
	}
	public Double getNetWeightPerPack() {
		return netWeightPerPack;
	}
	public void setNetWeightPerPack(Double netWeightPerPack) {
		this.netWeightPerPack = netWeightPerPack;
	}
	public Integer getWrapNo() {
		return wrapNo;
	}
	public void setWrapNo(Integer wrapNo) {
		this.wrapNo = wrapNo;
	}
	
	
	
}
