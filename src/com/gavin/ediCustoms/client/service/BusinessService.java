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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("business")
public interface BusinessService extends RemoteService {	
	//Voucher
	List<Voucher> listVoucher(Long voucherId);
	Long saveVoucher(Voucher voucher);
	void deleteVoucher(List<Long> ids);
	void updateVoucher(List<Voucher> vouchers);
	
	//ContractHead
	ContractHead getContractHead(Long id);
	List<ContractHead> listContractHead(Long enterpriseId);
	Long saveContractHead(ContractHead contractHead);
	void deleteContractHead(List<Long> ids);
	void updateContractHead(List<ContractHead> contractHeads);
	
	//ContractProduct
	List<ContractProduct> listContractProduct(Long contractHeadId);
	Long saveContractProduct(ContractProduct contractProduct);
	void deleteContractProduct(List<Long> ids);
	void updateContractProduct(List<ContractProduct> contractProducts);
	
	//ContractMaterial
	List<ContractMaterial> listContractMaterial(Long contractHeadId);
	Long saveContractMaterial(ContractMaterial contractMaterial);
	void deleteContractMaterial(List<Long> ids);
	void updateContractMaterial(List<ContractMaterial> contractMaterials);
	
	//ContractConsume
	List<ContractConsume> listContractConsumeByProductId(Long contractProductId);
	List<ContractConsume> listContractConsumeByMaterialId(Long contractMaterialId);
	Long saveContractConsume(ContractConsume contractConsume);
	void deleteContractConsume(List<Long> ids);
	void updateContractConsume(List<ContractConsume> contractConsumes);	
	
	//TotalBillHead
	List<TotalBillHead> listTotalBillHead(Long enterpriseId,boolean isExport);
	Long saveTotalBillHead(TotalBillHead totalBillHead);
	void deleteTotalBillHead(List<Long> ids);
	void updateTotalBillHead(List<TotalBillHead> totalBillHeads);

	
	//CustomsDeclarationHead
	CustomsDeclarationHead getCustomsDeclarationHead(Long id);
	List<CustomsDeclarationHead> listCustomsDeclarationHead(Long enterpriseId,boolean isExport);
	List<CustomsDeclarationHead> listCustomsDeclarationHeadByConditions(Long enterpriseId,boolean isExport,String preEntryId, Date start, Date end);
	Long saveCustomsDeclarationHead(CustomsDeclarationHead customsDeclarationHead);
	void deleteCustomsDeclarationHead(List<Long> ids);
	void updateCustomsDeclarationHead(List<CustomsDeclarationHead> customsDeclarationHeads);	
	
	//codeGenerate
	String getPreEntryId(Long enterpriseId, Long loadPortId, boolean isExport);
	String getBillNo(Long enterpriseId, String customsCode,	boolean isExport) ;
	
	//CustomsDeclarationGood
	List<CustomsDeclarationGood> listCustomsDeclarationGood(Long totalBillHeadId);
	Long saveCustomsDeclarationGood(CustomsDeclarationGood customsDeclarationGood);
	void deleteCustomsDeclarationGood(List<Long> ids);
	void updateCustomsDeclarationGood(List<CustomsDeclarationGood> customsDeclarationGoods);
	
	//Container
	List<Container> listContainer(Long totalBillHeadId);
	Long saveContainer(Container container);
	void deleteContainer(List<Long> ids);
	void updateContainer(List<Container> containers);
	
	//PackingItem
	List<PackingItem> listPackingItem(Long customsDeclarationHeadId);
	Long savePackingItem(PackingItem packingItem);
	void deletePackingItem(List<Long> ids);
	void updatePackingItem(List<PackingItem> packingItems);
	
	
	//列出接收单位为本单位的出口报关单。
	List<CustomsDeclarationHead> listRelativeCustomsDeclarationHead(Long enterpriseId);
	
	//导入
	String importCustomsDeclaration(Long enterpriseId,Long id);	
	
	/*//导入装箱单
	String importFromCustomsDeclaration(Long customsDeclarationHeadId);*/
	
	//发票
	ForeignEnterprise getForeignEnterpriseByCustomsDeclaration(Long id);
	
	
	//收货人
	List<DeliveryRecord> listDeliveryRecord(Long customsDeclarationHeadId);
	void saveDeliveryRecords(Long customsDeclarationHeadId,List<DeliveryRecord> list);
	
	//报关申报
	String customsDeclare(Long id); 
	
	//司机纸
	Map<String, Object> getDriverPaper(String transportTool);

	
}
