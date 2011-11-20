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
import com.gavin.ediCustoms.entity.edi.dictionary.Bracket;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BracketPanel extends CommonDictionaryPanel<Bracket> {
	
	public BracketPanel(String heading){
		this.heading=heading;
		beanModelFactory=BeanModelLookup.get().getFactory(Bracket.class);
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
		column.setHeader("托架代码");
		column.setWidth(100);
		column.setRowHeader(true);
		column.setEditor(tce);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("manufacturer");
		column.setHeader("生产厂家");
		column.setWidth(150);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("modelNo");
		column.setHeader("型号");
		column.setWidth(100);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("specification");
		column.setHeader("规格");
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
	protected void list(AsyncCallback<List<Bracket>> callback) {
		getDictionaryService().listBracket(callback);		
	}

	@Override
	protected void save(Bracket t, AsyncCallback<Long> callback) {
		getDictionaryService().saveBracket(t, callback);
	}

	@Override
	protected void delete(List<Long> ids, AsyncCallback<Void> callback) {
		getDictionaryService().deleteBracket(ids, callback);
	}

	@Override
	protected void update(List<Bracket> dictionarys,
			AsyncCallback<Void> callback) {
		getDictionaryService().updateBracket(dictionarys, callback);
	}

	@Override
	protected FormCallback<Bracket> initNewItemFormPanel(
			FormPanel newItemFormPanel) {
		FormData formData = new FormData("-20");		
		TextField<String> code = new TextField<String>();
		code.setName("code");
		code.setFieldLabel("托架代码");
		code.setAllowBlank(false);
		newItemFormPanel.add(code, formData);
		
		TextField<String> manufacturer = new TextField<String>();
		manufacturer.setName("manufacturer");
		manufacturer.setFieldLabel("生产厂家");
		newItemFormPanel.add(manufacturer, formData);
		
		TextField<String> modelNo = new TextField<String>();
		modelNo.setName("modelNo");
		modelNo.setFieldLabel("型号");
		modelNo.setAllowBlank(false);
		newItemFormPanel.add(modelNo, formData);
		
		TextField<String> specification = new TextField<String>();
		specification.setName("specification");
		specification.setFieldLabel("规格");
		specification.setAllowBlank(false);
		newItemFormPanel.add(specification, formData);
		
		NumberField weight=new NumberField();
		weight.setPropertyEditorType(Double.class);
		weight.setName("weight");
		weight.setFieldLabel("重量");
		newItemFormPanel.add(weight, formData);
		
		FormBinding binding = new FormBinding(newItemFormPanel,true);  
		BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(Bracket.class);
		final BeanModel beanModel = beanModelFactory.createModel(new Bracket());
		binding.bind(beanModel);		
		
		FormCallback<Bracket> formCallback=new FormCallback<Bracket>() {	
			@Override
			public Bracket getBean() {
				return beanModel.getBean();
			}
		};
		return formCallback;
	}	

	
}
