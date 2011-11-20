package com.gavin.ediCustoms.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;



import com.gavin.ediCustoms.client.service.BusinessService;
import com.gavin.ediCustoms.entity.edi.ForeignEnterprise;
import com.gavin.ediCustoms.entity.edi.Permission;
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
import com.gavin.ediCustoms.server.Dispatcher.MessageDispatcher;
import com.gavin.ediCustoms.server.dao.ContainerDao;
import com.gavin.ediCustoms.server.dao.ContractConsumeDao;
import com.gavin.ediCustoms.server.dao.ContractHeadDao;
import com.gavin.ediCustoms.server.dao.ContractMaterialDao;
import com.gavin.ediCustoms.server.dao.ContractProductDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationGoodDao;
import com.gavin.ediCustoms.server.dao.CustomsDeclarationHeadDao;
import com.gavin.ediCustoms.server.dao.DeliveryRecordDao;
import com.gavin.ediCustoms.server.dao.ForeignEnterpriseDao;
import com.gavin.ediCustoms.server.dao.PackingItemDao;
import com.gavin.ediCustoms.server.dao.PermissionDao;
import com.gavin.ediCustoms.server.dao.TotalBillHeadDao;
import com.gavin.ediCustoms.server.dao.VoucherDao;
import com.gavin.ediCustoms.server.dao.dictionary.LoadPortDao;
import com.gavin.ediCustoms.server.utils.CodeGenerator;
import com.gavin.ediCustoms.server.utils.DriverPaperUtil;
import com.gavin.ediCustoms.server.utils.Importor;
import com.gavin.ediCustoms.shared.UserState;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class BusinessServiceImpl extends RemoteServiceServlet implements
		BusinessService {
	
	private WebApplicationContext ctx;
	private PermissionDao permissionDao;
	private VoucherDao voucherDao;
	private ContractHeadDao contractHeadDao;
	private ContractProductDao contractProductDao;
	private ContractMaterialDao contractMaterialDao;
	private ContractConsumeDao contractConsumeDao;
	private TotalBillHeadDao totalBillHeadDao;
	private CustomsDeclarationGoodDao customsDeclarationGoodDao;
	private CustomsDeclarationHeadDao customsDeclarationHeadDao;	
	private PackingItemDao packingItemDao;
	private ContainerDao containerDao;
	private ForeignEnterpriseDao foreignEnterpriseDao;
	private DeliveryRecordDao deliveryRecordDao;
	
	private CodeGenerator codeGenerator;
	private LoadPortDao loadPortDao;
	private Importor importor;	
	private MessageDispatcher messageDispatcher;
	
	private DriverPaperUtil driverPaperUtil;

	
	private UserState getUserState() {
		return (UserState)this.getThreadLocalRequest().getSession().getAttribute("userState");
		//return (UserState) request.getSession().getAttribute("userState");
	}
	
	private Permission getPermission(Long userId, Long enterpriseId) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("userId", userId);
		map.put("enterpriseId", enterpriseId);
		permissionDao.findAnd(map).get(0);
		List<Permission> list = permissionDao.findAnd(map);
		if (list.size() != 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void init() throws ServletException {
		super.init();
		ctx = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
		if (ctx == null) {
			throw new RuntimeException(
					"Check Your Web.Xml Setting, No Spring Context Configured");
		}
		permissionDao = (PermissionDao) ctx.getBean("permissionDao");
		voucherDao = (VoucherDao) ctx.getBean("voucherDao");
		contractHeadDao = (ContractHeadDao) ctx.getBean("contractHeadDao");
		contractProductDao=(ContractProductDao) ctx.getBean("contractProductDao");
		contractMaterialDao=(ContractMaterialDao) ctx.getBean("contractMaterialDao");
		contractConsumeDao=(ContractConsumeDao) ctx.getBean("contractConsumeDao");
		totalBillHeadDao=(TotalBillHeadDao)ctx.getBean("totalBillHeadDao");
		customsDeclarationHeadDao=(CustomsDeclarationHeadDao)ctx.getBean("customsDeclarationHeadDao");
		customsDeclarationGoodDao=(CustomsDeclarationGoodDao)ctx.getBean("customsDeclarationGoodDao");
		packingItemDao=(PackingItemDao)ctx.getBean("packingItemDao");
		containerDao=(ContainerDao)ctx.getBean("containerDao");
		foreignEnterpriseDao=(ForeignEnterpriseDao)ctx.getBean("foreignEnterpriseDao");
		deliveryRecordDao=(DeliveryRecordDao)ctx.getBean("deliveryRecordDao");
		
		codeGenerator=(CodeGenerator)ctx.getBean("codeGenerator");
		loadPortDao=(LoadPortDao)ctx.getBean("loadPortDao");
		importor=(Importor)ctx.getBean("importor");
		messageDispatcher=(MessageDispatcher)ctx.getBean("messageDispatcher");
		driverPaperUtil=(DriverPaperUtil)ctx.getBean("driverPaperUtil");
	}

	@Override
	public List<Voucher> listVoucher(Long enterpriseId) {
		if (getUserState() == null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), enterpriseId);
		if (permission==null) {
			return null;
		}		
		return voucherDao.find("ownerId", enterpriseId);
	}

	@Override
	public Long saveVoucher(Voucher voucher) {
		if (getUserState() == null) {
			return null;
		}
		if (voucher==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), voucher.getOwnerId());
		if (permission==null) {
			return null;
		}
		if (!permission.getCanAdd()) {
			return null;
		}
		return (Long) voucherDao.save(voucher);
	}

	@Override
	public void deleteVoucher(List<Long> ids) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			Voucher voucher=voucherDao.get(id);
			if (voucher==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), voucher.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanDelete()) {
				return ;
			}
			voucherDao.delete(id);
		}
	}

	@Override
	public void updateVoucher(List<Voucher> vouchers) {
		if (getUserState() == null) {
			return ;
		}		
		for (Iterator<Voucher> iterator = vouchers.iterator(); iterator
				.hasNext();) {
			Voucher voucher = (Voucher) iterator.next();
			if (voucher==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), voucher.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanUpdate()) {
				return ;
			}
			voucherDao.update(voucher);
		}
	}
	
	@Override
	public ContractHead getContractHead(
			Long id) {
		if (getUserState() == null) {
			return null;
		}
		ContractHead contractHead=contractHeadDao.get(id);
		Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
		if (permission==null) {
			return null;
		}		
		return contractHead;
	}
	
	@Override
	public List<ContractHead> listContractHead(Long enterpriseId) {
		if (getUserState() == null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), enterpriseId);
		if (permission==null) {
			return null;
		}		
		return contractHeadDao.find("ownerId", enterpriseId);
	}

	@Override
	public Long saveContractHead(ContractHead contractHead) {
		if (getUserState() == null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		if (!permission.getCanAdd()) {
			return null;
		}
		return (Long) contractHeadDao.save(contractHead);
	}

	@Override
	public void deleteContractHead(List<Long> ids) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			ContractHead contractHead=contractHeadDao.get(id);
			if (contractHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanDelete()) {
				return ;
			}
			contractHeadDao.delete(id);
		}
	}

	@Override
	public void updateContractHead(List<ContractHead> contractHeads) {
		if (getUserState() == null) {
			return ;
		}	
		for (Iterator<ContractHead> iterator = contractHeads.iterator(); iterator
				.hasNext();) {
			ContractHead contractHead = (ContractHead) iterator.next();
			if (contractHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanUpdate()) {
				return ;
			}
			contractHeadDao.update(contractHead);
		}
	}
	
	@Override
	public List<ContractProduct> listContractProduct(Long contractHeadId) {
		if (getUserState() == null) {
			return null;
		}
		ContractHead contractHead=contractHeadDao.get(contractHeadId);
		if (contractHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		return contractProductDao.list(contractHeadId);
	}

	@Override
	public Long saveContractProduct(ContractProduct contractProduct) {
		if (getUserState() == null) {
			return null;
		}
		ContractHead contractHead=contractHeadDao.get(contractProduct.getContractHeadId());
		if (contractHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		if (!permission.getCanAdd()) {
			return null;
		}
		return (Long) contractProductDao.save(contractProduct);
	}

	@Override
	public void deleteContractProduct(List<Long> ids) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			ContractProduct contractProduct=contractProductDao.get(id);		
			if (contractProduct==null) {
				return;
			}
			ContractHead contractHead=contractHeadDao.get(contractProduct.getContractHeadId());
			if (contractHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanDelete()) {
				return ;
			}
			contractProductDao.delete(id);
		}
	}

	@Override
	public void updateContractProduct(List<ContractProduct> contractProducts) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<ContractProduct> iterator = contractProducts.iterator(); iterator
				.hasNext();) {
			ContractProduct contractProduct = (ContractProduct) iterator.next();
			if (contractProduct==null) {
				return;
			}
			ContractHead contractHead=contractHeadDao.get(contractProduct.getContractHeadId());
			if (contractHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanUpdate()) {
				return ;
			}
			contractProductDao.update(contractProduct);
		}
	}
	
	@Override
	public List<ContractMaterial> listContractMaterial(Long contractHeadId) {
		if (getUserState() == null) {
			return null;
		}
		ContractHead contractHead=contractHeadDao.get(contractHeadId);
		if (contractHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		return contractMaterialDao.find("contractHeadId", contractHeadId);
	}

	@Override
	public Long saveContractMaterial(ContractMaterial contractMaterial) {
		if (getUserState() == null) {
			return null;
		}
		ContractHead contractHead=contractHeadDao.get(contractMaterial.getContractHeadId());
		if (contractHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		if (!permission.getCanAdd()) {
			return null;
		}
		return (Long) contractMaterialDao.save(contractMaterial);
	}

	@Override
	public void deleteContractMaterial(List<Long> ids) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			ContractMaterial contractMaterial=contractMaterialDao.get(id);	
			if (contractMaterial==null) {
				return;
			}
			ContractHead contractHead=contractHeadDao.get(contractMaterial.getContractHeadId());
			if (contractHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanDelete()) {
				return ;
			}
			contractMaterialDao.delete(id);
		}
	}

	@Override
	public void updateContractMaterial(List<ContractMaterial> contractMaterials) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<ContractMaterial> iterator = contractMaterials.iterator(); iterator
				.hasNext();) {
			ContractMaterial contractMaterial = (ContractMaterial) iterator.next();
			if (contractMaterial==null) {
				return;
			}
			ContractHead contractHead=contractHeadDao.get(contractMaterial.getContractHeadId());
			if (contractHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanUpdate()) {
				return ;
			}
			contractMaterialDao.update(contractMaterial);
		}
	}
	
	@Override
	public List<ContractConsume> listContractConsumeByProductId(Long contractProductId) {
		if (getUserState() == null) {
			return null;
		}
		ContractProduct contractProduct=contractProductDao.get(contractProductId);		
		if (contractProduct==null) {
			return null;
		}
		ContractHead contractHead=contractHeadDao.get(contractProduct.getContractHeadId());
		if (contractHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
		if (permission==null) {
			return null ;
		}
		return contractConsumeDao.find("contractProductId", contractProductId);
	}
	
	@Override
	public List<ContractConsume> listContractConsumeByMaterialId(Long contractMaterialId) {
		if (getUserState() == null) {
			return null;
		}
		ContractMaterial contractMaterial=contractMaterialDao.get(contractMaterialId);	
		if (contractMaterial==null) {
			return null;
		}
		ContractHead contractHead=contractHeadDao.get(contractMaterial.getContractHeadId());
		if (contractHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
		if (permission==null) {
			return null ;
		}
		return contractConsumeDao.find("contractMaterialId", contractMaterialId);
	}

	@Override
	public Long saveContractConsume(ContractConsume contractConsume) {
		if (getUserState() == null) {
			return null;
		}
		if (contractConsume==null) {
			return null;
		}
		ContractMaterial contractMaterial=contractMaterialDao.get(contractConsume.getContractMaterialId());	
		if (contractMaterial==null) {
			return null;
		}
		ContractHead contractHead=contractHeadDao.get(contractMaterial.getContractHeadId());
		if (contractHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
		if (permission==null) {
			return null ;
		}
		if (!permission.getCanAdd()) {
			return null ;
		}
		return (Long) contractConsumeDao.save(contractConsume);
	}

	@Override
	public void deleteContractConsume(List<Long> ids) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			ContractConsume contractConsume=contractConsumeDao.get(id);
			if (contractConsume==null) {
				return ;
			}
			ContractMaterial contractMaterial=contractMaterialDao.get(contractConsume.getContractMaterialId());	
			if (contractMaterial==null) {
				return ;
			}
			ContractHead contractHead=contractHeadDao.get(contractMaterial.getContractHeadId());
			if (contractHead==null) {
				return ;
			}
			Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
			if (permission==null) {
				return  ;
			}
			if (!permission.getCanDelete()) {
				return  ;
			}
			contractConsumeDao.delete(id);
		}
	}

	@Override
	public void updateContractConsume(List<ContractConsume> contractConsumes) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<ContractConsume> iterator = contractConsumes.iterator(); iterator
				.hasNext();) {
			ContractConsume contractConsume = (ContractConsume) iterator.next();
			if (contractConsume==null) {
				return ;
			}
			ContractMaterial contractMaterial=contractMaterialDao.get(contractConsume.getContractMaterialId());	
			if (contractMaterial==null) {
				return ;
			}
			ContractHead contractHead=contractHeadDao.get(contractMaterial.getContractHeadId());
			if (contractHead==null) {
				return ;
			}
			Permission permission=getPermission(getUserState().getId(), contractHead.getOwnerId());
			if (permission==null) {
				return  ;
			}
			if (!permission.getCanUpdate()) {
				return  ;
			}
			contractConsumeDao.update(contractConsume);
		}
	}	
	
	
	@Override
	public List<TotalBillHead> listTotalBillHead(Long enterpriseId,boolean isExport) {
		if (getUserState() == null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), enterpriseId);
		if (permission==null) {
			return null;
		}
		Map<String, Object> map=new LinkedHashMap<String, Object>();
		map.put("ownerId", enterpriseId);
		map.put("isExport", isExport);
		return totalBillHeadDao.findAnd(map);
	}

	@Override
	public Long saveTotalBillHead(TotalBillHead totalBillHead) {
		if (getUserState() == null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), totalBillHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		if (!permission.getCanAdd()) {
			return null;
		}
		return (Long) totalBillHeadDao.save(totalBillHead);
	}

	@Override
	public void deleteTotalBillHead(List<Long> ids) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			TotalBillHead totalBillHead=totalBillHeadDao.get(id);
			if (totalBillHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), totalBillHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanDelete()) {
				return ;
			}
			totalBillHeadDao.delete(id);
		}
	}

	@Override
	public void updateTotalBillHead(List<TotalBillHead> totalBillHeads) {
		if (getUserState() == null) {
			return ;
		}	
		for (Iterator<TotalBillHead> iterator = totalBillHeads.iterator(); iterator
				.hasNext();) {
			TotalBillHead totalBillHead = (TotalBillHead) iterator.next();
			if (totalBillHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), totalBillHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanUpdate()) {
				return ;
			}
			totalBillHeadDao.update(totalBillHead);
		}
	}

	
	@Override
	public CustomsDeclarationHead getCustomsDeclarationHead(
			Long id) {
		if (getUserState() == null) {
			return null;
		}
		CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(id);
		Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
		if (permission==null) {
			return null;
		}		
		return customsDeclarationHead;
	}
	
	@Override
	public List<CustomsDeclarationHead> listCustomsDeclarationHead(Long enterpriseId,boolean isExport) {
		if (getUserState() == null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), enterpriseId);
		if (permission==null) {
			return null;
		}		
		Map<String, Object> map=new LinkedHashMap<String, Object>();
		map.put("ownerId", enterpriseId);
		map.put("isExport", isExport);
		
		return customsDeclarationHeadDao.find(map,true,"declareTime desc",20);
	}
	
	@SuppressWarnings("deprecation")
	public List<CustomsDeclarationHead> listCustomsDeclarationHeadByConditions(Long enterpriseId,boolean isExport,String preEntryId, Date start, Date end){
		if (getUserState() == null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), enterpriseId);
		if (permission==null) {
			return null;
		}		
		Map<String, Object> map=new LinkedHashMap<String, Object>();
		map.put("ownerId", enterpriseId);
		map.put("isExport", isExport);
		if (preEntryId!=null && !preEntryId.trim().equals("")) {
			map.put("preEntryId like", "%"+preEntryId+"%");
		}
		if (start!=null) {
			map.put("declareTime>=", start);
		}
		if (end!=null) {
			end.setHours(23);
			end.setMinutes(59);
			end.setSeconds(59);
			map.put("declareTime<=", end);
		}
		return customsDeclarationHeadDao.findAnd(map);
		
	}

	@Override
	public Long saveCustomsDeclarationHead(CustomsDeclarationHead customsDeclarationHead) {
		if (getUserState() == null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		if (!permission.getCanAdd()) {
			return null;
		}
		return (Long) customsDeclarationHeadDao.save(customsDeclarationHead);
	}

	@Override
	public void deleteCustomsDeclarationHead(List<Long> ids) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(id);
			if (customsDeclarationHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanDelete()) {
				return ;
			}
			customsDeclarationHeadDao.delete(id);
		}
	}

	@Override
	public void updateCustomsDeclarationHead(List<CustomsDeclarationHead> customsDeclarationHeads) {
		if (getUserState() == null) {
			return ;
		}	
		for (Iterator<CustomsDeclarationHead> iterator = customsDeclarationHeads.iterator(); iterator
				.hasNext();) {
			CustomsDeclarationHead customsDeclarationHead = (CustomsDeclarationHead) iterator.next();
			if (customsDeclarationHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanUpdate()) {
				return ;
			}
			customsDeclarationHeadDao.update(customsDeclarationHead);
		}
	}
	
	
	@Override
	public List<CustomsDeclarationGood> listCustomsDeclarationGood(Long customsDeclarationHeadId) {
		if (getUserState() == null) {
			return null;
		}
		CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(customsDeclarationHeadId);
		if (customsDeclarationHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		return customsDeclarationGoodDao.find("customsDeclarationHeadId", customsDeclarationHeadId);
	}

	@Override
	public Long saveCustomsDeclarationGood(CustomsDeclarationGood customsDeclarationGood) {
		if (getUserState() == null) {
			return null;
		}
		CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(customsDeclarationGood.getCustomsDeclarationHeadId());
		if (customsDeclarationHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		if (!permission.getCanAdd()) {
			return null;
		}
		return (Long) customsDeclarationGoodDao.save(customsDeclarationGood);
	}

	@Override
	public void deleteCustomsDeclarationGood(List<Long> ids) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			CustomsDeclarationGood customsDeclarationGood=customsDeclarationGoodDao.get(id);		
			if (customsDeclarationGood==null) {
				return;
			}
			CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(customsDeclarationGood.getCustomsDeclarationHeadId());
			if (customsDeclarationHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanDelete()) {
				return ;
			}
			customsDeclarationGoodDao.delete(id);
		}
	}

	@Override
	public void updateCustomsDeclarationGood(List<CustomsDeclarationGood> customsDeclarationGoods) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<CustomsDeclarationGood> iterator = customsDeclarationGoods.iterator(); iterator
				.hasNext();) {
			CustomsDeclarationGood customsDeclarationGood = (CustomsDeclarationGood) iterator.next();
			if (customsDeclarationGood==null) {
				return;
			}
			CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(customsDeclarationGood.getCustomsDeclarationHeadId());
			if (customsDeclarationHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanUpdate()) {
				return ;
			}
			customsDeclarationGoodDao.update(customsDeclarationGood);
		}
	}
	
	@Override
	public List<Container> listContainer(Long customsDeclarationHeadId) {
		if (getUserState() == null) {
			return null;
		}
		CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(customsDeclarationHeadId);
		if (customsDeclarationHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		return containerDao.find("customsDeclarationHeadId", customsDeclarationHeadId);
	}

	@Override
	public Long saveContainer(Container container) {
		if (getUserState() == null) {
			return null;
		}
		CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(container.getCustomsDeclarationHeadId());
		if (customsDeclarationHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		if (!permission.getCanAdd()) {
			return null;
		}
		return (Long) containerDao.save(container);
	}

	@Override
	public void deleteContainer(List<Long> ids) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			Container container=containerDao.get(id);		
			if (container==null) {
				return;
			}
			CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(container.getCustomsDeclarationHeadId());
			if (customsDeclarationHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanDelete()) {
				return ;
			}
			containerDao.delete(id);
		}
	}

	@Override
	public void updateContainer(List<Container> containers) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<Container> iterator = containers.iterator(); iterator
				.hasNext();) {
			Container container = (Container) iterator.next();
			if (container==null) {
				return;
			}
			CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(container.getCustomsDeclarationHeadId());
			if (customsDeclarationHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanUpdate()) {
				return ;
			}
			containerDao.update(container);
		}
	}

	@Override
	public List<PackingItem> listPackingItem(Long customsDeclarationHeadId) {
		if (getUserState() == null) {
			return null;
		}
		CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(customsDeclarationHeadId);
		if (customsDeclarationHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		return packingItemDao.find("customsDeclarationHeadId", customsDeclarationHeadId);
	}

	@Override
	public Long savePackingItem(PackingItem packingItem) {
		if (getUserState() == null) {
			return null;
		}
		CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(packingItem.getCustomsDeclarationHeadId());
		if (customsDeclarationHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		if (!permission.getCanAdd()) {
			return null;
		}
		return (Long) packingItemDao.save(packingItem);
	}

	@Override
	public void deletePackingItem(List<Long> ids) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			PackingItem packingItem=packingItemDao.get(id);		
			if (packingItem==null) {
				return;
			}
			CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(packingItem.getCustomsDeclarationHeadId());
			if (customsDeclarationHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanDelete()) {
				return ;
			}
			packingItemDao.delete(id);
		}
	}

	@Override
	public void updatePackingItem(List<PackingItem> packingItems) {
		if (getUserState() == null) {
			return ;
		}
		for (Iterator<PackingItem> iterator = packingItems.iterator(); iterator
				.hasNext();) {
			PackingItem packingItem = (PackingItem) iterator.next();
			if (packingItem==null) {
				return;
			}
			CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(packingItem.getCustomsDeclarationHeadId());
			if (customsDeclarationHead==null) {
				return;
			}
			Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
			if (permission==null) {
				return ;
			}
			if (!permission.getCanUpdate()) {
				return ;
			}
			packingItemDao.update(packingItem);
		}
	}
	
	@Override
	public String getPreEntryId(Long enterpriseId, Long loadPortId,
			boolean isExport) {		
		if (getUserState() == null) {
			return null;
		}
		String customsCode=loadPortDao.get(loadPortId).getCustomsCode();
		return codeGenerator.getPreEntryId(enterpriseId, customsCode, isExport);
	}
	
	@Override
	public String getBillNo(Long enterpriseId, String customsCode,
			boolean isExport) {		
		if (getUserState() == null) {
			return null;
		}
		return codeGenerator.getPreEntryId(enterpriseId, customsCode, isExport);
	}

	@Override
	public String importCustomsDeclaration(Long enterpriseId, Long id) {
		if (getUserState() == null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), enterpriseId);
		if (permission==null) {
			return null;
		}
		if (!permission.getCanAdd()) {
			return null;
		}
		if (id==null) {
			return "所选为空";
		}
		return importor.importCustomsDeclaration(enterpriseId, id);
	}

	@Override
	public ForeignEnterprise getForeignEnterpriseByCustomsDeclaration(Long id) {
		if (getUserState() == null) {
			return null;
		}
		CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(id);
		if (customsDeclarationHead==null) {
			return null;
		}
		ContractHead contractHead=contractHeadDao.get(customsDeclarationHead.getContractHeadId());
		
		if (contractHead!=null && contractHead.getForeignEnterpriseId()!=null) {
			return foreignEnterpriseDao.get(contractHead.getForeignEnterpriseId());
		}else{
			return null;
		}
	}

	/*@Override
	public String importFromCustomsDeclaration(Long customsDeclarationHeadId) {		
		if (getUserState() == null) {
			return null;
		}
		CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(customsDeclarationHeadId);
		if (customsDeclarationHead==null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
		if (permission==null) {
			return null;
		}
		return importor.importPackingItem(customsDeclarationHeadId);
	}*/

	@Override
	public List<DeliveryRecord> listDeliveryRecord(Long customsDeclarationHeadId) {
		if (getUserState() == null) {
			return null;
		}
		// TODO 权限检查
		return deliveryRecordDao.find("customsDeclarationHeadId", customsDeclarationHeadId);
	}

	@Override
	public void saveDeliveryRecords(Long customsDeclarationHeadId,List<DeliveryRecord> list) {
		if (getUserState() == null) {
			return ;
		}
		// TODO 权限检查
		deliveryRecordDao.delete("customsDeclarationHeadId", customsDeclarationHeadId);
		boolean[] used = new boolean[list.size()];
		List<DeliveryRecord> result = new ArrayList<DeliveryRecord>();
		for (int i = 0; i < list.size(); i++) {
			if (used[i])
				continue;
			DeliveryRecord deliveryRecord=list.get(i);
			used[i] = true;
			for (int j = i + 1; j < list.size(); j++) {
				if (used[j])
					continue;
				DeliveryRecord dr = list.get(j);
				if (deliveryRecord.getDestinationEnterpriseId().equals(dr.getDestinationEnterpriseId())&&
						deliveryRecord.getCustomsDeclarationGoodId().equals(dr.getCustomsDeclarationGoodId())) {
					deliveryRecord.setQuantity(deliveryRecord.getQuantity()+dr.getQuantity());
					used[j] = true;
				}
			}
			result.add(deliveryRecord);
		}
		
		for(DeliveryRecord deliveryRecord:result){
			deliveryRecordDao.save(deliveryRecord);
		}
	}

	@Override
	public String customsDeclare(Long customsDeclarationHeadId) {
		if (getUserState() == null) {
			return "错误";
		}
		CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(customsDeclarationHeadId);
		if (customsDeclarationHead==null) {
			return "错误";
		}
		Permission permission=getPermission(getUserState().getId(), customsDeclarationHead.getOwnerId());
		if (permission==null) {
			return "错误";
		}
		return messageDispatcher.sendMessage(customsDeclarationHeadId,getUserState().getId());
	}

	@Override
	public List<CustomsDeclarationHead> listRelativeCustomsDeclarationHead(
			Long enterpriseId) {
		if (getUserState() == null) {
			return null;
		}
		Permission permission=getPermission(getUserState().getId(), enterpriseId);
		if (permission==null) {
			return null;
		}
		List<DeliveryRecord> deliveryRecords=deliveryRecordDao.find("destinationEnterpriseId", enterpriseId);
		Map<Long, Boolean> map=new HashMap<Long, Boolean>();
		List<CustomsDeclarationHead> result=new ArrayList<CustomsDeclarationHead>();
		for(DeliveryRecord deliveryRecord:deliveryRecords){
			if (!map.containsKey(deliveryRecord.getCustomsDeclarationHeadId())) {
				CustomsDeclarationHead customsDeclarationHead=customsDeclarationHeadDao.get(deliveryRecord.getCustomsDeclarationHeadId());
				if (customsDeclarationHead!=null) {
					result.add(customsDeclarationHead);
					map.put(deliveryRecord.getCustomsDeclarationHeadId(), true);
				}
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Object> getDriverPaper(String transportTool) {
		return driverPaperUtil.getContent(transportTool);
	}

		
	
}
