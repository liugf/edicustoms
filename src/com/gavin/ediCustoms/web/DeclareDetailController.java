package com.gavin.ediCustoms.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.dictionary.ReceiptType;
import com.gavin.ediCustoms.server.Dispatcher.DispatchRecord;
import com.gavin.ediCustoms.server.Dispatcher.DispatchRecordDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationHeadDao;
import com.gavin.ediCustoms.server.dao.dictionary.ReceiptTypeDao;

@Controller
public class DeclareDetailController {
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/declareDetail.do")
	public String declareDetail(Long id,ModelMap map) throws Exception {
		CustomsDeclarationHead customsDeclarationHead = customsDeclarationHeadDao
		.get(id);
		List<DispatchRecord> dispatchRecords = dispatchRecordDao.find("customsDeclarationHeadId", customsDeclarationHead.getId());
		List<ReceiptType> receiptTypes = receiptTypeDao.list();
		Map<String, String> receiptTypeMap = new HashMap<String, String>();
		for (ReceiptType receiptType : receiptTypes) {
			receiptTypeMap.put(receiptType.getCode(), receiptType.getName());
		}
		for (DispatchRecord dispatchRecord : dispatchRecords) {
			String status;
			status = receiptTypeMap.get(dispatchRecord.getChannel());
			if (status!=null) {
				dispatchRecord.setChannel(dispatchRecord.getChannel()+status);
			}
		}
		
		map.put("customsDeclarationHead", customsDeclarationHead);
		map.put("dispatchRecords", dispatchRecords);
		
		return "declareDetail";
	}
	
	@Autowired
	private CustomsDeclarationHeadDao customsDeclarationHeadDao;
	@Autowired
	private DispatchRecordDao dispatchRecordDao;
	@Autowired
	private ReceiptTypeDao receiptTypeDao;
}
