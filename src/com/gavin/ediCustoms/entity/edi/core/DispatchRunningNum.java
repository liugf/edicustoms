package com.gavin.ediCustoms.entity.edi.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DispatchRunningNum {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private long RunningNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getRunningNum() {
		return RunningNum;
	}

	public void setRunningNum(long runningNum) {
		RunningNum = runningNum;
	}
	
	
}
