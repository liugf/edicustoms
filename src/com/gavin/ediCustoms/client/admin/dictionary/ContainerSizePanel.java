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
import com.gavin.ediCustoms.entity.edi.dictionary.ContainerSize;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ContainerSizePanel extends CommonDictionaryPanel<ContainerSize> {
	
	public ContainerSizePanel(String heading){
		this.heading=heading;
		beanModelFactory=BeanModelLookup.get().getFactory(ContainerSize.class);
	}	

	@Override
	protected List<ColumnConfig> getColumnConfigs() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		TextField<String> tf = new TextField<String>();
		CellEditor tce = new CellEditor(tf);
		ColumnConfig column = new ColumnConfig();
		NumberField inf=new NumberField();
		inf.setPropertyEditorType(Integer.class);
		CellEditor ice = new CellEditor(inf);

		configs.add(selectionMode.getColumn());

		column.setId("code");
		column.setHeader("代码");
		column.setWidth(100);
		column.setRowHeader(true);
		column.setEditor(tce);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("size");
		column.setHeader("集装箱尺寸");
		column.setWidth(100);
		column.setEditor(tce);
		configs.add(column);		
	
		column = new ColumnConfig();
		column.setId("valentNum");
		column.setHeader("等同标准箱个数");
		column.setWidth(100);
		column.setEditor(ice);
		configs.add(column);
		
		return configs;
	}

	@Override
	protected void list(AsyncCallback<List<ContainerSize>> callback) {
		getDictionaryService().listContainerSize(callback);		
	}

	@Override
	protected void save(ContainerSize t, AsyncCallback<Long> callback) {
		getDictionaryService().saveContainerSize(t, callback);
	}

	@Override
	protected void delete(List<Long> ids, AsyncCallback<Void> callback) {
		getDictionaryService().deleteContainerSize(ids, callback);
	}

	@Override
	protected void update(List<ContainerSize> dictionarys,
			AsyncCallback<Void> callback) {
		getDictionaryService().updateContainerSize(dictionarys, callback);
	}

	@Override
	protected FormCallback<ContainerSize> initNewItemFormPanel(
			FormPanel newItemFormPanel) {
		FormData formData = new FormData("-20");		
		TextField<String> code = new TextField<String>();
		code.setName("code");
		code.setFieldLabel("代码");
		code.setAllowBlank(false);
		newItemFormPanel.add(code, formData);
		
		TextField<String> size = new TextField<String>();
		size.setName("size");
		size.setFieldLabel("集装箱尺寸");
		newItemFormPanel.add(size, formData);
		
		NumberField weight=new NumberField();
		weight.setPropertyEditorType(Integer.class);
		weight.setName("valentNum");
		weight.setFieldLabel("等同标准箱个数");
		newItemFormPanel.add(weight, formData);
		
		FormBinding binding = new FormBinding(newItemFormPanel,true);  
		BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(ContainerSize.class);
		final BeanModel beanModel = beanModelFactory.createModel(new ContainerSize());
		binding.bind(beanModel);		
		
		FormCallback<ContainerSize> formCallback=new FormCallback<ContainerSize>() {	
			@Override
			public ContainerSize getBean() {
				return beanModel.getBean();
			}
		};
		return formCallback;
	}	

	
}
