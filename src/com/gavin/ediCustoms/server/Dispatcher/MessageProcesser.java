package com.gavin.ediCustoms.server.Dispatcher;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationHeadDao;
import com.gavin.ediCustoms.server.dao.EnterpriseDao;
import com.gavin.ediCustoms.server.utils.MoneyManager;

@Service("messageProcesser")
public class MessageProcesser {
	public void createRecord(Long customsDeclarationHeadId,File messageFile){
		SAXReader reader = new SAXReader();
		try {
			DispatchRecord dispatchRecord=new DispatchRecord();
			if (messageFile.exists()) {
				Map<String,String> map = new HashMap<String,String>();
		        map.put("tcs","http://www.chinaport.gov.cn/tcs");
		        reader.getDocumentFactory().setXPathNamespaceURIs(map);
				Document document = reader.read(messageFile);								
				@SuppressWarnings("unchecked")
				List<Element> list=document.selectNodes("/TCS101Message/MessageHead/MessageId");
				dispatchRecord.setCustomsDeclarationHeadId(customsDeclarationHeadId);
				dispatchRecord.setMessageId(list.get(0).getText());
				dispatchRecord.setChannel("000");
				dispatchRecord.setNote("生成报文成功");
				dispatchRecord.setDate(new Date());
				dispatchRecordDao.save(dispatchRecord);				
				
				/*CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(dispatchRecord.getCustomsDeclarationHeadId());
				customsDeclarationHead.setIsDeclared(true);		
				customsDeclarationHeadDao.update(customsDeclarationHead);*/
			}
		} catch (Exception e) {			
			e.printStackTrace();
			return;
		}
	}
	
	
	private void updateTaskId(String requestMessageId,String taskId){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("channel", "000");
		map.put("messageId", requestMessageId);
		List<DispatchRecord> list=dispatchRecordDao.findAnd(map);
		if (list.size()==0) {
			return;
		}
		DispatchRecord dispatchRecord=list.get(0);	
		dispatchRecord.setTaskId(taskId);
		dispatchRecordDao.update(dispatchRecord);
	}
	
	private void updateEportNo(String taskId,String eportNo,String channel,String note,String entryNo){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("channel", "000");
		map.put("taskId", taskId);
		List<DispatchRecord> list=dispatchRecordDao.findAnd(map);
		if (list.size()==0) {
			return;
		}
		DispatchRecord oldRecord=list.get(0);
		DispatchRecord newRecord= new DispatchRecord();
		newRecord.setCustomsDeclarationHeadId(oldRecord.getCustomsDeclarationHeadId());
		newRecord.setTaskId(taskId);
		newRecord.setMessageId(oldRecord.getMessageId());
		
		newRecord.setEportNo(eportNo);
		newRecord.setChannel(channel);
		newRecord.setNote(note);
		newRecord.setDate(new Date());
		dispatchRecordDao.save(newRecord);
		
		if (channel.equals("016")) {
			CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(oldRecord.getCustomsDeclarationHeadId());
			if (!customsDeclarationHead.isPass()) {
				customsDeclarationHead.setElectronicPort(eportNo);	
				customsDeclarationHead.setEntryId(entryNo);
				customsDeclarationHead.setPass(true);
				Enterprise enterprise = enterpriseDao.get(customsDeclarationHead
						.getOwnerId());
				moneyManager.alter(customsDeclarationHead.getOwnerId(), 0, -enterprise.getProxyCostPerDeal(), entryNo+"代收费", "user");
				customsDeclarationHead.setProxyCost(enterprise.getProxyCostPerDeal());
				moneyManager.alter(customsDeclarationHead.getOwnerId(), 0, -enterprise.getServiceCostPerDeal(), entryNo+"服务费", "user");
				customsDeclarationHead.setServiceCost(enterprise.getServiceCostPerDeal());
				customsDeclarationHead.setIsDeclared(true);		
				customsDeclarationHeadDao.update(customsDeclarationHead);
			}
			
		}
	}
	
	/**
	 * @param address
	 */
	@SuppressWarnings("unchecked")	
	public void processReturnMessage(String address){
		File responeFile=new File(address);
		SAXReader reader = new SAXReader();
		
		if (responeFile.exists()) {
			try {
				Map<String,String> map = new HashMap<String,String>();
				map.put("tcs","http://www.chinaport.gov.cn/tcs/v2");
		        reader.getDocumentFactory().setXPathNamespaceURIs(map);
				Document document = reader.read(responeFile);
				List<Element> list=document.selectNodes("/TCS101Message/MessageHead/MessageType");
				String messageType=list.get(0).getText();
				if (messageType.equals("TcsFlow201Response")) {
					
					String taskId=null;
					list=document.selectNodes("//tcs:TaskId");
					if (list.size()!=0) {
						taskId=list.get(0).getText();	
					}
							
					String requestMessageId=null;
					list=document.selectNodes("//tcs:RequestMessageId");
					if (list.size()!=0) {
						requestMessageId=list.get(0).getText();
					}
					
					updateTaskId(requestMessageId, taskId);
				
				}else {
					String taskId=null;
					list=document.selectNodes("//tcs:TaskId");
					if (list.size()!=0) {
						taskId=list.get(0).getText();	
					}
					
					String eportNo=null;
					list=document.selectNodes("//tcs:EportNo");
					if (list.size()!=0) {
						eportNo=list.get(0).getText();
					}
					
					String channel=null;
					list=document.selectNodes("//tcs:Channel");
					if (list.size()!=0) {
						channel=list.get(0).getText();
					}
					
					String note=null;
					String entryNo=null;
					if (channel == null) {
						return;
					} else if (channel.equals("001")) {
						list=document.selectNodes("//tcs:ResultInformation");
						note=list.get(0).getText();
					} else if (channel.equals("016")) {
						list=document.selectNodes("//tcs:Note");
						note=list.get(0).getText();
						list=document.selectNodes("//tcs:EntryNo");
						entryNo=list.get(0).getText();
					}else {
						list=document.selectNodes("//tcs:Note");
						note=list.get(0).getText();
					}
					
					updateEportNo(taskId, eportNo,channel,note,entryNo);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
		/*try {
			if (responeFile.exists()) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("tcs","http://www.chinaport.gov.cn/tcs/v2");
		        reader.getDocumentFactory().setXPathNamespaceURIs(map);
				Document document = reader.read(responeFile);
				List<Element> list=document.selectNodes("/TCS101Message/MessageHead/MessageType");
				String messageType=list.get(0).getText();
				if (messageType.equals("TcsFlow201Response")) {
					
					list=document.selectNodes("//tcs:TaskId");
					String taskId=list.get(0).getText();	
										
					list=document.selectNodes("//tcs:RequestMessageId");
					String requestMessageId=list.get(0).getText();	
					
					updateTaskId(requestMessageId, taskId);
				
				}else {
					list=document.selectNodes("//tcs:TaskId");
					String taskId=list.get(0).getText();	
					
					list=document.selectNodes("//tcs:EportNo");
					String eportNo=list.get(0).getText();
					
					list=document.selectNodes("//tcs:Channel");
					String channel=list.get(0).getText();
					
					String note=null;
					String entryNo=null;
					if (channel.equals("001")) {
						list=document.selectNodes("//tcs:ResultInformation");
						note=list.get(0).getText();
					} else if (channel.equals("016")) {
						list=document.selectNodes("//tcs:Note");
						note=list.get(0).getText();
						list=document.selectNodes("//tcs:EntryNo");
						entryNo=list.get(0).getText();
					}else {
						list=document.selectNodes("//tcs:Note");
						note=list.get(0).getText();
					}
					
					updateEportNo(taskId, eportNo,channel,note,entryNo);
					
				}
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}*/
		
	}
	
	@Autowired
	private DispatchRecordDao dispatchRecordDao;
	@Autowired
	private CustomsDeclarationHeadDao customsDeclarationHeadDao;
	@Autowired
	private MoneyManager moneyManager;
	@Autowired
	private EnterpriseDao enterpriseDao;
}
