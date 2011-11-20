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
import com.gavin.ediCustoms.entity.edi.dictionary.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UnitPanel extends CommonDictionaryPanel<Unit> {
	
	public UnitPanel(String heading){
		this.heading=heading;
		beanModelFactory=BeanModelLookup.get().getFactory(Unit.class);
	}	

	@Override
	protected List<ColumnConfig> getColumnConfigs() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		TextField<String> tf = new TextField<String>();
		CellEditor tce = new CellEditor(tf);
		NumberField fnf=new NumberField();
		fnf.setPropertyEditorType(Double.class);
		CellEditor fce = new CellEditor(fnf);
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
		column.setId("convertCode");
		column.setHeader("转换码");
		column.setWidth(100);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("convertRate");
		column.setHeader("转换率");
		column.setWidth(100);
		column.setEditor(fce);
		configs.add(column);
		
		return configs;
	}

	@Override
	protected void list(AsyncCallback<List<Unit>> callback) {
		getDictionaryService().listUnit(callback);		
	}

	@Override
	protected void save(Unit t, AsyncCallback<Long> callback) {
		getDictionaryService().saveUnit(t, callback);
	}

	@Override
	protected void delete(List<Long> ids, AsyncCallback<Void> callback) {
		getDictionaryService().deleteUnit(ids, callback);
	}

	@Override
	protected void update(List<Unit> dictionarys,
			AsyncCallback<Void> callback) {
		getDictionaryService().updateUnit(dictionarys, callback);
	}

	@Override
	protected FormCallback<Unit> initNewItemFormPanel(
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
		
		TextField<String> convertCode = new TextField<String>();
		convertCode.setName("convertCode");
		convertCode.setFieldLabel("转换码");
		newItemFormPanel.add(convertCode, formData);
		
		NumberField convertRate=new NumberField();
		convertRate.setPropertyEditorType(Double.class);
		convertRate.setName("convertRate");
		convertRate.setFieldLabel("转换率");
		newItemFormPanel.add(convertRate, formData);
		
		FormBinding binding = new FormBinding(newItemFormPanel,true);  
		BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(Unit.class);
		final BeanModel beanModel = beanModelFactory.createModel(new Unit());
		binding.bind(beanModel);		
		
		FormCallback<Unit> formCallback=new FormCallback<Unit>() {	
			@Override
			public Unit getBean() {
				return beanModel.getBean();
			}
		};
		return formCallback;
	}	

	
}
