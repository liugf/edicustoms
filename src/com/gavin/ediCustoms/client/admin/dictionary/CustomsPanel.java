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
import com.gavin.ediCustoms.entity.edi.dictionary.Customs;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CustomsPanel extends CommonDictionaryPanel<Customs> {
	
	public CustomsPanel(String heading){
		this.heading=heading;
		beanModelFactory=BeanModelLookup.get().getFactory(Customs.class);
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
		
		return configs;
	}

	@Override
	protected void list(AsyncCallback<List<Customs>> callback) {
		getDictionaryService().listCustoms(callback);		
	}

	@Override
	protected void save(Customs t, AsyncCallback<Long> callback) {
		getDictionaryService().saveCustoms(t, callback);
	}

	@Override
	protected void delete(List<Long> ids, AsyncCallback<Void> callback) {
		getDictionaryService().deleteCustoms(ids, callback);
	}

	@Override
	protected void update(List<Customs> dictionarys,
			AsyncCallback<Void> callback) {
		getDictionaryService().updateCustoms(dictionarys, callback);
	}

	@Override
	protected FormCallback<Customs> initNewItemFormPanel(
			FormPanel newItemFormPanel) {
		FormData formData = new FormData("-20");		
		TextField<String> code = new TextField<String>();
		code.setName("code");
		code.setFieldLabel("编号");
		code.setAllowBlank(false);
		newItemFormPanel.add(code, formData);
		TextField<String> name= new TextField<String>();
		name.setName("name");
		name.setFieldLabel("名称");
		name.setAllowBlank(false);
		newItemFormPanel.add(name, formData);
		
		FormBinding binding = new FormBinding(newItemFormPanel,true);  
		BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(Customs.class);
		final BeanModel beanModel = beanModelFactory.createModel(new Customs());
		binding.bind(beanModel);
		
		FormCallback<Customs> formCallback=new FormCallback<Customs>() {	
			@Override
			public Customs getBean() {
				return beanModel.getBean();
			}
		};
		return formCallback;
	}	

	
}
