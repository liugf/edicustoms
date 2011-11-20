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
import com.gavin.ediCustoms.entity.edi.dictionary.WrapType;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class WrapTypePanel extends CommonDictionaryPanel<WrapType> {
	
	public WrapTypePanel(String heading){
		this.heading=heading;
		beanModelFactory=BeanModelLookup.get().getFactory(WrapType.class);
	}	

	@Override
	protected List<ColumnConfig> getColumnConfigs() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		TextField<String> tf = new TextField<String>();
		CellEditor tce = new CellEditor(tf);
		ColumnConfig column = new ColumnConfig();

		configs.add(selectionMode.getColumn());

		column.setId("code");
		column.setHeader("编号");
		column.setWidth(100);
		column.setRowHeader(true);
		column.setEditor(tce);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("name");
		column.setHeader("名称");
		column.setWidth(200);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("unit");
		column.setHeader("单位");
		column.setWidth(200);
		column.setEditor(tce);
		configs.add(column);
		
		return configs;
	}

	@Override
	protected void list(AsyncCallback<List<WrapType>> callback) {
		getDictionaryService().listWrapType(callback);		
	}

	@Override
	protected void save(WrapType t, AsyncCallback<Long> callback) {
		getDictionaryService().saveWrapType(t, callback);
	}

	@Override
	protected void delete(List<Long> ids, AsyncCallback<Void> callback) {
		getDictionaryService().deleteWrapType(ids, callback);
	}

	@Override
	protected void update(List<WrapType> dictionarys,
			AsyncCallback<Void> callback) {
		getDictionaryService().updateWrapType(dictionarys, callback);
	}

	@Override
	protected FormCallback<WrapType> initNewItemFormPanel(
			FormPanel newItemFormPanel) {
		FormData formData = new FormData("-20");		
		TextField<String> code = new TextField<String>();	
		code.setName("code");
		code.setFieldLabel("编号");
		code.setAllowBlank(false);
		newItemFormPanel.add(code, formData);
		TextField<String> name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel("名称");
		name.setAllowBlank(false);
		newItemFormPanel.add(name, formData);
		TextField<String> unit = new TextField<String>();
		unit.setName("unit");
		unit.setFieldLabel("名称");
		unit.setAllowBlank(false);
		newItemFormPanel.add(unit, formData);
		
		FormBinding binding = new FormBinding(newItemFormPanel,true);  
		BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(WrapType.class);
		final BeanModel beanModel = beanModelFactory.createModel(new WrapType());
		binding.bind(beanModel);
		
		FormCallback<WrapType> formCallback=new FormCallback<WrapType>() {	
			@Override
			public WrapType getBean() {
				return beanModel.getBean();
			}
		};
		return formCallback;
	}	

	
}
