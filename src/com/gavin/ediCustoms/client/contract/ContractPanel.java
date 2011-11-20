package com.gavin.ediCustoms.client.contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.binding.Converter;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.BindingEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.ForeignEnterprise;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.entity.edi.contract.ContractHead;
import com.gavin.ediCustoms.entity.edi.dictionary.BringInMode;
import com.gavin.ediCustoms.entity.edi.dictionary.Country;
import com.gavin.ediCustoms.entity.edi.dictionary.Currency;
import com.gavin.ediCustoms.entity.edi.dictionary.Customs;
import com.gavin.ediCustoms.entity.edi.dictionary.DealMode;
import com.gavin.ediCustoms.entity.edi.dictionary.InvestMode;
import com.gavin.ediCustoms.entity.edi.dictionary.ProcessType;
import com.gavin.ediCustoms.entity.edi.dictionary.TaxKind;
import com.gavin.ediCustoms.entity.edi.dictionary.TradeMode;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ContractPanel extends MyLayoutContainer {
	private ListStore<BeanModel> permitedEnterpriseStore;
	private ListStore<BeanModel> runEnterpriseStore;
	private ListStore<BeanModel> store;
	private FormBinding formBindings;
	private BeanModelFactory beanModelFactory;
	private BeanModel beanModel;

	private Button saveButton;
	private Button cancelButton;

	private Button updateButton;
	private Button resetButton;

	private Button editButton;
	private Button addButton;
	private Button deleteButton;
	private Button importButton;

	private Window importWindow;

	private FormPanel formPanel;
	private Grid<BeanModel> grid;

	private StoreFilterField<BeanModel> filter;
	private PermitedEnterprise permitedEnterprise;

	private ContractProductTabItem contractProductTabItem;
	private ContractMaterialTabItem contractMaterialTabItem;
	private ContractConsumeTabItem contractConsumeTabItem;
	private ContractBalanceTabItem contractBalanceTabItem;
	private ContractProcessFeeTabItem contractProcessFeeTabItem;

	public ContractPanel(ListStore<BeanModel> permitedEnterpriseStore) {
		this.permitedEnterpriseStore = permitedEnterpriseStore;
		beanModelFactory = BeanModelLookup.get().getFactory(ContractHead.class);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());

		TabPanel tabPanel = new TabPanel();

		TabItem contractHead = new TabItem("合同头");
		contractHead.setLayout(new FitLayout());

		ContentPanel content = new ContentPanel();
		content.setHeaderVisible(false);
		content.setBodyBorder(false);
		content.setLayout(new FitLayout());

		LayoutContainer frame = new LayoutContainer();
		frame.setStyleAttribute("padding", "10px");
		frame.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame.setLayout(new BorderLayout());

		// filter
		filter = new StoreFilterField<BeanModel>() {
			@Override
			protected boolean doSelect(Store<BeanModel> store,
					BeanModel parent, BeanModel record, String property,
					String filter) {
				String typeDate = record.get("typeDate").toString()
						.toLowerCase();
				String preManualNo = record.get("preManualNo").toString()
						.toLowerCase();
				if (typeDate.indexOf(filter.toLowerCase()) != -1
						|| preManualNo.indexOf(filter.toLowerCase()) != -1) {
					return true;
				}
				return false;
			}
		};

		store = new ListStore<BeanModel>();
		store.sort("no", Style.SortDir.ASC);
		filter.bind(store);

		grid = createGrid();
		grid.getSelectionModel().addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<BeanModel>>() {
					public void handleEvent(SelectionChangedEvent<BeanModel> be) {
						store.rejectChanges();
						resetState();
					}
				});

		formPanel = createForm();

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 164);
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);

		frame.add(grid, westData);
		frame.add(formPanel, centerData);

		content.add(frame);

		// 添加工具栏
		ToolBar toolBar = new ToolBar();

		toolBar.add(new LabelToolItem("发货单位："));
		ComboBox<BeanModel> permitedEnterpriseComboBox = new ComboBox<BeanModel>();
		permitedEnterpriseComboBox.setWidth(250);
		permitedEnterpriseComboBox.setEmptyText("请选择...");
		permitedEnterpriseComboBox.setDisplayField("displayName");
		permitedEnterpriseComboBox.setStore(permitedEnterpriseStore);
		permitedEnterpriseComboBox.setTypeAhead(true);
		permitedEnterpriseComboBox.setTriggerAction(TriggerAction.ALL);

		permitedEnterpriseComboBox.addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<BeanModel>>() {

					@Override
					public void handleEvent(SelectionChangedEvent<BeanModel> be) {
						if (be.getSelectedItem() == null) {
							return;
						}
						permitedEnterprise = be.getSelectedItem().getBean();
						contractProductTabItem
								.setPermitedEnterprise(permitedEnterprise);
						contractProductTabItem.refreshVoucherStore();
						contractMaterialTabItem
								.setPermitedEnterprise(permitedEnterprise);
						contractMaterialTabItem.refleshVoucherStore();
						contractConsumeTabItem
								.setPermitedEnterprise(permitedEnterprise);
						reflashStores();
						if (permitedEnterprise.getCanAdd()) {
							addButton.enable();
							importButton.enable();
						} else {
							addButton.disable();
							importButton.disable();
						}
						editButton.disable();
						deleteButton.disable();

					}

				});
		toolBar.add(permitedEnterpriseComboBox);

		toolBar.add(new SeparatorToolItem());

		toolBar.add(new LabelToolItem("搜索："));
		toolBar.add(filter);

		toolBar.add(new SeparatorToolItem());

		importButton = new Button("从Excel导入", Resources.ICONS.leadIn());
		importButton.disable();
		toolBar.add(importButton);
		importButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				getImportDialog().show();
			}
		});

		toolBar.add(new SeparatorToolItem());

		addButton = new Button("添加", Resources.ICONS.add());
		addButton.disable();
		importButton.disable();
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				beanModel = beanModelFactory.createModel(new ContractHead());
				formPanel.reset();
				formBindings.bind(beanModel);
				formBindings.addListener(Events.Bind,
						new Listener<BindingEvent>() {
							@Override
							public void handleEvent(BindingEvent be) {
								formBindings.getModel().set("ownerId",
										permitedEnterprise.getEnterpriseId());
								for (Field<?> field : formPanel.getFields()) {
									field.clearInvalid();
								}
								formBindings.removeAllListeners();

							}
						});
				formPanel.setReadOnly(false);
				saveButton.setVisible(true);
				cancelButton.setVisible(true);
				updateButton.setVisible(false);
				resetButton.setVisible(false);
				addButton.disable();
				importButton.disable();

			}
		});
		toolBar.add(addButton);

		toolBar.add(new SeparatorToolItem());

		editButton = new Button("修改", Resources.ICONS.edit());
		editButton.disable();
		editButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (grid.getSelectionModel().getSelection().size() > 0) {
					formBindings.bind((BeanModel) grid.getSelectionModel()
							.getSelection().get(0));
					formPanel.setReadOnly(false);
					updateButton.enable();
					resetButton.enable();
					editButton.disable();
				} else {
					formBindings.unbind();
				}
			}
		});
		toolBar.add(editButton);

		toolBar.add(new SeparatorToolItem());

		deleteButton = new Button("删除", Resources.ICONS.delete());
		deleteButton.disable();
		toolBar.add(deleteButton);
		deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				MessageBox.confirm("确认", "确定要删除选中的条目吗？",
						new Listener<MessageBoxEvent>() {
							@Override
							public void handleEvent(MessageBoxEvent be) {
								if (be.getButtonClicked().getText() == GXT.MESSAGES
										.messageBox_yes()) {
									delete();
								}
							}
						});
			}
		});

		toolBar.add(new SeparatorToolItem());		

		content.setTopComponent(toolBar);

		contractHead.add(content);
		tabPanel.add(contractHead);

		contractProductTabItem = new ContractProductTabItem();
		contractProductTabItem.addListener(Events.Select,
				new Listener<ComponentEvent>() {
					public void handleEvent(ComponentEvent be) {
						if (grid.getSelectionModel().getSelection().size() > 0
								&& permitedEnterprise != null) {
							ContractHead selectedContractHead = grid
									.getSelectionModel().getSelection().get(0)
									.getBean();
							contractProductTabItem
									.reflashStore(selectedContractHead.getId());
							contractProductTabItem.resetState();
						} else {
							MessageBox.alert("提示", "请选择合同头", null);
						}
					}
				});
		tabPanel.add(contractProductTabItem);

		contractMaterialTabItem = new ContractMaterialTabItem();
		contractMaterialTabItem.addListener(Events.Select,
				new Listener<ComponentEvent>() {
					public void handleEvent(ComponentEvent be) {
						if (grid.getSelectionModel().getSelection().size() > 0
								&& permitedEnterprise != null) {
							ContractHead selectedContractHead = grid
									.getSelectionModel().getSelection().get(0)
									.getBean();
							contractMaterialTabItem
									.reflashStore(selectedContractHead.getId());
							contractMaterialTabItem.resetState();
						} else {
							MessageBox.alert("提示", "请选择合同头", null);
						}
					}
				});
		tabPanel.add(contractMaterialTabItem);

		contractConsumeTabItem = new ContractConsumeTabItem();
		contractConsumeTabItem.addListener(Events.Select,
				new Listener<ComponentEvent>() {
					public void handleEvent(ComponentEvent be) {
						if (grid.getSelectionModel().getSelection().size() > 0
								&& permitedEnterprise != null) {
							ContractHead selectedContractHead = grid
									.getSelectionModel().getSelection().get(0)
									.getBean();
							contractConsumeTabItem
									.refleshProductAndMaterialStore(selectedContractHead
											.getId());

						} else {
							MessageBox.alert("提示", "请选择合同头", null);
						}
					}
				});
		tabPanel.add(contractConsumeTabItem);

		contractBalanceTabItem = new ContractBalanceTabItem();
		contractBalanceTabItem.addListener(Events.Select,
				new Listener<ComponentEvent>() {
					public void handleEvent(ComponentEvent be) {
						if (grid.getSelectionModel().getSelection().size() > 0
								&& permitedEnterprise != null) {
							ContractHead selectedContractHead = grid
									.getSelectionModel().getSelection().get(0)
									.getBean();
							contractBalanceTabItem
									.refleshStores(selectedContractHead.getId());

						} else {
							MessageBox.alert("提示", "请选择合同头", null);
						}
					}
				});
		tabPanel.add(contractBalanceTabItem);

		contractProcessFeeTabItem = new ContractProcessFeeTabItem();
		contractProcessFeeTabItem.addListener(Events.Select,
				new Listener<ComponentEvent>() {
					public void handleEvent(ComponentEvent be) {
						if (grid.getSelectionModel().getSelection().size() > 0
								&& permitedEnterprise != null) {
							ContractHead selectedContractHead = grid
									.getSelectionModel().getSelection().get(0)
									.getBean();
							contractProcessFeeTabItem
									.refleshStore(selectedContractHead.getId());
						} else {
							MessageBox.alert("提示", "请选择合同头", null);
						}
					}
				});
		tabPanel.add(contractProcessFeeTabItem);
		
		
		

		add(tabPanel);
	}

	private void reflashStores() {
		AsyncCallback<List<ContractHead>> getCallback = new AsyncCallback<List<ContractHead>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ContractHead> result) {
				Collections.sort(result, new Comparator<ContractHead>() {
					public int compare(ContractHead arg0, ContractHead arg1) {
						if (arg0 == null) {
							return -1;
						}
						if (arg1 == null) {
							return 1;
						}
						if (arg0.getTypeDate()!=null && arg1.getTypeDate()!=null) {
							return -arg0.getTypeDate()
							.compareTo(arg1.getTypeDate());
						}
						return 0;
					}
				});
				store.removeAll();
				store.add(beanModelFactory.createModel(result));
			}
		};
		getBusinessService().listContractHead(
				permitedEnterprise.getEnterpriseId(), getCallback);

		AsyncCallback<List<Enterprise>> callback = new AsyncCallback<List<Enterprise>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<Enterprise> result) {
				Collections.sort(result, new Comparator<Enterprise>() {
					public int compare(Enterprise arg0, Enterprise arg1) {
						return arg0.getTradeCode().compareTo(
								arg1.getTradeCode());
					}
				});
				runEnterpriseStore.removeAll();
				BeanModelFactory bmf = BeanModelLookup.get().getFactory(
						Enterprise.class);
				for (Enterprise enterprise : result) {
					BeanModel bm = bmf.createModel(enterprise);
					bm.set("displayName", bm.get("tradeCode").toString()
							+ bm.get("registeName").toString());
					runEnterpriseStore.add(bm);
				}
			}
		};
		getSystemService().listRunEnterprise(
				permitedEnterprise.getEnterpriseId(), callback);
	}

	private Grid<BeanModel> createGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();

		GridCellRenderer<BeanModel> dateRenderer = new GridCellRenderer<BeanModel>() {
			@Override
			public Object render(BeanModel model, String property,
					com.extjs.gxt.ui.client.widget.grid.ColumnData config,
					int rowIndex, int colIndex, ListStore<BeanModel> store,
					Grid<BeanModel> grid) {				
				Date date = model.get(property);
				if (date==null) {
					return null;
				}
				return date.toString().substring(0, 10);
			}
		};

		column.setId("typeDate");
		column.setRenderer(dateRenderer);
		column.setHeader("填写日期");
		column.setWidth(70);
		column.setRowHeader(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("manualNo");
		column.setHeader("手册编号");
		column.setWidth(90);
		configs.add(column);

		store.setMonitorChanges(true);
		Grid<BeanModel> grid = new Grid<BeanModel>(store, new ColumnModel(
				configs));
		grid.setBorders(true);
		return grid;
	}

	private FormPanel createForm() {
		final FormPanel panel = new FormPanel();
		panel.setBodyBorder(false);
		panel.setHeaderVisible(false);

		LayoutContainer main = new LayoutContainer();
		main.setLayout(new ColumnLayout());

		LayoutContainer left = new LayoutContainer();
		left.setStyleAttribute("paddingRight", "10px");
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(65);
		left.setLayout(layout);

		LayoutContainer right = new LayoutContainer();
		right.setStyleAttribute("paddingLeft", "10px");
		layout = new FormLayout();
		layout.setLabelWidth(65);
		right.setLayout(layout);

		FormData formData = new FormData("-20");

		runEnterpriseStore = new ListStore<BeanModel>();
		ComboBox<BeanModel> runEnterprise = new ComboBox<BeanModel>();
		runEnterprise.setAllowBlank(false);
		runEnterprise.setName("runEnterpriseId");
		runEnterprise.setForceSelection(true);
		runEnterprise.setEmptyText("请选择...");
		runEnterprise.setDisplayField("displayName");
		runEnterprise.setFieldLabel("经营单位");
		runEnterprise.setStore(runEnterpriseStore);
		runEnterprise.setTypeAhead(true);
		runEnterprise.setTriggerAction(TriggerAction.ALL);
		runEnterprise.setWidth(50);
		left.add(runEnterprise, formData);

		final ListStore<BeanModel> foreignEnterpriseStore = getForeignEnterpriseStore();
		ComboBox<BeanModel> foreignEnterprise = new ComboBox<BeanModel>();
		foreignEnterprise.setName("foreignEnterpriseId");
		foreignEnterprise.setForceSelection(true);
		foreignEnterprise.setEmptyText("请选择...");
		foreignEnterprise.setDisplayField("registeName");
		foreignEnterprise.setFieldLabel("外商公司");
		foreignEnterprise.setStore(foreignEnterpriseStore);
		foreignEnterprise.setTypeAhead(true);
		foreignEnterprise.setTriggerAction(TriggerAction.ALL);
		right.add(foreignEnterprise, formData);

		TextField<String> manualNo = new TextField<String>();
		manualNo.setName("manualNo");
		manualNo.setFieldLabel("手册编号");
		manualNo.setAllowBlank(false);
		left.add(manualNo, formData);

		TextField<String> preManualNo = new TextField<String>();
		preManualNo.setName("preManualNo");
		preManualNo.setFieldLabel("预录入号");
		right.add(preManualNo, formData);

		final ListStore<BeanModel> tradeModeStore = getTradeModeStore();
		ComboBox<BeanModel> tradeMode = new ComboBox<BeanModel>();
		tradeMode.setForceSelection(true);
		tradeMode.setAllowBlank(false);
		tradeMode.setName("tradeMode");
		tradeMode.setEmptyText("请选择...");
		tradeMode.setDisplayField("displayName");
		tradeMode.setFieldLabel("贸易方式");
		tradeMode.setStore(tradeModeStore);
		tradeMode.setTypeAhead(true);
		tradeMode.setTriggerAction(TriggerAction.ALL);
		left.add(tradeMode, formData);

		final ListStore<BeanModel> taxKindStore = getTaxKindStore();
		ComboBox<BeanModel> taxKind = new ComboBox<BeanModel>();
		taxKind.setForceSelection(true);
		taxKind.setAllowBlank(false);
		taxKind.setName("taxKind");
		taxKind.setEmptyText("请选择...");
		taxKind.setDisplayField("displayName");
		taxKind.setFieldLabel("征免性质");
		taxKind.setStore(taxKindStore);
		taxKind.setTypeAhead(true);
		taxKind.setTriggerAction(TriggerAction.ALL);
		right.add(taxKind, formData);

		final ListStore<BeanModel> processTypeStore = getProcessTypeStore();
		ComboBox<BeanModel> processType = new ComboBox<BeanModel>();
		processType.setForceSelection(true);
		//processType.setAllowBlank(false);
		processType.setName("processType");
		processType.setEmptyText("请选择...");
		processType.setDisplayField("displayName");
		processType.setFieldLabel("加工种类");
		processType.setStore(processTypeStore);
		processType.setTypeAhead(true);
		processType.setTriggerAction(TriggerAction.ALL);
		left.add(processType, formData);

		TextField<String> certificationCode = new TextField<String>();
		certificationCode.setName("certificationCode");
		certificationCode.setFieldLabel("批文号");
		right.add(certificationCode, formData);

		TextField<String> protocolCode = new TextField<String>();
		protocolCode.setName("protocolCode");
		protocolCode.setFieldLabel("协议书号");
		left.add(protocolCode, formData);

		TextField<String> licenceCode = new TextField<String>();
		licenceCode.setName("licenceCode");
		licenceCode.setFieldLabel("许可证号");
		right.add(licenceCode, formData);

		DateField importDeadline = new DateField();
		importDeadline.setName("importDeadline");
		//importDeadline.setAllowBlank(false);
		importDeadline.setFieldLabel("进口期限");
		left.add(importDeadline, formData);

		DateField exportDeadline = new DateField();
		exportDeadline.setName("exportDeadline");
		//exportDeadline.setAllowBlank(false);
		exportDeadline.setFieldLabel("出口期限");
		right.add(exportDeadline, formData);

		TextField<String> importContract = new TextField<String>();
		//importContract.setAllowBlank(false);
		importContract.setName("importContract");
		importContract.setFieldLabel("进口合同");
		left.add(importContract, formData);

		TextField<String> exportContract = new TextField<String>();
		//exportContract.setAllowBlank(false);
		exportContract.setName("exportContract");
		exportContract.setFieldLabel("出口合同");
		right.add(exportContract, formData);

		final ListStore<BeanModel> currencyStore = getCurrencyStore();
		ComboBox<BeanModel> importCurrency = new ComboBox<BeanModel>();
		importCurrency.setForceSelection(true);
		//importCurrency.setAllowBlank(false);
		importCurrency.setName("importCurrency");
		importCurrency.setEmptyText("请选择...");
		importCurrency.setDisplayField("displayName");
		importCurrency.setFieldLabel("进口币制");
		importCurrency.setStore(currencyStore);
		importCurrency.setTypeAhead(true);
		importCurrency.setTriggerAction(TriggerAction.ALL);
		left.add(importCurrency, formData);

		ComboBox<BeanModel> exportCurrency = new ComboBox<BeanModel>();
		exportCurrency.setForceSelection(true);
		//exportCurrency.setAllowBlank(false);
		exportCurrency.setName("exportCurrency");
		exportCurrency.setEmptyText("请选择...");
		exportCurrency.setDisplayField("displayName");
		exportCurrency.setFieldLabel("出口币制");
		exportCurrency.setStore(currencyStore);
		exportCurrency.setTypeAhead(true);
		exportCurrency.setTriggerAction(TriggerAction.ALL);
		right.add(exportCurrency, formData);

		final ListStore<BeanModel> countryStore = getCountryStore();
		ComboBox<BeanModel> tradeCountry = new ComboBox<BeanModel>();
		tradeCountry.setForceSelection(true);
		//tradeCountry.setAllowBlank(false);
		tradeCountry.setName("tradeCountry");
		tradeCountry.setEmptyText("请选择...");
		tradeCountry.setDisplayField("displayName");
		tradeCountry.setFieldLabel("贸易国别");
		tradeCountry.setStore(countryStore);
		tradeCountry.setTypeAhead(true);
		tradeCountry.setTriggerAction(TriggerAction.ALL);
		left.add(tradeCountry, formData);

		final ListStore<BeanModel> investModeStore = getInvestModeStore();
		ComboBox<BeanModel> investMode = new ComboBox<BeanModel>();
		investMode.setForceSelection(true);
		//investMode.setAllowBlank(false);
		investMode.setName("investMode");
		investMode.setEmptyText("请选择...");
		investMode.setDisplayField("displayName");
		investMode.setFieldLabel("投资方式");
		investMode.setStore(investModeStore);
		investMode.setTypeAhead(true);
		investMode.setTriggerAction(TriggerAction.ALL);
		right.add(investMode, formData);

		NumberField investMoney = new NumberField();
		investMoney.setPropertyEditorType(Double.class);
		investMoney.setFormat(NumberFormat.getFormat("#0.00"));
		investMoney.setName("investMoney");
		investMoney.setFieldLabel("投资额");
		left.add(investMoney, formData);

		ComboBox<BeanModel> investCurrency = new ComboBox<BeanModel>();
		investCurrency.setForceSelection(true);
		investCurrency.setName("investCurrency");
		investCurrency.setEmptyText("请选择...");
		investCurrency.setDisplayField("displayName");
		investCurrency.setFieldLabel("投资币制");
		investCurrency.setStore(currencyStore);
		investCurrency.setTypeAhead(true);
		investCurrency.setTriggerAction(TriggerAction.ALL);
		right.add(investCurrency, formData);

		NumberField superviseFeeRate = new NumberField();
		superviseFeeRate.setPropertyEditorType(Double.class);
		superviseFeeRate.setName("superviseFeeRate");
		superviseFeeRate.setFieldLabel("监管费率");
		left.add(superviseFeeRate, formData);

		NumberField superviseFee = new NumberField();
		superviseFee.setPropertyEditorType(Double.class);
		superviseFee.setFormat(NumberFormat.getFormat("#0.00"));
		superviseFee.setName("superviseFee");
		superviseFee.setFieldLabel("监管费");
		right.add(superviseFee, formData);

		NumberField saleInScale = new NumberField();
		saleInScale.setPropertyEditorType(Double.class);
		saleInScale.setFormat(NumberFormat.getFormat("#0.0000"));
		saleInScale.setName("saleInScale");
		saleInScale.setFieldLabel("内销比例");
		left.add(saleInScale, formData);

		final ListStore<BeanModel> dealModeStore = getDealModeStore();
		ComboBox<BeanModel> dealMode = new ComboBox<BeanModel>();
		dealMode.setForceSelection(true);
		//dealMode.setAllowBlank(false);
		dealMode.setName("dealMode");
		dealMode.setEmptyText("请选择...");
		dealMode.setDisplayField("displayName");
		dealMode.setFieldLabel("成交方式");
		dealMode.setStore(dealModeStore);
		dealMode.setTypeAhead(true);
		dealMode.setTriggerAction(TriggerAction.ALL);
		left.add(dealMode, formData);

		final ListStore<BeanModel> bringInModeStore = getBringInModeStore();
		ComboBox<BeanModel> bringInMode = new ComboBox<BeanModel>();
		bringInMode.setForceSelection(true);
		bringInMode.setName("bringInMode");
		bringInMode.setEmptyText("请选择...");
		bringInMode.setDisplayField("displayName");
		bringInMode.setFieldLabel("引进方式");
		bringInMode.setStore(bringInModeStore);
		bringInMode.setTypeAhead(true);
		bringInMode.setTriggerAction(TriggerAction.ALL);
		right.add(bringInMode, formData);

		final ListStore<BeanModel> customStore = getCustomsStore();
		ComboBox<BeanModel> port1 = new ComboBox<BeanModel>();
		port1.setForceSelection(true);
		//port1.setAllowBlank(false);
		port1.setName("port1");
		port1.setEmptyText("请选择...");
		port1.setDisplayField("displayName");
		port1.setFieldLabel("口岸一");
		port1.setStore(customStore);
		port1.setTypeAhead(true);
		port1.setTriggerAction(TriggerAction.ALL);
		right.add(port1, formData);

		ComboBox<BeanModel> port2 = new ComboBox<BeanModel>();
		port2.setForceSelection(true);
		port2.setName("port2");
		port2.setEmptyText("请选择...");
		port2.setDisplayField("displayName");
		port2.setFieldLabel("口岸二");
		port2.setStore(customStore);
		port2.setTypeAhead(true);
		port2.setTriggerAction(TriggerAction.ALL);
		left.add(port2, formData);

		ComboBox<BeanModel> port3 = new ComboBox<BeanModel>();
		port3.setForceSelection(true);
		port3.setName("port3");
		port3.setEmptyText("请选择...");
		port3.setDisplayField("displayName");
		port3.setFieldLabel("口岸三");
		port3.setStore(customStore);
		port3.setTypeAhead(true);
		port3.setTriggerAction(TriggerAction.ALL);
		right.add(port3, formData);

		ComboBox<BeanModel> port4 = new ComboBox<BeanModel>();
		port4.setForceSelection(true);
		port4.setName("port4");
		port4.setEmptyText("请选择...");
		port4.setDisplayField("displayName");
		port4.setFieldLabel("口岸四");
		port4.setStore(customStore);
		port4.setTypeAhead(true);
		port4.setTriggerAction(TriggerAction.ALL);
		left.add(port4, formData);

		ComboBox<BeanModel> port5 = new ComboBox<BeanModel>();
		port5.setForceSelection(true);
		port5.setName("port5");
		port5.setEmptyText("请选择...");
		port5.setDisplayField("displayName");
		port5.setFieldLabel("口岸五");
		port5.setStore(customStore);
		port5.setTypeAhead(true);
		port5.setTriggerAction(TriggerAction.ALL);
		right.add(port5, formData);

		TextField<String> typeMan = new TextField<String>();
		typeMan.setName("typeMan");
		typeMan.setFieldLabel("录入人员");
		left.add(typeMan, formData);

		DateField typeDate = new DateField();
		typeDate.setAllowBlank(false);
		typeDate.setName("typeDate");
		typeDate.setFieldLabel("录入日期");
		right.add(typeDate, formData);

		TextField<String> note = new TextField<String>();
		note.setName("note");
		note.setFieldLabel("备注");
		left.add(note, formData);

		main.add(left, new ColumnData(.5));
		main.add(right, new ColumnData(.5));

		panel.add(main, new FormData("100%"));

		formBindings = new FormBinding(panel, true);

		formBindings.getBinding(runEnterprise).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
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
		formBindings.getBinding(foreignEnterprise).setConverter(
				new Converter() {
					@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
						return ((BeanModel) value).get("id");
					}

					@Override
					public Object convertModelValue(Object value) {
						if (value != null) {
							return foreignEnterpriseStore
									.findModel("id", value);
						} else {
							return null;
						}
					}
				});
		formBindings.getBinding(tradeMode).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
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
 if (value == null) {return null;}
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
		formBindings.getBinding(processType).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return processTypeStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		Converter currencyConverter = new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return currencyStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		};
		formBindings.getBinding(importCurrency).setConverter(currencyConverter);
		formBindings.getBinding(exportCurrency).setConverter(currencyConverter);
		formBindings.getBinding(tradeCountry).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
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
		formBindings.getBinding(investMode).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return investModeStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(investCurrency).setConverter(currencyConverter);
		formBindings.getBinding(dealMode).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
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
		formBindings.getBinding(bringInMode).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return bringInModeStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		Converter customsConverter = new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return customStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		};
		formBindings.getBinding(port1).setConverter(customsConverter);
		formBindings.getBinding(port2).setConverter(customsConverter);
		formBindings.getBinding(port3).setConverter(customsConverter);
		formBindings.getBinding(port4).setConverter(customsConverter);
		formBindings.getBinding(port5).setConverter(customsConverter);

		formBindings.setStore((Store<BeanModel>) grid.getStore());

		panel.setReadOnly(true);

		panel.setButtonAlign(HorizontalAlignment.CENTER);

		saveButton = new Button("保存");
		saveButton.setVisible(false);
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!panel.isValid())
					return;
				save();
			}
		});
		panel.addButton(saveButton);

		cancelButton = new Button("取消");
		cancelButton.setVisible(false);
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				resetState();
			}
		});
		panel.addButton(cancelButton);

		updateButton = new Button("更新");
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

	private void save() {
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
					ContractHead newItem = (ContractHead) beanModel.getBean();
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

		getBusinessService().saveContractHead(
				(ContractHead) beanModel.getBean(), newCallback);
	}

	private void update() {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(Void result) {
				MessageBox.info("提示", "修改成功！", null);
				store.commitChanges();
				resetState();
			}
		};
		List<ContractHead> list = new ArrayList<ContractHead>();
		list.add((ContractHead) grid.getSelectionModel().getSelection().get(0)
				.getBean());
		getBusinessService().updateContractHead(list, callback);
	}

	private void delete() {
		final AsyncCallback<Void> deleteCallback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			@Override
			public void onSuccess(Void result) {
				for (Iterator<BeanModel> iterator = grid.getSelectionModel()
						.getSelectedItems().iterator(); iterator.hasNext();) {
					BeanModel beanModel = (BeanModel) iterator.next();
					store.remove(beanModel);
				}
				resetState();
			}
		};
		getBusinessService().deleteContractHead(
				models2Ids(grid.getSelectionModel().getSelectedItems()),
				deleteCallback);
	}

	private void resetState() {
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
		if (permitedEnterprise.getCanUpdate()) {
			editButton.enable();
		} else {
			editButton.disable();
		}
		if (permitedEnterprise.getCanDelete()) {
			deleteButton.enable();
		} else {
			deleteButton.disable();
		}
		if (grid.getSelectionModel().getSelection().size() > 0) {
			formBindings.bind((BeanModel) grid.getSelectionModel()
					.getSelection().get(0));
		} else {
			formBindings.unbind();
			if (permitedEnterprise.getCanAdd()) {
				addButton.enable();
				importButton.enable();
			} else {
				addButton.disable();
				importButton.disable();
			}
			editButton.disable();
			deleteButton.disable();
		}
	}

	private List<Long> models2Ids(List<BeanModel> models) {
		List<Long> ids = new ArrayList<Long>();
		for (Iterator<BeanModel> iterator = models.iterator(); iterator
				.hasNext();) {
			BeanModel beanModel = (BeanModel) iterator.next();
			ids.add(((ContractHead) beanModel.getBean()).getId());
		}
		return ids;
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

	private ListStore<BeanModel> getCurrencyStore() {
		final ListStore<BeanModel> currencyStore = new ListStore<BeanModel>();
		AsyncCallback<List<Currency>> getCallback = new AsyncCallback<List<Currency>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<Currency> result) {
				Collections.sort(result, new Comparator<Currency>() {
					public int compare(Currency arg0, Currency arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<Currency> iterator = result.iterator(); iterator
						.hasNext();) {
					Currency currency = (Currency) iterator.next();
					beanModel = BeanModelLookup.get()
							.getFactory(Currency.class).createModel(currency);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					currencyStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listCurrency(getCallback);
		return currencyStore;
	}

	private ListStore<BeanModel> getCountryStore() {
		final ListStore<BeanModel> countryStore = new ListStore<BeanModel>();
		AsyncCallback<List<Country>> getCallback = new AsyncCallback<List<Country>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<Country> result) {
				Collections.sort(result, new Comparator<Country>() {
					public int compare(Country arg0, Country arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<Country> iterator = result.iterator(); iterator
						.hasNext();) {
					Country country = (Country) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(Country.class)
							.createModel(country);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					countryStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listCountry(getCallback);
		return countryStore;
	}

	private ListStore<BeanModel> getTradeModeStore() {
		final ListStore<BeanModel> tradeModeStore = new ListStore<BeanModel>();
		AsyncCallback<List<TradeMode>> getCallback = new AsyncCallback<List<TradeMode>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<TradeMode> result) {
				Collections.sort(result, new Comparator<TradeMode>() {
					public int compare(TradeMode arg0, TradeMode arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<TradeMode> iterator = result.iterator(); iterator
						.hasNext();) {
					TradeMode tradeMode = (TradeMode) iterator.next();
					beanModel = BeanModelLookup.get()
							.getFactory(TradeMode.class).createModel(tradeMode);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					tradeModeStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listTradeMode(getCallback);
		return tradeModeStore;
	}

	private ListStore<BeanModel> getTaxKindStore() {
		final ListStore<BeanModel> taxKindStore = new ListStore<BeanModel>();
		AsyncCallback<List<TaxKind>> getCallback = new AsyncCallback<List<TaxKind>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<TaxKind> result) {
				Collections.sort(result, new Comparator<TaxKind>() {
					public int compare(TaxKind arg0, TaxKind arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<TaxKind> iterator = result.iterator(); iterator
						.hasNext();) {
					TaxKind taxKind = (TaxKind) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(TaxKind.class)
							.createModel(taxKind);
					beanModel
							.set("displayName", beanModel.get("code")
									.toString()
									+ beanModel.get("shortName").toString());
					taxKindStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listTaxKind(getCallback);
		return taxKindStore;
	}

	private ListStore<BeanModel> getProcessTypeStore() {
		final ListStore<BeanModel> processTypeStore = new ListStore<BeanModel>();
		AsyncCallback<List<ProcessType>> getCallback = new AsyncCallback<List<ProcessType>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ProcessType> result) {
				Collections.sort(result, new Comparator<ProcessType>() {
					public int compare(ProcessType arg0, ProcessType arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<ProcessType> iterator = result.iterator(); iterator
						.hasNext();) {
					ProcessType processType = (ProcessType) iterator.next();
					beanModel = BeanModelLookup.get()
							.getFactory(ProcessType.class)
							.createModel(processType);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					processTypeStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listProcessType(getCallback);
		return processTypeStore;
	}

	private ListStore<BeanModel> getInvestModeStore() {
		final ListStore<BeanModel> investModeStore = new ListStore<BeanModel>();
		AsyncCallback<List<InvestMode>> getCallback = new AsyncCallback<List<InvestMode>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<InvestMode> result) {
				Collections.sort(result, new Comparator<InvestMode>() {
					public int compare(InvestMode arg0, InvestMode arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<InvestMode> iterator = result.iterator(); iterator
						.hasNext();) {
					InvestMode investMode = (InvestMode) iterator.next();
					beanModel = BeanModelLookup.get()
							.getFactory(InvestMode.class)
							.createModel(investMode);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					investModeStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listInvestMode(getCallback);
		return investModeStore;
	}

	private ListStore<BeanModel> getDealModeStore() {
		final ListStore<BeanModel> dealModeStore = new ListStore<BeanModel>();
		AsyncCallback<List<DealMode>> getCallback = new AsyncCallback<List<DealMode>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<DealMode> result) {
				Collections.sort(result, new Comparator<DealMode>() {
					public int compare(DealMode arg0, DealMode arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<DealMode> iterator = result.iterator(); iterator
						.hasNext();) {
					DealMode dealMode = (DealMode) iterator.next();
					beanModel = BeanModelLookup.get()
							.getFactory(DealMode.class).createModel(dealMode);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					dealModeStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listDealMode(getCallback);
		return dealModeStore;
	}

	private ListStore<BeanModel> getCustomsStore() {
		final ListStore<BeanModel> customsStore = new ListStore<BeanModel>();
		AsyncCallback<List<Customs>> getCallback = new AsyncCallback<List<Customs>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<Customs> result) {
				Collections.sort(result, new Comparator<Customs>() {
					public int compare(Customs arg0, Customs arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<Customs> iterator = result.iterator(); iterator
						.hasNext();) {
					Customs customs = (Customs) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(Customs.class)
							.createModel(customs);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					customsStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listCustoms(getCallback);
		return customsStore;
	}

	private ListStore<BeanModel> getBringInModeStore() {
		final ListStore<BeanModel> bringInModeStore = new ListStore<BeanModel>();
		AsyncCallback<List<BringInMode>> getCallback = new AsyncCallback<List<BringInMode>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<BringInMode> result) {
				Collections.sort(result, new Comparator<BringInMode>() {
					public int compare(BringInMode arg0, BringInMode arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<BringInMode> iterator = result.iterator(); iterator
						.hasNext();) {
					BringInMode bringInMode = (BringInMode) iterator.next();
					beanModel = BeanModelLookup.get()
							.getFactory(BringInMode.class)
							.createModel(bringInMode);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					bringInModeStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listBringInMode(getCallback);
		return bringInModeStore;
	}

	public static native void openNewWindow(String url) /*-{
		window.open(url,'','height=1000,width=720,scrollbars=yes,status=yes');
	}-*/;
	
	
	private Window getImportDialog() {
		if (importWindow != null) {
			return importWindow;
		}

		importWindow = new Window();
		importWindow.setIcon(Resources.ICONS.leadIn());
		importWindow.setHeading("从Excel导入");
		importWindow.setWidth(450);
		importWindow.setHeight(120);
		importWindow.setLayout(new FitLayout());
		
		// 选择上传路径 form
		final FormPanel uploadFormPanel = new FormPanel ();
		uploadFormPanel.setLabelWidth(100);
		uploadFormPanel.setFieldWidth(300);
		
		uploadFormPanel.setHeaderVisible(false);
		uploadFormPanel.setBodyBorder(false);
		// 设置form参数MULTIPART
		uploadFormPanel.setEncoding (FormPanel.Encoding.MULTIPART);
		// 设置提交方式POST
		uploadFormPanel.setMethod (FormPanel.Method.POST);
		// 设置上传请求地址
		uploadFormPanel.setAction("/uploadContract.do");
		uploadFormPanel.addListener(Events.Submit, new Listener<FormEvent>() { 
            public void handleEvent(FormEvent be) {             	
            	reflashStores();
            	String response= be.getResultHtml();
            	importWindow.hide();
            	setIsWaiting(false);
            	if (response.trim().equals("null")) {					
					MessageBox.info("提示", "导入成功", null);
				}else {
					MessageBox.alert("错误", response, null);
				}                
            } 
        });  
		
		final NumberField id=new NumberField();
		id.setVisible(false);
		id.setPropertyEditorType(Long.class);
		id.setName("enterpriseId");		
		uploadFormPanel.add(id);
		
		// 文件选择
		FileUploadField fileUploadField = new FileUploadField ();
		fileUploadField.setFieldLabel ("请选择上传文件");
		fileUploadField.setName ("file");
		uploadFormPanel.add(fileUploadField);
		
		importWindow.add(uploadFormPanel);

		importWindow.addButton(new Button("确定",
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						id.setValue(permitedEnterprise.getEnterpriseId());
						uploadFormPanel.submit();
						setIsWaiting(true);
					}
				}));
		importWindow.addButton(new Button("取消",
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {						
						importWindow.hide();
					}
				}));
		importWindow.setFocusWidget(importWindow.getButtonBar().getItem(0));

		importWindow.setButtonAlign(HorizontalAlignment.CENTER);

		return importWindow;
	}

}
