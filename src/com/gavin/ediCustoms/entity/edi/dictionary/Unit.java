package com.gavin.ediCustoms.entity.edi.dictionary;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Unit implements Serializable,BaseItem{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String code;
	private String name;
	private String convertCode;
	private double convertRate;
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
	public String getConvertCode() {
		return convertCode;
	}
	public void setConvertCode(String convertCode) {
		this.convertCode = convertCode;
	}
	public double getConvertRate() {
		return convertRate;
	}
	public void setConvertRate(double convertRate) {
		this.convertRate = convertRate;
	}
	
	
}
