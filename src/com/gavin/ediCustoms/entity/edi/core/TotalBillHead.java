package com.gavin.ediCustoms.entity.edi.core;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Entity
public class TotalBillHead implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long ownerId;
	
	//进出口标记
	private Boolean isExport=true;
	
	
	
	//清单号 
	private String billNo;
	
	//区域类型 
	private int areaType;
	//进出区标识 
	private int ioFlag;
	//进区类型
	private int ioType;
	
	//申报海关
	private String customsCode;
	
	private Long oppositeEnterpriseId;
		
	private String emsNo;
	private String linkBillType;
	//关联单证编号
	private String linkBillNo;
	
	//经营单位
	private Long runEnterpriseId;

	private int packNo;
	private String wrapType;
	private double grossWeight;
	private double netWeight;
	private Long contractHeadId;

	private String tradeMode;
	private String iePort;
	private Date declareTime;
	private Date ieDate;
	private String transportMode;
	//运输工具名称
	private String transportTool;
	private String entryBillNo;
	
	//征免方式 CutMode
	private String taxKind;
	private String licenseNo;
	//成交方式 TransMode
	private String dealMode;
	private double feeRate;
	private double insurRate;
	private double otherRate;
	private String contractNo;
	//集装箱个数
	private int containerNo;
	//备注 Notes
	private String note;
	//清单审核状态
	private int status;
	
	private String operator;
	private Date operateTime;
	//批文号 ApprNo
	private String certificationCode;
	private String destinationPort;
	private String tradeCountry;
	private int linkType;
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
	public Boolean getIsExport() {
		return isExport;
	}
	public void setIsExport(Boolean isExport) {
		this.isExport = isExport;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public int getAreaType() {
		return areaType;
	}
	public void setAreaType(int areaType) {
		this.areaType = areaType;
	}
	public int getIoFlag() {
		return ioFlag;
	}
	public void setIoFlag(int ioFlag) {
		this.ioFlag = ioFlag;
	}
	public int getIoType() {
		return ioType;
	}
	public void setIoType(int ioType) {
		this.ioType = ioType;
	}
	public String getCustomsCode() {
		return customsCode;
	}
	public void setCustomsCode(String customsCode) {
		this.customsCode = customsCode;
	}
	public Long getOppositeEnterpriseId() {
		return oppositeEnterpriseId;
	}
	public void setOppositeEnterpriseId(Long oppositeEnterpriseId) {
		this.oppositeEnterpriseId = oppositeEnterpriseId;
	}
	public String getEmsNo() {
		return emsNo;
	}
	public void setEmsNo(String emsNo) {
		this.emsNo = emsNo;
	}
	public String getLinkBillType() {
		return linkBillType;
	}
	public void setLinkBillType(String linkBillType) {
		this.linkBillType = linkBillType;
	}
	public String getLinkBillNo() {
		return linkBillNo;
	}
	public void setLinkBillNo(String linkBillNo) {
		this.linkBillNo = linkBillNo;
	}
	public Long getRunEnterpriseId() {
		return runEnterpriseId;
	}
	public void setRunEnterpriseId(Long runEnterpriseId) {
		this.runEnterpriseId = runEnterpriseId;
	}
	public int getPackNo() {
		return packNo;
	}
	public void setPackNo(int packNo) {
		this.packNo = packNo;
	}
	public String getWrapType() {
		return wrapType;
	}
	public void setWrapType(String wrapType) {
		this.wrapType = wrapType;
	}
	public double getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(double grossWeight) {
		this.grossWeight = grossWeight;
	}
	public double getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(double netWeight) {
		this.netWeight = netWeight;
	}
	public Long getContractHeadId() {
		return contractHeadId;
	}
	public void setContractHeadId(Long contractHeadId) {
		this.contractHeadId = contractHeadId;
	}
	public String getTradeMode() {
		return tradeMode;
	}
	public void setTradeMode(String tradeMode) {
		this.tradeMode = tradeMode;
	}
	public String getIePort() {
		return iePort;
	}
	public void setIePort(String iePort) {
		this.iePort = iePort;
	}
	public Date getDeclareTime() {
		return declareTime;
	}
	public void setDeclareTime(Date declareTime) {
		this.declareTime = declareTime;
	}
	public Date getIeDate() {
		return ieDate;
	}
	public void setIeDate(Date ieDate) {
		this.ieDate = ieDate;
	}
	public String getTransportMode() {
		return transportMode;
	}
	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
	}
	public String getTransportTool() {
		return transportTool;
	}
	public void setTransportTool(String transportTool) {
		this.transportTool = transportTool;
	}
	public String getEntryBillNo() {
		return entryBillNo;
	}
	public void setEntryBillNo(String entryBillNo) {
		this.entryBillNo = entryBillNo;
	}
	public String getTaxKind() {
		return taxKind;
	}
	public void setTaxKind(String taxKind) {
		this.taxKind = taxKind;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getDealMode() {
		return dealMode;
	}
	public void setDealMode(String dealMode) {
		this.dealMode = dealMode;
	}
	public double getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(double feeRate) {
		this.feeRate = feeRate;
	}
	public double getInsurRate() {
		return insurRate;
	}
	public void setInsurRate(double insurRate) {
		this.insurRate = insurRate;
	}
	public double getOtherRate() {
		return otherRate;
	}
	public void setOtherRate(double otherRate) {
		this.otherRate = otherRate;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public int getContainerNo() {
		return containerNo;
	}
	public void setContainerNo(int containerNo) {
		this.containerNo = containerNo;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public String getCertificationCode() {
		return certificationCode;
	}
	public void setCertificationCode(String certificationCode) {
		this.certificationCode = certificationCode;
	}
	public String getDestinationPort() {
		return destinationPort;
	}
	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}
	public String getTradeCountry() {
		return tradeCountry;
	}
	public void setTradeCountry(String tradeCountry) {
		this.tradeCountry = tradeCountry;
	}
	public int getLinkType() {
		return linkType;
	}
	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}
	


	
	
	
}
