package com.gavin.ediCustoms.entity.edi.contract;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class ContractHead implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	//收发货单位
	private Long ownerId;
	
	//外商公司
	private Long foreignEnterpriseId;
	//经营单位
	private Long runEnterpriseId;
	//预录入编号
	private String preManualNo;
	//手册编号
	private String manualNo;
	//贸易方式
	private String tradeMode;
	//征免性质
	private String taxKind;
	//加工种类
	private String processType;
	//批文号
	private String certificationCode;
	//协议书号
	private String protocolCode;
	//许可证号
	private String licenceCode;
	//贸易国别
	private String tradeCountry;
	//进口期限
	private Date importDeadline;
	//出口期限
	private Date exportDeadline;
	//进口合同
	private String importContract;
	//出口合同
	private String exportContract;
	//进口总额
	private double importTotalMoney;
	//出口总额
	private double exportTotalMoney;
	//进口币制
	private String importCurrency;
	//出口币制
	private String exportCurrency;
	//投资方式
	private String investMode;
	//投资额
	private double investMoney;
	//投资币制
	private String investCurrency;
	//内销比例
	private double saleInScale;
	//监管费率
	private double superviseFeeRate;
	//监管费
	private double superviseFee;
	//成交方式
	private String dealMode;
	//引进方式
	private String bringInMode;
	//出口延期
	private String exportDelay;
	//口岸一
	private String port1;
	//口岸二
	private String port2;
	//口岸三
	private String port3;
	//口岸四
	private String port4;
	//口岸五
	private String port5;
	//备注
	private String note;
	//录入员
	private String typeMan;
	//录入日期
	private Date typeDate;
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
	public Long getForeignEnterpriseId() {
		return foreignEnterpriseId;
	}
	public void setForeignEnterpriseId(Long foreignEnterpriseId) {
		this.foreignEnterpriseId = foreignEnterpriseId;
	}
	public Long getRunEnterpriseId() {
		return runEnterpriseId;
	}
	public void setRunEnterpriseId(Long runEnterpriseId) {
		this.runEnterpriseId = runEnterpriseId;
	}
	public String getPreManualNo() {
		return preManualNo;
	}
	public void setPreManualNo(String preManualNo) {
		this.preManualNo = preManualNo;
	}
	public String getManualNo() {
		return manualNo;
	}
	public void setManualNo(String manualNo) {
		this.manualNo = manualNo;
	}
	public String getTradeMode() {
		return tradeMode;
	}
	public void setTradeMode(String tradeMode) {
		this.tradeMode = tradeMode;
	}
	public String getTaxKind() {
		return taxKind;
	}
	public void setTaxKind(String taxKind) {
		this.taxKind = taxKind;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getCertificationCode() {
		return certificationCode;
	}
	public void setCertificationCode(String certificationCode) {
		this.certificationCode = certificationCode;
	}
	public String getProtocolCode() {
		return protocolCode;
	}
	public void setProtocolCode(String protocolCode) {
		this.protocolCode = protocolCode;
	}
	public String getLicenceCode() {
		return licenceCode;
	}
	public void setLicenceCode(String licenceCode) {
		this.licenceCode = licenceCode;
	}
	public String getTradeCountry() {
		return tradeCountry;
	}
	public void setTradeCountry(String tradeCountry) {
		this.tradeCountry = tradeCountry;
	}
	public Date getImportDeadline() {
		return importDeadline;
	}
	public void setImportDeadline(Date importDeadline) {
		this.importDeadline = importDeadline;
	}
	public Date getExportDeadline() {
		return exportDeadline;
	}
	public void setExportDeadline(Date exportDeadline) {
		this.exportDeadline = exportDeadline;
	}
	public String getImportContract() {
		return importContract;
	}
	public void setImportContract(String importContract) {
		this.importContract = importContract;
	}
	public String getExportContract() {
		return exportContract;
	}
	public void setExportContract(String exportContract) {
		this.exportContract = exportContract;
	}
	public double getImportTotalMoney() {
		return importTotalMoney;
	}
	public void setImportTotalMoney(double importTotalMoney) {
		this.importTotalMoney = importTotalMoney;
	}
	public double getExportTotalMoney() {
		return exportTotalMoney;
	}
	public void setExportTotalMoney(double exportTotalMoney) {
		this.exportTotalMoney = exportTotalMoney;
	}
	public String getImportCurrency() {
		return importCurrency;
	}
	public void setImportCurrency(String importCurrency) {
		this.importCurrency = importCurrency;
	}
	public String getExportCurrency() {
		return exportCurrency;
	}
	public void setExportCurrency(String exportCurrency) {
		this.exportCurrency = exportCurrency;
	}
	public String getInvestMode() {
		return investMode;
	}
	public void setInvestMode(String investMode) {
		this.investMode = investMode;
	}
	public double getInvestMoney() {
		return investMoney;
	}
	public void setInvestMoney(double investMoney) {
		this.investMoney = investMoney;
	}
	public String getInvestCurrency() {
		return investCurrency;
	}
	public void setInvestCurrency(String investCurrency) {
		this.investCurrency = investCurrency;
	}
	public double getSaleInScale() {
		return saleInScale;
	}
	public void setSaleInScale(double saleInScale) {
		this.saleInScale = saleInScale;
	}
	public double getSuperviseFeeRate() {
		return superviseFeeRate;
	}
	public void setSuperviseFeeRate(double superviseFeeRate) {
		this.superviseFeeRate = superviseFeeRate;
	}
	public double getSuperviseFee() {
		return superviseFee;
	}
	public void setSuperviseFee(double superviseFee) {
		this.superviseFee = superviseFee;
	}
	public String getDealMode() {
		return dealMode;
	}
	public void setDealMode(String dealMode) {
		this.dealMode = dealMode;
	}
	public String getBringInMode() {
		return bringInMode;
	}
	public void setBringInMode(String bringInMode) {
		this.bringInMode = bringInMode;
	}
	public String getExportDelay() {
		return exportDelay;
	}
	public void setExportDelay(String exportDelay) {
		this.exportDelay = exportDelay;
	}
	public String getPort1() {
		return port1;
	}
	public void setPort1(String port1) {
		this.port1 = port1;
	}
	public String getPort2() {
		return port2;
	}
	public void setPort2(String port2) {
		this.port2 = port2;
	}
	public String getPort3() {
		return port3;
	}
	public void setPort3(String port3) {
		this.port3 = port3;
	}
	public String getPort4() {
		return port4;
	}
	public void setPort4(String port4) {
		this.port4 = port4;
	}
	public String getPort5() {
		return port5;
	}
	public void setPort5(String port5) {
		this.port5 = port5;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getTypeMan() {
		return typeMan;
	}
	public void setTypeMan(String typeMan) {
		this.typeMan = typeMan;
	}
	public Date getTypeDate() {
		return typeDate;
	}
	public void setTypeDate(Date typeDate) {
		this.typeDate = typeDate;
	}
	
	
}
