package com.gavin.ediCustoms.client.admin.dictionary;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.gavin.ediCustoms.entity.edi.dictionary.GoodClassification;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GoodClassificationPanel extends CommonDictionaryPanel<GoodClassification> {
	
	public GoodClassificationPanel(String heading){
		this.heading=heading;
		beanModelFactory=BeanModelLookup.get().getFactory(GoodClassification.class);
	}	

	@Override
	protected List<ColumnConfig> getColumnConfigs() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		CellEditor tce = new CellEditor(new TextField<String>());
		NumberField dnf=new NumberField();
		dnf.setPropertyEditorType(Double.class);
		NumberField inf=new NumberField();
		inf.setPropertyEditorType(Integer.class);
		CellEditor dce = new CellEditor(dnf);
		CellEditor ice = new CellEditor(inf);
		ColumnConfig column = new ColumnConfig();

		configs.add(selectionMode.getColumn());

		column.setId("code");
		column.setHeader("编码");
		column.setWidth(100);
		column.setRowHeader(true);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("plusCode");
		column.setHeader("附加编码");
		column.setWidth(100);
		column.setEditor(tce);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("name");
		column.setHeader("商品名称");
		column.setWidth(200);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("unit1");
		column.setHeader("第一单位");
		column.setWidth(100);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("unit2");
		column.setHeader("第二单位");
		column.setWidth(100);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("lowRate");
		column.setHeader("Low_Rate");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("highRate");
		column.setHeader("High_Rate");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("outRate");
		column.setHeader("Out_Rate");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("regMark");
		column.setHeader("Reg_Mark");
		column.setWidth(100);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("regRate");
		column.setHeader("Reg_Rate");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("taxType");
		column.setHeader("Tax_Type");
		column.setWidth(100);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("taxRate");
		column.setHeader("Tax_Rate");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("commRate");
		column.setHeader("Comm_Rate");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("taiwanRate");
		column.setHeader("Taiwan_Rate");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("otherType");
		column.setHeader("Other_Type");
		column.setWidth(100);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("otherRate");
		column.setHeader("Other_Rate");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("iLowPrice");
		column.setHeader("ILow_Price");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("iHighPrice");
		column.setHeader("IHigh_Price");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("eLowPrice");
		column.setHeader("ELow_Price");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("eHighPrice");
		column.setHeader("EHigh_Price");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("maxIn");
		column.setHeader("Max_In");
		column.setWidth(100);
		column.setEditor(ice);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("maxOut");
		column.setHeader("Max_Out");
		column.setWidth(100);
		column.setEditor(ice);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("controlMark");
		column.setHeader("Control_Mark");
		column.setWidth(100);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("chkPrice");
		column.setHeader("Chk_Price");
		column.setWidth(100);
		column.setEditor(dce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("tariffMark");
		column.setHeader("Tariff_Mark");
		column.setWidth(100);
		column.setEditor(tce);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("note");
		column.setHeader("备注");
		column.setWidth(300);
		column.setEditor(tce);
		configs.add(column);
		
		return configs;
	}

	@Override
	protected void list(AsyncCallback<List<GoodClassification>> callback) {
		getDictionaryService().listGoodClassification(callback);		
	}

	@Override
	protected void save(GoodClassification t, AsyncCallback<Long> callback) {
		getDictionaryService().saveGoodClassification(t, callback);
	}

	@Override
	protected void delete(List<Long> ids, AsyncCallback<Void> callback) {
		getDictionaryService().deleteGoodClassification(ids, callback);
	}

	@Override
	protected void update(List<GoodClassification> dictionarys,
			AsyncCallback<Void> callback) {
		getDictionaryService().updateGoodClassification(dictionarys, callback);
	}

	@Override
	protected FormCallback<GoodClassification> initNewItemFormPanel(
			final FormPanel newItemFormPanel) {
		newItemFormPanel.setWidth(600);
		
		LayoutContainer main = new LayoutContainer();  
	    main.setLayout(new ColumnLayout());  
	    
	    LayoutContainer left = new LayoutContainer();  
	    left.setStyleAttribute("paddingRight", "10px");  
	    FormLayout layout = new FormLayout();  
	    left.setLayout(layout); 
	    
	    LayoutContainer right = new LayoutContainer();  
	    right.setStyleAttribute("paddingLeft", "10px");  
	    layout = new FormLayout();  
	    right.setLayout(layout);  	    
	    
	    FormData formData = new FormData("-20");	
	    
		TextField<String> code = new TextField<String>();
		code.setName("code");
		code.setFieldLabel("编码");
		code.setAllowBlank(false);
		left.add(code, formData);
		
		TextField<String> plusCode = new TextField<String>();
		plusCode.setName("plusCode");
		plusCode.setFieldLabel("附加编码");
		right.add(plusCode, formData);
		
		TextField<String> name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel("商品名称");
		name.setAllowBlank(false);
		left.add(name, formData);
	    
		TextField<String> unit1 = new TextField<String>();
		unit1.setName("unit1");
		unit1.setFieldLabel("第一单位");
		unit1.setAllowBlank(false);
		right.add(unit1, formData);
		
		TextField<String> unit2 = new TextField<String>();
		unit2.setName("unit2");
		unit2.setFieldLabel("第二单位");
		left.add(unit2, formData);
		
		NumberField lowRate=new NumberField();
		lowRate.setPropertyEditorType(Double.class);
		lowRate.setName("lowRate");
		lowRate.setFieldLabel("Low_Rate");
		right.add(lowRate, formData);
		
		NumberField highRate=new NumberField();
		highRate.setPropertyEditorType(Double.class);
		highRate.setName("highRate");
		highRate.setFieldLabel("High_Rate");
		left.add(highRate, formData);
		
		NumberField outRate=new NumberField();
		outRate.setPropertyEditorType(Double.class);
		outRate.setName("outRate");
		outRate.setFieldLabel("Out_Rate");
		right.add(outRate, formData);
		
		TextField<String> regMark = new TextField<String>();
		regMark.setName("regMark");
		regMark.setFieldLabel("Reg_Mark");
		left.add(regMark, formData);
		
		NumberField regRate=new NumberField();
		regRate.setPropertyEditorType(Double.class);
		regRate.setName("regRate");
		regRate.setFieldLabel("Reg_Rate");
		right.add(regRate, formData);
		
		TextField<String> taxType = new TextField<String>();
		taxType.setName("taxType");
		taxType.setFieldLabel("Tax_Type");
		left.add(taxType, formData);
		
		NumberField taxRate=new NumberField();
		taxRate.setPropertyEditorType(Double.class);
		taxRate.setName("taxRate");
		taxRate.setFieldLabel("Tax_Rate");
		right.add(taxRate, formData);
		
		NumberField commRate=new NumberField();
		commRate.setPropertyEditorType(Double.class);
		commRate.setName("commRate");
		commRate.setFieldLabel("Comm_Rate");
		left.add(commRate, formData);
		
		NumberField taiwanRate=new NumberField();
		taiwanRate.setPropertyEditorType(Double.class);
		taiwanRate.setName("taiwanRate");
		taiwanRate.setFieldLabel("Taiwan_Rate");
		right.add(taiwanRate, formData);
		
		TextField<String> otherType = new TextField<String>();
		otherType.setName("otherType");
		otherType.setFieldLabel("Other_Type");
		left.add(otherType, formData);
		
		NumberField otherRate=new NumberField();
		otherRate.setPropertyEditorType(Double.class);
		otherRate.setName("otherRate");
		otherRate.setFieldLabel("Other_Rate");
		right.add(otherRate, formData);
		
		NumberField iLowPrice=new NumberField();
		iLowPrice.setPropertyEditorType(Double.class);
		iLowPrice.setName("iLowPrice");
		iLowPrice.setFieldLabel("ILow_Price");
		left.add(iLowPrice, formData);
		
		NumberField iHighPrice=new NumberField();
		iHighPrice.setPropertyEditorType(Double.class);
		iHighPrice.setName("iHighPrice");
		iHighPrice.setFieldLabel("IHigh_Price");
		right.add(iHighPrice, formData);
		
		NumberField eLowPrice=new NumberField();
		eLowPrice.setPropertyEditorType(Double.class);
		eLowPrice.setName("eLowPrice");
		eLowPrice.setFieldLabel("ELow_Price");
		left.add(eLowPrice, formData);
		
		NumberField eHighPrice=new NumberField();
		eHighPrice.setPropertyEditorType(Double.class);
		eHighPrice.setName("eHighPrice");
		eHighPrice.setFieldLabel("EHigh_Price");
		right.add(eHighPrice, formData);
		
		NumberField maxIn=new NumberField();
		maxIn.setPropertyEditorType(Integer.class);
		maxIn.setName("maxIn");
		maxIn.setFieldLabel("Max_In");
		left.add(maxIn, formData);
		
		NumberField maxOut=new NumberField();
		maxOut.setPropertyEditorType(Integer.class);
		maxOut.setName("maxOut");
		maxOut.setFieldLabel("Max_Out");
		right.add(maxOut, formData);

		TextField<String> controlMark = new TextField<String>();
		controlMark.setName("controlMark");
		controlMark.setFieldLabel("Control_Mark");
		left.add(controlMark, formData);
		
		NumberField chkPrice=new NumberField();
		chkPrice.setPropertyEditorType(Double.class);
		chkPrice.setName("chkPrice");
		chkPrice.setFieldLabel("Chk_Price");
		right.add(chkPrice, formData);
		
		TextField<String> tariffMark = new TextField<String>();
		tariffMark.setName("tariffMark");
		tariffMark.setFieldLabel("Tariff_Mark");
		left.add(tariffMark, formData);
		
		TextField<String> note = new TextField<String>();
		note.setName("note");
		note.setFieldLabel("备注");
		right.add(note, formData);
		
	    
	    main.add(left, new ColumnData(.5));  
	    main.add(right, new ColumnData(.5));  
	  
	    newItemFormPanel.add(main, new FormData("100%"));  		
		
		
		final FormBinding binding = new FormBinding(newItemFormPanel,true); 
		BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(GoodClassification.class);
		final BeanModel beanModel = beanModelFactory.createModel(new GoodClassification());
		binding.bind(beanModel);		
		
		FormCallback<GoodClassification> formCallback=new FormCallback<GoodClassification>() {	
			@Override
			public GoodClassification getBean() {
				return beanModel.getBean();
			}
		};
		return formCallback;
	}	

	
}
