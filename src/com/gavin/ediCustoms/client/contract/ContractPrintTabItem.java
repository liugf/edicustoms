package com.gavin.ediCustoms.client.contract;


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
import com.extjs.gxt.ui.client.event.Events;
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
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.gavin.ediCustoms.client.myUI.MyTabItem;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.ForeignEnterprise;
import com.gavin.ediCustoms.entity.edi.contract.ContractHead;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.dictionary.DealMode;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ContractPrintTabItem extends MyTabItem {
	
	private FormPanel formPanel;	
	
	private Enterprise enterprise;
	private ForeignEnterprise foreignEnterprise;
	private ContractHead contractHead;
	
	private ComboBox<BeanModel> foreignEnterpriseComboBox;
	private ComboBox<BeanModel> enterpriseComboBox;
	private ComboBox<BeanModel> contractHeadComboBox;

	public ContractPrintTabItem() {
		setText("打印合同");	
		enterprise=new Enterprise();
		foreignEnterprise=new ForeignEnterprise();
		contractHead=new ContractHead();
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
		
	    
		FieldSet fieldSet = new FieldSet();  
		fieldSet.setHeading("外商公司情况"); 
		FormLayout layout = new FormLayout();  
	    layout.setLabelWidth(75); 
	    layout.setDefaultWidth(300);
	    fieldSet.setLayout(layout); 
	    
	    
	    
	    final ListStore<BeanModel> foreignEnterpriseStore = getForeignEnterpriseStore();
		foreignEnterpriseComboBox = new ComboBox<BeanModel>();
		foreignEnterpriseComboBox.setTabIndex(1);
		foreignEnterpriseComboBox.setForceSelection(true);
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
				registeName_f.setValue(be.getSelectedItem().get("registeName").toString());
				address_f.setValue(be.getSelectedItem().get("address").toString());
				telephone_f.setValue(be.getSelectedItem().get("telephone").toString());
				fax_f.setValue(be.getSelectedItem().get("fax").toString());
			}
		});
		
		
		
		
		fieldSet.add(foreignEnterpriseComboBox);
		fieldSet.add(registeName_f);
		fieldSet.add(address_f);
		fieldSet.add(telephone_f);
		fieldSet.add(fax_f);
		panel.add(fieldSet);
		
		fieldSet = new FieldSet();  
		fieldSet.setHeading("发货单位情况"); 
		layout = new FormLayout();  
	    layout.setLabelWidth(75); 
	    layout.setDefaultWidth(300);
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
		panel.add(fieldSet);
		
		
		
		fieldSet = new FieldSet();
		fieldSet.setHeading("其他信息"); 
		layout = new FormLayout();  
	    layout.setLabelWidth(75); 
	    layout.setDefaultWidth(443);
		fieldSet.setLayout(layout); 
		
		
		
		contractHeadComboBox = new ComboBox<BeanModel>();
		contractHeadComboBox.setStore(new ListStore<BeanModel>());
		contractHeadComboBox.setValue(BeanModelLookup.get().getFactory(CustomsDeclarationHead.class).createModel(contractHead));
		
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
				
		final NumberField contractHeadId=new NumberField();		
		contractHeadId.setPropertyEditorType(Long.class);
		contractHeadId.setName("id");
		contractHeadId.setVisible(false);
		panel.add(contractHeadId);
		
		final TextField<String> contractNo = new TextField<String>();
		contractNo.setTabIndex(9);
		contractNo.setName("exportContract");
		contractNo.setFieldLabel("合同号码");
		left.add(contractNo);
		
		final DateField typeDate = new DateField();
		typeDate.setTabIndex(10);
		typeDate.setName("typeDate");
		typeDate.setAllowBlank(false);
		typeDate.setFieldLabel("日期");
		middle.add(typeDate);
		
		TextField<String> signedIn = new TextField<String>();
		signedIn.setTabIndex(11);
		signedIn.setName("signedIn");
		signedIn.setFieldLabel("签定地");
		right.add(signedIn);
		
		final TextField<String> dealMode = new TextField<String>();
		dealMode.setTabIndex(12);
		dealMode.setName("dealMode");
		dealMode.setFieldLabel("成交方式");
		left.add(dealMode);
		
		NumberField balance = new NumberField();
		balance.setTabIndex(13);
		balance.setAllowBlank(false);
		balance.setPropertyEditorType(Double.class);
		balance.setName("balance");
		balance.setFormat(NumberFormat.getFormat("#0.0000"));
		balance.setFieldLabel("差额百分比");
		balance.setValue(0);
		middle.add(balance);
		
		TextField<String> portOfShipment = new TextField<String>();
		portOfShipment.setTabIndex(14);
		portOfShipment.setName("portOfShipment");
		portOfShipment.setFieldLabel("装运口岸");
		right.add(portOfShipment);
		
		TextField<String> portOfDestination = new TextField<String>();
		portOfDestination.setTabIndex(15);
		portOfDestination.setName("portOfDestination");
		portOfDestination.setFieldLabel("目的口岸");
		left.add(portOfDestination);
		
		final DateField exportDeadline = new DateField();
		exportDeadline.setTabIndex(16);
		exportDeadline.setName("exportDeadline");
		exportDeadline.setAllowBlank(false);
		exportDeadline.setFieldLabel("出口期限");
		middle.add(exportDeadline);
		
		final DateField importDeadline = new DateField();
		importDeadline.setTabIndex(17);
		importDeadline.setName("importDeadline");
		importDeadline.setAllowBlank(false);
		importDeadline.setFieldLabel("进口期限");
		right.add(importDeadline);

		
		
		TextField<String> shipingMark = new TextField<String>();
		shipingMark.setTabIndex(18);
		shipingMark.setName("shipingMark");
		shipingMark.setValue("N/M");
		shipingMark.setFieldLabel("转运标记");
		left.add(shipingMark);	
		
		TextField<String> manufactureCountry = new TextField<String>();
		manufactureCountry.setTabIndex(19);
		manufactureCountry.setName("manufactureCountry");
		manufactureCountry.setFieldLabel("生产国别");
		manufactureCountry.setValue("中国");
		middle.add(manufactureCountry);	
		
		TextField<String> insurance = new TextField<String>();
		insurance.setTabIndex(20);
		insurance.setName("insurance");
		insurance.setFieldLabel("保险");
		insurance.setValue("由买方负责");
		right.add(insurance);
		
		
		frame.add(left, new ColumnData(.333333333));
		frame.add(middle, new ColumnData(.333333333));	
		frame.add(right, new ColumnData(.333333333));		
		fieldSet.add(frame);	
		
		TextField<String> termsOfPayment = new TextField<String>();
		termsOfPayment.setTabIndex(21);
		termsOfPayment.setName("termsOfPayment");
		termsOfPayment.setFieldLabel("付款条件");
		termsOfPayment.setValue("T/T 先出后结，可以分批出口，按实际出口成交数量月结九十天结清。");
		fieldSet.add(termsOfPayment);
		
		
		panel.add(fieldSet);		
		
		final AsyncCallback<DealMode> dealModeCallback = new AsyncCallback<DealMode>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(DealMode result) {
				dealMode.setValue(result.getName());
			}

		};
		
		contractHeadComboBox.addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<BeanModel>>() {
			@Override
			public void handleEvent(SelectionChangedEvent<BeanModel> be) {
				if (be.getSelectedItem()==null) {
					return;
				}
				contractHeadId.setValue(new Long(be.getSelectedItem().get("id").toString()));
				contractNo.setValue(be.getSelectedItem().get("importContract").toString());	
				typeDate.setValue((Date)be.getSelectedItem().get("typeDate"));
				importDeadline.setValue((Date)be.getSelectedItem().get("importDeadline"));
				exportDeadline.setValue((Date)be.getSelectedItem().get("exportDeadline"));
				
				getDictionaryService().getDealMode(be.getSelectedItem().get("dealMode").toString(), dealModeCallback);

			}
		});
		
		
		
		Button printButton1= new Button("打印合同",Resources.ICONS.printer());
		printButton1.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void componentSelected(ButtonEvent ce) {
				Map<String, Object> paramsMap=new HashMap<String, Object>();
				for(Field field:formPanel.getFields()){
					if (field.getName()!=null) {
						String key=field.getName();
						if (key.equals("typeDate")||key.equals("exportDeadline")||key.equals("importDeadline")) {
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
		
		
		panel.addButton(printButton1);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		
		
		return panel;
	}

	public void reflashStores(Long customsDeclarationHeadId){
			
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
				foreignEnterprise=result;
				if (formPanel!=null) {
					foreignEnterpriseComboBox.setValue(BeanModelLookup.get().getFactory(ForeignEnterprise.class).createModel(foreignEnterprise));
				}
			}

		};
		
		AsyncCallback<ContractHead> contractHeadCallback = new AsyncCallback<ContractHead>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(ContractHead result) {	
				contractHead=result;
				if (formPanel!=null) {
					contractHeadComboBox.setValue(BeanModelLookup.get().getFactory(ContractHead.class).createModel(contractHead));
				}
				getSystemService().getEnterprise(result.getOwnerId(), enterpriseCallback);
				getSystemService().getForeignEnterprise(result.getForeignEnterpriseId(), foreignEnterpriseCallback);				
			}
		};
		getBusinessService().getContractHead(customsDeclarationHeadId, contractHeadCallback);
			
		
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


	

}
