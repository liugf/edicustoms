package com.gavin.ediCustoms.server.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.ForeignEnterprise;
import com.gavin.ediCustoms.entity.edi.contract.ContractHead;
import com.gavin.ediCustoms.entity.edi.core.Container;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationGood;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.dictionary.Country;
import com.gavin.ediCustoms.entity.edi.dictionary.Currency;
import com.gavin.ediCustoms.entity.edi.dictionary.Customs;
import com.gavin.ediCustoms.entity.edi.dictionary.District;
import com.gavin.ediCustoms.entity.edi.dictionary.LoadPort;
import com.gavin.ediCustoms.entity.edi.dictionary.TradeMode;
import com.gavin.ediCustoms.entity.edi.dictionary.Truck;
import com.gavin.ediCustoms.entity.edi.dictionary.WrapType;
import com.gavin.ediCustoms.server.dao.ContainerDao;
import com.gavin.ediCustoms.server.dao.ContractHeadDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationGoodDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationHeadDao;
import com.gavin.ediCustoms.server.dao.EnterpriseDao;
import com.gavin.ediCustoms.server.dao.ForeignEnterpriseDao;
import com.gavin.ediCustoms.server.dao.dictionary.CountryDao;
import com.gavin.ediCustoms.server.dao.dictionary.CurrencyDao;
import com.gavin.ediCustoms.server.dao.dictionary.CustomsDao;
import com.gavin.ediCustoms.server.dao.dictionary.DistrictDao;
import com.gavin.ediCustoms.server.dao.dictionary.LoadPortDao;
import com.gavin.ediCustoms.server.dao.dictionary.TradeModeDao;
import com.gavin.ediCustoms.server.dao.dictionary.TruckDao;
import com.gavin.ediCustoms.server.dao.dictionary.WrapTypeDao;

@Service("driverPaperUtil")
public class DriverPaperUtil {
	public Map<String, Object> getContent(String transportTool) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<CustomsDeclarationHead> customsDeclarationHeads = customsDeclarationHeadDao
				.find("transportTool", transportTool);
		if (customsDeclarationHeads.size() == 0) {
			return result;
		}
		
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat weightFormat = new DecimalFormat("0.###");
		DecimalFormat moneyFormat = new DecimalFormat("0.00");

		Truck truck = truckDao.get(customsDeclarationHeads.get(0)
				.getTruckCode());
		if (truck != null) {
			result.put("truckNo", truck.getTruckNo());
			result.put("hkTruckNo", truck.getHkTruckNo());
			result.put("truckCorporationName", truck.getCorporationName());
			result.put("truckCorporationAddress", truck.getOrganizationAddress());
			result.put("truckCorporationTelephone", truck.getOrganizationTelephone());
			result.put("truckDriverName", truck.getDriverName());
			result.put("truckCode", truck.getCode());
			
		}

		CustomsDeclarationHead customsDeclarationHead = customsDeclarationHeads
				.get(0);
		if (customsDeclarationHead != null) {
			result.put("declareTime", dateformat.format(customsDeclarationHead.getDeclareTime()));
		}
		District district = districtDao.get(customsDeclarationHead
				.getOwnerDistrict());
		if (district != null) {
			result.put("ownerDistrict", district.getName());
		}
		TradeMode tradeMode=tradeModeDao.get(customsDeclarationHead.getTradeMode());
		if (tradeMode != null) {
			result.put("tradeMode", tradeMode.getName());
		}
		Customs iePort = customsDao.get(customsDeclarationHead.getIePort());
		if (iePort != null) {
			result.put("iePort", iePort.getName());
		}
		LoadPort loadPort =  loadPortDao.get(customsDeclarationHead.getLoadPort());
		if (loadPort != null) {
			Customs declareCustoms = customsDao.get(loadPort.getCustomsCode());
			if (declareCustoms!=null) {
				result.put("declareCustoms", declareCustoms.getName());
			}
		}
		
		result.put("no", 1);
		result.put("contractNo", customsDeclarationHead.getContractNo());

		int packNo = 0;
		double grossWeight = 0;
		double netWeight = 0;

		List<CustomsDeclarationGood> customsDeclarationGoods = new ArrayList<CustomsDeclarationGood>();
		for (int i = 0; i < customsDeclarationHeads.size(); i++) {
			customsDeclarationGoods.addAll(customsDeclarationGoodDao.find(
					"customsDeclarationHeadId", customsDeclarationHeads.get(i)
							.getId()));
			packNo += customsDeclarationHeads.get(i).getPackNo();
			grossWeight += customsDeclarationHeads.get(i).getGrossWeight();
			netWeight += customsDeclarationHeads.get(i).getNetWeight();
		}

		double totleMoney = 0;
		for (CustomsDeclarationGood customsDeclarationGood : customsDeclarationGoods) {
			totleMoney += customsDeclarationGood.getTotalPrice();
		}

		if (customsDeclarationGoods.size() > 0) {
			result.put("goodNameAndModel", customsDeclarationGoods.get(0).getName());
			result.put("goodCode", customsDeclarationGoods.get(0).getCodeTS());
			result.put("andMore", "等一批(详见清单)");
			if (customsDeclarationGoods.get(0).getOriginCountry()!=null) {
				Country country=countryDao.get(customsDeclarationGoods.get(0).getOriginCountry());
				if (country!=null) {
					result.put("originCountry",country.getName());
				}
			}
			
			if (customsDeclarationGoods.get(0).getCurrency()!=null) {
				Currency currency = currencyDao.get(customsDeclarationGoods.get(0).getCurrency());
				if (currency!=null) {
					result.put("totleMoney", currency.getShortName() +" "+ moneyFormat.format(totleMoney));
				}
				
			}
			
		}

		WrapType wrapType = wrapTypeDao.get(customsDeclarationHead
				.getWrapType());
		if (wrapType != null) {
			result.put("wrapType", packNo + wrapType.getName());
			result.put("packNo", packNo + wrapType.getName());
		}

		result.put("grossWeight", weightFormat.format(grossWeight));
		result.put("netWeight", weightFormat.format(netWeight));
		
		Enterprise enterprise = enterpriseDao.get(customsDeclarationHead.getOwnerId());
		if (enterprise!=null) {
			result.put("enterpriseName", enterprise.getRegisteName());
			result.put("enterpriseName2", enterprise.getRegisteName());
		}
		if (customsDeclarationHead.getContractHeadId()!=null) {
			ContractHead contractHead = contractHeadDao.get(customsDeclarationHead.getContractHeadId());
			if (contractHead!=null) {
				if (contractHead.getForeignEnterpriseId()!=null) {
					ForeignEnterprise foreignEnterprise=foreignEnterpriseDao.get(contractHead.getForeignEnterpriseId());
					if (foreignEnterprise!=null) {
						result.put("foreignEnterpriseName", foreignEnterprise.getRegisteName());
					}
				}
				
			}
		}
		List<Container> containers = containerDao.find("customsDeclarationHeadId", customsDeclarationHead.getId());
		if (containers.size()>0) {
			result.put("containerNo", containers.get(0).getContainerNo());
		}else {
			result.put("containerNo", "吨车");
		}
		

		return result;
	}

	@Autowired
	private CustomsDeclarationHeadDao customsDeclarationHeadDao;
	@Autowired
	private CustomsDeclarationGoodDao customsDeclarationGoodDao;
	@Autowired
	private EnterpriseDao enterpriseDao;
	@Autowired
	private ForeignEnterpriseDao foreignEnterpriseDao;
	@Autowired
	private ContractHeadDao contractHeadDao;
	@Autowired
	private TruckDao truckDao;
	@Autowired
	private DistrictDao districtDao;
	@Autowired
	private WrapTypeDao wrapTypeDao;
	@Autowired
	private ContainerDao containerDao;
	@Autowired
	private TradeModeDao tradeModeDao;
	@Autowired
	private CustomsDao customsDao;
	@Autowired
	private LoadPortDao loadPortDao;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private CountryDao countryDao;
}
