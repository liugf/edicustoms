package com.gavin.ediCustoms.entity.edi;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Permission implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long userId;
	private Long enterpriseId;
	//能否录入
	private Boolean canAdd=true;
	//能否修改
	private Boolean canUpdate=true;
	//能否删除
	private Boolean canDelete=true;	
	
	//入库
	private Boolean canPass=false;
	//退库
	private Boolean canDisPass=false;
	
	public Permission(){
		
	}
	
	public Permission(Long userId,Long enterpriseId){
		this.userId=userId;
		this.enterpriseId=enterpriseId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Boolean getCanAdd() {
		return canAdd;
	}

	public void setCanAdd(Boolean canAdd) {
		this.canAdd = canAdd;
	}

	public Boolean getCanUpdate() {
		return canUpdate;
	}

	public void setCanUpdate(Boolean canUpdate) {
		this.canUpdate = canUpdate;
	}

	public Boolean getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(Boolean canDelete) {
		this.canDelete = canDelete;
	}

	public Boolean getCanPass() {
		return canPass;
	}

	public void setCanPass(Boolean canPass) {
		this.canPass = canPass;
	}

	public Boolean getCanDisPass() {
		return canDisPass;
	}

	public void setCanDisPass(Boolean canDisPass) {
		this.canDisPass = canDisPass;
	}
	
	
	
}
