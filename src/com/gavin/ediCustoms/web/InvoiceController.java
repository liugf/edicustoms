package com.gavin.ediCustoms.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.ForeignEnterprise;
import com.gavin.ediCustoms.entity.edi.contract.ContractHead;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationGood;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.core.PackingItem;
import com.gavin.ediCustoms.entity.edi.dictionary.Country;
import com.gavin.ediCustoms.entity.edi.dictionary.Currency;
import com.gavin.ediCustoms.entity.edi.dictionary.DealMode;
import com.gavin.ediCustoms.entity.edi.dictionary.Truck;
import com.gavin.ediCustoms.entity.edi.dictionary.Unit;
import com.gavin.ediCustoms.entity.edi.dictionary.WrapType;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationGoodDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationHeadDao;
import com.gavin.ediCustoms.server.dao.EnterpriseDao;
import com.gavin.ediCustoms.server.dao.PackingItemDao;
import com.gavin.ediCustoms.server.dao.dictionary.CountryDao;
import com.gavin.ediCustoms.server.dao.dictionary.CurrencyDao;
import com.gavin.ediCustoms.server.dao.dictionary.DealModeDao;
import com.gavin.ediCustoms.server.dao.dictionary.TruckDao;
import com.gavin.ediCustoms.server.dao.dictionary.UnitDao;
import com.gavin.ediCustoms.server.dao.dictionary.WrapTypeDao;

class ParamsWrap1 {
	private Enterprise enterprise = new Enterprise();
	private ForeignEnterprise foreignEnterprise = new ForeignEnterprise();

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public ForeignEnterprise getForeignEnterprise() {
		return foreignEnterprise;
	}

	public void setForeignEnterprise(ForeignEnterprise foreignEnterprise) {
		this.foreignEnterprise = foreignEnterprise;
	}
}

@Controller
public class InvoiceController {	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/contract.do")
	public String contract(String exportDeadline, String importDeadline,HttpServletRequest request, ModelMap map,
			ParamsWrap1 paramsWrap1, Date declareTime) throws Exception {
		Map<String, String[]> params = request.getParameterMap();
		Long id = new Long(params.get("id")[0]);
		CustomsDeclarationHead customsDeclarationHead = customsDeclarationHeadDao
				.get(id);
		List<CustomsDeclarationGood> customsDeclarationGoods = customsDeclarationGoodDao
				.find("customsDeclarationHeadId", id);
		getCustomsDeclarationHeadDetail(customsDeclarationHead);
		double totalPrice = 0;
		for (CustomsDeclarationGood customsDeclarationGood : customsDeclarationGoods) {
			getCustomsDeclarationGoodDetail(customsDeclarationGood);
			totalPrice += customsDeclarationGood.getTotalPrice();
		}
		
		Enterprise enterprise = enterpriseDao.get(customsDeclarationHead
				.getOwnerId());

		paramsWrap1.getEnterprise().setBank(enterprise.getBank());
		paramsWrap1.getEnterprise().setBankAccount(enterprise.getBankAccount());

		ContractHead contractHead = new ContractHead();	
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date date=customsDeclarationHead.getDeclareTime();
		Calendar ca = Calendar.getInstance(); 
		ca.setTime(date); 
		ca.add(Calendar.DAY_OF_YEAR,-5); 
		if (exportDeadline!=null && !exportDeadline.equals("null")) {
			contractHead.setExportDeadline(fmt.parse(exportDeadline));
		}else {
			contractHead.setExportDeadline(ca.getTime());
		}
		ca.add(Calendar.MONTH,+3); 
		if (importDeadline!=null && !importDeadline.equals("null")) {
			contractHead.setImportDeadline(fmt.parse(importDeadline));
		}else {
			contractHead.setImportDeadline(ca.getTime());
		}
		
		
		map.put("contractHead", contractHead);
		map.put("customsDeclarationHead", customsDeclarationHead);
		map.put("customsDeclarationGoods", customsDeclarationGoods);
		map.put("enterprise", paramsWrap1.getEnterprise());
		map.put("foreignEnterprise", paramsWrap1.getForeignEnterprise());
		map.put("totalPrice", totalPrice);
		map.put("dateTime", declareTime);
		map.putAll(params);

		return "contract";
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping("/packingList1.do")
	public String packingList1(HttpServletRequest request, ModelMap map,
			ParamsWrap1 paramsWrap1, Date declareTime) throws Exception {
		Map<String, String[]> params = request.getParameterMap();
		Long id = new Long(params.get("id")[0]);
		CustomsDeclarationHead customsDeclarationHead = customsDeclarationHeadDao
				.get(id);
		List<PackingItem> packingItems= packingItemDao.find("customsDeclarationHeadId", id);
		getCustomsDeclarationHeadDetail(customsDeclarationHead);
		int totleWrapNo = 0;
		int totlePackNo = 0;
		double totleNetWeight = 0;
		double totleGrossWeight = 0;
		for (PackingItem packingItem : packingItems) {
			getPackingItemDetail(packingItem);
			if (packingItem.getWrapNo()!=null) {
				totleWrapNo+=packingItem.getWrapNo();
			}
			if (packingItem.getPackNo()!=null) {
				totlePackNo+=packingItem.getPackNo();
			}
			if (packingItem.getNetWeight()!=0) {
				totleNetWeight+=packingItem.getNetWeight();
			}
			if (packingItem.getGrossWeight()!=0) {
				totleGrossWeight+=packingItem.getGrossWeight();
			}
		}

		Enterprise enterprise = enterpriseDao.get(customsDeclarationHead
				.getOwnerId());

		paramsWrap1.getEnterprise().setBank(enterprise.getBank());
		paramsWrap1.getEnterprise().setBankAccount(enterprise.getBankAccount());

		map.put("customsDeclarationHead", customsDeclarationHead);
		map.put("packingItems", packingItems);
		map.put("enterprise", paramsWrap1.getEnterprise());
		map.put("foreignEnterprise", paramsWrap1.getForeignEnterprise());
		map.put("dateTime", declareTime);
		map.put("totleWrapNo", totleWrapNo);
		map.put("totlePackNo", totlePackNo);
		map.put("totleNetWeight", totleNetWeight);
		map.put("totleGrossWeight", totleGrossWeight);
		map.putAll(params);

		return "packingList1";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/packingList3.do")
	public String packingList3(HttpServletRequest request, ModelMap map,
			ParamsWrap1 paramsWrap1, Date declareTime) throws Exception {
		Map<String, String[]> params = request.getParameterMap();
		Long id = new Long(params.get("id")[0]);
		CustomsDeclarationHead customsDeclarationHead = customsDeclarationHeadDao
				.get(id);
		List<PackingItem> packingItems= packingItemDao.find("customsDeclarationHeadId", id);
		getCustomsDeclarationHeadDetail(customsDeclarationHead);
		int totleWrapNo = 0;
		int totlePackNo = 0;
		double totleNetWeight = 0;
		double totleGrossWeight = 0;
		for (PackingItem packingItem : packingItems) {
			getPackingItemDetail(packingItem);
			if (packingItem.getWrapNo()!=null) {
				totleWrapNo+=packingItem.getWrapNo();
			}
			if (packingItem.getPackNo()!=null) {
				totlePackNo+=packingItem.getPackNo();
			}
			if (packingItem.getNetWeight()!=0) {
				totleNetWeight+=packingItem.getNetWeight();
			}
			if (packingItem.getGrossWeight()!=0) {
				totleGrossWeight+=packingItem.getGrossWeight();
			}
		}


		Enterprise enterprise = enterpriseDao.get(customsDeclarationHead
				.getOwnerId());

		paramsWrap1.getEnterprise().setBank(enterprise.getBank());
		paramsWrap1.getEnterprise().setBankAccount(enterprise.getBankAccount());

		map.put("customsDeclarationHead", customsDeclarationHead);
		map.put("packingItems", packingItems);
		map.put("enterprise", paramsWrap1.getEnterprise());
		map.put("foreignEnterprise", paramsWrap1.getForeignEnterprise());
		map.put("dateTime", declareTime);
		map.put("totleWrapNo", totleWrapNo);
		map.put("totlePackNo", totlePackNo);
		map.put("totleNetWeight", totleNetWeight);
		map.put("totleGrossWeight", totleGrossWeight);
		map.putAll(params);

		return "packingList3";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/packingList2.do")
	public String packingList2(HttpServletRequest request, ModelMap map,
			ParamsWrap1 paramsWrap1, Date declareTime) throws Exception {
		Map<String, String[]> params = request.getParameterMap();
		Long id = new Long(params.get("id")[0]);
		CustomsDeclarationHead customsDeclarationHead = customsDeclarationHeadDao
				.get(id);
		List<CustomsDeclarationGood> customsDeclarationGoods = customsDeclarationGoodDao
				.find("customsDeclarationHeadId", id);
		getCustomsDeclarationHeadDetail(customsDeclarationHead);
		double totalPrice = 0;
		for (CustomsDeclarationGood customsDeclarationGood : customsDeclarationGoods) {
			getCustomsDeclarationGoodDetail(customsDeclarationGood);
			totalPrice += customsDeclarationGood.getTotalPrice();
		}
		
		Truck truck = truckDao.get(customsDeclarationHead.getTruckCode());

		Enterprise enterprise = enterpriseDao.get(customsDeclarationHead
				.getOwnerId());

		paramsWrap1.getEnterprise().setTradeCode(enterprise.getTradeCode());
		paramsWrap1.getEnterprise().setBank(enterprise.getBank());
		paramsWrap1.getEnterprise().setBankAccount(enterprise.getBankAccount());

		
		map.put("customsDeclarationHead", customsDeclarationHead);
		map.put("customsDeclarationGoods", customsDeclarationGoods);
		map.put("truck", truck);
		map.put("enterprise", paramsWrap1.getEnterprise());
		map.put("foreignEnterprise", paramsWrap1.getForeignEnterprise());
		map.put("totalPrice", totalPrice);
		map.put("dateTime", declareTime);
		map.putAll(params);
		
		if (customsDeclarationHead.getIsExport()) {
			return "packingList2-a";
		}else {
			return "packingList2-b";
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/invoice1.do")
	public String invoice1(HttpServletRequest request, ModelMap map,
			ParamsWrap1 paramsWrap1, Date declareTime) throws Exception {
		Map<String, String[]> params = request.getParameterMap();
		Long id = new Long(params.get("id")[0]);
		CustomsDeclarationHead customsDeclarationHead = customsDeclarationHeadDao
				.get(id);
		List<CustomsDeclarationGood> customsDeclarationGoods = customsDeclarationGoodDao
				.find("customsDeclarationHeadId", id);
		getCustomsDeclarationHeadDetail(customsDeclarationHead);
		double totalPrice = 0;
		for (CustomsDeclarationGood customsDeclarationGood : customsDeclarationGoods) {
			getCustomsDeclarationGoodDetail(customsDeclarationGood);
			totalPrice += customsDeclarationGood.getTotalPrice();
		}

		Enterprise enterprise = enterpriseDao.get(customsDeclarationHead
				.getOwnerId());

		paramsWrap1.getEnterprise().setBank(enterprise.getBank());
		paramsWrap1.getEnterprise().setBankAccount(enterprise.getBankAccount());

		
		map.put("customsDeclarationHead", customsDeclarationHead);
		map.put("customsDeclarationGoods", customsDeclarationGoods);
		map.put("enterprise", paramsWrap1.getEnterprise());
		map.put("foreignEnterprise", paramsWrap1.getForeignEnterprise());
		map.put("totalPrice", totalPrice);
		map.put("dateTime", declareTime);
		map.putAll(params);

		return "invoice1";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/invoice2.do")
	public String invoice2(HttpServletRequest request, ModelMap map,
			ParamsWrap1 paramsWrap1, Date declareTime) throws Exception {
		Map<String, String[]> params = request.getParameterMap();
		Long id = new Long(params.get("id")[0]);
		CustomsDeclarationHead customsDeclarationHead = customsDeclarationHeadDao
				.get(id);
		List<CustomsDeclarationGood> customsDeclarationGoods = customsDeclarationGoodDao
				.find("customsDeclarationHeadId", id);
		// getCustomsDeclarationHeadDetail(customsDeclarationHead);
		double totalPrice = 0;
		for (CustomsDeclarationGood customsDeclarationGood : customsDeclarationGoods) {
			getCustomsDeclarationGoodDetail(customsDeclarationGood);
			totalPrice += customsDeclarationGood.getTotalPrice();
		}

		Enterprise enterprise = enterpriseDao.get(customsDeclarationHead
				.getOwnerId());

		paramsWrap1.getEnterprise().setBank(enterprise.getBank());
		paramsWrap1.getEnterprise().setBankAccount(enterprise.getBankAccount());

		map.put("customsDeclarationGoods", customsDeclarationGoods);
		map.put("enterprise", paramsWrap1.getEnterprise());
		map.put("foreignEnterprise", paramsWrap1.getForeignEnterprise());
		map.put("totalPrice", totalPrice);
		map.put("dateTime", declareTime);
		map.putAll(params);

		return "invoice2";
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}

	private void getCustomsDeclarationHeadDetail(
			CustomsDeclarationHead customsDeclarationHead) {
		DealMode dealMode = dealModeDao.get(customsDeclarationHead
				.getDealMode());
		if (dealMode != null) {
			customsDeclarationHead.setDealMode(dealMode.getName());
		}				
		WrapType wrapType=wrapTypeDao.get(customsDeclarationHead.getWrapType());
		if (wrapType != null) {
			customsDeclarationHead.setWrapType(wrapType.getUnit());
		}
		Country country = countryDao.get(customsDeclarationHead.getTradeCountry());
		if (country != null) {
			customsDeclarationHead.setTradeCountry(country.getName());
		}

	}
	

	private void getCustomsDeclarationGoodDetail(
			CustomsDeclarationGood customsDeclarationGood) {
		Unit unit = unitDao.get(customsDeclarationGood.getDeclareUnit());
		if (unit != null) {
			customsDeclarationGood.setDeclareUnit(unit.getName());
		}
		Currency currency = currencyDao.get(customsDeclarationGood
				.getCurrency());
		if (currency != null) {
			customsDeclarationGood.setCurrency(currency.getShortName());
		}
		WrapType wrapType=wrapTypeDao.get(customsDeclarationGood.getWrapType());
		if (wrapType != null) {
			customsDeclarationGood.setWrapType(wrapType.getUnit());
		}
		Country country = countryDao.get(customsDeclarationGood.getOriginCountry());
		if (country != null) {
			customsDeclarationGood.setOriginCountry(country.getName());
		}
	}
	
	private void getPackingItemDetail(
			PackingItem packingItem) {
		Unit unit = unitDao.get(packingItem.getDeclareUnit());
		if (unit != null) {
			packingItem.setDeclareUnit(unit.getName());
		}
				
	}

	@Autowired
	private CustomsDeclarationHeadDao customsDeclarationHeadDao;
	@Autowired
	private CustomsDeclarationGoodDao customsDeclarationGoodDao;
	@Autowired
	private PackingItemDao packingItemDao;
	@Autowired
	private EnterpriseDao enterpriseDao;

	@Autowired
	private DealModeDao dealModeDao;
	@Autowired
	private UnitDao unitDao;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private WrapTypeDao wrapTypeDao;
	@Autowired
	private CountryDao countryDao;
	@Autowired
	private TruckDao truckDao;
}
