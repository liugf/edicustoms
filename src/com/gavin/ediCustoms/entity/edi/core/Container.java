package com.gavin.ediCustoms.entity.edi.core;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gavin.ediCustoms.entity.edi.MyJavaBean;


@Entity
public class Container extends MyJavaBean implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private Long customsDeclarationHeadId;
	
	private String containerNo;		
	private String size;
	private String type;
	private String bracket;
	private int valentNum;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustomsDeclarationHeadId() {
		return customsDeclarationHeadId;
	}
	public void setCustomsDeclarationHeadId(Long customsDeclarationHeadId) {
		this.customsDeclarationHeadId = customsDeclarationHeadId;
	}
	public String getContainerNo() {
		return containerNo;
	}
	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBracket() {
		return bracket;
	}
	public void setBracket(String bracket) {
		this.bracket = bracket;
	}
	public int getValentNum() {
		return valentNum;
	}
	public void setValentNum(int valentNum) {
		this.valentNum = valentNum;
	}
	

	
	
	
}
