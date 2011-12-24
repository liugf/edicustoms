package com.gavin.ediCustoms.client.core;


import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.gavin.ediCustoms.client.myUI.MyTabItem;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.ForeignEnterprise;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.entity.edi.contract.ContractHead;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.dictionary.DealMode;
import com.gavin.ediCustoms.entity.edi.dictionary.District;
import com.gavin.ediCustoms.entity.edi.dictionary.Port;
import com.gavin.ediCustoms.entity.edi.dictionary.TradeMode;
import com.gavin.ediCustoms.entity.edi.dictionary.WrapType;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ImportPrintTabItem extends MyTabItem implements CustomsDeclarationPanelListener{
	
	private FormPanel formPanel;	
	
	private Enterprise enterprise;
	private ForeignEnterprise foreignEnterprise;
	private CustomsDeclarationHead customsDeclarationHead;
	
	private ComboBox<BeanModel> foreignEnterpriseComboBox;
	private ComboBox<BeanModel> enterpriseComboBox;
	private ComboBox<BeanModel> customsDeclarationHeadComboBox;
	
	private CustomsDeclarationHead selectedCustomsDeclarationHead;

	public ImportPrintTabItem() {
		setText("打印单据");	
		enterprise=new Enterprise();
		foreignEnterprise=new ForeignEnterprise();
		customsDeclarationHead=new CustomsDeclarationHead();
		
		addListener(Events.Select,
				new Listener<ComponentEvent>() {
					public void handleEvent(ComponentEvent be) {
						if (selectedCustomsDeclarationHead == null) {
							MessageBox.alert("提示", "请选择报关单表头", null);
							be.cancelBubble();
						} else {
							if (isDirty) {
								refresh();
								isDirty=false;
							}							
						}
					}
				});
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		
		ContentPanel content = new ContentPanel();
		content.setHeaderVisible(false);
		content.setBodyBorder(false);
		content.setStyleAttribute("backgroundColor", "#dfe8f6");
		content.setLayout(new FitLayout());
		
		formPanel=createForm();
		content.add(formPanel);				
		
		
		add(content);	
		
		
	}
	
	private FormPanel createForm() {
		FormPanel panel = new FormPanel();
		panel.setStyleAttribute("backgroundColor", "#dfe8f6");
		panel.setBodyBorder(false);
		panel.setHeaderVisible(false);		
		
		
		LayoutContainer enterpriseframe = new LayoutContainer();
		enterpriseframe.setStyleAttribute("marginBottom", "10px");
		enterpriseframe.setLayout(new ColumnLayout());
		
	    
		FieldSet fieldSet = new FieldSet();  
		fieldSet.setHeading("外商公司情况"); 
		FormLayout layout = new FormLayout();  
	    layout.setLabelWidth(75); 
	    layout.setDefaultWidth(280);
	    fieldSet.setLayout(layout); 
	    
	    
	    
	    final ListStore<BeanModel> foreignEnterpriseStore = getForeignEnterpriseStore();
		foreignEnterpriseComboBox = new ComboBox<BeanModel>();
		foreignEnterpriseComboBox.setTabIndex(1);
		//foreignEnterpriseComboBox.setForceSelection(true);
		foreignEnterpriseComboBox.setEmptyText("请选择...");
		foreignEnterpriseComboBox.setDisplayField("registeName");
		foreignEnterpriseComboBox.setFieldLabel("外商公司");
		foreignEnterpriseComboBox.setStore(foreignEnterpriseStore);
		foreignEnterpriseComboBox.setTypeAhead(true);
		foreignEnterpriseComboBox.setTriggerAction(TriggerAction.ALL);		
		foreignEnterpriseComboBox.setValue(BeanModelLookup.get().getFactory(ForeignEnterprise.class).createModel(foreignEnterprise));
		
		final TextField<String> registeName_f=new TextField<String>();
		registeName_f.setName("foreignEnterprise.registeName");
		registeName_f.setVisible(false);
		
		final TextField<String> address_f = new TextField<String>();
		address_f.setTabIndex(2);
		address_f.setName("foreignEnterprise.address");
		address_f.setFieldLabel("单位地址");
		
		final TextField<String> telephone_f = new TextField<String>();
		telephone_f.setTabIndex(3);
		telephone_f.setName("foreignEnterprise.telephone");
		telephone_f.setFieldLabel("联系电话");
		
		final TextField<String> fax_f = new TextField<String>();
		fax_f.setTabIndex(4);
		fax_f.setName("foreignEnterprise.fax");
		fax_f.setFieldLabel("传真");
		
		foreignEnterpriseComboBox.addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<BeanModel>>() {
			@Override
			public void handleEvent(SelectionChangedEvent<BeanModel> be) {
				if (be.getSelectedItem()==null) {
					return;
				}
				registeName_f.setValue((String)be.getSelectedItem().get("registeName"));
				address_f.setValue((String)be.getSelectedItem().get("address"));
				telephone_f.setValue((String)be.getSelectedItem().get("telephone"));
				fax_f.setValue((String)be.getSelectedItem().get("fax"));
			}
		});
		
		foreignEnterpriseComboBox.addListener(Events.Blur, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				registeName_f.setValue(foreignEnterpriseComboBox.getRawValue());
			}
		});
		
		
		fieldSet.add(foreignEnterpriseComboBox);
		fieldSet.add(registeName_f);
		fieldSet.add(address_f);
		fieldSet.add(telephone_f);
		fieldSet.add(fax_f);
		
		fieldSet.setStyleAttribute("padding", "10px 10px 10px 10px");
		enterpriseframe.add(fieldSet,new ColumnData(0.48));
		
		fieldSet = new FieldSet();  
		fieldSet.setHeading("收货单位情况"); 
		layout = new FormLayout();  
	    layout.setLabelWidth(75); 
	    layout.setDefaultWidth(280);
		fieldSet.setLayout(layout); 
		
		
		enterpriseComboBox = new ComboBox<BeanModel>();
		enterpriseComboBox.setStore(new ListStore<BeanModel>());
		enterpriseComboBox.setValue(BeanModelLookup.get().getFactory(Enterprise.class).createModel(enterprise));
		
		final TextField<String> registeName_e = new TextField<String>();
		registeName_e.setTabIndex(5);
		registeName_e.setName("enterprise.registeName");
		registeName_e.setFieldLabel("单位名称");
		
		
		final TextField<String> address_e = new TextField<String>();
		address_e.setTabIndex(6);
		address_e.setName("enterprise.address");
		address_e.setFieldLabel("单位地址");
		
		final TextField<String> telephone_e = new TextField<String>();
		telephone_e.setTabIndex(7);
		telephone_e.setName("enterprise.telephone");
		telephone_e.setFieldLabel("联系电话");
		
		final TextField<String> fax_e = new TextField<String>();
		fax_e.setTabIndex(8);
		fax_e.setName("enterprise.fax");
		fax_e.setFieldLabel("传真");
		
		enterpriseComboBox.addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<BeanModel>>() {
			@Override
			public void handleEvent(SelectionChangedEvent<BeanModel> be) {
				if (be.getSelectedItem()==null) {
					return;
				}
				registeName_e.setValue(be.getSelectedItem().get("registeName").toString());
				address_e.setValue(be.getSelectedItem().get("address").toString());
				telephone_e.setValue(be.getSelectedItem().get("telephone").toString());
				fax_e.setValue(be.getSelectedItem().get("fax").toString());
			}
		});
		
		fieldSet.add(registeName_e);
		fieldSet.add(address_e);
		fieldSet.add(telephone_e);
		fieldSet.add(fax_e);		
		enterpriseframe.add(new LayoutContainer(),new ColumnData(0.04));
		fieldSet.setStyleAttribute("padding", "10px 10px 10px 10px");
		enterpriseframe.add(fieldSet,new ColumnData(0.48));
		
		panel.add(enterpriseframe);
		
		fieldSet = new FieldSet();
		fieldSet.setHeading("其他信息"); 
		layout = new FormLayout();  
	    layout.setLabelWidth(75); 
	    layout.setDefaultWidth(443);
		fieldSet.setLayout(layout); 
		
		LayoutContainer frame = new LayoutContainer();
		frame.setLayout(new ColumnLayout());

		LayoutContainer left = new LayoutContainer();
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(75);
		formLayout.setDefaultWidth(170);
		left.setLayout(formLayout);
		
		LayoutContainer middle = new LayoutContainer();
		formLayout = new FormLayout();
		formLayout.setLabelWidth(75);
		formLayout.setDefaultWidth(170);
		middle.setLayout(formLayout);

		LayoutContainer right = new LayoutContainer();
		formLayout = new FormLayout();
		formLayout.setLabelWidth(75);
		formLayout.setDefaultWidth(170);
		right.setLayout(formLayout);
		
		customsDeclarationHeadComboBox = new ComboBox<BeanModel>();
		customsDeclarationHeadComboBox.setStore(new ListStore<BeanModel>());
		customsDeclarationHeadComboBox.setValue(BeanModelLookup.get().getFactory(CustomsDeclarationHead.class).createModel(customsDeclarationHead));
		
				
		final NumberField customsDeclarationHeadId=new NumberField();		
		customsDeclarationHeadId.setPropertyEditorType(Long.class);
		customsDeclarationHeadId.setName("id");
		customsDeclarationHeadId.setVisible(false);
		panel.add(customsDeclarationHeadId);
		
		
		final TextField<String> invoiceNo = new TextField<String>();
		invoiceNo.setTabIndex(9);
		invoiceNo.setName("invoiceNo");
		invoiceNo.setFieldLabel("发票号码");		
		left.add(invoiceNo);
		
		final TextField<String> contractNo = new TextField<String>();
		contractNo.setTabIndex(10);
		contractNo.setName("contractNo");
		contractNo.setFieldLabel("合同号码");
		middle.add(contractNo);
		
		final TextField<String> manualNo = new TextField<String>();
		manualNo.setTabIndex(11);
		manualNo.setName("manualNo");
		manualNo.setFieldLabel("手册编号");
		right.add(manualNo);
		
		final DateField declareTime = new DateField();
		declareTime.setTabIndex(12);
		declareTime.setName("declareTime");
		declareTime.setAllowBlank(false);
		declareTime.setFieldLabel("日期");
		left.add(declareTime);
		
		final TextField<String> tradeMode = new TextField<String>();
		tradeMode.setTabIndex(13);
		tradeMode.setName("tradeMode");
		tradeMode.setFieldLabel("贸易方式");
		middle.add(tradeMode);
		
		final TextField<String> payWay = new TextField<String>();
		payWay.setTabIndex(14);
		payWay.setName("payWay");
		payWay.setFieldLabel("收汇方式");
		right.add(payWay);
		
		final TextField<String> iePort = new TextField<String>();
		iePort.setTabIndex(15);
		iePort.setName("iePort");
		iePort.setFieldLabel("发运岸");		
		left.add(iePort);	
		
		final TextField<String> transhipPort = new TextField<String>();
		transhipPort.setTabIndex(16);
		transhipPort.setName("transhipPort");
		transhipPort.setFieldLabel("转运港");		
		middle.add(transhipPort);
		
		final TextField<String> destinationPort = new TextField<String>();
		destinationPort.setTabIndex(17);
		destinationPort.setName("destinationPort");
		destinationPort.setFieldLabel("目的港");		
		right.add(destinationPort);		
		
		final TextField<String> creditNo = new TextField<String>();
		creditNo.setTabIndex(18);
		creditNo.setName("creditNo");
		creditNo.setFieldLabel("信用证号");
		left.add(creditNo);		
		
		SimpleComboBox<String> transportTool = new SimpleComboBox<String>();
		transportTool.setTabIndex(19);
		transportTool.setName("transportTool");
		transportTool.setFieldLabel("运输工具");
		transportTool.setForceSelection(true);
		transportTool.add("汽车");
		transportTool.add("火车");
		transportTool.add("飞机");
		transportTool.add("轮船");
		transportTool.setSimpleValue("汽车");
		transportTool.setTriggerAction(TriggerAction.ALL);
		middle.add(transportTool);		
		
		final TextField<String> shipingMark = new TextField<String>();
		shipingMark.setTabIndex(20);
		shipingMark.setValue("N/M");
		shipingMark.setName("shipingMark");
		shipingMark.setFieldLabel("唛头");
		right.add(shipingMark);
		
		final TextField<String> wrapType = new TextField<String>();
		wrapType.setTabIndex(21);
		wrapType.setName("wrapType");
		wrapType.setFieldLabel("包装种类");
		left.add(wrapType);		
		
		final TextField<String> signedIn = new TextField<String>();
		signedIn.setTabIndex(22);
		signedIn.setName("signedIn");
		signedIn.setFieldLabel("签定地");
		middle.add(signedIn);
		
		final TextField<String> dealMode = new TextField<String>();
		dealMode.setTabIndex(23);
		dealMode.setName("dealMode");
		dealMode.setFieldLabel("成交方式");
		right.add(dealMode);
		
		NumberField balance = new NumberField();
		balance.setTabIndex(24);
		balance.setAllowBlank(false);
		balance.setPropertyEditorType(Double.class);
		balance.setName("balance");
		balance.setFormat(NumberFormat.getFormat("#0.0000"));
		balance.setFieldLabel("差额百分比");
		balance.setValue(0);
		left.add(balance);
		
		TextField<String> insurance = new TextField<String>();
		insurance.setTabIndex(28);
		insurance.setName("insurance");
		insurance.setFieldLabel("保险");
		insurance.setValue("由买方负责");
		middle.add(insurance);
		
		final TextField<String> dealModeAddress = new TextField<String>();
		dealModeAddress.setTabIndex(28);
		dealModeAddress.setName("dealModeAddress");
		dealModeAddress.setFieldLabel("成交地址");
		right.add(dealModeAddress);
		
		TextField<String> manufactureCountry = new TextField<String>();
		manufactureCountry.setTabIndex(27);
		manufactureCountry.setName("manufactureCountry");
		manufactureCountry.setFieldLabel("生产国别");
		manufactureCountry.setValue("中国");
		left.add(manufactureCountry);	
		
		final DateField exportDeadline = new DateField();
		exportDeadline.setTabIndex(25);
		exportDeadline.setName("exportDeadline");
		exportDeadline.setAllowBlank(false);
		exportDeadline.setFieldLabel("出口期限");
		middle.add(exportDeadline);
		
		final DateField importDeadline = new DateField();
		importDeadline.setTabIndex(26);
		importDeadline.setName("importDeadline");
		importDeadline.setAllowBlank(false);
		importDeadline.setFieldLabel("进口期限");
		right.add(importDeadline);
		
		frame.add(left, new ColumnData(.333333333));
		frame.add(middle, new ColumnData(.333333333));	
		frame.add(right, new ColumnData(.333333333));		
		fieldSet.add(frame);	
		
		TextField<String> termsOfPayment = new TextField<String>();
		termsOfPayment.setTabIndex(29);
		termsOfPayment.setName("termsOfPayment");
		termsOfPayment.setFieldLabel("付款条件");
		termsOfPayment.setValue("T/T 先出后结，可以分批出口，按实际出口成交数量月结九十天结清。");
		fieldSet.add(termsOfPayment);
		
		final TextField<String>  note =new TextField<String>();
		note.setTabIndex(30);
		note.setName("note");
		note.setFieldLabel("备注");	
		fieldSet.add(note);		
	
		
		panel.add(fieldSet);
		
		final AsyncCallback<District> ownerDistrictCallback = new AsyncCallback<District>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(District result) {
				System.out.println(result);
				iePort.setValue(result.getName());
				signedIn.setValue(result.getName());
				dealModeAddress.setValue(result.getName());
			}
		};
		
		final AsyncCallback<Port> destinationPortCallback = new AsyncCallback<Port>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(Port result) {
				destinationPort.setValue(result.getName());
			}
		};
		
		final AsyncCallback<ContractHead> contractHeadCallback = new AsyncCallback<ContractHead>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(ContractHead result) {
				manualNo.setValue(result.getManualNo());
				importDeadline.setValue(result.getImportDeadline());
				exportDeadline.setValue(result.getExportDeadline());
			}
		};
		
		final AsyncCallback<TradeMode> tradeModeCallback = new AsyncCallback<TradeMode>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(TradeMode result) {
				tradeMode.setValue(result.getName());
			}
		};
		
		final AsyncCallback<WrapType> wrapTypeCallback = new AsyncCallback<WrapType>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(WrapType result) {
				wrapType.setValue(result.getName());
			}
		};
		
		final AsyncCallback<DealMode> dealModeCallback = new AsyncCallback<DealMode>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(DealMode result) {
				dealMode.setValue(result.getName());
			}

		};
		
		customsDeclarationHeadComboBox.addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<BeanModel>>() {
			@Override
			public void handleEvent(SelectionChangedEvent<BeanModel> be) {
				if (be.getSelectedItem()==null) {
					return;
				}
				contractNo.setValue((String)be.getSelectedItem().get("contractNo"));	
				customsDeclarationHeadId.setValue(new Long(be.getSelectedItem().get("id").toString()));
				declareTime.setValue((Date)be.getSelectedItem().get("declareTime"));
				String preEntryIdString=(String)be.getSelectedItem().get("preEntryId");
				invoiceNo.setValue(preEntryIdString.substring(preEntryIdString.length()-13, preEntryIdString.length()));

				getDictionaryService().getDistrict((String)be.getSelectedItem().get("ownerDistrict"), ownerDistrictCallback);
				getDictionaryService().getPort((String)be.getSelectedItem().get("destinationPort"), destinationPortCallback);
				
				if (be.getSelectedItem().get("contractHeadId")!=null) {
					contractNo.setValue((String)be.getSelectedItem().get("contractNo"));
					getBusinessService().getContractHead((Long)be.getSelectedItem().get("contractHeadId"), contractHeadCallback);
				}else {
					exportDeadline.setValue(new Date());
					importDeadline.setValue(new Date());
				}
				
				getDictionaryService().getTradeMode((String)be.getSelectedItem().get("tradeMode"), tradeModeCallback);
				//getDictionaryService().getPayWay(be.getSelectedItem().get("payWay").toString(), payWayCallback);
				getDictionaryService().getWrapType((String)be.getSelectedItem().get("wrapType"), wrapTypeCallback);
				getDictionaryService().getDealMode((String)be.getSelectedItem().get("dealMode"), dealModeCallback);
			}
		});
		
		
		
		Button printButton1= new Button("打印发票",Resources.ICONS.printer());
		printButton1.setTabIndex(31);
		printButton1.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void componentSelected(ButtonEvent ce) {
				Map<String, Object> paramsMap=new HashMap<String, Object>();
				for(Field field:formPanel.getFields()){
					if (field.getName()!=null) {
						String key=field.getName();
						if (field instanceof DateField) {
							Date date=(Date) field.getValue();
							String value;
							if (date!=null) {
								value=date2String(date);
							}else {
								value=null;
							}
							paramsMap.put(key, value);
						}else{
							String value=field.getValue()==null?"":field.getValue().toString();	
							paramsMap.put(key, value);
						}
					}									
				}
				openNewWindow("/invoice1.do",paramsMap);				
			}
		});	
		
		
		Button printButton3= new Button("打印装箱单",Resources.ICONS.printer());
		printButton3.setTabIndex(33);
		printButton3.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void componentSelected(ButtonEvent ce) {				
				Map<String, Object> paramsMap=new HashMap<String, Object>();
				for(Field field:formPanel.getFields()){
					if (field.getName()!=null) {						
						String key=field.getName();
						if (key.equals("declareTime")) {
							Date date=(Date) field.getValue();
							String value;
							if (date!=null) {
								value=date2String(date);
							}else {
								value=null;
							}
							paramsMap.put(key, value);
						}else{
							String value=field.getValue()==null?"":field.getValue().toString();	
							paramsMap.put(key, value);
						}					
					}		
				}
				openNewWindow("/packingList1.do",paramsMap);	
			}
		});
		
		Button printButton4= new Button("装箱单(凤岗长安)",Resources.ICONS.printer());
		printButton4.setTabIndex(33);
		printButton4.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void componentSelected(ButtonEvent ce) {				
				Map<String, Object> paramsMap=new HashMap<String, Object>();
				for(Field field:formPanel.getFields()){
					if (field.getName()!=null) {						
						String key=field.getName();
						if (key.equals("declareTime")) {
							Date date=(Date) field.getValue();
							String value;
							if (date!=null) {
								value=date2String(date);
							}else {
								value=null;
							}
							paramsMap.put(key, value);
						}else{
							String value=field.getValue()==null?"":field.getValue().toString();	
							paramsMap.put(key, value);
						}					
					}		
				}
				openNewWindow("/packingList2.do",paramsMap);	
			}
		});
		
		Button printButton5= new Button("打印合同",Resources.ICONS.printer());
		printButton5.setTabIndex(34);
		printButton5.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void componentSelected(ButtonEvent ce) {				
				Map<String, Object> paramsMap=new HashMap<String, Object>();
				for(Field field:formPanel.getFields()){
					if (field.getName()!=null) {						
						String key=field.getName();
						if (key.equals("declareTime")||key.equals("exportDeadline")||key.equals("importDeadline")) {
							Date date=(Date) field.getValue();
							String value;
							if (date!=null) {
								value=date2String(date);
							}else {
								value=null;
							}
							paramsMap.put(key, value);
						}else{
							String value=field.getValue()==null?"":field.getValue().toString();	
							paramsMap.put(key, value);
						}					
					}		
				}
				openNewWindow("/contract.do",paramsMap);	
			}
		});
		
		Button printButton6= new Button("打印装箱单(物流)",Resources.ICONS.printer());
		printButton6.setTabIndex(33);
		printButton6.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void componentSelected(ButtonEvent ce) {				
				Map<String, Object> paramsMap=new HashMap<String, Object>();
				for(Field field:formPanel.getFields()){
					if (field.getName()!=null) {						
						String key=field.getName();
						if (key.equals("declareTime")) {
							Date date=(Date) field.getValue();
							String value;
							if (date!=null) {
								value=date2String(date);
							}else {
								value=null;
							}
							paramsMap.put(key, value);
						}else{
							String value=field.getValue()==null?"":field.getValue().toString();	
							paramsMap.put(key, value);
						}					
					}		
				}
				openNewWindow("/packingList3.do",paramsMap);	
			}
		});
		
		panel.addButton(printButton1);
		panel.addButton(printButton3);	
		panel.addButton(printButton4);	
		panel.addButton(printButton6);
		panel.addButton(printButton5);	
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		
		
		return panel;
	}

	public void refresh(){
		final AsyncCallback<Enterprise> enterpriseCallback = new AsyncCallback<Enterprise>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(Enterprise result) {				
				enterprise=result;
				if (formPanel!=null) {
					enterpriseComboBox.setValue(BeanModelLookup.get().getFactory(Enterprise.class).createModel(enterprise));
				}
			}

		};
		
		final AsyncCallback<ForeignEnterprise> foreignEnterpriseCallback = new AsyncCallback<ForeignEnterprise>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(ForeignEnterprise result) {	
				if (result==null) {
					return;
				}
				foreignEnterprise=result;
				if (formPanel!=null) {
					foreignEnterpriseComboBox.setValue(BeanModelLookup.get().getFactory(ForeignEnterprise.class).createModel(foreignEnterprise));
				}
			}

		};
		
		if (formPanel!=null) {
			customsDeclarationHeadComboBox.setValue(BeanModelLookup.get().getFactory(CustomsDeclarationHead.class).createModel(selectedCustomsDeclarationHead));
		}
		getSystemService().getEnterprise(selectedCustomsDeclarationHead.getOwnerId(), enterpriseCallback);
		if (selectedCustomsDeclarationHead.getContractHeadId()!=null) {
			getBusinessService().getForeignEnterpriseByCustomsDeclaration(selectedCustomsDeclarationHead.getId(),foreignEnterpriseCallback);
		}
		
		/*AsyncCallback<CustomsDeclarationHead> customsDeclarationHeadCallback = new AsyncCallback<CustomsDeclarationHead>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(CustomsDeclarationHead result) {	
				customsDeclarationHead=result;
				if (formPanel!=null) {
					customsDeclarationHeadComboBox.setValue(BeanModelLookup.get().getFactory(CustomsDeclarationHead.class).createModel(customsDeclarationHead));
				}
				getSystemService().getEnterprise(result.getOwnerId(), enterpriseCallback);
				getBusinessService().getForeignEnterpriseByCustomsDeclaration(result.getId(),foreignEnterpriseCallback);
				
			}

		};
		getBusinessService().getCustomsDeclarationHead(customsDeclarationHeadId, customsDeclarationHeadCallback);
			*/
		
	}
	
	private ListStore<BeanModel> getForeignEnterpriseStore() {
		final ListStore<BeanModel> foreignEnterpriseStore = new ListStore<BeanModel>();
		AsyncCallback<List<ForeignEnterprise>> getCallback = new AsyncCallback<List<ForeignEnterprise>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ForeignEnterprise> result) {
				Collections.sort(result, new Comparator<ForeignEnterprise>() {
					public int compare(ForeignEnterprise arg0,
							ForeignEnterprise arg1) {
						return arg0.getTradeCode().compareTo(
								arg1.getTradeCode());
					}
				});
				foreignEnterpriseStore.removeAll();
				foreignEnterpriseStore.add(BeanModelLookup.get()
						.getFactory(ForeignEnterprise.class)
						.createModel(result));
			}

		};
		getSystemService().listForeignEnterprise(getCallback);
		return foreignEnterpriseStore;
	}


	@SuppressWarnings("deprecation")
	private String date2String(Date date){
		int year=date.getYear()+1900;
		int month=date.getMonth()+1;
		int day=date.getDate();
		String monthString=month>9?""+month:"0"+month;
		String daysString=day>9?""+day:"0"+day;
		return year+"-"+monthString+"-"+daysString;
	}

	@Override
	public void changePermitedEnterprise(PermitedEnterprise permitedEnterprise) {
	}

	@Override
	public void changeCustomsDeclarationHead(
			CustomsDeclarationHead selectedCustomsDeclarationHead) {
		this.selectedCustomsDeclarationHead=selectedCustomsDeclarationHead;
		isDirty=true;
	}
	

}
