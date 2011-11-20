package com.gavin.ediCustoms.server.Dispatcher;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.server.config.ApplicationConfiguration;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationHeadDao;
import com.gavin.ediCustoms.server.dao.EnterpriseDao;

@Service("messageDispatcher")
public class MessageDispatcher{
	
	@Autowired
	private MessageGenerator messageGenerator;

	public MessageDispatcher() throws Exception {
		
	}

	public String sendMessage(Long customsDeclarationHeadId, Long userId) {
		
		CustomsDeclarationHead customsDeclarationHead = customsDeclarationHeadDao.get(customsDeclarationHeadId);	
		if (customsDeclarationHead==null) {
			return "该报关单不存在！";
		}
		
		if (!applicationConfiguration.isDebug()) {
			if (customsDeclarationHead.getIsDeclared()) {
				return "该报文已经申报过！";
			}
		}
		
		Enterprise enterprise = enterpriseDao.get(customsDeclarationHead
				.getOwnerId());
		if (enterprise.getMoney()<enterprise.getProxyCostPerDeal()) {
			return "余额不足，请联系新泽充值！";
		}
		
		File messageFile = messageGenerator.generateMessage(customsDeclarationHeadId);
		messageProcesser.createRecord(customsDeclarationHeadId, messageFile);	
		return "报文生成成功，请稍后查询结果";
	}	
	
	
	@Autowired
	private CustomsDeclarationHeadDao customsDeclarationHeadDao;
	@Autowired
	private EnterpriseDao enterpriseDao;
	@Autowired
	private MessageProcesser messageProcesser;
	@Autowired
	private ApplicationConfiguration applicationConfiguration;
}
