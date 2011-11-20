package com.gavin.ediCustoms.entity.edi.core;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class TotalBillList implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	//总清单头
	private Long totalBillHeadId;
	
	//清单表体序号
	private int no;
	private int noInContract;
	
	private int linkBillItemNo;
	private int copItem;
	private int corrBillNo;
	private int corrBillGNo;
	
	//商品编号 
	private String code="";
	private String plusCode="";
	
	//商品编码 
	private String codeTS="";
	//商品名称 GName 
	private String name="";
	//规格
	private String goodModel="";
	//毛重
	private double grossWeight;
	
	//第一计量单位
	private String unit1="";
	//第一数量
	private double quantity1;
	//第二计量单位
	private String unit2="";
	//第二数量
	private double quantity2;
	
	//申报数量
	private double quantity;
	//申报单价
	private double unitPrice;
	//申报总价格
	private double totalPrice;
	//申报单位 Gunit
	private String declareUnit="";
	
	private String currency;
	
	//原产国
	private String originCountry="";
	//征免方式
	private String taxMode;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTotalBillHeadId() {
		return totalBillHeadId;
	}
	public void setTotalBillHeadId(Long totalBillHeadId) {
		this.totalBillHeadId = totalBillHeadId;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getLinkBillItemNo() {
		return linkBillItemNo;
	}
	public void setLinkBillItemNo(int linkBillItemNo) {
		this.linkBillItemNo = linkBillItemNo;
	}
	public int getCopItem() {
		return copItem;
	}
	public void setCopItem(int copItem) {
		this.copItem = copItem;
	}
	public int getCorrBillNo() {
		return corrBillNo;
	}
	public void setCorrBillNo(int corrBillNo) {
		this.corrBillNo = corrBillNo;
	}
	public int getCorrBillGNo() {
		return corrBillGNo;
	}
	public void setCorrBillGNo(int corrBillGNo) {
		this.corrBillGNo = corrBillGNo;
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
	public String getCodeTS() {
		return codeTS;
	}
	public void setCodeTS(String codeTS) {
		this.codeTS = codeTS;
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
	public double getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(double grossWeight) {
		this.grossWeight = grossWeight;
	}
	public String getUnit1() {
		return unit1;
	}
	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}
	public double getQuantity1() {
		return quantity1;
	}
	public void setQuantity1(double quantity1) {
		this.quantity1 = quantity1;
	}
	public String getUnit2() {
		return unit2;
	}
	public void setUnit2(String unit2) {
		this.unit2 = unit2;
	}
	public double getQuantity2() {
		return quantity2;
	}
	public void setQuantity2(double quantity2) {
		this.quantity2 = quantity2;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getDeclareUnit() {
		return declareUnit;
	}
	public void setDeclareUnit(String declareUnit) {
		this.declareUnit = declareUnit;
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
	public String getTaxMode() {
		return taxMode;
	}
	public void setTaxMode(String taxMode) {
		this.taxMode = taxMode;
	}
	public int getNoInContract() {
		return noInContract;
	}
	public void setNoInContract(int noInContract) {
		this.noInContract = noInContract;
	}

	
	
	
	
	
}
