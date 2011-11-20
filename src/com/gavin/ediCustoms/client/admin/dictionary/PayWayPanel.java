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
import com.gavin.ediCustoms.entity.edi.dictionary.PayWay;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PayWayPanel extends CommonDictionaryPanel<PayWay> {
	
	public PayWayPanel(String heading){
		this.heading=heading;
		beanModelFactory=BeanModelLookup.get().getFactory(PayWay.class);
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
		column.setHeader("结汇方式");
		column.setWidth(200);
		column.setEditor(tce);
		configs.add(column);
		
		return configs;
	}

	@Override
	protected void list(AsyncCallback<List<PayWay>> callback) {
		getDictionaryService().listPayWay(callback);		
	}

	@Override
	protected void save(PayWay t, AsyncCallback<Long> callback) {
		getDictionaryService().savePayWay(t, callback);
	}

	@Override
	protected void delete(List<Long> ids, AsyncCallback<Void> callback) {
		getDictionaryService().deletePayWay(ids, callback);
	}

	@Override
	protected void update(List<PayWay> dictionarys,
			AsyncCallback<Void> callback) {
		getDictionaryService().updatePayWay(dictionarys, callback);
	}

	@Override
	protected FormCallback<PayWay> initNewItemFormPanel(
			FormPanel newItemFormPanel) {
		FormData formData = new FormData("-20");		
		TextField<String> code = new TextField<String>();	
		code.setName("code");
		code.setFieldLabel("编号");
		code.setAllowBlank(false);
		newItemFormPanel.add(code, formData);
		TextField<String> name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel("结汇方式");
		name.setAllowBlank(false);
		newItemFormPanel.add(name, formData);
		
		FormBinding binding = new FormBinding(newItemFormPanel,true);  
		BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(PayWay.class);
		final BeanModel beanModel = beanModelFactory.createModel(new PayWay());
		binding.bind(beanModel);
		
		FormCallback<PayWay> formCallback=new FormCallback<PayWay>() {	
			@Override
			public PayWay getBean() {
				return beanModel.getBean();
			}
		};
		return formCallback;
	}	

	
}
