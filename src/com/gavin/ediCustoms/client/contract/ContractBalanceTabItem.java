package com.gavin.ediCustoms.client.contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.i18n.client.NumberFormat;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.gavin.ediCustoms.client.myUI.MyTabItem;
import com.gavin.ediCustoms.entity.edi.contract.ContractConsume;
import com.gavin.ediCustoms.entity.edi.contract.ContractMaterial;
import com.gavin.ediCustoms.entity.edi.contract.ContractProduct;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ContractBalanceTabItem extends MyTabItem {
	private ListStore<BeanModel> store;
	private ListStore<BeanModel> contractMaterialStore;
	private ListStore<BeanModel> contractProductStore;
	private ListStore<BeanModel> contractConsumeStore;
	
	private boolean isContractProductStoreLoaded=false;
	private boolean isContractMaterialStoreLoaded=false;

	public ContractBalanceTabItem() {
		setText("平衡表");	
		store=new ListStore<BeanModel>();
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();
		
		column = new ColumnConfig();
		column.setId("no");
		column.setHeader("料件序号");
		column.setWidth(70);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("name");
		column.setHeader("料件名称");
		column.setWidth(250);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("quantity");
		column.setHeader("备案数量");
		column.setWidth(100);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setNumberFormat(NumberFormat.getFormat("0.0000"));
		column.setId("exportQuantity");
		column.setHeader("出口用量");
		column.setWidth(100);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setNumberFormat(NumberFormat.getFormat("0.0000"));
		column.setId("difference");
		column.setHeader("差额");
		column.setWidth(100);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setNumberFormat(NumberFormat.getFormat("0.0000"));
		column.setId("differentRate");
		column.setHeader("差额比");
		column.setWidth(100);
		configs.add(column);
		
		store.setMonitorChanges(true);
		Grid<BeanModel> grid = new Grid<BeanModel>(store, new ColumnModel(
				configs));
		add(grid);
		
	}

	public void refleshStores(Long contractHeadId){
		if (contractProductStore==null) {
			contractProductStore=new ListStore<BeanModel>();
		}
		if (contractMaterialStore==null) {
			contractMaterialStore=new ListStore<BeanModel>();
		}
		if (contractConsumeStore==null) {
			contractConsumeStore=new ListStore<BeanModel>();
		}
		
		isContractProductStoreLoaded=false;
		isContractMaterialStoreLoaded=false;
		
		AsyncCallback<List<ContractProduct>> contractProductCallback = new AsyncCallback<List<ContractProduct>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ContractProduct> result) {				
				contractProductStore.removeAll();
				contractProductStore.add(BeanModelLookup.get().getFactory(ContractProduct.class)
						.createModel(result));	
				isContractProductStoreLoaded=true;
				calculateStore();
			}

		};
		getBusinessService().listContractProduct(contractHeadId,contractProductCallback);	
		
		AsyncCallback<List<ContractMaterial>> contractMaterialCallback = new AsyncCallback<List<ContractMaterial>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ContractMaterial> result) {
				Collections.sort(result, new Comparator<ContractMaterial>() {
					public int compare(ContractMaterial arg0, ContractMaterial arg1) {
						return arg0.getNo()-arg1.getNo();
					}
				});
				contractMaterialStore.removeAll();
				contractMaterialStore.add(BeanModelLookup.get().getFactory(ContractMaterial.class)
						.createModel(result));	
				isContractMaterialStoreLoaded=true;
				calculateStore();
			}

		};
		getBusinessService().listContractMaterial(contractHeadId,contractMaterialCallback);	
		
	}
	
	private void calculateStore(){
		if (!(isContractProductStoreLoaded&&isContractMaterialStoreLoaded)) {
			return;
		}
		store.removeAll();
		for(BeanModel beanModel:contractMaterialStore.getModels()){			
			double exportQuantity=0;
			beanModel.set("exportQuantity", exportQuantity);
			updateBeanModel(beanModel);
			store.add(beanModel);			
		}	
	}
	
	private void updateBeanModel(final BeanModel beanModel){		
		AsyncCallback<List<ContractConsume>> contractConsumeCallback = new AsyncCallback<List<ContractConsume>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ContractConsume> result) {	
				for(ContractConsume contractConsume:result){
					BeanModel productBeanModel=contractProductStore.findModel("id", contractConsume.getContractProductId());
					double productQuantity=productBeanModel.get("quantity");	
					double used= contractConsume.getUsed();
					double wasted=contractConsume.getWasted();
					double exportQuantity=beanModel.get("exportQuantity");
					exportQuantity+=(productQuantity*used)/(1-wasted/100);
					beanModel.set("exportQuantity", exportQuantity);
					double materialQuantity=beanModel.get("quantity");
					double difference=materialQuantity-exportQuantity;
					beanModel.set("difference", difference);
					beanModel.set("differentRate", difference/materialQuantity);
					
				}
			}
		};		
		getBusinessService().listContractConsumeByMaterialId((Long)beanModel.get("id"),contractConsumeCallback);
	}
	
	

}
