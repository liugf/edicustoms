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
import com.gavin.ediCustoms.entity.edi.dictionary.District;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DistrictPanel extends CommonDictionaryPanel<District> {
	
	public DistrictPanel(String heading){
		this.heading=heading;
		beanModelFactory=BeanModelLookup.get().getFactory(District.class);
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
		column.setHeader("地区名称");
		column.setWidth(200);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("type");
		column.setHeader("类别");
		column.setWidth(100);
		column.setEditor(tce);
		configs.add(column);
		
		return configs;
	}

	@Override
	protected void list(AsyncCallback<List<District>> callback) {
		getDictionaryService().listDistrict(callback);		
	}

	@Override
	protected void save(District t, AsyncCallback<Long> callback) {
		getDictionaryService().saveDistrict(t, callback);
	}

	@Override
	protected void delete(List<Long> ids, AsyncCallback<Void> callback) {
		getDictionaryService().deleteDistrict(ids, callback);
	}

	@Override
	protected void update(List<District> dictionarys,
			AsyncCallback<Void> callback) {
		getDictionaryService().updateDistrict(dictionarys, callback);
	}

	@Override
	protected FormCallback<District> initNewItemFormPanel(
			FormPanel newItemFormPanel) {
		FormData formData = new FormData("-20");		
		TextField<String> code = new TextField<String>();	
		code.setName("code");
		code.setFieldLabel("编号");
		code.setAllowBlank(false);
		newItemFormPanel.add(code, formData);
		TextField<String> name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel("地区名称");
		name.setAllowBlank(false);
		newItemFormPanel.add(name, formData);		
		NumberField type=new NumberField();
		type.setPropertyEditorType(Integer.class);
		type.setName("type");
		type.setFieldLabel("类别");
		newItemFormPanel.add(type, formData);
		
		FormBinding binding = new FormBinding(newItemFormPanel,true);  
		BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(District.class);
		final BeanModel beanModel = beanModelFactory.createModel(new District());
		binding.bind(beanModel);
		
		FormCallback<District> formCallback=new FormCallback<District>() {	
			@Override
			public District getBean() {
				return beanModel.getBean();
			}
		};
		return formCallback;
	}	

	
}
