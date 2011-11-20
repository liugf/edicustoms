package com.gavin.ediCustoms.server;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


import com.gavin.ediCustoms.client.service.DictionaryService;
import com.gavin.ediCustoms.entity.edi.dictionary.Attachment;
import com.gavin.ediCustoms.entity.edi.dictionary.Bracket;
import com.gavin.ediCustoms.entity.edi.dictionary.BringInMode;
import com.gavin.ediCustoms.entity.edi.dictionary.ContainerSize;
import com.gavin.ediCustoms.entity.edi.dictionary.Country;
import com.gavin.ediCustoms.entity.edi.dictionary.Currency;
import com.gavin.ediCustoms.entity.edi.dictionary.Customs;
import com.gavin.ediCustoms.entity.edi.dictionary.DealMode;
import com.gavin.ediCustoms.entity.edi.dictionary.DeclareProperty;
import com.gavin.ediCustoms.entity.edi.dictionary.District;
import com.gavin.ediCustoms.entity.edi.dictionary.GoodClassification;
import com.gavin.ediCustoms.entity.edi.dictionary.InvestMode;
import com.gavin.ediCustoms.entity.edi.dictionary.IronChest;
import com.gavin.ediCustoms.entity.edi.dictionary.LoadPort;
import com.gavin.ediCustoms.entity.edi.dictionary.PayWay;
import com.gavin.ediCustoms.entity.edi.dictionary.Port;
import com.gavin.ediCustoms.entity.edi.dictionary.ProcessType;
import com.gavin.ediCustoms.entity.edi.dictionary.TaxKind;
import com.gavin.ediCustoms.entity.edi.dictionary.TaxMode;
import com.gavin.ediCustoms.entity.edi.dictionary.TradeMode;
import com.gavin.ediCustoms.entity.edi.dictionary.TransportMode;
import com.gavin.ediCustoms.entity.edi.dictionary.Truck;
import com.gavin.ediCustoms.entity.edi.dictionary.Unit;
import com.gavin.ediCustoms.entity.edi.dictionary.Useage;
import com.gavin.ediCustoms.entity.edi.dictionary.WrapType;
import com.gavin.ediCustoms.server.dao.base.BaseDao;
import com.gavin.ediCustoms.server.dao.dictionary.CustomsDao;
import com.gavin.ediCustoms.server.dao.dictionary.DealModeDao;
import com.gavin.ediCustoms.server.dao.dictionary.DistrictDao;
import com.gavin.ediCustoms.server.dao.dictionary.PayWayDao;
import com.gavin.ediCustoms.server.dao.dictionary.PortDao;
import com.gavin.ediCustoms.server.dao.dictionary.TradeModeDao;
import com.gavin.ediCustoms.server.dao.dictionary.WrapTypeDao;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DictionaryServiceImpl extends RemoteServiceServlet implements
		DictionaryService {

	private WebApplicationContext ctx;
	private BaseDao<LoadPort> loadPortDao;
	private WrapTypeDao wrapTypeDao;
	private CustomsDao customsDao;
	private BaseDao<Country> countryDao;
	private BaseDao<Currency> currencyDao;
	private BaseDao<GoodClassification> goodClassificationDao;
	private BaseDao<Unit> unitDao;
	private TradeModeDao tradeModeDao;
	private BaseDao<TaxKind> taxKindDao;
	private BaseDao<ProcessType> processTypeDao;
	private BaseDao<InvestMode> investModeDao;
	private DealModeDao dealModeDao;
	private BaseDao<BringInMode> bringInModeDao;
	private BaseDao<TaxMode> taxModeDao;
	private BaseDao<TransportMode> transportModeDao;
	private PortDao portDao;
	private DistrictDao districtDao;
	private PayWayDao payWayDao;
	private BaseDao<Useage> useageDao;
	private BaseDao<Bracket> bracketDao;
	private BaseDao<IronChest> ironChestDao;
	private BaseDao<ContainerSize> containerSizeDao;
	private BaseDao<Truck> truckDao;
	private BaseDao<Attachment> attachmentDao;
	private BaseDao<DeclareProperty> declarePropertyDao;
	
	

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws ServletException {
		super.init();
		ctx = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
		if (ctx == null) {
			throw new RuntimeException(
					"Check Your Web.Xml Setting, No Spring Context Configured");
		}
		loadPortDao = (BaseDao<LoadPort>) ctx.getBean("loadPortDao");
		wrapTypeDao = (WrapTypeDao) ctx.getBean("wrapTypeDao");
		customsDao = (CustomsDao) ctx.getBean("customsDao");
		countryDao = (BaseDao<Country>) ctx.getBean("countryDao");
		currencyDao = (BaseDao<Currency>) ctx.getBean("currencyDao");
		goodClassificationDao = (BaseDao<GoodClassification>) ctx.getBean("goodClassificationDao");
		unitDao = (BaseDao<Unit>) ctx.getBean("unitDao");
		tradeModeDao=(TradeModeDao) ctx.getBean("tradeModeDao");
		taxKindDao=(BaseDao<TaxKind>) ctx.getBean("taxKindDao");
		processTypeDao=(BaseDao<ProcessType>) ctx.getBean("processTypeDao");
		investModeDao=(BaseDao<InvestMode>) ctx.getBean("investModeDao");
		dealModeDao=(DealModeDao) ctx.getBean("dealModeDao");
		bringInModeDao=(BaseDao<BringInMode>) ctx.getBean("bringInModeDao");
		taxModeDao=(BaseDao<TaxMode>) ctx.getBean("taxModeDao");
		transportModeDao=(BaseDao<TransportMode>) ctx.getBean("transportModeDao");
		portDao=(PortDao) ctx.getBean("portDao");
		districtDao=(DistrictDao) ctx.getBean("districtDao");
		payWayDao=(PayWayDao) ctx.getBean("payWayDao");
		useageDao=(BaseDao<Useage>) ctx.getBean("useageDao");	
		bracketDao=(BaseDao<Bracket>) ctx.getBean("bracketDao");	
		ironChestDao=(BaseDao<IronChest>) ctx.getBean("ironChestDao");	
		containerSizeDao=(BaseDao<ContainerSize>) ctx.getBean("containerSizeDao");	
		truckDao=(BaseDao<Truck>) ctx.getBean("truckDao");	
		attachmentDao=(BaseDao<Attachment>) ctx.getBean("attachmentDao");
		declarePropertyDao=(BaseDao<DeclareProperty>) ctx.getBean("declarePropertyDao");
	}

	@Override
	public List<LoadPort> listLoadPort() {
		return loadPortDao.list();
	}

	@Override
	public Long saveLoadPort(LoadPort loadPort) {
		return (Long) loadPortDao.save(loadPort);
	}

	@Override
	public void deleteLoadPort(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			loadPortDao.delete(id);
		}
	}

	@Override
	public void updateLoadPort(List<LoadPort> loadPorts) {
		for (Iterator<LoadPort> iterator = loadPorts.iterator(); iterator
				.hasNext();) {
			LoadPort loadPort = (LoadPort) iterator.next();
			loadPortDao.update(loadPort);
		}
	}
	
	@Override
	public WrapType getWrapType(String code){
		return wrapTypeDao.get(code);
	}

	@Override
	public List<WrapType> listWrapType() {
		return wrapTypeDao.list();
	}

	@Override
	public Long saveWrapType(WrapType wrapType) {
		return (Long) wrapTypeDao.save(wrapType);
	}

	@Override
	public void deleteWrapType(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			wrapTypeDao.delete(id);
		}
	}

	@Override
	public void updateWrapType(List<WrapType> wrapTypes) {
		for (Iterator<WrapType> iterator = wrapTypes.iterator(); iterator
				.hasNext();) {
			WrapType wrapType = (WrapType) iterator.next();
			wrapTypeDao.update(wrapType);
		}
	}
	
	@Override
	public Customs getCustoms(String code) {		
		return customsDao.get(code);
	}

	@Override
	public List<Customs> listCustoms() {
		return customsDao.list();
	}

	@Override
	public Long saveCustoms(Customs customs) {
		return (Long) customsDao.save(customs);
	}

	@Override
	public void deleteCustoms(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			customsDao.delete(id);
		}
	}

	@Override
	public void updateCustoms(List<Customs> customss) {
		for (Iterator<Customs> iterator = customss.iterator(); iterator
				.hasNext();) {
			Customs customs = (Customs) iterator.next();
			customsDao.update(customs);
		}
	}
	
	@Override
	public List<Country> listCountry() {
		return countryDao.list();
	}

	@Override
	public Long saveCountry(Country country) {
		return (Long) countryDao.save(country);
	}

	@Override
	public void deleteCountry(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			countryDao.delete(id);
		}
	}

	@Override
	public void updateCountry(List<Country> countrys) {
		for (Iterator<Country> iterator = countrys.iterator(); iterator
				.hasNext();) {
			Country country = (Country) iterator.next();
			countryDao.update(country);
		}
	}
	
	@Override
	public List<Currency> listCurrency() {
		return currencyDao.list();
	}

	@Override
	public Long saveCurrency(Currency currency) {
		return (Long) currencyDao.save(currency);
	}

	@Override
	public void deleteCurrency(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			currencyDao.delete(id);
		}
	}

	@Override
	public void updateCurrency(List<Currency> currencys) {
		for (Iterator<Currency> iterator = currencys.iterator(); iterator
				.hasNext();) {
			Currency currency = (Currency) iterator.next();
			currencyDao.update(currency);
		}
	}
	
	@Override
	public List<GoodClassification> listGoodClassification() {
		return goodClassificationDao.list();
	}

	@Override
	public Long saveGoodClassification(GoodClassification goodClassification) {
		return (Long) goodClassificationDao.save(goodClassification);
	}

	@Override
	public void deleteGoodClassification(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			goodClassificationDao.delete(id);
		}
	}

	@Override
	public void updateGoodClassification(List<GoodClassification> goodClassifications) {
		for (Iterator<GoodClassification> iterator = goodClassifications.iterator(); iterator
				.hasNext();) {
			GoodClassification goodClassification = (GoodClassification) iterator.next();
			goodClassificationDao.update(goodClassification);
		}
	}
	
	@Override
	public List<Unit> listUnit() {
		return unitDao.list();
	}

	@Override
	public Long saveUnit(Unit unit) {
		return (Long) unitDao.save(unit);
	}

	@Override
	public void deleteUnit(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			unitDao.delete(id);
		}
	}

	@Override
	public void updateUnit(List<Unit> units) {
		for (Iterator<Unit> iterator = units.iterator(); iterator
				.hasNext();) {
			Unit unit = (Unit) iterator.next();
			unitDao.update(unit);
		}
	}
	
	@Override
	public TradeMode getTradeMode(String code){
		return tradeModeDao.get(code);		
	}
	
	@Override
	public List<TradeMode> listTradeMode() {
		return tradeModeDao.list();
	}

	@Override
	public Long saveTradeMode(TradeMode tradeMode) {
		return (Long) tradeModeDao.save(tradeMode);
	}

	@Override
	public void deleteTradeMode(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			tradeModeDao.delete(id);
		}
	}

	@Override
	public void updateTradeMode(List<TradeMode> tradeModes) {
		for (Iterator<TradeMode> iterator = tradeModes.iterator(); iterator
				.hasNext();) {
			TradeMode tradeMode = (TradeMode) iterator.next();
			tradeModeDao.update(tradeMode);
		}
	}
	
	@Override
	public List<TaxKind> listTaxKind() {
		return taxKindDao.list();
	}

	@Override
	public Long saveTaxKind(TaxKind taxKind) {
		return (Long) taxKindDao.save(taxKind);
	}

	@Override
	public void deleteTaxKind(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			taxKindDao.delete(id);
		}
	}

	@Override
	public void updateTaxKind(List<TaxKind> taxKinds) {
		for (Iterator<TaxKind> iterator = taxKinds.iterator(); iterator
				.hasNext();) {
			TaxKind taxKind = (TaxKind) iterator.next();
			taxKindDao.update(taxKind);
		}
	}
	
	@Override
	public List<ProcessType> listProcessType() {
		return processTypeDao.list();
	}

	@Override
	public Long saveProcessType(ProcessType processType) {
		return (Long) processTypeDao.save(processType);
	}

	@Override
	public void deleteProcessType(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			processTypeDao.delete(id);
		}
	}

	@Override
	public void updateProcessType(List<ProcessType> processTypes) {
		for (Iterator<ProcessType> iterator = processTypes.iterator(); iterator
				.hasNext();) {
			ProcessType processType = (ProcessType) iterator.next();
			processTypeDao.update(processType);
		}
	}
	
	@Override
	public List<InvestMode> listInvestMode() {
		return investModeDao.list();
	}

	@Override
	public Long saveInvestMode(InvestMode investMode) {
		return (Long) investModeDao.save(investMode);
	}

	@Override
	public void deleteInvestMode(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			investModeDao.delete(id);
		}
	}

	@Override
	public void updateInvestMode(List<InvestMode> investModes) {
		for (Iterator<InvestMode> iterator = investModes.iterator(); iterator
				.hasNext();) {
			InvestMode investMode = (InvestMode) iterator.next();
			investModeDao.update(investMode);
		}
	}
	
	@Override
	public DealMode getDealMode(String code) {		
		return dealModeDao.get(code);
	}
	
	@Override
	public List<DealMode> listDealMode() {
		return dealModeDao.list();
	}

	@Override
	public Long saveDealMode(DealMode dealMode) {
		return (Long) dealModeDao.save(dealMode);
	}

	@Override
	public void deleteDealMode(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			dealModeDao.delete(id);
		}
	}

	@Override
	public void updateDealMode(List<DealMode> dealModes) {
		for (Iterator<DealMode> iterator = dealModes.iterator(); iterator
				.hasNext();) {
			DealMode dealMode = (DealMode) iterator.next();
			dealModeDao.update(dealMode);
		}
	}
	
	@Override
	public List<BringInMode> listBringInMode() {
		return bringInModeDao.list();
	}

	@Override
	public Long saveBringInMode(BringInMode bringInMode) {
		return (Long) bringInModeDao.save(bringInMode);
	}

	@Override
	public void deleteBringInMode(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			bringInModeDao.delete(id);
		}
	}

	@Override
	public void updateBringInMode(List<BringInMode> bringInModes) {
		for (Iterator<BringInMode> iterator = bringInModes.iterator(); iterator
				.hasNext();) {
			BringInMode bringInMode = (BringInMode) iterator.next();
			bringInModeDao.update(bringInMode);
		}
	}
	
	@Override
	public List<TaxMode> listTaxMode() {
		return taxModeDao.list();
	}

	@Override
	public Long saveTaxMode(TaxMode taxMode) {
		return (Long) taxModeDao.save(taxMode);
	}

	@Override
	public void deleteTaxMode(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			taxModeDao.delete(id);
		}
	}

	@Override
	public void updateTaxMode(List<TaxMode> taxModes) {
		for (Iterator<TaxMode> iterator = taxModes.iterator(); iterator
				.hasNext();) {
			TaxMode taxMode = (TaxMode) iterator.next();
			taxModeDao.update(taxMode);
		}
	}
	
	@Override
	public List<TransportMode> listTransportMode() {
		return transportModeDao.list();
	}

	@Override
	public Long saveTransportMode(TransportMode transportMode) {
		return (Long) transportModeDao.save(transportMode);
	}

	@Override
	public void deleteTransportMode(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			transportModeDao.delete(id);
		}
	}

	@Override
	public void updateTransportMode(List<TransportMode> transportModes) {
		for (Iterator<TransportMode> iterator = transportModes.iterator(); iterator
				.hasNext();) {
			TransportMode transportMode = (TransportMode) iterator.next();
			transportModeDao.update(transportMode);
		}
	}
	
	@Override
	public Port getPort(String code) {
		return portDao.get(code);
	}
	
	@Override
	public List<Port> listPort() {
		return portDao.list();
	}

	@Override
	public Long savePort(Port port) {
		return (Long) portDao.save(port);
	}

	@Override
	public void deletePort(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			portDao.delete(id);
		}
	}

	@Override
	public void updatePort(List<Port> ports) {
		for (Iterator<Port> iterator = ports.iterator(); iterator
				.hasNext();) {
			Port port = (Port) iterator.next();
			portDao.update(port);
		}
	}
	
	@Override
	public District getDistrict(String code) {
		return districtDao.get(code);
	}
	
	@Override
	public List<District> listDistrict() {
		return districtDao.list();
	}

	@Override
	public Long saveDistrict(District district) {
		return (Long) districtDao.save(district);
	}

	@Override
	public void deleteDistrict(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			districtDao.delete(id);
		}
	}

	@Override
	public void updateDistrict(List<District> districts) {
		for (Iterator<District> iterator = districts.iterator(); iterator
				.hasNext();) {
			District district = (District) iterator.next();
			districtDao.update(district);
		}
	}
	
	@Override
	public PayWay getPayWay(String code){
		return payWayDao.get(code);
	}
	
	@Override
	public List<PayWay> listPayWay() {
		return payWayDao.list();
	}

	@Override
	public Long savePayWay(PayWay payWay) {
		return (Long) payWayDao.save(payWay);
	}

	@Override
	public void deletePayWay(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			payWayDao.delete(id);
		}
	}

	@Override
	public void updatePayWay(List<PayWay> payWays) {
		for (Iterator<PayWay> iterator = payWays.iterator(); iterator
				.hasNext();) {
			PayWay payWay = (PayWay) iterator.next();
			payWayDao.update(payWay);
		}
	}
	
	@Override
	public List<Useage> listUseage() {
		return useageDao.list();
	}

	@Override
	public Long saveUseage(Useage useage) {
		return (Long) useageDao.save(useage);
	}

	@Override
	public void deleteUseage(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			useageDao.delete(id);
		}
	}

	@Override
	public void updateUseage(List<Useage> useages) {
		for (Iterator<Useage> iterator = useages.iterator(); iterator
				.hasNext();) {
			Useage useage = (Useage) iterator.next();
			useageDao.update(useage);
		}
	}
	
	@Override
	public List<Bracket> listBracket() {
		return bracketDao.list();
	}

	@Override
	public Long saveBracket(Bracket bracket) {
		return (Long) bracketDao.save(bracket);
	}

	@Override
	public void deleteBracket(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			bracketDao.delete(id);
		}
	}

	@Override
	public void updateBracket(List<Bracket> brackets) {
		for (Iterator<Bracket> iterator = brackets.iterator(); iterator
				.hasNext();) {
			Bracket bracket = (Bracket) iterator.next();
			bracketDao.update(bracket);
		}
	}
	
	@Override
	public List<IronChest> listIronChest() {
		return ironChestDao.list();
	}

	@Override
	public Long saveIronChest(IronChest ironChest) {
		return (Long) ironChestDao.save(ironChest);
	}

	@Override
	public void deleteIronChest(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			ironChestDao.delete(id);
		}
	}

	@Override
	public void updateIronChest(List<IronChest> ironChests) {
		for (Iterator<IronChest> iterator = ironChests.iterator(); iterator
				.hasNext();) {
			IronChest ironChest = (IronChest) iterator.next();
			ironChestDao.update(ironChest);
		}
	}
	
	@Override
	public List<ContainerSize> listContainerSize() {
		return containerSizeDao.list();
	}

	@Override
	public Long saveContainerSize(ContainerSize containerSize) {
		return (Long) containerSizeDao.save(containerSize);
	}

	@Override
	public void deleteContainerSize(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			containerSizeDao.delete(id);
		}
	}

	@Override
	public void updateContainerSize(List<ContainerSize> containerSizes) {
		for (Iterator<ContainerSize> iterator = containerSizes.iterator(); iterator
				.hasNext();) {
			ContainerSize containerSize = (ContainerSize) iterator.next();
			containerSizeDao.update(containerSize);
		}
	}
	
	@Override
	public List<Truck> listTruck() {
		return truckDao.list();
	}

	@Override
	public Long saveTruck(Truck truck) {
		return (Long) truckDao.save(truck);
	}

	@Override
	public void deleteTruck(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			truckDao.delete(id);
		}
	}

	@Override
	public void updateTruck(List<Truck> trucks) {
		for (Iterator<Truck> iterator = trucks.iterator(); iterator
				.hasNext();) {
			Truck truck = (Truck) iterator.next();
			truckDao.update(truck);
		}
	}
	
	@Override
	public List<Attachment> listAttachment() {
		return attachmentDao.list();
	}

	@Override
	public Long saveAttachment(Attachment attachment) {
		return (Long) attachmentDao.save(attachment);
	}

	@Override
	public void deleteAttachment(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			attachmentDao.delete(id);
		}
	}

	@Override
	public void updateAttachment(List<Attachment> attachments) {
		for (Iterator<Attachment> iterator = attachments.iterator(); iterator
				.hasNext();) {
			Attachment attachment = (Attachment) iterator.next();
			attachmentDao.update(attachment);
		}
	}
	
	@Override
	public List<DeclareProperty> listDeclareProperty() {
		return declarePropertyDao.list();
	}

	@Override
	public Long saveDeclareProperty(DeclareProperty declareProperty) {
		return (Long) declarePropertyDao.save(declareProperty);
	}

	@Override
	public void deleteDeclareProperty(List<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			declarePropertyDao.delete(id);
		}
	}

	@Override
	public void updateDeclareProperty(List<DeclareProperty> declarePropertys) {
		for (Iterator<DeclareProperty> iterator = declarePropertys.iterator(); iterator
				.hasNext();) {
			DeclareProperty declareProperty = (DeclareProperty) iterator.next();
			declarePropertyDao.update(declareProperty);
		}
	}

}
