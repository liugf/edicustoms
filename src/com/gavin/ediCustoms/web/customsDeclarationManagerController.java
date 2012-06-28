package com.gavin.ediCustoms.web;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.contract.ContractHead;
import com.gavin.ediCustoms.entity.edi.core.Container;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationGood;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.dictionary.Country;
import com.gavin.ediCustoms.entity.edi.dictionary.Port;
import com.gavin.ediCustoms.entity.edi.dictionary.TradeMode;
import com.gavin.ediCustoms.entity.edi.dictionary.Unit;
import com.gavin.ediCustoms.server.dao.ContainerDao;
import com.gavin.ediCustoms.server.dao.ContractHeadDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationGoodDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationHeadDao;
import com.gavin.ediCustoms.server.dao.EnterpriseDao;
import com.gavin.ediCustoms.server.dao.dictionary.CountryDao;
import com.gavin.ediCustoms.server.dao.dictionary.PortDao;
import com.gavin.ediCustoms.server.dao.dictionary.TradeModeDao;
import com.gavin.ediCustoms.server.dao.dictionary.UnitDao;

@Controller
public class CustomsDeclarationManagerController {
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/customsDeclarationManager.do")
	public String customsDeclarationManager(Long enterpriseId,String contractNo,Boolean isExport,Boolean isImport, String start,String end,ModelMap map) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ownerId", enterpriseId);
		if (contractNo!=null && !contractNo.isEmpty()) {
			System.out.println("contractNo");
			params.put("contractNo", contractNo);
			map.put("contractNo", contractNo);
		}
		if (!isExport.equals(isImport)) {
			params.put("isExport", isExport);
			map.put("isExport", isExport);
		}
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (start!=null && !start.isEmpty()) {
			Date startDate = fmt.parse(start);
			params.put("declareTime>=", startDate);
			map.put("startDate", startDate);
		}
		if (end!=null && !end.isEmpty()) {
			Date endDate = fmt.parse(end);
			params.put("declareTime<=", endDate);
			map.put("endDate", endDate);
		}
		params.put("isPass", true);
		List<CustomsDeclarationHead> customsDeclarationHeads
			=customsDeclarationHeadDao.findAnd(params);
		Enterprise enterprise=enterpriseDao.get(enterpriseId);
		
		double totleMoney=0;
		double totleGrossWeight=0;
		double totleNetWeight=0;
		int totlePage=0;
		for (CustomsDeclarationHead customsDeclarationHead : customsDeclarationHeads) {
			if (customsDeclarationHead.getContractHeadId()!=null) {
				ContractHead contractHead= contractHeadDao.get(customsDeclarationHead.getContractHeadId());
				if (contractHead!=null) {
					customsDeclarationHead.set("certificationCode", contractHead.getCertificationCode());
				}
			}
			List<CustomsDeclarationGood> customsDeclarationGoods=customsDeclarationGoodDao.find("customsDeclarationHeadId", customsDeclarationHead.getId());
			double totlePrice=0;
			for (CustomsDeclarationGood customsDeclarationGood : customsDeclarationGoods) {
				totlePrice+=customsDeclarationGood.getTotalPrice();
			}
			customsDeclarationHead.set("totleMoney", totlePrice);
			totleMoney+=totlePrice;
			totleGrossWeight+=customsDeclarationHead.getGrossWeight();
			totleNetWeight+=customsDeclarationHead.getNetWeight();
			int pageNum=0;
			if(customsDeclarationGoods.size()>0){
				pageNum=(customsDeclarationGoods.size()-1)/5+1;
			}
			customsDeclarationHead.set("pageNum", pageNum);
			totlePage+=pageNum;
			
			List<Container> containers = containerDao.find("customsDeclarationHeadId", customsDeclarationHead.getId());
			if (containers.size()>0) {
				customsDeclarationHead.set("containerNo", containers.get(0).getContainerNo());
			}
			
		}
		map.put("totlePage", totlePage);
		map.put("totleGrossWeight", totleGrossWeight);
		map.put("totleNetWeight", totleNetWeight);
		map.put("totleMoney", totleMoney);
		map.put("enterprise", enterprise);
		map.put("customsDeclarationHeads", customsDeclarationHeads);
		map.put("today", new Date());
		return "customsDeclarationManager";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/customsDeclarationExcel.do")
	public String customsDeclarationExcel(HttpServletResponse response,Long enterpriseId,String contractNo,Boolean isExport,Boolean isImport, String start,String end,ModelMap map) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ownerId", enterpriseId);
		if (contractNo!=null && !contractNo.isEmpty()) {
			System.out.println("contractNo");
			params.put("contractNo", contractNo);
			map.put("contractNo", contractNo);
		}
		if (!isExport.equals(isImport)) {
			params.put("isExport", isExport);
			map.put("isExport", isExport);
		}
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (start!=null && !start.isEmpty()) {
			Date startDate = fmt.parse(start);
			params.put("declareTime>=", startDate);
			map.put("startDate", startDate);
		}
		if (end!=null && !end.isEmpty()) {
			Date endDate = fmt.parse(end);
			params.put("declareTime<=", endDate);
			map.put("endDate", endDate);
		}
		params.put("isPass", true);
		List<CustomsDeclarationHead> customsDeclarationHeads
			=customsDeclarationHeadDao.findAnd(params);
		Enterprise enterprise=enterpriseDao.get(enterpriseId);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {   
            OutputStream os = response.getOutputStream();// 取得输出流   
            response.reset();// 清空输出流   
            response.setHeader("Content-disposition",   
                    "attachment; filename="+enterprise.getTradeCode()+"-"+sdf.format(new Date())+".xls");// 设定输出文件头   
            response.setContentType("application/msexcel");// 定义输出类型   
            exportExcel(os, enterprise, customsDeclarationHeads);// 调用生成excel文件bean   
        } catch (Exception e) {   
            System.out.println(e);   
        }  
		return null;
	}
	
	
	
	
	private String exportExcel(OutputStream os,Enterprise enterprise, List<CustomsDeclarationHead> customsDeclarationHeads)   
            throws Exception {   
  
        WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件   
        WritableSheet wsheet = wbook.createSheet(enterprise.getRegisteName()+"报关单", 0); // sheet名称   
        wsheet.addCell(new Label(0, 0, "报关单号"));   
        wsheet.addCell(new Label(1, 0, "进出口类型"));   
        wsheet.addCell(new Label(2, 0, "贸易方式"));   
        wsheet.addCell(new Label(3, 0, "客户名称"));   
        wsheet.addCell(new Label(4, 0, "运抵(启运)国"));   
        wsheet.addCell(new Label(5, 0, "指运(装货)港"));   
        wsheet.addCell(new Label(6, 0, "法定数量"));   
        wsheet.addCell(new Label(7, 0, "法定单位"));  
        wsheet.addCell(new Label(8, 0, "报关单价")); 
        wsheet.addCell(new Label(9, 0, "核销单号")); 
        wsheet.addCell(new Label(10, 0, "产销目的国"));
        wsheet.addCell(new Label(11, 0, "报关日期"));
        wsheet.addCell(new Label(12, 0, "运输工具名称"));
        wsheet.addCell(new Label(13, 0, "车牌号码"));
        wsheet.addCell(new Label(14, 0, "商品编码"));
        wsheet.addCell(new Label(15, 0, "附加编码"));
        wsheet.addCell(new Label(16, 0, "商品名称"));
        wsheet.addCell(new Label(17, 0, "商品规格"));
        wsheet.addCell(new Label(18, 0, "价值"));
	    
        
        
        int i=1;
        for (CustomsDeclarationHead customsDeclarationHead : customsDeclarationHeads) {
        	List<CustomsDeclarationGood> customsDeclarationGoods=customsDeclarationGoodDao.find("customsDeclarationHeadId", customsDeclarationHead.getId());
        	for (CustomsDeclarationGood customsDeclarationGood : customsDeclarationGoods) {
        		wsheet.addCell(new Label(0, i, customsDeclarationHead.getEntryId()));   
            	wsheet.addCell(new Label(1, i, customsDeclarationHead.getTransportMode()));
            	if (customsDeclarationHead.getTradeMode()!=null) {
            		TradeMode tradeMode = tradeModeDao.get(customsDeclarationHead.getTradeMode());
            		if (tradeMode!=null) {
            			wsheet.addCell(new Label(2, i, tradeMode.getName()));
    				}
    			}
            	wsheet.addCell(new Label(3, i, enterprise.getRegisteName()));
            	if (customsDeclarationHead.getTradeCountry()!=null) {
            		Country country = countryDao.get(customsDeclarationHead.getTradeCountry());
            		if (country!=null) {
            			wsheet.addCell(new Label(4, i, country.getName()));
    				}
    			}
            	if (customsDeclarationHead.getDestinationPort()!=null) {
            		Port port = portDao.get(customsDeclarationHead.getDestinationPort());
            		if (port!=null) {
            			wsheet.addCell(new Label(5, i, port.getName()));
    				}
    			}   
            	wsheet.addCell(new Number(6, i, customsDeclarationGood.getQuantity()));
            	if (customsDeclarationGood.getDeclareUnit()!=null) {
            		Unit unit = unitDao.get(customsDeclarationGood.getDeclareUnit());
            		if (unit!=null) {
            			wsheet.addCell(new Label(7, i, unit.getName()));
    				}
    			}
            	wsheet.addCell(new Label(8, i, new DecimalFormat("#.0000").format(customsDeclarationGood.getDeclarePrice())));
            	wsheet.addCell(new Label(9, i, customsDeclarationHead.getCertificationCode()));
            	if (customsDeclarationGood.getOriginCountry()!=null) {
            		Country country = countryDao.get(customsDeclarationGood.getOriginCountry());
            		if (country!=null) {
            			wsheet.addCell(new Label(10, i, country.getName()));
    				}
    			}
            	if (customsDeclarationHead.getDeclareTime()!=null) {
            		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            		wsheet.addCell(new Label(11, i, fmt.format(customsDeclarationHead.getDeclareTime())));
    			}
            	wsheet.addCell(new Label(12, i, customsDeclarationHead.getTransportTool()));
            	wsheet.addCell(new Label(13, i, customsDeclarationHead.getBillNo()));
            	wsheet.addCell(new Label(14, i, customsDeclarationGood.getCode()));
            	wsheet.addCell(new Label(15, i, customsDeclarationGood.getPlusCode()));
            	wsheet.addCell(new Label(16, i, customsDeclarationGood.getName()));
            	wsheet.addCell(new Label(17, i, customsDeclarationGood.getShenbaoguifan()));
            	wsheet.addCell(new Label(18, i, new DecimalFormat("#.00").format(customsDeclarationGood.getTotalPrice()) ));
            	
            	
            	++i;
			}
		}  
        // 主体内容生成结束   
        wbook.write(); // 写入文件   
        wbook.close();   
        os.close();   
        return "success";  
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
	private CustomsDeclarationHeadDao customsDeclarationHeadDao;
	@Autowired
	private CustomsDeclarationGoodDao customsDeclarationGoodDao;
	@Autowired
	private ContractHeadDao contractHeadDao;
	@Autowired
	private ContainerDao containerDao;
	@Autowired
	private TradeModeDao tradeModeDao;
	@Autowired
	private CountryDao countryDao;
	@Autowired
	private PortDao portDao;
	@Autowired
	private UnitDao unitDao;
	
}
