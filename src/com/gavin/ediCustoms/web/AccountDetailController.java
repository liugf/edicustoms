package com.gavin.ediCustoms.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gavin.ediCustoms.entity.edi.AccountRecord;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.server.dao.AccountRecordDao;
import com.gavin.ediCustoms.server.dao.EnterpriseDao;

@Controller
public class AccountDetailController {
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/accountDetail.do")
	public String declareDetail(Long enterpriseId,Date start,Date end,ModelMap map) throws Exception {
		Enterprise enterprise=enterpriseDao.get(enterpriseId);
		List<AccountRecord> accountRecords=accountRecordDao.findByPeriod(enterpriseId, start, end);
		map.put("enterprise", enterprise);
		map.put("accountRecords", accountRecords);
		map.put("start", start);
		map.put("end", end);
		return "accountDetail";
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	
	@Autowired
	private EnterpriseDao enterpriseDao;
	@Autowired
	private AccountRecordDao accountRecordDao;
	
}
