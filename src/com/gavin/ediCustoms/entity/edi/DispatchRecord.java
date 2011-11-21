package com.gavin.ediCustoms.entity.edi;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DispatchRecord {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long customsDeclarationHeadId;
	private String messageId;
	private String taskId;
	
	//Channel
	private String channel;
	//产生的数据中心的统一编号
	private String eportNo;
	private String note;
	
	//时间
	private Date date;
	
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
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getEportNo() {
		return eportNo;
	}
	public void setEportNo(String eportNo) {
		this.eportNo = eportNo;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
	
}
