package com.gavin.ediCustoms.server.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gavin.ediCustoms.entity.edi.AccountRecord;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.server.dao.AccountRecordDao;
import com.gavin.ediCustoms.server.dao.EnterpriseDao;

@Service("moneyManager")
public class MoneyManager {
	@Autowired
	private AccountRecordDao accountRecordDao;
	@Autowired
	private EnterpriseDao enterpriseDao;
	
	public Double alter(long enterpriseId,long operatorId, double increase, String note, String operatorType){
		Enterprise enterprise= enterpriseDao.get(enterpriseId);
		if (enterprise == null) {
			return null;
		}
		double moneyBefore = enterprise.getMoney();
		double moneyAfter = moneyBefore+increase;
		enterprise.setMoney(moneyAfter);
		enterpriseDao.update(enterprise);
		AccountRecord accountRecord=new AccountRecord();
		accountRecord.setEnterpriseId(enterpriseId);
		
		if (operatorType == "admin") {
			accountRecord.setAdministratorId(operatorId);
		}else {
			accountRecord.setUserId(operatorId);
		}
		
		accountRecord.setMoneyBefore(moneyBefore);
		accountRecord.setMoneyAfter(moneyAfter);
		accountRecord.setIncrease(increase);
		accountRecord.setNote(note);
		accountRecord.setCreated(new Date());
		accountRecordDao.save(accountRecord);
		return moneyAfter;		
	}
}
