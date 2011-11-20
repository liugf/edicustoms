package com.gavin.ediCustoms.client.admin.dictionary;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.gavin.ediCustoms.entity.edi.dictionary.Truck;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TruckPanel extends CommonDictionaryPanel<Truck> {
	
	public TruckPanel(String heading){
		this.heading=heading;
		beanModelFactory=BeanModelLookup.get().getFactory(Truck.class);
	}	

	@Override
	protected List<ColumnConfig> getColumnConfigs() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		TextField<String> tf = new TextField<String>();
		CellEditor tce = new CellEditor(tf);
		ColumnConfig column = new ColumnConfig();

		configs.add(selectionMode.getColumn());

		column.setId("code");
		column.setHeader("汽车编码");
		column.setWidth(150);
		column.setRowHeader(true);
		column.setEditor(tce);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("icCard");
		column.setHeader("IC卡号");
		column.setWidth(150);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("truckNo");
		column.setHeader("大陆车牌");
		column.setWidth(150);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("hkTruckNo");
		column.setHeader("香港车牌");
		column.setWidth(150);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("corporationName");
		column.setHeader("承运单位名称");
		column.setWidth(150);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("organizationCode");
		column.setHeader("承运单位组织机构代码");
		column.setWidth(150);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("organizationAddress");
		column.setHeader("承运单位地址");
		column.setWidth(150);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("organizationTelephone");
		column.setHeader("承运单位电话");
		column.setWidth(150);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("driverName");
		column.setHeader("司机姓名");
		column.setWidth(150);
		column.setEditor(tce);
		configs.add(column);
		
		return configs;
	}

	@Override
	protected void list(AsyncCallback<List<Truck>> callback) {
		getDictionaryService().listTruck(callback);		
	}

	@Override
	protected void save(Truck t, AsyncCallback<Long> callback) {
		getDictionaryService().saveTruck(t, callback);
	}

	@Override
	protected void delete(List<Long> ids, AsyncCallback<Void> callback) {
		getDictionaryService().deleteTruck(ids, callback);
	}

	@Override
	protected void update(List<Truck> dictionarys,
			AsyncCallback<Void> callback) {
		getDictionaryService().updateTruck(dictionarys, callback);
	}

	@Override
	protected FormCallback<Truck> initNewItemFormPanel(
			FormPanel newItemFormPanel) {
		FormData formData = new FormData("-20");		
		TextField<String> code = new TextField<String>();
		code.setName("code");
		code.setFieldLabel("汽车编码");
		code.setAllowBlank(false);
		newItemFormPanel.add(code, formData);
		TextField<String> icCard = new TextField<String>();
		icCard.setName("icCard");
		icCard.setFieldLabel("IC卡号");
		newItemFormPanel.add(icCard, formData);
		TextField<String> truckNo = new TextField<String>();
		truckNo.setName("truckNo");
		truckNo.setFieldLabel("大陆车牌");
		newItemFormPanel.add(truckNo, formData);
		TextField<String> hkTruckNo = new TextField<String>();
		hkTruckNo.setName("hkTruckNo");
		hkTruckNo.setFieldLabel("香港车牌");
		newItemFormPanel.add(hkTruckNo, formData);
		TextField<String> corporationName = new TextField<String>();
		corporationName.setName("corporationName");
		corporationName.setFieldLabel("承运单位名称");
		newItemFormPanel.add(corporationName, formData);
		TextField<String> organizationCode = new TextField<String>();
		organizationCode.setName("organizationCode");
		organizationCode.setFieldLabel("承运单位组织机构代码");
		newItemFormPanel.add(organizationCode, formData);
		TextField<String> organizationAddress = new TextField<String>();
		organizationAddress.setName("organizationAddress");
		organizationAddress.setFieldLabel("承运单位地址");
		newItemFormPanel.add(organizationAddress, formData);
		TextField<String> organizationTelephone = new TextField<String>();
		organizationTelephone.setName("organizationTelephone");
		organizationTelephone.setFieldLabel("承运单位电话");
		newItemFormPanel.add(organizationTelephone, formData);
		TextField<String> driverName = new TextField<String>();
		driverName.setName("driverName");
		driverName.setFieldLabel("司机姓名");
		newItemFormPanel.add(driverName, formData);
		
		FormBinding binding = new FormBinding(newItemFormPanel,true);  
		BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(Truck.class);
		final BeanModel beanModel = beanModelFactory.createModel(new Truck());
		binding.bind(beanModel);		
		
		FormCallback<Truck> formCallback=new FormCallback<Truck>() {	
			@Override
			public Truck getBean() {
				return beanModel.getBean();
			}
		};
		return formCallback;
	}	

	
}
