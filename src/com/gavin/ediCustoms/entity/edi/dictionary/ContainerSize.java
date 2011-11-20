package com.gavin.ediCustoms.entity.edi.dictionary;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class ContainerSize implements Serializable,BaseItem{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String code;
	private String size;
	private int valentNum; //相当的标准箱个数
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
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getValentNum() {
		return valentNum;
	}
	public void setValentNum(int valentNum) {
		this.valentNum = valentNum;
	}
	
	
}
