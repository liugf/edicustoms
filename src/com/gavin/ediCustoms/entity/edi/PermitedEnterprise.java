package com.gavin.ediCustoms.entity.edi;

import java.io.Serializable;

public class PermitedEnterprise implements Serializable{	
	private Long enterpriseId;
	private String enterpriseCode;
	private String enterpriseName;
	
	//是否申报单位
	private Boolean isDeclare;
	
	//能否录入
	private Boolean canAdd;
	//能否修改
	private Boolean canUpdate;
	//能否删除
	private Boolean canDelete;
	//入库
	private Boolean canPass;
	//退库
	private Boolean canDisPass;
	
	public PermitedEnterprise(){
		
	}
	
	public PermitedEnterprise(Enterprise enterprise,Permission permission){
		enterpriseId=enterprise.getId();
		enterpriseCode=enterprise.getTradeCode();
		enterpriseName=enterprise.getRegisteName();
		canAdd=permission.getCanAdd();
		canUpdate=permission.getCanUpdate();
		canDelete=permission.getCanDelete();
		canPass=permission.getCanPass();
		canDisPass=permission.getCanDisPass();
		isDeclare=enterprise.getIsDeclare();
	}	

	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
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
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public Boolean getIsDeclare() {
		return isDeclare;
	}

	public void setIsDeclare(Boolean isDeclare) {
		this.isDeclare = isDeclare;
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
