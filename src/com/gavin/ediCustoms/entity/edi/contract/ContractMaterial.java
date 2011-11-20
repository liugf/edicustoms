package com.gavin.ediCustoms.entity.edi.contract;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class ContractMaterial implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long GoodClassificationId;

	//商品序号
	private int no;
	//合同头
	private Long contractHeadId;
	//商品编号 
	private String code="";
	private String plusCode="";
	//商品名称 GName 
	private String name="";
	//规格
	private String goodModel="";
	//数量
	private double quantity;
	//单价
	private double price;
	//申报单位 Gunit
	private String declareUnit="";
	//产销国
	private String originCountry="";
	//征免方式
	private String taxMode;
	//进口方式
	private String bringInMode;
	//总价
	private double totalPrice;
	//征税比例
	private double taxScale;
	
	//第一计量单位
	private String unit1="";
	//第二计量单位
	private String unit2="";
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}	
	public Long getContractHeadId() {
		return contractHeadId;
	}
	public void setContractHeadId(Long contractHeadId) {
		this.contractHeadId = contractHeadId;
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
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDeclareUnit() {
		return declareUnit;
	}
	public void setDeclareUnit(String declareUnit) {
		this.declareUnit = declareUnit;
	}
	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	public String getTaxMode() {
		return taxMode;
	}
	public void setTaxMode(String taxMode) {
		this.taxMode = taxMode;
	}
	
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public double getTaxScale() {
		return taxScale;
	}
	public void setTaxScale(double taxScale) {
		this.taxScale = taxScale;
	}
	public String getBringInMode() {
		return bringInMode;
	}
	public void setBringInMode(String bringInMode) {
		this.bringInMode = bringInMode;
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
	public Long getGoodClassificationId() {
		return GoodClassificationId;
	}
	public void setGoodClassificationId(Long goodClassificationId) {
		GoodClassificationId = goodClassificationId;
	}
	
	
}
