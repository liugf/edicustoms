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
import com.gavin.ediCustoms.entity.edi.dictionary.Port;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PortPanel extends CommonDictionaryPanel<Port> {
	
	public PortPanel(String heading){
		this.heading=heading;
		beanModelFactory=BeanModelLookup.get().getFactory(Port.class);
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
		column.setHeader("港口名");
		column.setWidth(200);
		column.setEditor(tce);
		configs.add(column);		
		
		return configs;
	}

	@Override
	protected void list(AsyncCallback<List<Port>> callback) {
		getDictionaryService().listPort(callback);		
	}

	@Override
	protected void save(Port t, AsyncCallback<Long> callback) {
		getDictionaryService().savePort(t, callback);
	}

	@Override
	protected void delete(List<Long> ids, AsyncCallback<Void> callback) {
		getDictionaryService().deletePort(ids, callback);
	}

	@Override
	protected void update(List<Port> dictionarys,
			AsyncCallback<Void> callback) {
		getDictionaryService().updatePort(dictionarys, callback);
	}

	@Override
	protected FormCallback<Port> initNewItemFormPanel(
			FormPanel newItemFormPanel) {
		FormData formData = new FormData("-20");		
		TextField<String> code = new TextField<String>();
		code.setName("code");
		code.setFieldLabel("编号");
		code.setAllowBlank(false);
		newItemFormPanel.add(code, formData);
		TextField<String> name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel("港口名");
		name.setAllowBlank(false);
		newItemFormPanel.add(name, formData);		
		
		FormBinding binding = new FormBinding(newItemFormPanel,true);  
		BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(Port.class);
		final BeanModel beanModel = beanModelFactory.createModel(new Port());
		binding.bind(beanModel);		
		
		FormCallback<Port> formCallback=new FormCallback<Port>() {	
			@Override
			public Port getBean() {
				return beanModel.getBean();
			}
		};
		return formCallback;
	}	

	
}
