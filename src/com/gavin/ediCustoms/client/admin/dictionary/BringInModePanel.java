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
import com.gavin.ediCustoms.entity.edi.dictionary.BringInMode;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BringInModePanel extends CommonDictionaryPanel<BringInMode> {
	
	public BringInModePanel(String heading){
		this.heading=heading;
		beanModelFactory=BeanModelLookup.get().getFactory(BringInMode.class);
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
		column.setHeader("引进方式");
		column.setWidth(200);
		column.setEditor(tce);
		configs.add(column);		
		
		return configs;
	}

	@Override
	protected void list(AsyncCallback<List<BringInMode>> callback) {
		getDictionaryService().listBringInMode(callback);		
	}

	@Override
	protected void save(BringInMode t, AsyncCallback<Long> callback) {
		getDictionaryService().saveBringInMode(t, callback);
	}

	@Override
	protected void delete(List<Long> ids, AsyncCallback<Void> callback) {
		getDictionaryService().deleteBringInMode(ids, callback);
	}

	@Override
	protected void update(List<BringInMode> dictionarys,
			AsyncCallback<Void> callback) {
		getDictionaryService().updateBringInMode(dictionarys, callback);
	}

	@Override
	protected FormCallback<BringInMode> initNewItemFormPanel(
			FormPanel newItemFormPanel) {
		FormData formData = new FormData("-20");		
		TextField<String> code = new TextField<String>();
		code.setName("code");
		code.setFieldLabel("编号");
		code.setAllowBlank(false);
		newItemFormPanel.add(code, formData);
		TextField<String> name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel("引进方式");
		name.setAllowBlank(false);
		newItemFormPanel.add(name, formData);		
		
		FormBinding binding = new FormBinding(newItemFormPanel,true);  
		BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(BringInMode.class);
		final BeanModel beanModel = beanModelFactory.createModel(new BringInMode());
		binding.bind(beanModel);		
		
		FormCallback<BringInMode> formCallback=new FormCallback<BringInMode>() {	
			@Override
			public BringInMode getBean() {
				return beanModel.getBean();
			}
		};
		return formCallback;
	}	

	
}
