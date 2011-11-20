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
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DictionaryServiceAsync {
	//LoadPort
	void listLoadPort(AsyncCallback<List<LoadPort>> callback);
	void saveLoadPort(LoadPort loadPort,AsyncCallback<Long> callback);
	void deleteLoadPort(List<Long> ids,AsyncCallback<Void> callback);
	void updateLoadPort(List<LoadPort> loadPorts,AsyncCallback<Void> callback);
	
	//WrapType
	void getWrapType(String code,AsyncCallback<WrapType> callback);
	void listWrapType(AsyncCallback<List<WrapType>> callback);
	void saveWrapType(WrapType wrapType,AsyncCallback<Long> callback);
	void deleteWrapType(List<Long> ids,AsyncCallback<Void> callback);
	void updateWrapType(List<WrapType> loadPorts,AsyncCallback<Void> callback);
	
	//Customs
	void getCustoms(String code,AsyncCallback<Customs> callback);
	void listCustoms(AsyncCallback<List<Customs>> callback);
	void saveCustoms(Customs customs,AsyncCallback<Long> callback);
	void deleteCustoms(List<Long> ids,AsyncCallback<Void> callback);
	void updateCustoms(List<Customs> loadPorts,AsyncCallback<Void> callback);
	
	//Country
	void listCountry(AsyncCallback<List<Country>> callback);
	void saveCountry(Country country,AsyncCallback<Long> callback);
	void deleteCountry(List<Long> ids,AsyncCallback<Void> callback);
	void updateCountry(List<Country> loadPorts,AsyncCallback<Void> callback);
	
	//Currency
	void listCurrency(AsyncCallback<List<Currency>> callback);
	void saveCurrency(Currency currency,AsyncCallback<Long> callback);
	void deleteCurrency(List<Long> ids,AsyncCallback<Void> callback);
	void updateCurrency(List<Currency> loadPorts,AsyncCallback<Void> callback);
	
	//GoodClassification
	void listGoodClassification(AsyncCallback<List<GoodClassification>> callback);
	void saveGoodClassification(GoodClassification goodClassification,AsyncCallback<Long> callback);
	void deleteGoodClassification(List<Long> ids,AsyncCallback<Void> callback);
	void updateGoodClassification(List<GoodClassification> loadPorts,AsyncCallback<Void> callback);
	
	//Unit
	void listUnit(AsyncCallback<List<Unit>> callback);
	void saveUnit(Unit unit,AsyncCallback<Long> callback);
	void deleteUnit(List<Long> ids,AsyncCallback<Void> callback);
	void updateUnit(List<Unit> loadPorts,AsyncCallback<Void> callback);
	
	//TradeMode
	void getTradeMode(String code,AsyncCallback<TradeMode> callback);
	void listTradeMode(AsyncCallback<List<TradeMode>> callback);
	void saveTradeMode(TradeMode tradeMode,AsyncCallback<Long> callback);
	void deleteTradeMode(List<Long> ids,AsyncCallback<Void> callback);
	void updateTradeMode(List<TradeMode> loadPorts,AsyncCallback<Void> callback);
	
	//TaxKind
	void listTaxKind(AsyncCallback<List<TaxKind>> callback);
	void saveTaxKind(TaxKind taxKind,AsyncCallback<Long> callback);
	void deleteTaxKind(List<Long> ids,AsyncCallback<Void> callback);
	void updateTaxKind(List<TaxKind> loadPorts,AsyncCallback<Void> callback);
	
	//ProcessType
	void listProcessType(AsyncCallback<List<ProcessType>> callback);
	void saveProcessType(ProcessType processType,AsyncCallback<Long> callback);
	void deleteProcessType(List<Long> ids,AsyncCallback<Void> callback);
	void updateProcessType(List<ProcessType> loadPorts,AsyncCallback<Void> callback);
	
	//InvestMode
	void listInvestMode(AsyncCallback<List<InvestMode>> callback);
	void saveInvestMode(InvestMode investMode,AsyncCallback<Long> callback);
	void deleteInvestMode(List<Long> ids,AsyncCallback<Void> callback);
	void updateInvestMode(List<InvestMode> loadPorts,AsyncCallback<Void> callback);
	
	//DealMode
	void getDealMode(String code,AsyncCallback<DealMode> callback);
	void listDealMode(AsyncCallback<List<DealMode>> callback);
	void saveDealMode(DealMode dealMode,AsyncCallback<Long> callback);
	void deleteDealMode(List<Long> ids,AsyncCallback<Void> callback);
	void updateDealMode(List<DealMode> loadPorts,AsyncCallback<Void> callback);
	
	//BringInMode
	void listBringInMode(AsyncCallback<List<BringInMode>> callback);
	void saveBringInMode(BringInMode bringInMode,AsyncCallback<Long> callback);
	void deleteBringInMode(List<Long> ids,AsyncCallback<Void> callback);
	void updateBringInMode(List<BringInMode> loadPorts,AsyncCallback<Void> callback);
	
	//TaxMode
	void listTaxMode(AsyncCallback<List<TaxMode>> callback);
	void saveTaxMode(TaxMode taxMode,AsyncCallback<Long> callback);
	void deleteTaxMode(List<Long> ids,AsyncCallback<Void> callback);
	void updateTaxMode(List<TaxMode> loadPorts,AsyncCallback<Void> callback);
	
	//TransportMode
	void listTransportMode(AsyncCallback<List<TransportMode>> callback);
	void saveTransportMode(TransportMode transportMode,AsyncCallback<Long> callback);
	void deleteTransportMode(List<Long> ids,AsyncCallback<Void> callback);
	void updateTransportMode(List<TransportMode> loadPorts,AsyncCallback<Void> callback);
	
	//Port
	void getPort(String code,AsyncCallback<Port> callback);
	void listPort(AsyncCallback<List<Port>> callback);
	void savePort(Port port,AsyncCallback<Long> callback);
	void deletePort(List<Long> ids,AsyncCallback<Void> callback);
	void updatePort(List<Port> loadPorts,AsyncCallback<Void> callback);
	
	//District
	void getDistrict(String code,AsyncCallback<District> callback);
	void listDistrict(AsyncCallback<List<District>> callback);
	void saveDistrict(District district,AsyncCallback<Long> callback);
	void deleteDistrict(List<Long> ids,AsyncCallback<Void> callback);
	void updateDistrict(List<District> loadPorts,AsyncCallback<Void> callback);
	
	//PayWay
	void getPayWay(String code,AsyncCallback<PayWay> callback);
	void listPayWay(AsyncCallback<List<PayWay>> callback);
	void savePayWay(PayWay payWay,AsyncCallback<Long> callback);
	void deletePayWay(List<Long> ids,AsyncCallback<Void> callback);
	void updatePayWay(List<PayWay> loadPorts,AsyncCallback<Void> callback);
	
	//Useage
	void listUseage(AsyncCallback<List<Useage>> callback);
	void saveUseage(Useage useage,AsyncCallback<Long> callback);
	void deleteUseage(List<Long> ids,AsyncCallback<Void> callback);
	void updateUseage(List<Useage> loadPorts,AsyncCallback<Void> callback);
	
	//Bracket
	void listBracket(AsyncCallback<List<Bracket>> callback);
	void saveBracket(Bracket bracket,AsyncCallback<Long> callback);
	void deleteBracket(List<Long> ids,AsyncCallback<Void> callback);
	void updateBracket(List<Bracket> loadPorts,AsyncCallback<Void> callback);
	
	//IronChest
	void listIronChest(AsyncCallback<List<IronChest>> callback);
	void saveIronChest(IronChest ironChest,AsyncCallback<Long> callback);
	void deleteIronChest(List<Long> ids,AsyncCallback<Void> callback);
	void updateIronChest(List<IronChest> loadPorts,AsyncCallback<Void> callback);
	
	//ContainerSize
	void listContainerSize(AsyncCallback<List<ContainerSize>> callback);
	void saveContainerSize(ContainerSize containerSize,AsyncCallback<Long> callback);
	void deleteContainerSize(List<Long> ids,AsyncCallback<Void> callback);
	void updateContainerSize(List<ContainerSize> loadPorts,AsyncCallback<Void> callback);
	
	//Truck
	void listTruck(AsyncCallback<List<Truck>> callback);
	void saveTruck(Truck truck,AsyncCallback<Long> callback);
	void deleteTruck(List<Long> ids,AsyncCallback<Void> callback);
	void updateTruck(List<Truck> loadPorts,AsyncCallback<Void> callback);
	
	//Attachment
	void listAttachment(AsyncCallback<List<Attachment>> callback);
	void saveAttachment(Attachment attachment,AsyncCallback<Long> callback);
	void deleteAttachment(List<Long> ids,AsyncCallback<Void> callback);
	void updateAttachment(List<Attachment> loadPorts,AsyncCallback<Void> callback);
	
	//DeclareProperty
	void listDeclareProperty(AsyncCallback<List<DeclareProperty>> callback);
	void saveDeclareProperty(DeclareProperty declareProperty,AsyncCallback<Long> callback);
	void deleteDeclareProperty(List<Long> ids,AsyncCallback<Void> callback);
	void updateDeclareProperty(List<DeclareProperty> loadPorts,AsyncCallback<Void> callback);
	
	
}
