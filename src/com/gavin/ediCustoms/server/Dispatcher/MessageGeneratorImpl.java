package com.gavin.ediCustoms.server.Dispatcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.contract.ContractHead;
import com.gavin.ediCustoms.entity.edi.core.Container;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationGood;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.core.DispatchRunningNum;
import com.gavin.ediCustoms.entity.edi.dictionary.Attachment;
import com.gavin.ediCustoms.entity.edi.dictionary.Bracket;
import com.gavin.ediCustoms.entity.edi.dictionary.IronChest;
import com.gavin.ediCustoms.entity.edi.dictionary.LoadPort;
import com.gavin.ediCustoms.server.config.ApplicationConfiguration;
import com.gavin.ediCustoms.server.dao.ContainerDao;
import com.gavin.ediCustoms.server.dao.ContractHeadDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationGoodDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationHeadDao;
import com.gavin.ediCustoms.server.dao.DispatchRunningNumDao;
import com.gavin.ediCustoms.server.dao.EnterpriseDao;
import com.gavin.ediCustoms.server.dao.dictionary.AttachmentDao;
import com.gavin.ediCustoms.server.dao.dictionary.BracketDao;
import com.gavin.ediCustoms.server.dao.dictionary.IronChestDao;
import com.gavin.ediCustoms.server.dao.dictionary.LoadPortDao;
import com.gavin.ediCustoms.server.freemarker.EmptyMethod;
import com.gavin.ediCustoms.server.utils.LogUtil;
import com.gavin.ediCustoms.shared.MapUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service("messageGenerator")
public class MessageGeneratorImpl implements MessageGenerator {

	private Template template;
	private String uploadTemp;
	private String tempDir;

	public MessageGeneratorImpl() {
		String templatePath = this.getClass().getResource("").getPath();
		templatePath = templatePath.substring(1, templatePath.lastIndexOf('/'));
		Configuration freemarkerConfig = new Configuration();
		freemarkerConfig.setDefaultEncoding("UTF-8");
		
		try {
			freemarkerConfig.setDirectoryForTemplateLoading(new File(
					templatePath));			
			template = freemarkerConfig
					.getTemplate("customsDeclarationXML.ftl");	
			template.setEncoding("UTF-8");
			template.setNumberFormat("0.####");
		} catch (IOException e) {
			Logger.getRootLogger().error(e.getMessage());
		}

		ResourceBundle bundle = ResourceBundle.getBundle("messageDispatcher");
		tempDir = bundle.getString("message.temp");
		uploadTemp = bundle.getString("message.uploadTemp");	
		
	}

	@Override
	public File generateMessage(Long id) {
		File file = null;
		File newFile = null;
		try {
			Map<String, Object> map = getDataModel(id);
			CustomsDeclarationHead customsDeclarationHead = (CustomsDeclarationHead) map
					.get("customsDeclarationHead");

			String fileLocation = tempDir + "\\"
					+ customsDeclarationHead.getPreEntryId() + ".xml";

			file = new File(fileLocation);
			if (!file.exists()) {
				file.createNewFile();
			}	
			
			if (applicationConfiguration.isDebug()) {
				String templatePath = this.getClass().getResource("").getPath();
				templatePath = templatePath.substring(1, templatePath.lastIndexOf('/'));
				Configuration freemarkerConfig = new Configuration();
				freemarkerConfig.setDefaultEncoding("UTF-8");
				try {
					freemarkerConfig.setDirectoryForTemplateLoading(new File(
							templatePath));			
					template = freemarkerConfig
							.getTemplate("customsDeclarationXML.ftl");	
					template.setEncoding("UTF-8");
					template.setNumberFormat("0.####");
				} catch (IOException e) {
					Logger.getRootLogger().error(e.getMessage());
				}
			}
			map.put("empty", new EmptyMethod());
			OutputStream ops = new FileOutputStream(file);
			template.process(map, new OutputStreamWriter(ops, "utf-8"));
			ops.close();
			newFile=new File(uploadTemp + "\\"+file.getName());
			rename(file, newFile);
			
			//template.process(map, new FileWriter(file));
		} catch (TemplateException e) {
			Logger.getRootLogger().error(LogUtil.getTrace(e));
			e.printStackTrace();
		} catch (IOException e) {
			Logger.getRootLogger().error(LogUtil.getTrace(e));
			e.printStackTrace();
		} catch (Exception e) {
			Logger.getRootLogger().error(LogUtil.getTrace(e));
			e.printStackTrace();
		}

		return newFile;
	}

	public Map<String, Object> getDataModel(Long id) throws Exception {
		CustomsDeclarationHead customsDeclarationHead = customsDeclarationHeadDao
				.get(id);
		Map<String, Object> condictions = new HashMap<String, Object>();
		condictions.put("customsDeclarationHeadId", id);
		List<CustomsDeclarationGood> customsDeclarationGoods = customsDeclarationGoodDao
				.find(condictions,true,"no",0);

		List<Container> containers = containerDao.find("customsDeclarationHeadId", id);
		for (Container container : containers) {
			IronChest ironChest = ironChestDao.get(container.getType());
			container.set("weight", ironChest.getWeight());
			Bracket bracket = bracketDao.get(container.getBracket());
			container.set("bracketWeight", bracket.getWeight());
		}
		Map<String, String> attachments = MapUtil.stringToMap(customsDeclarationHead.getCertMark());
		
		
		//getCustomsDeclarationHeadDetail(customsDeclarationHead);
		double totalPrice = 0;
		for (CustomsDeclarationGood customsDeclarationGood : customsDeclarationGoods) {
			// getCustomsDeclarationGoodDetail(customsDeclarationGood);
			totalPrice += customsDeclarationGood.getTotalPrice();
		}
		
		String waybillNo = customsDeclarationHead.getTransportTool()!=null?
				("000" + customsDeclarationHead.getTransportTool().substring(1))
				:"";
		customsDeclarationHead.set("waybillNo", waybillNo);

		boolean isNomalTrade;
		if (customsDeclarationHead.getTradeMode().equals("0110")) {
			isNomalTrade=true;
		}else {
			isNomalTrade=false;
		}
		
		String manualNo=null;
		if (customsDeclarationHead.getContractHeadId()!=null) {
			ContractHead contractHead=contractHeadDao.get(
					customsDeclarationHead.getContractHeadId());
			manualNo =contractHead==null?"":contractHead.getManualNo();
		}
		
		
		Enterprise enterprise = enterpriseDao.get(customsDeclarationHead
				.getOwnerId());
		Enterprise runEnterprise = enterpriseDao.get(customsDeclarationHead
				.getRunEnterpriseId());
		Enterprise declareEnterprise = enterpriseDao.get(customsDeclarationHead
				.getDeclareEnterpriseId());		

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("isNomalTrade", isNomalTrade);
		map.put("customsDeclarationHead", customsDeclarationHead);
		map.put("customsDeclarationGoods", customsDeclarationGoods);
		map.put("containers", containers);
		map.put("attachments", attachments);
		
		
		
		map.put("manualNo", manualNo);
		map.put("enterprise", enterprise);
		map.put("runEnterprise", runEnterprise);
		map.put("declareEnterprise", declareEnterprise);
		map.put("totalPrice", totalPrice);
		map.put("container", getContainer(id));
		map.put("attachment",
				getAttachment(customsDeclarationHead.getCertMark()));
		map.put("transportDate", new Date());
		
		long runningNum=getDispatchRunningNum();
		
		Date date=new Date();
		map.put("messageId", enterprise.getJichengtongId()+runningNum);
		map.put("messageTime",new SimpleDateFormat("yyyyMMddhhmmss").format(date));
		map.put("SignDate", new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(date));
		//map.put("senderId",enterprise.getJichengtongId()+"@TCS 52001");
		//map.put("senderAddress", enterprise.getJichengtongId()+"@"+enterprise.getQuyujiedian());
		//map.put("receiverId",enterprise.getJichengtongId());
		//map.put("receiverAddress", enterprise.getJichengtongId()+"@"+enterprise.getQuyujiedian());
		map.put("tcsDocumentNo", enterprise.getJichengtongId()+"001"+new SimpleDateFormat("yyyyMMdd").format(date)+new DecimalFormat("0000").format(runningNum));
		map.put("loadPortCustoms", getLoadPort(customsDeclarationHead.getLoadPort()).getCustomsCode());
		map.put("userId",enterprise.getJichengtongId());
		map.put("userPrivateKey",enterprise.getUserPrivateKey());
		map.put("bpNo",enterprise.getBpNo());
		map.put("taskId",enterprise.getJichengtongId()+new SimpleDateFormat("yyyyMMdd").format(date)+runningNum);
		map.put("note", "[装卸口岸]"+getLoadPort(customsDeclarationHead.getLoadPort()).getName()
				+ "  " +(customsDeclarationHead.getNote()==null?"":customsDeclarationHead.getNote()));
		

		return map;
	}	
	
	
	private LoadPort getLoadPort(String id){
		LoadPort loadPort=loadPortDao.get(id);
		if (loadPort!=null) {
			return loadPort;
		}
		return null;
	}

	private String getContainer(Long customsDeclarationHeadId) {
		List<Container> containers = containerDao.find(
				"customsDeclarationHeadId", customsDeclarationHeadId);
		int containerNum = containers.size();
		if (containerNum == 0) {
			return "";
		}
		int standardNum = 0;
		for (Container container : containers) {
			standardNum += container.getValentNum();
		}
		return containers.get(0).getContainerNo() + " * " + containerNum + "("
				+ standardNum + ")";
	}

	private String getAttachment(String certMark) {
		if (certMark==null) {
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder();
		Map<String, String> map = MapUtil.stringToMap(certMark);
		for (String key : map.keySet()) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append("  ");
			}
			Attachment attachment=attachmentDao.get(key);
			if (attachment!=null) {
				stringBuilder.append(attachmentDao.get(key).getName());
			}			
		}
		return stringBuilder.toString();
	}

	private long getDispatchRunningNum() {
		List<DispatchRunningNum> list = dispatchRunningNumDao.list();
		if (list.size() == 0) {
			DispatchRunningNum dispatchRunningNum=new DispatchRunningNum();
			dispatchRunningNum.setRunningNum(1);
			dispatchRunningNumDao.save(dispatchRunningNum);
			return 0;
		}
		DispatchRunningNum dispatchRunningNum = list.get(0);
		long result=dispatchRunningNum.getRunningNum();
		dispatchRunningNum.setRunningNum(result+1);
		dispatchRunningNumDao.update(dispatchRunningNum);
		return result;
	}
	
	private boolean rename(File file1,File file2){
		try {
			if (file2.exists()) {
				file2.delete();
			}
			return file1.renameTo(file2);
		} catch (Exception e) {
			return false;
		}
	}

	@Autowired
	private CustomsDeclarationHeadDao customsDeclarationHeadDao;
	@Autowired
	private CustomsDeclarationGoodDao customsDeclarationGoodDao;
	@Autowired
	private ContractHeadDao contractHeadDao;
	@Autowired
	private EnterpriseDao enterpriseDao;
	@Autowired
	private ContainerDao containerDao;
	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private DispatchRunningNumDao dispatchRunningNumDao;
	@Autowired
	private LoadPortDao loadPortDao;
	@Autowired
	private ApplicationConfiguration applicationConfiguration;
	@Autowired
	private IronChestDao ironChestDao;
	@Autowired
	private BracketDao bracketDao;

}
