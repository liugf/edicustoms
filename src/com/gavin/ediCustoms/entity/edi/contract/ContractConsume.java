package com.gavin.ediCustoms.entity.edi.contract;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class ContractConsume implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private Long contractProductId;
	private Long contractMaterialId;	
	private double used;
	private double wasted;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getContractProductId() {
		return contractProductId;
	}
	public void setContractProductId(Long contractProductId) {
		this.contractProductId = contractProductId;
	}
	public Long getContractMaterialId() {
		return contractMaterialId;
	}
	public void setContractMaterialId(Long contractMaterialId) {
		this.contractMaterialId = contractMaterialId;
	}
	public double getUsed() {
		return used;
	}
	public void setUsed(double used) {
		this.used = used;
	}
	public double getWasted() {
		return wasted;
	}
	public void setWasted(double wasted) {
		this.wasted = wasted;
	}
	
		
}
