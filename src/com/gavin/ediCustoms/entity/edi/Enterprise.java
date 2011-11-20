package com.gavin.ediCustoms.entity.edi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Enterprise implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/* begin 标准数据接口 */
	// 企业注册号 TRADE_CO
	private String tradeCode;
	// 企业注册名称(简) REG_CO
	private String registeName;
	// 注册海关代码 CUSTOMS_CODE
	private String customsCode;
	// 操作人 Operator
	private String operator;
	// 操作时间 OperTime
	private String operateTime;
	/* end 标准数据接口 */

	private String ownerCode;

	// 是否申报单位
	private Boolean isDeclare = false;

	// 地址
	private String address;
	// 联系人
	private String contact;
	// 法人代码
	private String legalPersonCode;
	private String telephone;
	private String fax;

	// EDI用户编码
	private String ediCode;

	// 开户银行
	private String bank;
	// 账户
	private String bankAccount;

	// 流水号
	private int runningNum;

	// 集成通平台用户编号
	private String jichengtongId;
	// 区域节点名称
	private String quyujiedian;
	// 集成通平台用户私钥
	private String userPrivateKey;
	// 集成通企业业务流程编号
	private String bpNo;
	//IC卡号
	private String icCardNo;
	//IC卡的用户证书号
	private String certificateNo;
	
	//ftp username
	private String ftpUsername;
	//ftp password
	private String ftpPassword;
	//账户金额
	private double money;
	
	//每张报表关代理费
	private double proxyCostPerDeal;
	//每张报表关服务费
	private double serviceCostPerDeal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getRegisteName() {
		return registeName;
	}

	public void setRegisteName(String registeName) {
		this.registeName = registeName;
	}

	public String getCustomsCode() {
		return customsCode;
	}

	public void setCustomsCode(String customsCode) {
		this.customsCode = customsCode;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getLegalPersonCode() {
		return legalPersonCode;
	}

	public void setLegalPersonCode(String legalPersonCode) {
		this.legalPersonCode = legalPersonCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Boolean getIsDeclare() {
		return isDeclare;
	}

	public void setIsDeclare(Boolean isDeclare) {
		this.isDeclare = isDeclare;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getEdiCode() {
		return ediCode;
	}

	public void setEdiCode(String ediCode) {
		this.ediCode = ediCode;
	}

	public int getRunningNum() {
		return runningNum;
	}

	public void setRunningNum(int runningNum) {
		this.runningNum = runningNum;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getJichengtongId() {
		return jichengtongId;
	}

	public void setJichengtongId(String jichengtongId) {
		this.jichengtongId = jichengtongId;
	}

	public String getQuyujiedian() {
		return quyujiedian;
	}

	public void setQuyujiedian(String quyujiedian) {
		this.quyujiedian = quyujiedian;
	}

	public String getUserPrivateKey() {
		return userPrivateKey;
	}

	public void setUserPrivateKey(String userPrivateKey) {
		this.userPrivateKey = userPrivateKey;
	}

	public String getBpNo() {
		return bpNo;
	}

	public void setBpNo(String bpNo) {
		this.bpNo = bpNo;
	}

	public String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getProxyCostPerDeal() {
		return proxyCostPerDeal;
	}

	public void setProxyCostPerDeal(double proxyCostPerDeal) {
		this.proxyCostPerDeal = proxyCostPerDeal;
	}

	public String getIcCardNo() {
		return icCardNo;
	}

	public void setIcCardNo(String icCardNo) {
		this.icCardNo = icCardNo;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public double getServiceCostPerDeal() {
		return serviceCostPerDeal;
	}

	public void setServiceCostPerDeal(double serviceCostPerDeal) {
		this.serviceCostPerDeal = serviceCostPerDeal;
	}

	



}
