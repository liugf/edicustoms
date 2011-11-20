package com.gavin.ediCustoms.entity.edi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RunRelation implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long runEnterpriseId;
	private Long manufactureEnterpriseId;

	public RunRelation() {

	}

	public RunRelation(Long runEnterpriseId,Long manufactureEnterpriseId) {
		this.runEnterpriseId=runEnterpriseId;
		this.manufactureEnterpriseId=manufactureEnterpriseId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRunEnterpriseId() {
		return runEnterpriseId;
	}

	public void setRunEnterpriseId(Long runEnterpriseId) {
		this.runEnterpriseId = runEnterpriseId;
	}

	public Long getManufactureEnterpriseId() {
		return manufactureEnterpriseId;
	}

	public void setManufactureEnterpriseId(Long manufactureEnterpriseId) {
		this.manufactureEnterpriseId = manufactureEnterpriseId;
	}

}
