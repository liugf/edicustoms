package com.gavin.ediCustoms.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.contract.ContractHead;
import com.gavin.ediCustoms.entity.edi.core.Container;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationGood;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.dictionary.Attachment;
import com.gavin.ediCustoms.entity.edi.dictionary.ContainerSize;
import com.gavin.ediCustoms.entity.edi.dictionary.Country;
import com.gavin.ediCustoms.entity.edi.dictionary.Currency;
import com.gavin.ediCustoms.entity.edi.dictionary.Customs;
import com.gavin.ediCustoms.entity.edi.dictionary.DealMode;
import com.gavin.ediCustoms.entity.edi.dictionary.District;
import com.gavin.ediCustoms.entity.edi.dictionary.LoadPort;
import com.gavin.ediCustoms.entity.edi.dictionary.PayWay;
import com.gavin.ediCustoms.entity.edi.dictionary.Port;
import com.gavin.ediCustoms.entity.edi.dictionary.TaxKind;
import com.gavin.ediCustoms.entity.edi.dictionary.TaxMode;
import com.gavin.ediCustoms.entity.edi.dictionary.TradeMode;
import com.gavin.ediCustoms.entity.edi.dictionary.TransportMode;
import com.gavin.ediCustoms.entity.edi.dictionary.Unit;
import com.gavin.ediCustoms.entity.edi.dictionary.Useage;
import com.gavin.ediCustoms.entity.edi.dictionary.WrapType;
import com.gavin.ediCustoms.server.dao.ContainerDao;
import com.gavin.ediCustoms.server.dao.ContractHeadDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationGoodDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationHeadDao;
import com.gavin.ediCustoms.server.dao.EnterpriseDao;
import com.gavin.ediCustoms.server.dao.dictionary.AttachmentDao;
import com.gavin.ediCustoms.server.dao.dictionary.ContainerSizeDao;
import com.gavin.ediCustoms.server.dao.dictionary.CountryDao;
import com.gavin.ediCustoms.server.dao.dictionary.CurrencyDao;
import com.gavin.ediCustoms.server.dao.dictionary.CustomsDao;
import com.gavin.ediCustoms.server.dao.dictionary.DealModeDao;
import com.gavin.ediCustoms.server.dao.dictionary.DistrictDao;
import com.gavin.ediCustoms.server.dao.dictionary.LoadPortDao;
import com.gavin.ediCustoms.server.dao.dictionary.PayWayDao;
import com.gavin.ediCustoms.server.dao.dictionary.PortDao;
import com.gavin.ediCustoms.server.dao.dictionary.TaxKindDao;
import com.gavin.ediCustoms.server.dao.dictionary.TaxModeDao;
import com.gavin.ediCustoms.server.dao.dictionary.TradeModeDao;
import com.gavin.ediCustoms.server.dao.dictionary.TransportModeDao;
import com.gavin.ediCustoms.server.dao.dictionary.UnitDao;
import com.gavin.ediCustoms.server.dao.dictionary.UseageDao;
import com.gavin.ediCustoms.server.dao.dictionary.WrapTypeDao;
import com.gavin.ediCustoms.shared.MapUtil;

@Controller
public class CustomsDeclarationDraftController {

	private boolean isNomalTrade;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/customsDeclarationDraft.do")
	public String print(Long id,ModelMap map) throws Exception {
		CustomsDeclarationHead customsDeclarationHead = customsDeclarationHeadDao
				.get(id);
		Map<String, Object> condictions = new HashMap<String, Object>();
		condictions.put("customsDeclarationHeadId", id);
		List<CustomsDeclarationGood> customsDeclarationGoods = customsDeclarationGoodDao
				.find(condictions,true,"no",0);

		if (customsDeclarationHead.getTradeMode().equals("0110")) {
			isNomalTrade=true;
		}else {
			isNomalTrade=false;
		}
		
		getCustomsDeclarationHeadDetail(customsDeclarationHead);
		double totalPrice=0;
		for(CustomsDeclarationGood customsDeclarationGood:customsDeclarationGoods){
			getCustomsDeclarationGoodDetail(customsDeclarationGood);
			totalPrice+=customsDeclarationGood.getTotalPrice();
		}
		
		String manualNo = "";
		if (!isNomalTrade) {
			ContractHead contractHead = contractHeadDao.get(customsDeclarationHead.getContractHeadId());
			if (contractHead!=null) {
				manualNo=contractHead.getManualNo();
			}
			
		}
		Enterprise enterprise=enterpriseDao.get(customsDeclarationHead.getOwnerId());
		Enterprise runEnterprise=enterpriseDao.get(customsDeclarationHead.getRunEnterpriseId());
		Enterprise declareEnterprise=enterpriseDao.get(customsDeclarationHead.getDeclareEnterpriseId());
		
		
		map.put("customsDeclarationHead", customsDeclarationHead);
		map.put("customsDeclarationGoods", customsDeclarationGoods);
		map.put("manualNo", manualNo);
		map.put("enterprise", enterprise);
		map.put("runEnterprise", runEnterprise);
		map.put("declareEnterprise", declareEnterprise);
		map.put("totalPrice", totalPrice);
		map.put("container", getContainer(id));
		map.put("attachment", getAttachment(customsDeclarationHead.getCertMark()));
		map.put("containerDetails", getContainerDetails(customsDeclarationHead.getId()));
		map.put("isNomalTrade", isNomalTrade);
		
		return "customsDeclarationDraft";
	}

	private void getCustomsDeclarationHeadDetail(
			CustomsDeclarationHead customsDeclarationHead) {
		Customs customs=(Customs) customsDao.get(customsDeclarationHead.getIePort());	
		if (customs!=null) {
			customsDeclarationHead.setIePort(customs.getName()+"("+customs.getCode()+")");
		}
		TransportMode transportMode=transportModeDao.get(customsDeclarationHead.getTransportMode());
		if (transportMode!=null) {
			customsDeclarationHead.setTransportMode(transportMode.getName());
		}
		TradeMode tradeMode=tradeModeDao.get(customsDeclarationHead.getTradeMode());
		if (tradeMode!=null) {
			customsDeclarationHead.setTradeMode(tradeMode.getName()+"("+tradeMode.getCode()+")");
		}
		TaxKind taxKind=taxKindDao.get(customsDeclarationHead.getTaxKind());
		if (taxKind!=null) {
			customsDeclarationHead.setTaxKind(taxKind.getName()+"("+taxKind.getCode()+")");
		}
		PayWay payWay=payWayDao.get(customsDeclarationHead.getPayWay());
		if (payWay!=null) {
			customsDeclarationHead.setPayWay(payWay.getName());
		}
		Country country=countryDao.get(customsDeclarationHead.getTradeCountry());
		if (country!=null) {
			customsDeclarationHead.setTradeCountry(country.getName()+"("+country.getCode()+")");
		}
		Port port=portDao.get(customsDeclarationHead.getDestinationPort());
		if (port!=null) {
			customsDeclarationHead.setDestinationPort(port.getName()+"("+port.getCode()+")");
		}
		District district=districtDao.get(customsDeclarationHead.getOwnerDistrict());
		if (district!=null) {
			customsDeclarationHead.setOwnerDistrict(district.getName()+"("+district.getCode()+")");
		}
		DealMode dealMode=dealModeDao.get(customsDeclarationHead.getDealMode());
		if (dealMode!=null) {
			customsDeclarationHead.setDealMode(dealMode.getName());
		}
		WrapType wrapType=wrapTypeDao.get(customsDeclarationHead.getWrapType());
		if (wrapType!=null) {
			customsDeclarationHead.setWrapType(wrapType.getName());
		}
		Useage useage=useageDao.get(customsDeclarationHead.getUseage());
		if (useage!=null) {
			customsDeclarationHead.setUseage(useage.getName());
		}
		LoadPort loadPort=loadPortDao.get(customsDeclarationHead.getLoadPort());
		if (loadPort!=null) {
			customsDeclarationHead.setLoadPort(loadPort.getName());
		}
		
		
	}

	private void getCustomsDeclarationGoodDetail(
			CustomsDeclarationGood customsDeclarationGood) {
		Unit unit=unitDao.get(customsDeclarationGood.getDeclareUnit());
		if (unit!=null) {
			customsDeclarationGood.setDeclareUnit(unit.getName());
		}
		Unit unit1=unitDao.get(customsDeclarationGood.getUnit1());
		if (unit1!=null) {
			customsDeclarationGood.setUnit1(unit1.getName());
		}
		Unit unit2=unitDao.get(customsDeclarationGood.getUnit2());
		if (unit2!=null) {
			customsDeclarationGood.setUnit2(unit2.getName());
		}
		Country country=countryDao.get(customsDeclarationGood.getOriginCountry());
		if (country!=null) {
			customsDeclarationGood.setOriginCountry(country.getName()+"<br/>("+country.getCode()+")");
		}
		Currency currency=currencyDao.get(customsDeclarationGood.getCurrency());
		if (currency!=null) {
			customsDeclarationGood.setCurrency(currency.getName()+"<br/>("+currency.getShortName()+")");
		}
		TaxMode taxMode=taxModeDao.get(customsDeclarationGood.getTaxMode());
		if (taxMode!=null) {
			customsDeclarationGood.setTaxMode(taxMode.getName());
		}		
		
	}
	
	private String getContainer(Long customsDeclarationHeadId){		
		List<Container> containers=containerDao.find("customsDeclarationHeadId", customsDeclarationHeadId);
		int containerNum=containers.size();
		if (containerNum==0) {
			return "";
		}
		int standardNum = 0;
		for(Container container:containers){
			standardNum+=container.getValentNum();
		}
		return containers.get(0).getContainerNo()+" * "+containerNum+"("+standardNum+")";
	}
	
	private List<String> getContainerDetails(Long customsDeclarationHeadId){
		List<Container> containers=containerDao.find("customsDeclarationHeadId", customsDeclarationHeadId);
		List<String> containerDetails = new ArrayList<String>();
		int containerNum=containers.size();
		if (containerNum==0) {
			return containerDetails;
		}
		for (Container container : containers) {
			String detail = "";
			detail += container.getContainerNo() + "&nbsp;&nbsp;";
			ContainerSize containerSize = containerSizeDao.get(container.getSize());
			if (containerSize != null) {
				detail += containerSize.getId() + "(" + containerSize.getSize() + ")" + "&nbsp;&nbsp;";;
			}
			detail += container.getType() + "&nbsp;&nbsp;";
			detail += container.getBracket();	
			containerDetails.add(detail);
		}
		
		return containerDetails;
	}
	
	private String getAttachment(String  certMark){		
		StringBuilder stringBuilder = new StringBuilder();
		Map<String, String> map=MapUtil.stringToMap(certMark);
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
	private ContainerSizeDao containerSizeDao;
	
	@Autowired
	private CustomsDao customsDao;
	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private LoadPortDao loadPortDao;
	@Autowired
	private TransportModeDao transportModeDao;
	@Autowired
	private TradeModeDao tradeModeDao;
	@Autowired
	private TaxKindDao taxKindDao;
	@Autowired
	private PayWayDao payWayDao;
	@Autowired
	private UseageDao useageDao;
	@Autowired
	private CountryDao countryDao;
	@Autowired
	private PortDao portDao;
	@Autowired
	private DistrictDao districtDao;
	@Autowired
	private DealModeDao dealModeDao;
	@Autowired
	private WrapTypeDao wrapTypeDao;
	@Autowired
	private UnitDao unitDao;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private TaxModeDao taxModeDao;

	
}
