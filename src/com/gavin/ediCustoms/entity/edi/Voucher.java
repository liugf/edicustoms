package com.gavin.ediCustoms.entity.edi;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Voucher implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long goodClassificationId;
	private Long ownerId;
	
	//商品序号
	private int no;
	//商品编号 
	private String code="";
	private String plusCode="";
	//商品名称 GName 
	private String name="";
	//规格
	private String goodModel="";
	//申报单位 Gunit
	private String declareUnit="";
	//第一单位
	private String unit1="";
	//第二单位
	private String unit2="";
	//申报单价 UnitPrice
	private double declarePrice;
	//币制  Curr
	private String currency="";	
	//产销国
	private String originCountry="";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPlusCode() {
		return plusCode;
	}
	public void setPlusCode(String plusCode) {
		this.plusCode = plusCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGoodModel() {
		return goodModel;
	}
	public void setGoodModel(String goodModel) {
		this.goodModel = goodModel;
	}
	public String getDeclareUnit() {
		return declareUnit;
	}
	public void setDeclareUnit(String declareUnit) {
		this.declareUnit = declareUnit;
	}	
	public double getDeclarePrice() {
		return declarePrice;
	}
	public void setDeclarePrice(double declarePrice) {
		this.declarePrice = declarePrice;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	public Long getGoodClassificationId() {
		return goodClassificationId;
	}
	public void setGoodClassificationId(Long goodClassificationId) {
		this.goodClassificationId = goodClassificationId;
	}
	public String getUnit1() {
		return unit1;
	}
	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}
	public String getUnit2() {
		return unit2;
	}
	public void setUnit2(String unit2) {
		this.unit2 = unit2;
	}

	
	
	
}
