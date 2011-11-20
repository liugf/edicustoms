package com.gavin.ediCustoms.entity.edi;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AccountRecord implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long enterpriseId;
	private Long administratorId;
	private Long userId;
	private Double moneyBefore;
	private Double moneyAfter;
	//操作金额
	private Double increase;
	//备注
	private String note;
	//操作日期
	private Date created;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public Long getAdministratorId() {
		return administratorId;
	}
	public void setAdministratorId(Long administratorId) {
		this.administratorId = administratorId;
	}
	public Double getIncrease() {
		return increase;
	}
	public void setIncrease(Double increase) {
		this.increase = increase;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Double getMoneyBefore() {
		return moneyBefore;
	}
	public void setMoneyBefore(Double moneyBefore) {
		this.moneyBefore = moneyBefore;
	}
	public Double getMoneyAfter() {
		return moneyAfter;
	}
	public void setMoneyAfter(Double moneyAfter) {
		this.moneyAfter = moneyAfter;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
}
