package com.gavin.ediCustoms.server.utils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.ForeignEnterprise;
import com.gavin.ediCustoms.entity.edi.Voucher;
import com.gavin.ediCustoms.entity.edi.contract.ContractConsume;
import com.gavin.ediCustoms.entity.edi.contract.ContractHead;
import com.gavin.ediCustoms.entity.edi.contract.ContractMaterial;
import com.gavin.ediCustoms.entity.edi.contract.ContractProduct;
import com.gavin.ediCustoms.entity.edi.dictionary.GoodClassification;
import com.gavin.ediCustoms.server.dao.ContractConsumeDao;
import com.gavin.ediCustoms.server.dao.ContractHeadDao;
import com.gavin.ediCustoms.server.dao.ContractMaterialDao;
import com.gavin.ediCustoms.server.dao.ContractProductDao;
import com.gavin.ediCustoms.server.dao.EnterpriseDao;
import com.gavin.ediCustoms.server.dao.ForeignEnterpriseDao;
import com.gavin.ediCustoms.server.dao.VoucherDao;
import com.gavin.ediCustoms.server.dao.dictionary.CountryDao;
import com.gavin.ediCustoms.server.dao.dictionary.CurrencyDao;
import com.gavin.ediCustoms.server.dao.dictionary.CustomsDao;
import com.gavin.ediCustoms.server.dao.dictionary.DealModeDao;
import com.gavin.ediCustoms.server.dao.dictionary.GoodClassificationDao;
import com.gavin.ediCustoms.server.dao.dictionary.ProcessTypeDao;
import com.gavin.ediCustoms.server.dao.dictionary.TaxKindDao;
import com.gavin.ediCustoms.server.dao.dictionary.TaxModeDao;
import com.gavin.ediCustoms.server.dao.dictionary.TradeModeDao;
import com.gavin.ediCustoms.server.dao.dictionary.UnitDao;

@Service("excelResolver")
public class ExcelResolver {
	private InputStream is;
	public String convert2Voucher(Long enterpriseId,String fileName) {
		Enterprise enterprise=enterpriseDao.get(enterpriseId);
		if (enterprise==null) {
			return "企业不存在！";
		}		
		
		int startIndex=2; //即第三行起是凭证内容

		try {			
			List<Voucher> list=new ArrayList<Voucher>();
			int no=0;
			is = new FileInputStream(fileName);
			jxl.Workbook rwb = Workbook.getWorkbook(is);
			Sheet sheet0 = rwb.getSheet(0);
			String tradeCode=sheet0.getCell(5, 2).getContents();
			if (!tradeCode.trim().equals(enterprise.getTradeCode())) {				
				return returnMessage( "上传文件与本单位信息不符！");
			}
			
			Sheet sheet1 = rwb.getSheet(1);
			int materialLength=sheet1.getRows()-startIndex;
			for (int i = 0; i < materialLength; i++) {
				no++;
				Voucher voucher=new Voucher();
				voucher.setOwnerId(enterpriseId);
				voucher.setNo(no);
				
				String code=sheet1.getCell(2,startIndex+i).getContents();
				voucher.setCode(code);
				
				String plusCode=sheet1.getCell(3,startIndex+i).getContents();
				voucher.setPlusCode(plusCode);
				
				String name=sheet1.getCell(4,startIndex+i).getContents();
				voucher.setName(name);
				
				String declareUnit=unitDao.getCodeByName(sheet1.getCell(5,startIndex+i).getContents());
				voucher.setDeclareUnit(declareUnit);
				
				String goodModel=sheet1.getCell(7,startIndex+i).getContents();
				voucher.setGoodModel(goodModel);
				
				Cell declarePriceCell=sheet1.getCell(9,startIndex+i);
				if (declarePriceCell.getType()==CellType.NUMBER) {
					voucher.setDeclarePrice(((NumberCell)declarePriceCell).getValue());
				}else if(!declarePriceCell.getContents().trim().isEmpty()){
					voucher.setDeclarePrice(Double.parseDouble(declarePriceCell.getContents().trim()));
				}		
				
				String currency=currencyDao.getCodeByName(sheet1.getCell(10,startIndex+i).getContents());
				voucher.setCurrency(currency);	
				
				list.add(voucher);
			}		
			
			Sheet sheet2 = rwb.getSheet(2);
			int productLength=sheet2.getRows()-startIndex;
			for (int i = 0; i < productLength; i++) {
				no++;
				Voucher voucher=new Voucher();
				voucher.setOwnerId(enterpriseId);
				voucher.setNo(no);
				
				String code=sheet2.getCell(2,startIndex+i).getContents();
				voucher.setCode(code);
				
				String plusCode=sheet2.getCell(3,startIndex+i).getContents();
				voucher.setPlusCode(plusCode);
				
				String name=sheet2.getCell(4,startIndex+i).getContents();
				voucher.setName(name);
				
				String declareUnit=unitDao.getCodeByName(sheet2.getCell(5,startIndex+i).getContents());
				voucher.setDeclareUnit(declareUnit);
				
				String goodModel=sheet2.getCell(7,startIndex+i).getContents();
				voucher.setGoodModel(goodModel);				
				
				Cell declarePriceCell=sheet2.getCell(8,startIndex+i);
				if (declarePriceCell.getType()==CellType.NUMBER) {
					voucher.setDeclarePrice(((NumberCell)declarePriceCell).getValue());
				}else if(!declarePriceCell.getContents().trim().isEmpty()){
					voucher.setDeclarePrice(Double.parseDouble(declarePriceCell.getContents().trim()));
				}					
				
				String currency=currencyDao.getCodeByName(sheet2.getCell(9,startIndex+i).getContents());
				voucher.setCurrency(currency);	
				
				list.add(voucher);
			}			
			
			voucherDao.delete("ownerId", enterpriseId);
			for (Iterator<Voucher> iterator = list.iterator(); iterator.hasNext();) {
				Voucher voucher = (Voucher) iterator.next();
				voucherDao.save(voucher);
			}
			rwb.close();
			is.close();
			
		} catch (Exception e) {
			return returnMessage("不能顺利导入，请检查所上传的excel是否符合要求");
		}
		
		File uploadedFile = new File(fileName);
		uploadedFile.delete();		
		return null;
	}

	public String convert2Contract(Long enterpriseId,String fileName) {
		Enterprise enterprise=enterpriseDao.get(enterpriseId);
		if (enterprise==null) {
			return "企业不存在！";
		}		
		
		int startIndex=2; //即第三行起是内容

		ContractHead contractHead=new ContractHead();
		try {				
			is = new FileInputStream(fileName);
			jxl.Workbook rwb = Workbook.getWorkbook(is);
			Sheet sheet0 = rwb.getSheet(0);
			String tradeCode=sheet0.getCell(5, 4).getContents();
			if (!tradeCode.trim().equals(enterprise.getTradeCode())) {
				return returnMessage("上传文件与本单位信息不符！");
			}
			String manualNo=sheet0.getCell(3, 2).getContents();
			contractHead.setManualNo(manualNo);
			contractHead.setOwnerId(enterprise.getId());
			
			contractHead.setRunEnterpriseId(getRunEnterpriseIdByCode(sheet0.getCell(1, 4).getContents()));
			contractHead.setForeignEnterpriseId(getForeignEnterpriseIdByName(sheet0.getCell(1, 5).getContents()));
			contractHead.setTradeMode(tradeModeDao.getCodeByName(sheet0.getCell(5, 5).getContents()));
			contractHead.setTaxKind(taxKindDao.getCodeByName(sheet0.getCell(1, 6).getContents()));
			contractHead.setTradeCountry(countryDao.getCodeByName(sheet0.getCell(3, 6).getContents()));
			contractHead.setDealMode(dealModeDao.getCodeByName(sheet0.getCell(5, 6).getContents()));
			
			
			Cell saleInScalesCell=sheet0.getCell(1,7);
			if (saleInScalesCell.getType()==CellType.NUMBER) {
				contractHead.setSaleInScale(((NumberCell)saleInScalesCell).getValue());
			}else if(!saleInScalesCell.getContents().trim().isEmpty()){
				contractHead.setSaleInScale(Double.parseDouble(saleInScalesCell.getContents().trim()));
			}
			
			contractHead.setProtocolCode(sheet0.getCell(3, 7).getContents());
			contractHead.setLicenceCode(sheet0.getCell(5, 7).getContents());
			contractHead.setCertificationCode(sheet0.getCell(1, 8).getContents());
			contractHead.setImportContract(sheet0.getCell(3, 8).getContents());
			contractHead.setExportContract(sheet0.getCell(5, 8).getContents());
			
			Cell importTotalMoneyCell=sheet0.getCell(1,7);
			if (importTotalMoneyCell.getType()==CellType.NUMBER) {
				contractHead.setImportTotalMoney(((NumberCell)importTotalMoneyCell).getValue());
			}else if(!importTotalMoneyCell.getContents().trim().isEmpty()){
				contractHead.setImportTotalMoney(Double.parseDouble(importTotalMoneyCell.getContents().trim()));
			}
						
			contractHead.setImportCurrency(currencyDao.getCodeByName(sheet0.getCell(3, 9).getContents()));
			
			Cell exportTotalMoneyCell=sheet0.getCell(1,7);
			if (exportTotalMoneyCell.getType()==CellType.NUMBER) {
				contractHead.setExportTotalMoney(((NumberCell)exportTotalMoneyCell).getValue());
			}else if(!exportTotalMoneyCell.getContents().trim().isEmpty()){
				contractHead.setExportTotalMoney(Double.parseDouble(exportTotalMoneyCell.getContents().trim()));
			}			
			
			contractHead.setExportCurrency(currencyDao.getCodeByName(sheet0.getCell(1, 10).getContents()));
			
			contractHead.setProcessType(processTypeDao.getCodeByName(sheet0.getCell(3, 10).getContents()));
			
			//口岸
			String portString=sheet0.getCell(3, 11).getContents().trim();
			String[] portArray=portString.split("/", 5);
			for (int i = 0; i < portArray.length; i++) {
				switch (i) {
				case 0:
					contractHead.setPort1(customsDao.getCodeByName(portArray[i]));
					break;
				case 1:
					contractHead.setPort2(customsDao.getCodeByName(portArray[i]));
					break;
				case 2:
					contractHead.setPort3(customsDao.getCodeByName(portArray[i]));
					break;
				case 3:
					contractHead.setPort4(customsDao.getCodeByName(portArray[i]));
					break;
				case 4:
					contractHead.setPort5(customsDao.getCodeByName(portArray[i]));
					break;
				default:
					break;
				}
			}
			
			Cell typeDateCell=sheet0.getCell(5, 13);
			if (typeDateCell.getType()==CellType.DATE) {
				contractHead.setTypeDate(((DateCell)typeDateCell).getDate());
			}else if(!typeDateCell.getContents().trim().isEmpty()){				
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = null;
				try {
					date=sf.parse(sheet0.getCell(5, 13).getContents());
					contractHead.setTypeDate(date);					
				} catch (ParseException e) {
					return returnMessage("日期转换失败，请注意格式为 yyyy-MM-dd");
				}
			}
			System.out.println(contractHead.getTypeDate());
			
			List<ContractHead> oldList=contractHeadDao.find("manualNo", contractHead.getManualNo());
			if (oldList.size()!=0) {
				for(ContractHead ch:oldList){
					contractHeadDao.delete(ch.getId());			
				}					
			}			
			contractHead.setId(contractHeadDao.save(contractHead));
			
			
			
			//材料表体
			Sheet sheet1 = rwb.getSheet(1);
			int materialLength=sheet1.getRows()-startIndex;
			List<ContractMaterial> contractMaterialList=new ArrayList<ContractMaterial>();
			for (int i = 0; i < materialLength; i++) {
				ContractMaterial contractMaterial=new ContractMaterial();
				contractMaterial.setContractHeadId(contractHead.getId());
				
				String noString=sheet1.getCell(0,i+startIndex).getContents().trim();
				if (!noString.isEmpty()) {
					int no=Integer.parseInt(noString);
					contractMaterial.setNo(no);
				}
				
				String code=sheet1.getCell(2,startIndex+i).getContents();
				contractMaterial.setCode(code);
				
				String plusCode=sheet1.getCell(3,startIndex+i).getContents();
				contractMaterial.setPlusCode(plusCode);
				
				String name=sheet1.getCell(4,startIndex+i).getContents();
				contractMaterial.setName(name);
				
				String goodModel=sheet1.getCell(5,startIndex+i).getContents();
				contractMaterial.setGoodModel(goodModel);
				
				String declareUnit=unitDao.getCodeByName(sheet1.getCell(7,startIndex+i).getContents());
				contractMaterial.setDeclareUnit(declareUnit);
				
				Map<String, Object> filter = new HashMap<String, Object>();
				filter.put("code", code);
				filter.put("plusCode", plusCode);
				List<GoodClassification> goodClassificationList = goodClassificationDao.findAnd(filter);
				if (goodClassificationList.size()>0) {
					contractMaterial.setUnit1(goodClassificationList.get(0).getUnit1());
					contractMaterial.setUnit2(goodClassificationList.get(0).getUnit2());
				}
				
				
				Cell quantityCell=sheet1.getCell(9,startIndex+i);
				if (quantityCell.getType()==CellType.NUMBER) {
					contractMaterial.setQuantity(((NumberCell)quantityCell).getValue());
				}else if(!quantityCell.getContents().trim().isEmpty()){
					contractMaterial.setQuantity(Double.parseDouble(quantityCell.getContents().trim()));
				}
				
				Cell priceCell=sheet1.getCell(10,startIndex+i);
				if (priceCell.getType()==CellType.NUMBER) {
					contractMaterial.setPrice(((NumberCell)priceCell).getValue());
				}else if(!priceCell.getContents().trim().isEmpty()){
					contractMaterial.setPrice(Double.parseDouble(priceCell.getContents().trim()));
				}
				
				Cell totalMoneyCell=sheet1.getCell(10,startIndex+i);
				if (totalMoneyCell.getType()==CellType.NUMBER) {
					contractMaterial.setTotalPrice(((NumberCell)totalMoneyCell).getValue());
				}else if(!totalMoneyCell.getContents().trim().isEmpty()){
					contractMaterial.setTotalPrice(Double.parseDouble(totalMoneyCell.getContents().trim()));
				}
				
				String originCountry=countryDao.getCodeByName(sheet1.getCell(13,startIndex+i).getContents());
				contractMaterial.setOriginCountry(originCountry);
				
				String taxMode=taxModeDao.getCodeByName(sheet1.getCell(15,startIndex+i).getContents());
				contractMaterial.setTaxMode(taxMode);
				
				contractMaterialList.add(contractMaterial);
			}
			
			
			
			
			//成品表体
			Sheet sheet2 = rwb.getSheet(2);
			int productLength=sheet2.getRows()-startIndex;
			List<ContractProduct> contractProductList=new ArrayList<ContractProduct>();			
			for (int i = 0; i < productLength; i++) {
				ContractProduct contractProduct=new ContractProduct();
				contractProduct.setContractHeadId(contractHead.getId());
				
				String noString=sheet2.getCell(0,i+startIndex).getContents().trim();
				if (!noString.isEmpty()) {
					int no=Integer.parseInt(noString);
					contractProduct.setNo(no);
				}
				
				String code=sheet2.getCell(2,startIndex+i).getContents();
				contractProduct.setCode(code);
				
				String plusCode=sheet2.getCell(3,startIndex+i).getContents();
				contractProduct.setPlusCode(plusCode);
				
				String name=sheet2.getCell(4,startIndex+i).getContents();
				contractProduct.setName(name);
				
				String goodModel=sheet2.getCell(5,startIndex+i).getContents();
				contractProduct.setGoodModel(goodModel);
				
				String declareUnit=unitDao.getCodeByName(sheet2.getCell(6,startIndex+i).getContents());
				contractProduct.setDeclareUnit(declareUnit);
			
				Cell quantityCell=sheet2.getCell(8,startIndex+i);
				if (quantityCell.getType()==CellType.NUMBER) {
					contractProduct.setQuantity(((NumberCell)quantityCell).getValue());
				}else if(!quantityCell.getContents().trim().isEmpty()){
					contractProduct.setQuantity(Double.parseDouble(quantityCell.getContents().trim()));
				}
				
				Cell priceCell=sheet2.getCell(9,startIndex+i);
				if (priceCell.getType()==CellType.NUMBER) {
					contractProduct.setPrice(((NumberCell)priceCell).getValue());
				}else if(!priceCell.getContents().trim().isEmpty()){
					contractProduct.setPrice(Double.parseDouble(priceCell.getContents().trim()));
				}
				
				Cell totalPriceCell=sheet2.getCell(10,startIndex+i);
				if (totalPriceCell.getType()==CellType.NUMBER) {
					contractProduct.setTotalPrice(((NumberCell)totalPriceCell).getValue());
				}else if(!totalPriceCell.getContents().trim().isEmpty()){
					contractProduct.setTotalPrice(Double.parseDouble(totalPriceCell.getContents().trim()));
				}
				
				String originCountry=countryDao.getCodeByName(sheet2.getCell(12,startIndex+i).getContents());
				contractProduct.setOriginCountry(originCountry);
				
				String taxMode=taxModeDao.getCodeByName(sheet2.getCell(14,startIndex+i).getContents());
				contractProduct.setTaxMode(taxMode);
				
				contractProductList.add(contractProduct);
			}
			
			for(ContractMaterial contractMaterial:contractMaterialList){
				contractMaterial.setId(contractMaterialDao.save(contractMaterial));
			}
			for(ContractProduct contractProduct:contractProductList){
				contractProduct.setId(contractProductDao.save(contractProduct));
			}
			
			//单损耗表
			Sheet sheet3 = rwb.getSheet(3);
			int consumeLength=sheet3.getRows()-startIndex;
			List<ContractConsume> contractConsumeList=new ArrayList<ContractConsume>();
			for (int i = 0; i < consumeLength; i++) {
				ContractConsume contractConsume=new ContractConsume();			
				Long contractProductId=getContractProductIdByNo(contractProductList, Integer.parseInt(sheet3.getCell(0,i+startIndex).getContents().trim()));
				contractConsume.setContractProductId(contractProductId);				
				Long contractMaterialId=getContractMaterialIdByNo(contractMaterialList, Integer.parseInt(sheet3.getCell(4,i+startIndex).getContents().trim()));
				contractConsume.setContractMaterialId(contractMaterialId);
				
				Cell usedCell=sheet3.getCell(8,i+startIndex);
				if (usedCell.getType()==CellType.NUMBER) {
					contractConsume.setUsed(((NumberCell)usedCell).getValue());
				}else if(!usedCell.getContents().trim().isEmpty()){
					contractConsume.setUsed(Double.parseDouble(usedCell.getContents().trim()));
				}
				
				Cell wastedcCell=sheet3.getCell(9,i+startIndex);
				if (wastedcCell.getType()==CellType.NUMBER) {
					contractConsume.setWasted(((NumberCell)wastedcCell).getValue());
				}else if(!usedCell.getContents().trim().isEmpty()) {
					contractConsume.setWasted(Double.parseDouble(wastedcCell.getContents().trim()));
				}				
				
				contractConsumeList.add(contractConsume);
			}
			
			for(ContractConsume contractConsume:contractConsumeList){
				contractConsumeDao.save(contractConsume);
			}
			
			rwb.close();
			is.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			return returnMessage("不能顺利导入，请检查所上传的excel是否符合要求");
		}
	
		return null;
	}	
	
	
	@Autowired
	private UnitDao unitDao;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private VoucherDao voucherDao;
	@Autowired
	private TradeModeDao tradeModeDao;
	@Autowired
	private TaxKindDao taxKindDao;
	@Autowired
	private CountryDao countryDao;
	@Autowired
	private DealModeDao dealModeDao;
	@Autowired
	private ProcessTypeDao processTypeDao;
	@Autowired
	private CustomsDao customsDao;
	@Autowired
	private TaxModeDao taxModeDao;
	
	@Autowired
	private ContractHeadDao contractHeadDao;
	@Autowired
	private ContractMaterialDao contractMaterialDao;
	@Autowired
	private ContractProductDao contractProductDao;
	@Autowired
	private ContractConsumeDao contractConsumeDao;
	@Autowired
	private EnterpriseDao enterpriseDao;
	@Autowired
	private ForeignEnterpriseDao foreignEnterpriseDao;
	@Autowired
	private GoodClassificationDao goodClassificationDao;
		
	
	private Long getForeignEnterpriseIdByName(String registeName){
		List<ForeignEnterprise> list=foreignEnterpriseDao.find("registeName",registeName);
		if (list.size()==0) {
			return null;
		}
		ForeignEnterprise foreignEnterprise=list.get(0);
		if (foreignEnterprise==null) {
			return null;
		}
		return foreignEnterprise.getId();
	}
	
	private Long getRunEnterpriseIdByCode(String tradeCode){
		List<Enterprise> list=enterpriseDao.find("tradeCode",tradeCode);
		if (list.size()==0) {
			return null;
		}
		Enterprise enterprise=list.get(0);
		if (enterprise==null) {
			return null;
		}
		return enterprise.getId();
	}
	
	private Long getContractMaterialIdByNo(List<ContractMaterial> list,int no){
		for (Iterator<ContractMaterial> iterator = list.iterator(); iterator.hasNext();) {
			ContractMaterial contractMaterial = (ContractMaterial) iterator
					.next();
			if (contractMaterial.getNo()==no) {
				return contractMaterial.getId();
			}
		}
		return null;
	}
	
	private Long getContractProductIdByNo(List<ContractProduct> list,int no){
		for (Iterator<ContractProduct> iterator = list.iterator(); iterator.hasNext();) {
			ContractProduct contractProduct = (ContractProduct) iterator.next();
			if (contractProduct.getNo()==no) {
				return contractProduct.getId();
			}
		}
		return null;
	}
	
	private String returnMessage(String string){
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string;
	}

}
