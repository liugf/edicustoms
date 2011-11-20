package com.gavin.ediCustoms.entity.edi.core;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class CustomsDeclarationGood implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long contractGoodId;
	
	private Long customsDeclarationHeadId;
	
	private int no;
	private Integer noInContract;
	
	private String codeTS="";
	private String code="";
	private String plusCode="";
	private String classMark;
	private String name="";	
	//产销国
	private String originCountry="";
	//新贸合同商品项目序号 W_CONTR_ITEM
	private int contractItemNo;
	//申报数量
	private double quantity;
	//申报单位 
	private String declareUnit="";
	//转换比例因子
	private double factor;
	//申报与备案单位不同时的折算数量
	private double converter;
	
	private String unit1="";
	private double quantity1;
	//第二计量单位
	private String unit2="";
	//第二数量
	private double quantity2;
	//成交币制
	private String currency;
	//申报单价
	private double declarePrice;
	//成交总价
	private double totalPrice;
	private String taxMode;
	//用途
	private String useTo;
	
	private String brandAndModel;
	private Double volume;
	private Double grossWeight;
	private Double netWeight;
	
	private int packNo; 
	
	private String shenbaoguifan;
	private String wrapType;
	
	private String goodVersion;

	
	public String getWrapType() {
		return wrapType;
	}
	public void setWrapType(String wrapType) {
		this.wrapType = wrapType;
	}
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
	public String getClassMark() {
		return classMark;
	}
	public void setClassMark(String classMark) {
		this.classMark = classMark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	public int getContractItemNo() {
		return contractItemNo;
	}
	public void setContractItemNo(int contractItemNo) {
		this.contractItemNo = contractItemNo;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getDeclareUnit() {
		return declareUnit;
	}
	public void setDeclareUnit(String declareUnit) {
		this.declareUnit = declareUnit;
	}
	public double getFactor() {
		return factor;
	}
	public void setFactor(double factor) {
		this.factor = factor;
	}
	public double getConverter() {
		return converter;
	}
	public void setConverter(double converter) {
		this.converter = converter;
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getDeclarePrice() {
		return declarePrice;
	}
	public void setDeclarePrice(double declarePrice) {
		this.declarePrice = declarePrice;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getTaxMode() {
		return taxMode;
	}
	public void setTaxMode(String taxMode) {
		this.taxMode = taxMode;
	}
	public String getUseTo() {
		return useTo;
	}
	public void setUseTo(String useTo) {
		this.useTo = useTo;
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
	public Integer getNoInContract() {
		return noInContract;
	}
	public void setNoInContract(Integer noInContract) {
		this.noInContract = noInContract;
	}
	public String getBrandAndModel() {
		return brandAndModel;
	}
	public void setBrandAndModel(String brandAndModel) {
		this.brandAndModel = brandAndModel;
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
	public Long getContractGoodId() {
		return contractGoodId;
	}
	public void setContractGoodId(Long contractGoodId) {
		this.contractGoodId = contractGoodId;
	}
	public String getCodeTS() {
		return codeTS;
	}
	public void setCodeTS(String codeTS) {
		this.codeTS = codeTS;
	}
	public String getShenbaoguifan() {
		return shenbaoguifan;
	}
	public void setShenbaoguifan(String shenbaoguifan) {
		this.shenbaoguifan = shenbaoguifan;
	}
	public int getPackNo() {
		return packNo;
	}
	public void setPackNo(int packNo) {
		this.packNo = packNo;
	}
	public String getGoodVersion() {
		return goodVersion;
	}
	public void setGoodVersion(String goodVersion) {
		this.goodVersion = goodVersion;
	}
	
	
	
}
