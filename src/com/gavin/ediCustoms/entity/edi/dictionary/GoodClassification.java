package com.gavin.ediCustoms.entity.edi.dictionary;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Entity
public class GoodClassification implements Serializable,BaseItem{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String code;
	private String name;
	
	private String plusCode;
	private String unit1;
	private String unit2;
	private double lowRate;
	private double highRate;
	private double outRate;
	
	//不确定类型
	private String regMark;
	
	private double regRate;
	private String taxType;
	private double taxRate;
	private double commRate;
	private double taiwanRate;
	private String otherType;
	private double otherRate;
	private double iLowPrice;
	private double iHighPrice;
	private double eLowPrice;
	private double eHighPrice;
	private int maxIn;
	private int maxOut;
	private String controlMark;
	private double chkPrice;
	private String tariffMark;
	private String note;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlusCode() {
		return plusCode;
	}
	public void setPlusCode(String plusCode) {
		this.plusCode = plusCode;
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
	public double getLowRate() {
		return lowRate;
	}
	public void setLowRate(double lowRate) {
		this.lowRate = lowRate;
	}
	public double getHighRate() {
		return highRate;
	}
	public void setHighRate(double highRate) {
		this.highRate = highRate;
	}
	public double getOutRate() {
		return outRate;
	}
	public void setOutRate(double outRate) {
		this.outRate = outRate;
	}
	public String getRegMark() {
		return regMark;
	}
	public void setRegMark(String regMark) {
		this.regMark = regMark;
	}
	public double getRegRate() {
		return regRate;
	}
	public void setRegRate(double regRate) {
		this.regRate = regRate;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}
	public double getCommRate() {
		return commRate;
	}
	public void setCommRate(double commRate) {
		this.commRate = commRate;
	}
	public double getTaiwanRate() {
		return taiwanRate;
	}
	public void setTaiwanRate(double taiwanRate) {
		this.taiwanRate = taiwanRate;
	}
	public String getOtherType() {
		return otherType;
	}
	public void setOtherType(String otherType) {
		this.otherType = otherType;
	}
	public double getOtherRate() {
		return otherRate;
	}
	public void setOtherRate(double otherRate) {
		this.otherRate = otherRate;
	}
	public double getiLowPrice() {
		return iLowPrice;
	}
	public void setiLowPrice(double iLowPrice) {
		this.iLowPrice = iLowPrice;
	}
	public double getiHighPrice() {
		return iHighPrice;
	}
	public void setiHighPrice(double iHighPrice) {
		this.iHighPrice = iHighPrice;
	}
	public double geteLowPrice() {
		return eLowPrice;
	}
	public void seteLowPrice(double eLowPrice) {
		this.eLowPrice = eLowPrice;
	}
	public double geteHighPrice() {
		return eHighPrice;
	}
	public void seteHighPrice(double eHighPrice) {
		this.eHighPrice = eHighPrice;
	}
	public int getMaxIn() {
		return maxIn;
	}
	public void setMaxIn(int maxIn) {
		this.maxIn = maxIn;
	}
	public int getMaxOut() {
		return maxOut;
	}
	public void setMaxOut(int maxOut) {
		this.maxOut = maxOut;
	}
	public String getControlMark() {
		return controlMark;
	}
	public void setControlMark(String controlMark) {
		this.controlMark = controlMark;
	}
	public double getChkPrice() {
		return chkPrice;
	}
	public void setChkPrice(double chkPrice) {
		this.chkPrice = chkPrice;
	}
	public String getTariffMark() {
		return tariffMark;
	}
	public void setTariffMark(String tariffMark) {
		this.tariffMark = tariffMark;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	
}
