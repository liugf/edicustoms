package com.gavin.ediCustoms.entity.edi.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gavin.ediCustoms.entity.edi.MyJavaBean;

@Entity
public class CustomsDeclarationHead extends MyJavaBean implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long ownerId;

	// 是否已经申报
	private Boolean isDeclared = false;

	// 进出口标记
	private Boolean isExport = true;

	// 是否入库
	private boolean isPass = false;

	private String preEntryId;
	private String entryId;
	// 电子口岸
	private String electronicPort;
	//海关统一编号
	private String ePortNo;

	private String loadPort;

	// 用途
	private String useage;

	private String transportMode;
	private String iePort;
	private String destinationPort;
	private String transportTool;

	// 经营单位
	private Long runEnterpriseId;

	// 货主单位地区代码
	private String ownerDistrict;

	// 申报单位
	private Long declareEnterpriseId;

	private String contractNo;
	// 内销比例
	private double saleInRatio;
	// 提运单或运单号
	private String billNo;
	private String tradeCountry;
	private String tradeMode;
	// 征免性质
	private String taxKind;
	// 纳税方式
	private String payMode;
	private String dealMode;
	// 外汇来源
	private String exchangeSource;
	// 收结汇方式
	private String payWay;
	private String feeMark;
	private String otherMark;
	private String insurMark;
	private String feeCurrency;
	private double feeRate;
	private String otherCurrency;
	private double otherRate;
	private String insurCurrency;
	private double insurRate;
	private int packNo;
	private double grossWeight;
	private double netWeight;
	private String licenseNo;
	// 批文号 ApprNo
	private String certificationCode;
	private Long contractHeadId;
	private Date ieDate;
	private Long declarantId;
	private String declarant;
	private String wrapType;
	// 随附单据
	private String certMark;
	private String note;
	private int itemsNo;
	private Date declareTime;
	private String operator;

	// 缴款单位标记
	private String giveTax;

	// 报税仓号
	private String baoShuiCangHao;
	
	
	
	
	//承运单位名称
	private String corporationName;
	//承运单位组织机构代码
	private String organizationCode;
	//进出境运输工具编号
	private String meansOfTransportCode;
	//进出境运输工具名称
	private String meansOfTransportName;
	//进出境运输工具航次
	private String meansOfTransportId;
	//进出境提单号
	private String billOfLadingNo;
	
	//境内运输方式
	private Integer localMeansOfTransportMode=4;
	//境内运输工具编号
	private String localMeansOfTransportCode;
	//境内运输工具名称
	private String localMeansOfTransportName;
	//境内运输工具航次
	private String localMeansOfTransportId;
	//IC卡号
	private String iccardNo;
	
	//进出口日期
	private Date importExportDate;
	
	//报关形式
	private String declareProperty="001";
	
	//汽车编号
	private String truckCode;
	
	//报表关代理费
	private double proxyCost;
	//报表关服务费
	private double serviceCost;

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

	public String getPreEntryId() {
		return preEntryId;
	}

	public void setPreEntryId(String preEntryId) {
		this.preEntryId = preEntryId;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public String getTransportMode() {
		return transportMode;
	}

	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
	}

	public String getIePort() {
		return iePort;
	}

	public void setIePort(String iePort) {
		this.iePort = iePort;
	}

	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getTransportTool() {
		return transportTool;
	}

	public void setTransportTool(String transportTool) {
		this.transportTool = transportTool;
	}

	public String getOwnerDistrict() {
		return ownerDistrict;
	}

	public void setOwnerDistrict(String ownerDistrict) {
		this.ownerDistrict = ownerDistrict;
	}

	public Long getDeclareEnterpriseId() {
		return declareEnterpriseId;
	}

	public void setDeclareEnterpriseId(Long declareEnterpriseId) {
		this.declareEnterpriseId = declareEnterpriseId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public double getSaleInRatio() {
		return saleInRatio;
	}

	public void setSaleInRatio(double saleInRatio) {
		this.saleInRatio = saleInRatio;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getTradeCountry() {
		return tradeCountry;
	}

	public void setTradeCountry(String tradeCountry) {
		this.tradeCountry = tradeCountry;
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

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getDealMode() {
		return dealMode;
	}

	public void setDealMode(String dealMode) {
		this.dealMode = dealMode;
	}

	public String getExchangeSource() {
		return exchangeSource;
	}

	public void setExchangeSource(String exchangeSource) {
		this.exchangeSource = exchangeSource;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getFeeMark() {
		return feeMark;
	}

	public void setFeeMark(String feeMark) {
		this.feeMark = feeMark;
	}

	public String getOtherMark() {
		return otherMark;
	}

	public void setOtherMark(String otherMark) {
		this.otherMark = otherMark;
	}

	public String getInsurMark() {
		return insurMark;
	}

	public void setInsurMark(String insurMark) {
		this.insurMark = insurMark;
	}

	public String getFeeCurrency() {
		return feeCurrency;
	}

	public void setFeeCurrency(String feeCurrency) {
		this.feeCurrency = feeCurrency;
	}

	public double getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(double feeRate) {
		this.feeRate = feeRate;
	}

	public String getOtherCurrency() {
		return otherCurrency;
	}

	public void setOtherCurrency(String otherCurrency) {
		this.otherCurrency = otherCurrency;
	}

	public double getOtherRate() {
		return otherRate;
	}

	public void setOtherRate(double otherRate) {
		this.otherRate = otherRate;
	}

	public String getInsurCurrency() {
		return insurCurrency;
	}

	public void setInsurCurrency(String insurCurrency) {
		this.insurCurrency = insurCurrency;
	}

	public double getInsurRate() {
		return insurRate;
	}

	public void setInsurRate(double insurRate) {
		this.insurRate = insurRate;
	}

	public int getPackNo() {
		return packNo;
	}

	public void setPackNo(int packNo) {
		this.packNo = packNo;
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

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getCertificationCode() {
		return certificationCode;
	}

	public void setCertificationCode(String certificationCode) {
		this.certificationCode = certificationCode;
	}

	public Long getContractHeadId() {
		return contractHeadId;
	}

	public void setContractHeadId(Long contractHeadId) {
		this.contractHeadId = contractHeadId;
	}

	public Date getIeDate() {
		return ieDate;
	}

	public void setIeDate(Date ieDate) {
		this.ieDate = ieDate;
	}

	public Long getDeclarantId() {
		return declarantId;
	}

	public void setDeclarantId(Long declarantId) {
		this.declarantId = declarantId;
	}

	public String getWrapType() {
		return wrapType;
	}

	public void setWrapType(String wrapType) {
		this.wrapType = wrapType;
	}

	public String getCertMark() {
		return certMark;
	}

	public void setCertMark(String certMark) {
		this.certMark = certMark;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getItemsNo() {
		return itemsNo;
	}

	public void setItemsNo(int itemsNo) {
		this.itemsNo = itemsNo;
	}

	public Date getDeclareTime() {
		return declareTime;
	}

	public void setDeclareTime(Date declareTime) {
		this.declareTime = declareTime;
	}

	public String getGiveTax() {
		return giveTax;
	}

	public void setGiveTax(String giveTax) {
		this.giveTax = giveTax;
	}

	public Long getRunEnterpriseId() {
		return runEnterpriseId;
	}

	public void setRunEnterpriseId(Long runEnterpriseId) {
		this.runEnterpriseId = runEnterpriseId;
	}

	public boolean isPass() {
		return isPass;
	}

	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}

	public Boolean getIsExport() {
		return isExport;
	}

	public void setIsExport(Boolean isExport) {
		this.isExport = isExport;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getElectronicPort() {
		return electronicPort;
	}

	public void setElectronicPort(String electronicPort) {
		this.electronicPort = electronicPort;
	}

	public String getUseage() {
		return useage;
	}

	public void setUseage(String useage) {
		this.useage = useage;
	}

	public String getBaoShuiCangHao() {
		return baoShuiCangHao;
	}

	public void setBaoShuiCangHao(String baoShuiCangHao) {
		this.baoShuiCangHao = baoShuiCangHao;
	}

	public String getLoadPort() {
		return loadPort;
	}

	public void setLoadPort(String loadPort) {
		this.loadPort = loadPort;
	}

	public Boolean getIsDeclared() {
		return isDeclared;
	}

	public void setIsDeclared(Boolean isDeclared) {
		this.isDeclared = isDeclared;
	}

	public String getCorporationName() {
		return corporationName;
	}

	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getMeansOfTransportCode() {
		return meansOfTransportCode;
	}

	public void setMeansOfTransportCode(String meansOfTransportCode) {
		this.meansOfTransportCode = meansOfTransportCode;
	}

	public String getMeansOfTransportName() {
		return meansOfTransportName;
	}

	public void setMeansOfTransportName(String meansOfTransportName) {
		this.meansOfTransportName = meansOfTransportName;
	}

	public String getMeansOfTransportId() {
		return meansOfTransportId;
	}

	public void setMeansOfTransportId(String meansOfTransportId) {
		this.meansOfTransportId = meansOfTransportId;
	}

	public String getBillOfLadingNo() {
		return billOfLadingNo;
	}

	public void setBillOfLadingNo(String billOfLadingNo) {
		this.billOfLadingNo = billOfLadingNo;
	}


	public Integer getLocalMeansOfTransportMode() {
		return localMeansOfTransportMode;
	}

	public void setLocalMeansOfTransportMode(Integer localMeansOfTransportMode) {
		this.localMeansOfTransportMode = localMeansOfTransportMode;
	}

	public String getLocalMeansOfTransportCode() {
		return localMeansOfTransportCode;
	}

	public void setLocalMeansOfTransportCode(String localMeansOfTransportCode) {
		this.localMeansOfTransportCode = localMeansOfTransportCode;
	}

	public String getLocalMeansOfTransportName() {
		return localMeansOfTransportName;
	}

	public void setLocalMeansOfTransportName(String localMeansOfTransportName) {
		this.localMeansOfTransportName = localMeansOfTransportName;
	}

	public String getLocalMeansOfTransportId() {
		return localMeansOfTransportId;
	}

	public void setLocalMeansOfTransportId(String localMeansOfTransportId) {
		this.localMeansOfTransportId = localMeansOfTransportId;
	}

	public String getIccardNo() {
		return iccardNo;
	}

	public void setIccardNo(String iccardNo) {
		this.iccardNo = iccardNo;
	}

	public String getDeclarant() {
		return declarant;
	}

	public void setDeclarant(String declarant) {
		this.declarant = declarant;
	}

	public Date getImportExportDate() {
		return importExportDate;
	}

	public void setImportExportDate(Date importExportDate) {
		this.importExportDate = importExportDate;
	}

	public String getDeclareProperty() {
		return declareProperty;
	}

	public void setDeclareProperty(String declareProperty) {
		this.declareProperty = declareProperty;
	}

	public String getePortNo() {
		return ePortNo;
	}

	public void setePortNo(String ePortNo) {
		this.ePortNo = ePortNo;
	}

	public String getTruckCode() {
		return truckCode;
	}

	public void setTruckCode(String truckCode) {
		this.truckCode = truckCode;
	}

	public double getProxyCost() {
		return proxyCost;
	}

	public void setProxyCost(double proxyCost) {
		this.proxyCost = proxyCost;
	}

	public double getServiceCost() {
		return serviceCost;
	}

	public void setServiceCost(double serviceCost) {
		this.serviceCost = serviceCost;
	}


}
