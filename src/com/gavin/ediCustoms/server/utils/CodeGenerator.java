package com.gavin.ediCustoms.server.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.admin.CompanyMessage;
import com.gavin.ediCustoms.server.dao.EnterpriseDao;
import com.gavin.ediCustoms.server.dao.admin.CompanyMessageDao;

@Service("codeGenerator")
public class CodeGenerator{
	private CompanyMessageDao companyMessageDao;
	private EnterpriseDao enterpriseDao;

	@Autowired
	public void setCompanyMessageDao(CompanyMessageDao companyMessageDao) {
		this.companyMessageDao = companyMessageDao;
	}

	@Autowired
	public void setEnterpriseDao(EnterpriseDao enterpriseDao) {
		this.enterpriseDao = enterpriseDao;
	}


	public String getPreEntryId(Long enterpriseId, String customsCode, boolean isExport) {
		CompanyMessage companyMessage=companyMessageDao.list().get(0);
		String ediPlatformCode=companyMessage.getEdiPlatformCode();
		ediPlatformCode="0000"+ediPlatformCode;
		ediPlatformCode=ediPlatformCode.substring(ediPlatformCode.length()-4, ediPlatformCode.length());
		
		Enterprise enterprise=enterpriseDao.get(enterpriseId);
		String ediCode=enterprise.getEdiCode();
		ediCode="0000"+ediCode;
		ediCode=ediCode.substring(ediCode.length()-4, ediCode.length());		
		
		customsCode="00"+customsCode;
		customsCode=customsCode.substring(customsCode.length()-2, customsCode.length());
		if (isExport) {
			int i=Integer.valueOf(customsCode)+50;
			customsCode=String.valueOf(i);
		}
		Date date=new Date();
		@SuppressWarnings("deprecation")
		String year=String.valueOf(date.getYear()+1900);
		return "0"+ediPlatformCode+ediCode+customsCode+year.substring(3, 4)+getRunningCode(enterprise);
	}
	
	public String getMessageId(){
		
		return null;		
	}
	
	public String getBillNo(Long enterpriseId, String customsCode, boolean isExport) {
		CompanyMessage companyMessage=companyMessageDao.list().get(0);
		String ediPlatformCode=companyMessage.getEdiPlatformCode();
		ediPlatformCode="0000"+ediPlatformCode;
		ediPlatformCode=ediPlatformCode.substring(ediPlatformCode.length()-4, ediPlatformCode.length());
		
		Enterprise enterprise=enterpriseDao.get(enterpriseId);
		String ediCode=enterprise.getEdiCode();
		ediCode="0000"+ediCode;
		ediCode=ediCode.substring(ediCode.length()-4, ediCode.length());		
		
		customsCode="00"+customsCode;
		customsCode=customsCode.substring(customsCode.length()-2, customsCode.length());
		if (isExport) {
			int i=Integer.valueOf(customsCode)+50;
			customsCode=String.valueOf(i);
		}
		Date date=new Date();
		@SuppressWarnings("deprecation")
		String year=String.valueOf(date.getYear()+1900);
		return "00"+ediPlatformCode+ediCode+customsCode+year.substring(3, 4)+getRunningCode(enterprise);
	}
	
	
	private String getRunningCode(Enterprise enterprise){
		String runningCode=String.valueOf(enterprise.getRunningNum());
		runningCode="000000"+runningCode;
		runningCode=runningCode.substring(runningCode.length()-6, runningCode.length());		
		enterprise.setRunningNum((enterprise.getRunningNum()+1)%1000000);
		enterpriseDao.update(enterprise);
		return runningCode;
	}

}
