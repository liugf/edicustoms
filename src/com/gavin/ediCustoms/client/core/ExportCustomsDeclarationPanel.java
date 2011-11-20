package com.gavin.ediCustoms.client.core;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.binding.Converter;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.BindingEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;

public class ExportCustomsDeclarationPanel extends CustomsDeclarationPanel {

	private ExportPrintTabItem exportPrintTabItem;
	private ExportCustomsDeclarationGoodTabItem customsDeclarationGoodTabItem;
	//private PackingItemTabItem packingItemTabItem;
	
	public ExportCustomsDeclarationPanel(
			ListStore<BeanModel> permitedEnterpriseStore) {
		super(permitedEnterpriseStore);
	}
	
	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		customsDeclarationHead.setText("出口表头");
		permitedEnterpriseComboBox.addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<BeanModel>>() {

					@Override
					public void handleEvent(SelectionChangedEvent<BeanModel> be) {
						if (be.getSelectedItem() == null) {
							return;
						}
						permitedEnterprise = be.getSelectedItem().getBean();
						notifyPermitedEnterpriseChange(permitedEnterprise);
						
						reflashStores();
						refleshContractHeadStore();
						searchButton.enable();
						if (permitedEnterprise.getCanAdd()) {
							addButton.enable();
						} else {
							addButton.disable();
						}
						disableOperateButton();
					}

				});

		customsDeclarationGoodTabItem = new ExportCustomsDeclarationGoodTabItem();
		tabPanel.add(customsDeclarationGoodTabItem);
		addListener(customsDeclarationGoodTabItem);

		containerTabItem = new ContainerTabItem();
		tabPanel.add(containerTabItem);
		addListener(containerTabItem);
		
		transitInformationTabItem = new TransitInformationTabItem();
		tabPanel.add(transitInformationTabItem);
		addListener(transitInformationTabItem);

		deliveryRecordTabItem = new DeliveryRecordTabItem();
		tabPanel.add(deliveryRecordTabItem);
		addListener(deliveryRecordTabItem);

		PackingItemTabItem packingItemTabItem=new PackingItemTabItem();
		tabPanel.add(packingItemTabItem);
		addListener(packingItemTabItem);
		
		exportPrintTabItem = new ExportPrintTabItem();
		tabPanel.add(exportPrintTabItem);
		addListener(exportPrintTabItem);
		
		

		add(tabPanel);
	}
	
	@Override
	protected FormPanel createForm() {
		final FormPanel panel = new FormPanel();
		panel.setBodyBorder(false);
		panel.setHeaderVisible(false);
		panel.setLabelWidth(80);

		LayoutContainer upFrame = new LayoutContainer();
		upFrame.setLayout(new ColumnLayout());

		LayoutContainer left = new LayoutContainer();
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(80);
		left.setLayout(layout);

		LayoutContainer right = new LayoutContainer();
		layout = new FormLayout();
		layout.setLabelWidth(80);
		right.setLayout(layout);

		FormData formData = new FormData("-20");

		final ListStore<BeanModel> declareEnterpriseStore = getDeclareEnterpriseStore();
		final ListStore<BeanModel> tradeModeStore = getTradeModeStore();
		final ListStore<BeanModel> taxKindStore = getTaxKindStore();
		final ListStore<BeanModel> useageStore = getUseageStore();
		final ListStore<BeanModel> loadPortStore = getLoadPortStore();
		final ListStore<BeanModel> countryStore = getCountryStore();
		final ListStore<BeanModel> dealModeStore = getDealModeStore();
		final ListStore<BeanModel> customsStore = getCustomsStore();
		final ListStore<BeanModel> wrapTypeStore = getWrapTypeStore();
		final ListStore<BeanModel> transportModeStore = getTransportModeStore();
		final ListStore<BeanModel> portStore = getPortStore();
		final ListStore<BeanModel> districtStore = getDistrictStore();
		final ListStore<BeanModel> payWayStore = getPayWayStore();
		final ListStore<BeanModel> truckStore = getTruckStore();
		final ListStore<BeanModel> declarePropertyStore = getDeclarePropertyStore();
		attachmentStore = getAttachmentStore();
		
		preEntryId = new TextField<String>();
		preEntryId.setTabIndex(1);
		preEntryId.setName("preEntryId");
		preEntryId.setFieldLabel("预录入号");
		left.add(preEntryId, formData);
		
		electronicPort = new TextField<String>();
		electronicPort.setTabIndex(2);
		electronicPort.setName("electronicPort");
		electronicPort.setFieldLabel("电子口岸");
		left.add(electronicPort, formData);
		
		runEnterpriseStore = new ListStore<BeanModel>();
		ComboBox<BeanModel> runEnterpriseId = new ComboBox<BeanModel>();
		runEnterpriseId.setTabIndex(3);
		runEnterpriseId.setAllowBlank(false);
		runEnterpriseId.setName("runEnterpriseId");
		runEnterpriseId.setForceSelection(true);
		runEnterpriseId.setEmptyText("请选择...");
		runEnterpriseId.setDisplayField("displayName");
		runEnterpriseId.setFieldLabel("经营单位");
		runEnterpriseId.setStore(runEnterpriseStore);
		runEnterpriseId.setTypeAhead(true);
		runEnterpriseId.setTriggerAction(TriggerAction.ALL);
		runEnterpriseId.setWidth(50);
		left.add(runEnterpriseId, formData);
		
		ComboBox<BeanModel> declareEnterpriseId = new ComboBox<BeanModel>();
		declareEnterpriseId.setTabIndex(4);
		declareEnterpriseId.setAllowBlank(false);
		declareEnterpriseId.setName("declareEnterpriseId");
		declareEnterpriseId.setForceSelection(true);
		declareEnterpriseId.setEmptyText("请选择...");
		declareEnterpriseId.setDisplayField("displayName");
		declareEnterpriseId.setFieldLabel("报关单位");
		declareEnterpriseId.setStore(declareEnterpriseStore);
		declareEnterpriseId.setTypeAhead(true);
		declareEnterpriseId.setTriggerAction(TriggerAction.ALL);
		left.add(declareEnterpriseId, formData);	
		
		DateField declareTime = new DateField();
		declareTime.setTabIndex(5);
		declareTime.setName("declareTime");
		declareTime.setAllowBlank(false);
		declareTime.setFieldLabel("申报日期");
		left.add(declareTime, formData);
		
		ComboBox<BeanModel> iePort = new ComboBox<BeanModel>();
		iePort.setTabIndex(6);
		iePort.setAllowBlank(false);
		iePort.setName("iePort");
		iePort.setForceSelection(true);
		iePort.setEmptyText("请选择...");
		iePort.setDisplayField("displayName");
		iePort.setFieldLabel("出口口岸");
		iePort.setStore(customsStore);
		iePort.setTypeAhead(true);
		iePort.setTriggerAction(TriggerAction.ALL);
		left.add(iePort, formData);
		
		loadPort = new ComboBox<BeanModel>();
		loadPort.setTabIndex(7);
		loadPort.setName("loadPort");
		loadPort.setForceSelection(true);
		loadPort.setAllowBlank(false);
		loadPort.setEmptyText("请选择...");
		loadPort.setDisplayField("displayName");
		loadPort.setFieldLabel("装卸口岸");
		loadPort.setStore(loadPortStore);
		loadPort.setTypeAhead(true);
		loadPort.setTriggerAction(TriggerAction.ALL);
		left.add(loadPort, formData);
		
		ComboBox<BeanModel> transportMode = new ComboBox<BeanModel>();
		transportMode.setTabIndex(8);
		transportMode.setName("transportMode");
		transportMode.setForceSelection(true);
		transportMode.setEmptyText("请选择...");
		transportMode.setDisplayField("displayName");
		transportMode.setFieldLabel("运输方式");
		transportMode.setStore(transportModeStore);
		transportMode.setTypeAhead(true);
		transportMode.setTriggerAction(TriggerAction.ALL);
		left.add(transportMode, formData);
		
		TextField<String> transportTool = new TextField<String>();
		transportTool.setTabIndex(9);
		transportTool.setName("transportTool");
		transportTool.setFieldLabel("运输工具");
		left.add(transportTool, formData);
		
		final ComboBox<BeanModel> truck = new ComboBox<BeanModel>();
		truck.setTabIndex(10);
		truck.setName("truckCode");
		truck.setForceSelection(true);
		truck.setEmptyText("请选择...");
		truck.setDisplayField("displayName");
		truck.setFieldLabel("汽车编号");
		truck.setStore(truckStore);
		truck.setTypeAhead(true);
		truck.setTriggerAction(TriggerAction.ALL);
		left.add(truck, formData);
		
		TextField<String> billNo = new TextField<String>();
		billNo.setTabIndex(10);
		billNo.setName("billNo");
		billNo.setFieldLabel("提运单号");
		left.add(billNo, formData);
		
		truck.addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<BeanModel>>() {

			@Override
			public void handleEvent(SelectionChangedEvent<BeanModel> be) {
				if (be.getSelectedItem()==null) {
					return;
				}
				formBindings.getModel().set("billNo", be.getSelectedItem().get("truckNo"));
				formBindings.getModel().set("truckCode", be.getSelectedItem().get("code"));
				formBindings.getModel().set("meansOfTransportCode", be.getSelectedItem().get("code"));
				formBindings.getModel().set("meansOfTransportName", be.getSelectedItem().get("truckNo"));
				formBindings.getModel().set("billOfLadingNo", be.getSelectedItem().get("truckNo"));
				formBindings.getModel().set("localMeansOfTransportCode", be.getSelectedItem().get("code"));
				formBindings.getModel().set("localMeansOfTransportName", be.getSelectedItem().get("truckNo"));
				
			}			
		});
		
		final ComboBox<BeanModel> contractHead = new ComboBox<BeanModel>();
		contractHead.setTabIndex(11);
		//contractHead.setName("contractHeadId");
		contractHead.setForceSelection(true);
		contractHead.setEmptyText("请选择...");
		contractHead.setDisplayField("manualNo");
		contractHead.setFieldLabel("备案号");
		contractHead.setStore(contractHeadStore);
		contractHead.setTypeAhead(true);
		contractHead.setTriggerAction(TriggerAction.ALL);
		left.add(contractHead, formData);
		
		final NumberField contractHeadId = new NumberField();
		contractHeadId.setVisible(false);
		contractHeadId.setName("contractHeadId");
		contractHeadId.setPropertyEditorType(Long.class);
		left.add(contractHeadId, formData);
		
		ComboBox<BeanModel> tradeMode = new ComboBox<BeanModel>();
		tradeMode.setTabIndex(12);
		tradeMode.setName("tradeMode");
		tradeMode.setForceSelection(true);
		tradeMode.setEmptyText("请选择...");
		tradeMode.setDisplayField("displayName");
		tradeMode.setFieldLabel("贸易方式");
		tradeMode.setStore(tradeModeStore);
		tradeMode.setTypeAhead(true);
		tradeMode.setTriggerAction(TriggerAction.ALL);
		left.add(tradeMode, formData);
		
		ComboBox<BeanModel> taxKind = new ComboBox<BeanModel>();
		taxKind.setTabIndex(13);
		taxKind.setName("taxKind");
		taxKind.setForceSelection(true);
		taxKind.setEmptyText("请选择...");
		taxKind.setDisplayField("displayName");
		taxKind.setFieldLabel("征免性质");
		taxKind.setStore(taxKindStore);
		taxKind.setTypeAhead(true);
		taxKind.setTriggerAction(TriggerAction.ALL);
		left.add(taxKind, formData);
		
		ComboBox<BeanModel> tradeCountry = new ComboBox<BeanModel>();
		tradeCountry.setTabIndex(14);
		tradeCountry.setName("tradeCountry");
		tradeCountry.setForceSelection(true);
		tradeCountry.setEmptyText("请选择...");
		tradeCountry.setDisplayField("displayName");
		tradeCountry.setFieldLabel("运抵国");
		tradeCountry.setStore(countryStore);
		tradeCountry.setTypeAhead(true);
		tradeCountry.setTriggerAction(TriggerAction.ALL);
		left.add(tradeCountry, formData);
		
		ComboBox<BeanModel> destinationPort = new ComboBox<BeanModel>();
		destinationPort.setTabIndex(15);
		destinationPort.setName("destinationPort");
		destinationPort.setForceSelection(true);
		destinationPort.setEmptyText("请选择...");
		destinationPort.setDisplayField("displayName");
		destinationPort.setFieldLabel("指运港");
		destinationPort.setStore(portStore);
		destinationPort.setTypeAhead(true);
		destinationPort.setTriggerAction(TriggerAction.ALL);
		left.add(destinationPort, formData);
		
		
		TextField<String> entryId = new TextField<String>();
		entryId.setTabIndex(28);
		entryId.setName("entryId");
		entryId.setFieldLabel("报关单号");
		right.add(entryId, formData);
		
		
		ComboBox<BeanModel> declareProperty = new ComboBox<BeanModel>();
		declareProperty.setTabIndex(29);
		declareProperty.setName("declareProperty");
		declareProperty.setForceSelection(true);
		declareProperty.setEmptyText("请选择...");
		declareProperty.setDisplayField("displayName");
		declareProperty.setFieldLabel("报关形式");
		declareProperty.setStore(declarePropertyStore);
		declareProperty.setTypeAhead(true);
		declareProperty.setTriggerAction(TriggerAction.ALL);
		right.add(declareProperty, formData);
		
		// 保税仓号
		TextField<String> baoshuicang = new TextField<String>();
		baoshuicang.setTabIndex(30);
		baoshuicang.setName("baoshuicang");
		baoshuicang.setFieldLabel("保税仓号");
		right.add(baoshuicang, formData);
		
			
		
		
		TextField<String> declarant = new TextField<String>();
		declarant.setTabIndex(31);
		declarant.setName("declarant");
		declarant.setFieldLabel("报关员");
		right.add(declarant, formData);

		

		TextField<String> certificationCode = new TextField<String>();
		certificationCode.setTabIndex(16);
		certificationCode.setName("certificationCode");
		certificationCode.setFieldLabel("收汇核销单");
		right.add(certificationCode, formData);

		
		
		ComboBox<BeanModel> ownerDistrict = new ComboBox<BeanModel>();
		ownerDistrict.setTabIndex(17);
		ownerDistrict.setName("ownerDistrict");
		ownerDistrict.setForceSelection(true);
		ownerDistrict.setEmptyText("请选择...");
		ownerDistrict.setDisplayField("displayName");
		ownerDistrict.setFieldLabel("境内货源地");
		ownerDistrict.setStore(districtStore);
		ownerDistrict.setTypeAhead(true);
		ownerDistrict.setTriggerAction(TriggerAction.ALL);
		right.add(ownerDistrict, formData);
		
		
		
		
		ComboBox<BeanModel> useage = new ComboBox<BeanModel>();
		useage.setTabIndex(18);
		useage.setName("useage");
		useage.setForceSelection(true);
		useage.setEmptyText("请选择...");
		useage.setDisplayField("displayName");
		useage.setFieldLabel("用途");
		useage.setStore(useageStore);
		useage.setTypeAhead(true);
		useage.setTriggerAction(TriggerAction.ALL);
		right.add(useage, formData);
		
		ComboBox<BeanModel> payWay = new ComboBox<BeanModel>();
		payWay.setTabIndex(19);
		payWay.setName("payWay");
		payWay.setForceSelection(true);
		payWay.setEmptyText("请选择...");
		payWay.setDisplayField("displayName");
		payWay.setFieldLabel("结汇方式");
		payWay.setStore(payWayStore);
		payWay.setTypeAhead(true);
		payWay.setTriggerAction(TriggerAction.ALL);
		right.add(payWay, formData);
		
		
		
		
		certMark = new TextField<String>();
		certMark.setTabIndex(20);
		certMark.setName("certMark");
		certMark.setFieldLabel("随附单证");
		right.add(certMark, formData);

		certMark.addListener(Events.Focus, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				getAttachmentWindow(false).show();
				initAttachmentFormpanel();
				getAttachmentWindow(false).focus();
			}
		});
		
		
		
		dealMode = new ComboBox<BeanModel>();
		dealMode.setTabIndex(21);
		dealMode.setName("dealMode");
		dealMode.setForceSelection(true);
		dealMode.setEmptyText("请选择...");
		dealMode.setDisplayField("displayName");
		dealMode.setFieldLabel("成交方式");
		dealMode.setStore(dealModeStore);
		dealMode.setTypeAhead(true);
		dealMode.setTriggerAction(TriggerAction.ALL);
		right.add(dealMode, formData);
		
		
		
		ComboBox<BeanModel> wrapType = new ComboBox<BeanModel>();
		wrapType.setTabIndex(22);
		wrapType.setName("wrapType");
		wrapType.setForceSelection(true);
		wrapType.setEmptyText("请选择...");
		wrapType.setDisplayField("displayName");
		wrapType.setFieldLabel("包装种类");
		wrapType.setStore(wrapTypeStore);
		wrapType.setTypeAhead(true);
		wrapType.setTriggerAction(TriggerAction.ALL);
		wrapType.setWidth(50);
		right.add(wrapType, formData);
		
		
		
		final TextField<String> contractNo = new TextField<String>();
		contractNo.setTabIndex(23);
		contractNo.setName("contractNo");
		contractNo.setFieldLabel("合同号");
		right.add(contractNo, formData);
		
		
		
		NumberField packNo = new NumberField();
		packNo.setTabIndex(24);
		packNo.setPropertyEditorType(Integer.class);
		packNo.setName("packNo");
		packNo.setFieldLabel("件数");
		right.add(packNo, formData);
		
		
		
		NumberField grossWeight = new NumberField();
		grossWeight.setTabIndex(25);
		grossWeight.setPropertyEditorType(Double.class);
		grossWeight.setFormat(NumberFormat.getFormat("#0.0000"));
		grossWeight.setName("grossWeight");
		grossWeight.setFieldLabel("毛重");
		right.add(grossWeight, formData);
		
		
		
		NumberField netWeight = new NumberField();
		netWeight.setTabIndex(26);
		netWeight.setPropertyEditorType(Double.class);
		netWeight.setFormat(NumberFormat.getFormat("#0.0000"));
		netWeight.setName("netWeight");
		netWeight.setFieldLabel("净重");
		right.add(netWeight, formData);
		
		TextField<String> note = new TextField<String>();
		note.setTabIndex(27);
		note.setName("note");
		note.setFieldLabel("备注");
		right.add(note, formData);
		

		upFrame.add(left, new ColumnData(.5));
		upFrame.add(right, new ColumnData(.5));

		panel.add(upFrame, new FormData(500, 200));

		
		contractHead.addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<BeanModel>>() {
			@Override
			public void handleEvent(SelectionChangedEvent<BeanModel> be) {
				if (be.getSelectedItem()==null) {
					return;
				}
				formBindings.getModel().set("contractHeadId", (Long)be.getSelectedItem().get("id"));
				if (isExport) {
					String contractString=(String)be.getSelectedItem().get("exportContract");
					if (contractString!=null && !contractString.trim().equals("")) {
						formBindings.getModel().set("contractNo", contractString);
					}
				} else {
					String contractString=(String)be.getSelectedItem().get("importContract");
					if (contractString!=null && !contractString.trim().equals("")) {
						formBindings.getModel().set("contractNo", contractString);
					}
					
				}
				
			}
		});

		certMark.addListener(Events.Focus, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				getAttachmentWindow(false).show();
				initAttachmentFormpanel();
				getAttachmentWindow(false).focus();
			}
		});


		formBindings = new FormBinding(panel, true);
		/*
		 * formBindings.addListener(Events.Bind, new Listener<BindingEvent>() {
		 * 
		 * @Override public void handleEvent(BindingEvent be) { if
		 * (formBindings.getModel().get("declareEnterpriseId")==null) { return;
		 * } refleshDeclarantStore((Long)
		 * formBindings.getModel().get("declareEnterpriseId")); } });
		 */
		
		
		formBindings.addListener(Events.Bind,
				new Listener<BindingEvent>() {
					@Override
					public void handleEvent(BindingEvent be) {
						contractHead.setValue(contractHeadStore.findModel("id", formBindings.getModel().get("contractHeadId")));
					}
				});

		formBindings.getBinding(iePort).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return customsStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(declareProperty).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return declarePropertyStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		/*formBindings.getBinding(contractHead).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("id");
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return contractHeadStore.findModel("id", value);
				} else {
					return null;
				}
			}
		});*/
		formBindings.getBinding(runEnterpriseId).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("id");
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return runEnterpriseStore.findModel("id", value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(transportMode).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return transportModeStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(loadPort).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return loadPortStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(truck).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return truckStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(destinationPort).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return portStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(ownerDistrict).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return districtStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(declareEnterpriseId).setConverter(
				new Converter() {
					@Override
public Object convertFieldValue(Object value) {
						if (value == null) {
							return null;
						}
						return ((BeanModel) value).get("id");
					}

					@Override
					public Object convertModelValue(Object value) {
						if (value != null) {
							return declareEnterpriseStore
									.findModel("id", value);
						} else {
							return null;
						}
					}
				});
		formBindings.getBinding(tradeCountry).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return countryStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(tradeMode).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return tradeModeStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(taxKind).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return taxKindStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(useage).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return useageStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(dealMode).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return dealModeStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(payWay).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return payWayStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(wrapType).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
				if (value == null) {
					return null;
				}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return wrapTypeStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		

		formBindings.setStore((Store<BeanModel>) grid.getStore());

		panel.setReadOnly(true);

		panel.setButtonAlign(HorizontalAlignment.CENTER);

		saveButton = new Button("保存");
		saveButton.setTabIndex(41);
		saveButton.setVisible(false);
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!panel.isValid())
					return;
				refleshPreEntryId((Long) loadPort.getValue().get("id"));
			}
		});
		panel.addButton(saveButton);

		cancelButton = new Button("取消");
		cancelButton.setTabIndex(42);
		cancelButton.setVisible(false);
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				resetState();
			}
		});
		panel.addButton(cancelButton);

		updateButton = new Button("更新");
		updateButton.setTabIndex(41);
		updateButton.disable();
		updateButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!panel.isValid())
					return;
				update();
			}
		});
		panel.addButton(updateButton);

		resetButton = new Button("取消");
		resetButton.setTabIndex(42);
		resetButton.disable();
		resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				store.rejectChanges();
				resetState();
			}
		});
		panel.addButton(resetButton);

		panel.setBorders(false);
		panel.setBodyBorder(false);

		return panel;
	}


}
