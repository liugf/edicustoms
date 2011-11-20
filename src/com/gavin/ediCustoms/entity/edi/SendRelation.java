package com.gavin.ediCustoms.entity.edi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SendRelation implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long sendEnterpriseId;
	private Long receiveEnterpriseId;

	public SendRelation() {

	}

	public SendRelation(Long sendEnterpriseId,Long receiveEnterpriseId) {
		this.sendEnterpriseId=sendEnterpriseId;
		this.receiveEnterpriseId=receiveEnterpriseId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSendEnterpriseId() {
		return sendEnterpriseId;
	}

	public void setSendEnterpriseId(Long sendEnterpriseId) {
		this.sendEnterpriseId = sendEnterpriseId;
	}

	public Long getReceiveEnterpriseId() {
		return receiveEnterpriseId;
	}

	public void setReceiveEnterpriseId(Long receiveEnterpriseId) {
		this.receiveEnterpriseId = receiveEnterpriseId;
	}

}
