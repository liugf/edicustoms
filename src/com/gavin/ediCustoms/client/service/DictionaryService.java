package com.gavin.ediCustoms.client.service;

import java.util.List;

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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("dictionary")
public interface DictionaryService extends RemoteService {
	//LoadPort
	List<LoadPort> listLoadPort();
	Long saveLoadPort(LoadPort loadPort);
	void deleteLoadPort(List<Long> ids);
	void updateLoadPort(List<LoadPort> loadPorts);

	//WrapType
	WrapType getWrapType(String code);
	List<WrapType> listWrapType();
	Long saveWrapType(WrapType wrapType);
	void deleteWrapType(List<Long> ids);
	void updateWrapType(List<WrapType> wrapTypes);
	
	//Customs
	Customs getCustoms(String code);
	List<Customs> listCustoms();
	Long saveCustoms(Customs customs);
	void deleteCustoms(List<Long> ids);
	void updateCustoms(List<Customs> customs);
	
	//Country
	List<Country> listCountry();
	Long saveCountry(Country country);
	void deleteCountry(List<Long> ids);
	void updateCountry(List<Country> countrys);
	
	//Currency
	List<Currency> listCurrency();
	Long saveCurrency(Currency currency);
	void deleteCurrency(List<Long> ids);
	void updateCurrency(List<Currency> currencys);
	
	//GoodClassification
	List<GoodClassification> listGoodClassification();
	Long saveGoodClassification(GoodClassification goodClassification);
	void deleteGoodClassification(List<Long> ids);
	void updateGoodClassification(List<GoodClassification> goodClassifications);
	
	//Unit
	List<Unit> listUnit();
	Long saveUnit(Unit unit);
	void deleteUnit(List<Long> ids);
	void updateUnit(List<Unit> units);
	
	//TradeMode
	TradeMode getTradeMode(String code);
	List<TradeMode> listTradeMode();
	Long saveTradeMode(TradeMode tradeMode);
	void deleteTradeMode(List<Long> ids);
	void updateTradeMode(List<TradeMode> tradeModes);
	
	//TaxKind
	List<TaxKind> listTaxKind();
	Long saveTaxKind(TaxKind taxKind);
	void deleteTaxKind(List<Long> ids);
	void updateTaxKind(List<TaxKind> taxKinds);
	
	//ProcessType
	List<ProcessType> listProcessType();
	Long saveProcessType(ProcessType processType);
	void deleteProcessType(List<Long> ids);
	void updateProcessType(List<ProcessType> processTypes);
	
	//InvestMode
	List<InvestMode> listInvestMode();
	Long saveInvestMode(InvestMode investMode);
	void deleteInvestMode(List<Long> ids);
	void updateInvestMode(List<InvestMode> investModes);
	
	//DealMode
	DealMode getDealMode(String code);
	List<DealMode> listDealMode();
	Long saveDealMode(DealMode dealMode);
	void deleteDealMode(List<Long> ids);
	void updateDealMode(List<DealMode> dealModes);
	
	//BringInMode
	List<BringInMode> listBringInMode();
	Long saveBringInMode(BringInMode bringInMode);
	void deleteBringInMode(List<Long> ids);
	void updateBringInMode(List<BringInMode> bringInModes);
	
	//TaxMode
	List<TaxMode> listTaxMode();
	Long saveTaxMode(TaxMode taxMode);
	void deleteTaxMode(List<Long> ids);
	void updateTaxMode(List<TaxMode> taxModes);
	
	//TransportMode
	List<TransportMode> listTransportMode();
	Long saveTransportMode(TransportMode transportMode);
	void deleteTransportMode(List<Long> ids);
	void updateTransportMode(List<TransportMode> transportModes);
	
	//Port
	Port getPort(String code);
	List<Port> listPort();
	Long savePort(Port port);
	void deletePort(List<Long> ids);
	void updatePort(List<Port> ports);
	
	//District
	District getDistrict(String code);
	List<District> listDistrict();
	Long saveDistrict(District district);
	void deleteDistrict(List<Long> ids);
	void updateDistrict(List<District> districts);
	
	//PayWay
	PayWay getPayWay(String code);
	List<PayWay> listPayWay();
	Long savePayWay(PayWay payWay);
	void deletePayWay(List<Long> ids);
	void updatePayWay(List<PayWay> payWays);
	
	//Useage
	List<Useage> listUseage();
	Long saveUseage(Useage useage);
	void deleteUseage(List<Long> ids);
	void updateUseage(List<Useage> useages);
	
	//Bracket
	List<Bracket> listBracket();
	Long saveBracket(Bracket bracket);
	void deleteBracket(List<Long> ids);
	void updateBracket(List<Bracket> brackets);
	
	//IronChest
	List<IronChest> listIronChest();
	Long saveIronChest(IronChest ironChest);
	void deleteIronChest(List<Long> ids);
	void updateIronChest(List<IronChest> ironChests);
	
	//ContainerSize
	List<ContainerSize> listContainerSize();
	Long saveContainerSize(ContainerSize containerSize);
	void deleteContainerSize(List<Long> ids);
	void updateContainerSize(List<ContainerSize> containerSizes);
	
	//Truck
	List<Truck> listTruck();
	Long saveTruck(Truck truck);
	void deleteTruck(List<Long> ids);
	void updateTruck(List<Truck> trucks);
	
	//Attachment
	List<Attachment> listAttachment();
	Long saveAttachment(Attachment attachment);
	void deleteAttachment(List<Long> ids);
	void updateAttachment(List<Attachment> attachments);
	
	//DeclareProperty
	List<DeclareProperty> listDeclareProperty();
	Long saveDeclareProperty(DeclareProperty declarePropery);
	void deleteDeclareProperty(List<Long> ids);
	void updateDeclareProperty(List<DeclareProperty> declareProperys);
	
	
}
