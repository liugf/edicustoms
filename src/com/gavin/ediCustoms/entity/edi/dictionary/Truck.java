package com.gavin.ediCustoms.entity.edi.dictionary;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Truck implements Serializable,BaseItem{	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String code;//汽车编号
	private String icCard;
	private String truckNo;//大陆车牌
	private String hkTruckNo;//香港车牌
	//承运单位名称
	private String corporationName;
	//承运单位组织机构代码
	private String organizationCode;
	//承运单位地址
	private String organizationAddress;
	//承运单位电话
	private String organizationTelephone;
	//司机姓名
	private String driverName;
	
	
	
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
	public String getIcCard() {
		return icCard;
	}
	public void setIcCard(String icCard) {
		this.icCard = icCard;
	}
	public String getTruckNo() {
		return truckNo;
	}
	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}
	public String getHkTruckNo() {
		return hkTruckNo;
	}
	public void setHkTruckNo(String hkTruckNo) {
		this.hkTruckNo = hkTruckNo;
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
	public String getOrganizationAddress() {
		return organizationAddress;
	}
	public void setOrganizationAddress(String organizationAddress) {
		this.organizationAddress = organizationAddress;
	}
	public String getOrganizationTelephone() {
		return organizationTelephone;
	}
	public void setOrganizationTelephone(String organizationTelephone) {
		this.organizationTelephone = organizationTelephone;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
	
}
