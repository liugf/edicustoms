package com.gavin.ediCustoms.client.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binding.Converter;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ImportCustomsDeclarationPanel extends CustomsDeclarationPanel {

	private Button importButton;
	
	private Window importWindow;
	private ListStore<BeanModel> exportCustomsDeclarationHeadStore;
	
	private ImportPrintTabItem importPrintTabItem;
	private ImportCustomsDeclarationGoodTabItem customsDeclarationGoodTabItem;
	
	public ImportCustomsDeclarationPanel(
			ListStore<BeanModel> permitedEnterpriseStore) {
		super(permitedEnterpriseStore);
		isExport=false;
	}
	
	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		customsDeclarationHead.setText("进口表头");
		
		toolBar.add(new SeparatorToolItem());

		importButton = new Button("导入", Resources.ICONS.leadIn());
		//importButton.setVisible(false);
		importButton.disable();
		toolBar.add(importButton);
		importButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				getImportDialog().show();
			}
		});
		
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
							importButton.enable();
						} else {
							addButton.disable();
							importButton.disable();
						}
						printButton.disable();
						declareButton.disable();
					}
				});

		customsDeclarationGoodTabItem = new ImportCustomsDeclarationGoodTabItem();
		tabPanel.add(customsDeclarationGoodTabItem);
		addListener(customsDeclarationGoodTabItem);

		containerTabItem = new ContainerTabItem();
		tabPanel.add(containerTabItem);
		addListener(containerTabItem);
		
		transitInformationTabItem = new TransitInformationTabItem();
		tabPanel.add(transitInformationTabItem);
		addListener(transitInformationTabItem);
		
		PackingItemTabItem packingItemTabItem=new PackingItemTabItem();
		tabPanel.add(packingItemTabItem);
		addListener(packingItemTabItem);

		importPrintTabItem = new ImportPrintTabItem();
		tabPanel.add(importPrintTabItem);
		addListener(importPrintTabItem);

		add(tabPanel);
	}
	
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
		iePort.setFieldLabel("进口口岸");
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
		
		ComboBox<BeanModel> contractHeadId = new ComboBox<BeanModel>();
		contractHeadId.setTabIndex(11);
		//contractHeadId.setAllowBlank(false);
		contractHeadId.setName("contractHeadId");
		contractHeadId.setForceSelection(true);
		contractHeadId.setEmptyText("请选择...");
		contractHeadId.setDisplayField("manualNo");
		contractHeadId.setFieldLabel("备案号");
		contractHeadId.setStore(contractHeadStore);
		contractHeadId.setTypeAhead(true);
		contractHeadId.setTriggerAction(TriggerAction.ALL);
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
		tradeCountry.setFieldLabel("启运国");
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
		destinationPort.setFieldLabel("装货港");
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

		

		DateField importExportDate = new DateField();
		importExportDate.setTabIndex(16);
		importExportDate.setName("importExportDate");
		importExportDate.setAllowBlank(false);
		importExportDate.setFieldLabel("进口日期");
		right.add(importExportDate, formData);

		
		
		ComboBox<BeanModel> ownerDistrict = new ComboBox<BeanModel>();
		ownerDistrict.setTabIndex(17);
		ownerDistrict.setName("ownerDistrict");
		ownerDistrict.setForceSelection(true);
		ownerDistrict.setEmptyText("请选择...");
		ownerDistrict.setDisplayField("displayName");
		ownerDistrict.setFieldLabel("境内目的地");
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
		
		

		
		contractHeadId.addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<BeanModel>>() {
			@Override
			public void handleEvent(SelectionChangedEvent<BeanModel> be) {
				if (be.getSelectedItem()==null) {
					return;
				}
				if (isExport) {
					contractNo.setValue((String)be.getSelectedItem().get("exportContract"));
				} else {
					contractNo.setValue((String)be.getSelectedItem().get("importContract"));
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
		formBindings.getBinding(contractHeadId).setConverter(new Converter() {
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
					return contractHeadStore.findModel("id", value);
				} else {
					return null;
				}
			}
		});
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
				if (validate()) {
					refleshPreEntryId((Long) loadPort.getValue().get("id"));
				}else {
					MessageBox.info("提示", "进口日期不可迟于申报日期", null);
				}
				
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
				if (validate()){
					update();
				}else {
					MessageBox.info("提示", "进口日期不可迟于申报日期", null);
				}
				
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

	protected void save() {
		final AsyncCallback<Long> newCallback = new AsyncCallback<Long>() {
			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			@Override
			public void onSuccess(Long result) {
				if (result == 0) {
					MessageBox.alert("提醒", "已存在相同条目(单位代码相同)，添加失败", null);
				} else {
					CustomsDeclarationHead newItem = (CustomsDeclarationHead) beanModel
							.getBean();
					newItem.setId(result);
					store.insert(beanModel, 0);
					store.commitChanges();
					MessageBox.info("提示", "添加成功！", null);

					formPanel.setReadOnly(true);
					saveButton.setVisible(false);
					cancelButton.setVisible(false);
					updateButton.setVisible(true);
					updateButton.disable();
					resetButton.setVisible(true);
					resetButton.disable();
					if (permitedEnterprise.getCanAdd()) {
						addButton.enable();
						importButton.enable();
					} else {
						addButton.disable();
						importButton.disable();
					}

				}
			}
		};

		getBusinessService().saveCustomsDeclarationHead(
				(CustomsDeclarationHead) beanModel.getBean(), newCallback);
	}
	
	private Window getImportDialog() {
		if (importWindow != null) {
			refleshExportHeadStroe();
			return importWindow;
		}
		exportCustomsDeclarationHeadStore = new ListStore<BeanModel>();
		refleshExportHeadStroe();

		importWindow = new Window();
		importWindow.setIcon(Resources.ICONS.leadIn());
		importWindow.setHeading("从出口报关单导入");
		importWindow.setWidth(250);
		importWindow.setHeight(250);
		importWindow.setLayout(new FitLayout());

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		final CheckBoxSelectionModel<BeanModel> selectionMode = new CheckBoxSelectionModel<BeanModel>();
		selectionMode.setSelectionMode(SelectionMode.SINGLE);
		configs.add(selectionMode.getColumn());

		GridCellRenderer<BeanModel> dateRenderer = new GridCellRenderer<BeanModel>() {
			@Override
			public Object render(BeanModel model, String property,
					com.extjs.gxt.ui.client.widget.grid.ColumnData config,
					int rowIndex, int colIndex, ListStore<BeanModel> store,
					Grid<BeanModel> grid) {
				Date date = model.get(property);
				return date.toString().substring(0, 10);
			}
		};

		ColumnConfig column = new ColumnConfig();
		column.setId("declareTime");
		column.setRenderer(dateRenderer);
		column.setHeader("录入时间");
		column.setWidth(70);
		column.setRowHeader(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("preEntryId");
		column.setHeader("预录入号");
		column.setWidth(125);
		configs.add(column);

		final Grid<BeanModel> relativeCustomsDeclarationHeadGrid = new Grid<BeanModel>(
				exportCustomsDeclarationHeadStore, new ColumnModel(configs));
		relativeCustomsDeclarationHeadGrid.setStyleAttribute("borderTop", "none");
		relativeCustomsDeclarationHeadGrid.setSelectionModel(selectionMode);
		relativeCustomsDeclarationHeadGrid.addPlugin(selectionMode);

		importWindow.add(relativeCustomsDeclarationHeadGrid);

		importWindow.addButton(new Button("确定",
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						if (relativeCustomsDeclarationHeadGrid.getSelectionModel().getSelectedItems().size()==0) {
							return;
						}
						importCustomsDeclaration((Long)relativeCustomsDeclarationHeadGrid.getSelectionModel().getSelectedItems().get(0).get("id"));
						importWindow.hide();
						selectionMode.deselectAll();
					}
				}));
		importWindow.addButton(new Button("取消",
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						selectionMode.deselectAll();
						importWindow.hide();
					}
				}));
		importWindow.setFocusWidget(importWindow.getButtonBar().getItem(0));

		importWindow.setButtonAlign(HorizontalAlignment.CENTER);

		return importWindow;
	}

	private void refleshExportHeadStroe() {
		AsyncCallback<List<CustomsDeclarationHead>> getCallback = new AsyncCallback<List<CustomsDeclarationHead>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<CustomsDeclarationHead> result) {
				Collections.sort(result, new Comparator<CustomsDeclarationHead>() {
					public int compare(CustomsDeclarationHead arg0, CustomsDeclarationHead arg1) {
						if (arg0 == null) {
							return -1;
						}
						if (arg1 == null) {
							return 1;
						}
						return -arg0.getDeclareTime().compareTo(
								arg1.getDeclareTime());
					}
				});
				exportCustomsDeclarationHeadStore.removeAll();
				exportCustomsDeclarationHeadStore.add(BeanModelLookup.get()
						.getFactory(CustomsDeclarationHead.class).createModel(result));
			}
		};
		getBusinessService().listRelativeCustomsDeclarationHead(
				permitedEnterprise.getEnterpriseId(), getCallback);
	}

	private void importCustomsDeclaration(Long id) {
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(String result) {
				if (result == null) {
					reflashStores();
					MessageBox.info("提示", "导入成功,请检查并补全所需字段", null);
				} else {
					MessageBox.alert("提示", result, null);
				}
			}
		};
		if (id==null) {
			return;
		}
		getBusinessService().importCustomsDeclaration(
				permitedEnterprise.getEnterpriseId(), id, callback);
	}
	
	private boolean validate(){
		Date declareTime = formBindings.getModel().get("declareTime");
		Date importExportDate = formBindings.getModel().get("importExportDate");
		if (declareTime==null || importExportDate==null) {
			return false;
		}
		return !importExportDate.after(declareTime);
	}

}
