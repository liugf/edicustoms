package com.gavin.ediCustoms.client.service;


import java.util.Date;
import java.util.List;

import com.gavin.ediCustoms.entity.edi.AccountRecord;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.ForeignEnterprise;
import com.gavin.ediCustoms.entity.edi.Permission;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.entity.edi.RunRelation;
import com.gavin.ediCustoms.entity.edi.SendRelation;
import com.gavin.ediCustoms.entity.edi.admin.Administrator;
import com.gavin.ediCustoms.entity.edi.admin.CompanyMessage;
import com.gavin.ediCustoms.entity.share.User;
import com.gavin.ediCustoms.shared.UserState;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SystemServiceAsync {
	//CompanyMessage
	void getCompanyMessage(AsyncCallback<CompanyMessage> callback);
	void updateCompanyMessage(CompanyMessage companyMessage,AsyncCallback<Void> callback);
	
	//Administrator
	void getAdministrator(AsyncCallback<Administrator> callback);
	void updateAdministrator(Administrator administrator,AsyncCallback<Void> callback);
	
	//Enterprise
	void getEnterprise(Long id,AsyncCallback<Enterprise> callback);
	void listEnterprise(AsyncCallback<List<Enterprise>> callback);
	void saveEnterprise(Enterprise enterprise,AsyncCallback<Long> callback);
	void deleteEnterprise(List<Long> ids, AsyncCallback<Void> callback);
	void updateEnterprise(List<Enterprise> enterprises, AsyncCallback<Void> callback);	
	
	//ForeignEnterprise
	void getForeignEnterprise(Long id,AsyncCallback<ForeignEnterprise> callback);
	void listForeignEnterprise(AsyncCallback<List<ForeignEnterprise>> callback);
	void saveForeignEnterprise(ForeignEnterprise foreignEnterprise,AsyncCallback<Long> callback);
	void deleteForeignEnterprise(List<Long> ids, AsyncCallback<Void> callback);
	void updateForeignEnterprise(List<ForeignEnterprise> foreignEnterprises, AsyncCallback<Void> callback);
	
	//User
	void listUser(AsyncCallback<List<User>> callback);
	void saveUser(User user,List<Permission> permissions,AsyncCallback<Long> callback);
	void deleteUser(List<Long> ids, AsyncCallback<Void> callback);
	void updateUser(User user,List<Permission> permissions, AsyncCallback<Void> callback);
	void updatePassword(String password, AsyncCallback<String> callback);
	
	//Permission
	void listPermission(AsyncCallback<List<Permission>> callback);
	void getPermission(Long userId,Long enterpriseId,AsyncCallback<Permission> callback);
	
	//Login
	void validateUser(String userName,String password,String checkCode,AsyncCallback<String> callback);
	void validateAdmin(String adminName,String password,String checkCode,AsyncCallback<String> callback);
	
	//PermitedEnterprise
	void listPermitedEnterprise(AsyncCallback<List<PermitedEnterprise>> callback);
	
	//userState
	void getState(AsyncCallback<UserState> callback);
	
	//sendRelation
	void listSendEnterprise(Long enterpriseId,AsyncCallback<List<Enterprise>> callback);
	void listReceiveEnterprise(Long enterpriseId,AsyncCallback<List<Enterprise>> callback);
	void listRunEnterprise(Long enterpriseId,AsyncCallback<List<Enterprise>> callback);
	void listDeclareEnterprise(AsyncCallback<List<Enterprise>> callback);
	void updateRelation(Long enterpriseId,List<SendRelation> sendRelations,List<RunRelation> runRelations,AsyncCallback<Void> callback);
	
	//充值
	void charge(Long enterpriseId,double increase,String note,AsyncCallback<Double> callback);
	void listAccountRecords(Long enterpriseId,AsyncCallback<List<AccountRecord>> callback);
	void listAccountRecordsByPeriod(Long enterpriseId,Date start,Date end,AsyncCallback<List<AccountRecord>> callback);
}
