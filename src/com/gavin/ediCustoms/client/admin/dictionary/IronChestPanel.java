package com.gavin.ediCustoms.client.admin.dictionary;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.gavin.ediCustoms.entity.edi.dictionary.IronChest;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class IronChestPanel extends CommonDictionaryPanel<IronChest> {
	
	public IronChestPanel(String heading){
		this.heading=heading;
		beanModelFactory=BeanModelLookup.get().getFactory(IronChest.class);
	}	

	@Override
	protected List<ColumnConfig> getColumnConfigs() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		TextField<String> tf = new TextField<String>();
		CellEditor tce = new CellEditor(tf);
		NumberField dnf=new NumberField();
		dnf.setPropertyEditorType(Double.class);
		CellEditor dce = new CellEditor(dnf);
		ColumnConfig column = new ColumnConfig();

		configs.add(selectionMode.getColumn());

		column.setId("code");
		column.setHeader("类型");
		column.setWidth(100);
		column.setRowHeader(true);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("size");
		column.setHeader("尺寸");
		column.setWidth(200);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("description");
		column.setHeader("特征");
		column.setWidth(150);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("weight");
		column.setHeader("重量");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		return configs;
	}

	@Override
	protected void list(AsyncCallback<List<IronChest>> callback) {
		getDictionaryService().listIronChest(callback);		
	}

	@Override
	protected void save(IronChest t, AsyncCallback<Long> callback) {
		getDictionaryService().saveIronChest(t, callback);
	}

	@Override
	protected void delete(List<Long> ids, AsyncCallback<Void> callback) {
		getDictionaryService().deleteIronChest(ids, callback);
	}

	@Override
	protected void update(List<IronChest> dictionarys,
			AsyncCallback<Void> callback) {
		getDictionaryService().updateIronChest(dictionarys, callback);
	}

	@Override
	protected FormCallback<IronChest> initNewItemFormPanel(
			FormPanel newItemFormPanel) {
		FormData formData = new FormData("-20");		
		TextField<String> code = new TextField<String>();
		code.setName("code");
		code.setFieldLabel("类型");
		code.setAllowBlank(false);
		newItemFormPanel.add(code, formData);
		
		TextField<String> size = new TextField<String>();
		size.setName("size");
		size.setFieldLabel("尺寸");
		size.setAllowBlank(false);
		newItemFormPanel.add(size, formData);
		
		TextField<String> description = new TextField<String>();
		description.setName("description");
		description.setFieldLabel("特征");
		newItemFormPanel.add(description, formData);
		
		NumberField weight=new NumberField();
		weight.setPropertyEditorType(Double.class);
		weight.setName("weight");
		weight.setFieldLabel("重量");
		newItemFormPanel.add(weight, formData);
		
		FormBinding binding = new FormBinding(newItemFormPanel,true);  
		BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(IronChest.class);
		final BeanModel beanModel = beanModelFactory.createModel(new IronChest());
		binding.bind(beanModel);		
		
		FormCallback<IronChest> formCallback=new FormCallback<IronChest>() {	
			@Override
			public IronChest getBean() {
				return beanModel.getBean();
			}
		};
		return formCallback;
	}	

	
}
