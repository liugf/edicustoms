package com.gavin.ediCustoms.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.gavin.ediCustoms.entity.edi.contract.ContractProduct;
import com.gavin.ediCustoms.entity.edi.dictionary.Currency;
import com.gavin.ediCustoms.entity.edi.dictionary.Unit;
import com.gavin.ediCustoms.server.dao.ContractHeadDao;
import com.gavin.ediCustoms.server.dao.ContractProductDao;
import com.gavin.ediCustoms.server.dao.dictionary.CurrencyDao;
import com.gavin.ediCustoms.server.dao.dictionary.UnitDao;

class ParamsWrap2{
	private Enterprise enterprise=new Enterprise();
	private ForeignEnterprise foreignEnterprise=new ForeignEnterprise();
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
@RequestMapping("/contract1.do")
public class ContractController{
	
	@SuppressWarnings("unchecked")
	@RequestMapping
	public String contract(ContractHead contractHead,HttpServletRequest request,ModelMap map,ParamsWrap2 paramsWrap1) throws Exception {
		Map<String, String[]> params=request.getParameterMap();
		Long id=new Long(params.get("id")[0]);
		contractHead.setExportCurrency(contractHeadDao.get(id).getExportCurrency());
		getContractHeadDetail(contractHead);
		List<ContractProduct> contractProducts = contractProductDao
				.find("contractHeadId", id);
		double totalPrice=0;
		for(ContractProduct contractProduct:contractProducts){
			getContractProductDetail(contractProduct);
			totalPrice+=contractProduct.getPrice()*contractProduct.getQuantity();
		}
		
		map.put("contractHead", contractHead);
		map.put("contractProducts", contractProducts);		
		map.put("enterprise", paramsWrap1.getEnterprise());
		map.put("foreignEnterprise", paramsWrap1.getForeignEnterprise());		
		map.put("totalPrice", totalPrice);	
		map.putAll(params);	
		
		return "contract";
	}
	
	@InitBinder
	 protected void initBinder(WebDataBinder binder) throws Exception { 
	        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	        CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
	        binder.registerCustomEditor(Date.class, dateEditor);
	}
	
	private void getContractProductDetail(
			ContractProduct contractProduct) {
		Unit unit=unitDao.get(contractProduct.getDeclareUnit());
		if (unit!=null) {
			contractProduct.setDeclareUnit(unit.getName());
		}		
	}
	
	private void getContractHeadDetail(
			ContractHead contractHead) {		
		Currency currency=currencyDao.get(contractHead.getExportCurrency());
		if (currency!=null) {
			contractHead.setExportCurrency(currency.getShortName());
		}
		
	}

	
	@Autowired
	private ContractHeadDao contractHeadDao;
	@Autowired
	private ContractProductDao contractProductDao;
	@Autowired
	private UnitDao unitDao;
	@Autowired
	private CurrencyDao currencyDao;
	
	
}
