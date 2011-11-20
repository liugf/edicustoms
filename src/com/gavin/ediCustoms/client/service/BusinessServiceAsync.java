package com.gavin.ediCustoms.client.service;

import java.util.Date;
import java.util.List;
import java.util.Map;


import com.gavin.ediCustoms.entity.edi.ForeignEnterprise;
import com.gavin.ediCustoms.entity.edi.Voucher;
import com.gavin.ediCustoms.entity.edi.contract.ContractConsume;
import com.gavin.ediCustoms.entity.edi.contract.ContractHead;
import com.gavin.ediCustoms.entity.edi.contract.ContractMaterial;
import com.gavin.ediCustoms.entity.edi.contract.ContractProduct;
import com.gavin.ediCustoms.entity.edi.core.Container;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationGood;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.core.DeliveryRecord;
import com.gavin.ediCustoms.entity.edi.core.PackingItem;
import com.gavin.ediCustoms.entity.edi.core.TotalBillHead;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BusinessServiceAsync {	
	//Voucher
	void listVoucher(Long enterpriseId,AsyncCallback<List<Voucher>> callback);
	void saveVoucher(Voucher voucher,AsyncCallback<Long> callback);
	void deleteVoucher(List<Long> ids,AsyncCallback<Void> callback);
	void updateVoucher(List<Voucher> loadPorts,AsyncCallback<Void> callback);
	
	//ContractHead
	void getContractHead(Long id,AsyncCallback<ContractHead> callback);
	void listContractHead(Long enterpriseId,AsyncCallback<List<ContractHead>> callback);
	void saveContractHead(ContractHead contractHead,AsyncCallback<Long> callback);
	void deleteContractHead(List<Long> ids,AsyncCallback<Void> callback);
	void updateContractHead(List<ContractHead> loadPorts,AsyncCallback<Void> callback);
	
	//ContractProduct
	void listContractProduct(Long contractHeadId,AsyncCallback<List<ContractProduct>> callback);
	void saveContractProduct(ContractProduct contractProduct,AsyncCallback<Long> callback);
	void deleteContractProduct(List<Long> ids,AsyncCallback<Void> callback);
	void updateContractProduct(List<ContractProduct> loadPorts,AsyncCallback<Void> callback);
	
	//ContractMaterial
	void listContractMaterial(Long contractHeadId,AsyncCallback<List<ContractMaterial>> callback);
	void saveContractMaterial(ContractMaterial contractMaterial,AsyncCallback<Long> callback);
	void deleteContractMaterial(List<Long> ids,AsyncCallback<Void> callback);
	void updateContractMaterial(List<ContractMaterial> loadPorts,AsyncCallback<Void> callback);
	
	//ContractConsume
	void listContractConsumeByProductId(Long contractProductId,AsyncCallback<List<ContractConsume>> callback);
	void listContractConsumeByMaterialId(Long contractMaterialId,AsyncCallback<List<ContractConsume>> callback);
	void saveContractConsume(ContractConsume contractConsume,AsyncCallback<Long> callback);
	void deleteContractConsume(List<Long> ids,AsyncCallback<Void> callback);
	void updateContractConsume(List<ContractConsume> loadPorts,AsyncCallback<Void> callback);
	
		
	//TotalBillHead
	void listTotalBillHead(Long enterpriseId,boolean isExport,AsyncCallback<List<TotalBillHead>> callback);
	void saveTotalBillHead(TotalBillHead totalBillHead,AsyncCallback<Long> callback);
	void deleteTotalBillHead(List<Long> ids,AsyncCallback<Void> callback);
	void updateTotalBillHead(List<TotalBillHead> loadPorts,AsyncCallback<Void> callback);
	
		
	//CustomsDeclarationHead
	void getCustomsDeclarationHead(Long id,AsyncCallback<CustomsDeclarationHead> callback);
	void listCustomsDeclarationHead(Long enterpriseId,boolean isExport,AsyncCallback<List<CustomsDeclarationHead>> callback);
	void listCustomsDeclarationHeadByConditions(Long enterpriseId,boolean isExport,String preEntryId, Date start, Date end, AsyncCallback<List<CustomsDeclarationHead>> callback);
	void saveCustomsDeclarationHead(CustomsDeclarationHead customsDeclarationHead,AsyncCallback<Long> callback);
	void deleteCustomsDeclarationHead(List<Long> ids,AsyncCallback<Void> callback);
	void updateCustomsDeclarationHead(List<CustomsDeclarationHead> loadPorts,AsyncCallback<Void> callback);
	
	//getPreEntryId
	void getPreEntryId(Long enterpriseId, Long loadPortId, boolean isExport,AsyncCallback<String> callback);
	void getBillNo(Long enterpriseId,String customsCode, boolean isExport,AsyncCallback<String> callback);
	
	//CustomsDeclarationGood
	void listCustomsDeclarationGood(Long totalBillHeadId,AsyncCallback<List<CustomsDeclarationGood>> callback);
	void saveCustomsDeclarationGood(CustomsDeclarationGood customsDeclarationGood,AsyncCallback<Long> callback);
	void deleteCustomsDeclarationGood(List<Long> ids,AsyncCallback<Void> callback);
	void updateCustomsDeclarationGood(List<CustomsDeclarationGood> loadPorts,AsyncCallback<Void> callback);
	
	//Container
	void listContainer(Long totalBillHeadId,AsyncCallback<List<Container>> callback);
	void saveContainer(Container container,AsyncCallback<Long> callback);
	void deleteContainer(List<Long> ids,AsyncCallback<Void> callback);
	void updateContainer(List<Container> loadPorts,AsyncCallback<Void> callback);
	
	//PackingItem
	void listPackingItem(Long customsDeclarationHeadId,AsyncCallback<List<PackingItem>> callback);
	void savePackingItem(PackingItem packingItem,AsyncCallback<Long> callback);
	void deletePackingItem(List<Long> ids,AsyncCallback<Void> callback);
	void updatePackingItem(List<PackingItem> loadPorts,AsyncCallback<Void> callback);
	
	void  listRelativeCustomsDeclarationHead(Long enterpriseId,AsyncCallback<List<CustomsDeclarationHead>> callback);
	
	void importCustomsDeclaration(Long enterpriseId,Long id,AsyncCallback<String> callback);

	
	/*//导入装箱单
	void importFromCustomsDeclaration(Long customsDeclarationHeadId ,AsyncCallback<String> callback);*/
	
	//发票
	void  getForeignEnterpriseByCustomsDeclaration(Long id,AsyncCallback<ForeignEnterprise> callback);
	
	//收货人
	void listDeliveryRecord(Long customsDeclarationHeadId,AsyncCallback<List<DeliveryRecord>> callback);
	void saveDeliveryRecords(Long customsDeclarationHeadId,List<DeliveryRecord> list,AsyncCallback<Void> callback);
	
	//报关申报
	void customsDeclare(Long id,AsyncCallback<String> callback); 
	
	//司机纸
	void getDriverPaper(String transportTool, AsyncCallback<Map<String, Object>> callback);
	
}
