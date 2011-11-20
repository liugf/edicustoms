package com.gavin.ediCustoms.client.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BindingEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
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
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.entity.edi.contract.ContractHead;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.dictionary.Attachment;
import com.gavin.ediCustoms.entity.edi.dictionary.Country;
import com.gavin.ediCustoms.entity.edi.dictionary.Customs;
import com.gavin.ediCustoms.entity.edi.dictionary.DealMode;
import com.gavin.ediCustoms.entity.edi.dictionary.DeclareProperty;
import com.gavin.ediCustoms.entity.edi.dictionary.District;
import com.gavin.ediCustoms.entity.edi.dictionary.LoadPort;
import com.gavin.ediCustoms.entity.edi.dictionary.PayWay;
import com.gavin.ediCustoms.entity.edi.dictionary.Port;
import com.gavin.ediCustoms.entity.edi.dictionary.TaxKind;
import com.gavin.ediCustoms.entity.edi.dictionary.TradeMode;
import com.gavin.ediCustoms.entity.edi.dictionary.TransportMode;
import com.gavin.ediCustoms.entity.edi.dictionary.Truck;
import com.gavin.ediCustoms.entity.edi.dictionary.Useage;
import com.gavin.ediCustoms.entity.edi.dictionary.WrapType;
import com.gavin.ediCustoms.resources.Resources;
import com.gavin.ediCustoms.shared.MapUtil;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class CustomsDeclarationPanel extends MyLayoutContainer {
	protected boolean isExport = true;
	
	protected TabPanel tabPanel;
	protected ComboBox<BeanModel> permitedEnterpriseComboBox;
	protected ToolBar toolBar;
	
	protected ListStore<BeanModel> permitedEnterpriseStore;
	protected ListStore<BeanModel> runEnterpriseStore;
	protected ListStore<BeanModel> store;
	protected FormBinding formBindings;
	protected BeanModelFactory beanModelFactory;
	protected BeanModel beanModel;

	protected Window attachmentWindow;
	protected ComboBox<BeanModel> dealMode;

	protected Button saveButton;
	protected Button cancelButton;

	protected Button updateButton;
	protected Button resetButton;

	protected Button searchButton;
	protected Button editButton;
	protected Button addButton;
	protected Button deleteButton;
	protected Button printButton;
	protected Button declareButton;
	protected Button declareDetailButton;
	protected Button passButton;
	protected Button refleshButton;

	protected FormPanel formPanel;
	protected Grid<BeanModel> grid;

	protected StoreFilterField<BeanModel> filter;
	protected PermitedEnterprise permitedEnterprise;

	protected ListStore<BeanModel> contractHeadStore;

	protected TabItem customsDeclarationHead;
	protected DeliveryRecordTabItem deliveryRecordTabItem;
	protected ContainerTabItem containerTabItem;
	protected TransitInformationTabItem transitInformationTabItem;

	protected TextField<String> preEntryId;
	protected TextField<String> electronicPort;
	protected ComboBox<BeanModel> loadPort;


	protected ListStore<BeanModel> attachmentStore;
	protected TextField<String> certMark;
	protected FormPanel attachmentFormpanel;
	
	protected Window searchWindow;
	
	protected boolean isPass=false;
	
	protected List<CustomsDeclarationPanelListener> listeners;

	public CustomsDeclarationPanel(
			ListStore<BeanModel> permitedEnterpriseStore) {
		this.permitedEnterpriseStore = permitedEnterpriseStore;
		beanModelFactory = BeanModelLookup.get().getFactory(
				CustomsDeclarationHead.class);
		listeners = new ArrayList<CustomsDeclarationPanelListener>();
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());

		tabPanel = new TabPanel();

		customsDeclarationHead = new TabItem("报关单表头");
		customsDeclarationHead.setLayout(new FitLayout());

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
				String declareTime = record.get("declareTime").toString()
						.toLowerCase();
				String preEntryId = record.get("preEntryId").toString()
						.toLowerCase();
				if (declareTime.indexOf(filter.toLowerCase()) != -1
						|| preEntryId.indexOf(filter.toLowerCase()) != -1) {
					return true;
				}
				return false;
			}
		};
		filter.setWidth(100);

		store = new ListStore<BeanModel>();
		store.sort("no", Style.SortDir.ASC);
		filter.bind(store);

		grid = createGrid();
		grid.getSelectionModel().addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<BeanModel>>() {
					public void handleEvent(SelectionChangedEvent<BeanModel> be) {
						if (grid.getSelectionModel().getSelection().size()<=0) {
							return;
						}
						store.rejectChanges();
						resetState();
						CustomsDeclarationHead selectedCustomsDeclarationHead = grid
						.getSelectionModel().getSelection().get(0).getBean();
						notifyCustomsDeclarationHeadChange(selectedCustomsDeclarationHead);
					}
				});

		contractHeadStore = new ListStore<BeanModel>();
		formPanel = createForm();

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 210);
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);

		frame.add(grid, westData);
		frame.add(formPanel, centerData);

		content.add(frame);

		// 添加工具栏
		toolBar = new ToolBar();

		toolBar.add(new LabelToolItem("发货单位："));
		permitedEnterpriseComboBox = new ComboBox<BeanModel>();
		permitedEnterpriseComboBox.setWidth(250);
		permitedEnterpriseComboBox.setEmptyText("请选择...");
		permitedEnterpriseComboBox.setDisplayField("displayName");
		permitedEnterpriseComboBox.setStore(permitedEnterpriseStore);
		permitedEnterpriseComboBox.setTypeAhead(true);
		permitedEnterpriseComboBox.setTriggerAction(TriggerAction.ALL);
		toolBar.add(permitedEnterpriseComboBox);

		toolBar.add(new SeparatorToolItem());
		searchButton = new Button("查找");
		searchButton.disable();
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				getSearchWindow().show();
			}
		});
		toolBar.add(searchButton);
		
		/*toolBar.add(new LabelToolItem("搜索："));
		toolBar.add(filter);*/

		toolBar.add(new SeparatorToolItem());

		addButton = new Button("添加", Resources.ICONS.add());
		addButton.disable();
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				beanModel = beanModelFactory
						.createModel(new CustomsDeclarationHead());
				
				//设置默认值
				if (isExport) {
					beanModel.set("dealMode", "3");
				}else {
					beanModel.set("dealMode", "1");
				}
				
				
				formPanel.reset();
				formBindings.bind(beanModel);
				formBindings.addListener(Events.Bind,
						new Listener<BindingEvent>() {
							@Override
							public void handleEvent(BindingEvent be) {
								formBindings.getModel().set("ownerId",
										permitedEnterprise.getEnterpriseId());
								formBindings.getModel().set("isExport",
										isExport);
								for (Field<?> field : formPanel.getFields()) {
									field.clearInvalid();
								}
								formBindings.removeListener(Events.Bind, this);
							}
						});
				formPanel.setReadOnly(false);
				if (attachmentFormpanel != null) {
					attachmentFormpanel.setReadOnly(false);
				}else {
					getAttachmentWindow(true);
					attachmentFormpanel.setReadOnly(false);
				}
				preEntryId.setReadOnly(true);
				//electronicPort.setReadOnly(true);
				// loadPort.setAllowBlank(false);
				formPanel.getFields().get(2).focus();
				saveButton.setVisible(true);
				cancelButton.setVisible(true);
				updateButton.setVisible(false);
				resetButton.setVisible(false);
				addButton.disable();

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
					if (attachmentFormpanel != null) {
						attachmentFormpanel.setReadOnly(false);
					}else {
						getAttachmentWindow(true);
						attachmentFormpanel.setReadOnly(false);
					}
					preEntryId.setReadOnly(true);
					//electronicPort.setReadOnly(true);
					// loadPort.setReadOnly(true);
					// loadPort.setAllowBlank(true);
					updateButton.enable();
					resetButton.enable();
					editButton.disable();
					formPanel.getFields().get(0).focus();
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

		printButton = new Button("打印", Resources.ICONS.printer());
		printButton.disable();
		toolBar.add(printButton);
		printButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				BeanModel bm = grid.getSelectionModel().getSelection().get(0);
				paramsMap.put("id", bm.get("id"));
				openNewWindow("/customsDeclaration.do", paramsMap);
			}
		});

		toolBar.add(new SeparatorToolItem());

		declareButton = new Button("申报",Resources.ICONS.submit());
		declareButton.disable();
		toolBar.add(declareButton);
		declareButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				final AsyncCallback<String> callback=new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						MessageBox.alert("警告", "发生错误，请刷新！", null);					
					}

					@Override
					public void onSuccess(String result) {
						if (result!=null) {
							MessageBox.info("提示", result, null);
						}						
					}
				};
				MessageBox.confirm("确认", "确定要向海关申报吗？",
						new Listener<MessageBoxEvent>() {
							@Override
							public void handleEvent(MessageBoxEvent be) {
								if (be.getButtonClicked().getText() == GXT.MESSAGES
										.messageBox_yes()) {
									BeanModel bm = grid.getSelectionModel().getSelection().get(0);
									getBusinessService().customsDeclare((Long)bm.get("id"), callback);
								}
							}
						});				
			}
		});
		
		toolBar.add(new SeparatorToolItem());
		
		declareDetailButton = new Button("申报明细");
		declareDetailButton.disable();
		toolBar.add(declareDetailButton);
		declareDetailButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				BeanModel bm = grid.getSelectionModel().getSelection().get(0);
				paramsMap.put("id", bm.get("id"));
				openNewWindow("/declareDetail.do", paramsMap);
			}
		});

		toolBar.add(new SeparatorToolItem());
		
		passButton = new Button("入库");
		passButton.disable();
		toolBar.add(passButton);
		passButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				changePass();
			}
		});
		
		toolBar.add(new SeparatorToolItem());

		refleshButton = new Button("刷新",Resources.ICONS.reflesh());
		toolBar.add(refleshButton);
		refleshButton
				.addSelectionListener(new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						reflashStores();
						// formPanel.reset();
					}
				});

		content.setTopComponent(toolBar);
		customsDeclarationHead.add(content);
		tabPanel.add(customsDeclarationHead);
		
		
		
		
	}	

	protected void reflashStores() {
		AsyncCallback<List<CustomsDeclarationHead>> getCallback = new AsyncCallback<List<CustomsDeclarationHead>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<CustomsDeclarationHead> result) {
				Collections.sort(result,
						new Comparator<CustomsDeclarationHead>() {
							public int compare(CustomsDeclarationHead arg0,
									CustomsDeclarationHead arg1) {
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
				store.removeAll();
				store.add(beanModelFactory.createModel(result));
			}
		};
		getBusinessService().listCustomsDeclarationHead(
				permitedEnterprise.getEnterpriseId(), isExport, getCallback);

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

	protected void refleshContractHeadStore() {
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
						return arg0.getTypeDate().compareTo(arg1.getTypeDate());
					}
				});
				contractHeadStore.removeAll();
				contractHeadStore.add(BeanModelLookup.get()
						.getFactory(ContractHead.class).createModel(result));
			}
		};
		getBusinessService().listContractHead(
				permitedEnterprise.getEnterpriseId(), getCallback);

	}

	protected Grid<BeanModel> createGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();

		GridCellRenderer<BeanModel> dateRenderer = new GridCellRenderer<BeanModel>() {
			@Override
			public Object render(BeanModel model, String property,
					com.extjs.gxt.ui.client.widget.grid.ColumnData config,
					int rowIndex, int colIndex, ListStore<BeanModel> store,
					Grid<BeanModel> grid) {
				Date date = model.get(property);
				if (date == null) {
					return null;
				}
				return date.toString().substring(0, 10);
			}
		};

		column.setId("declareTime");

		column.setRenderer(dateRenderer);
		column.setHeader("申报日期");
		column.setWidth(70);
		column.setRowHeader(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("preEntryId");
		column.setHeader("预录入号");
		column.setWidth(125);
		configs.add(column);

		store.setMonitorChanges(true);
		Grid<BeanModel> grid = new Grid<BeanModel>(store, new ColumnModel(
				configs));
		grid.setBorders(true);
		return grid;
	}

	protected abstract FormPanel createForm();
	
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
					} else {
						addButton.disable();
					}

				}
			}
		};

		getBusinessService().saveCustomsDeclarationHead(
				(CustomsDeclarationHead) beanModel.getBean(), newCallback);
	}

	protected void update() {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(Void result) {
				MessageBox.info("提示", "修改成功！", null);
				store.commitChanges();
				resetState();
				CustomsDeclarationHead selectedCustomsDeclarationHead = grid
				.getSelectionModel().getSelection().get(0).getBean();
				notifyCustomsDeclarationHeadChange(selectedCustomsDeclarationHead);
			}
		};
		List<CustomsDeclarationHead> list = new ArrayList<CustomsDeclarationHead>();
		list.add((CustomsDeclarationHead) grid.getSelectionModel()
				.getSelection().get(0).getBean());
		getBusinessService().updateCustomsDeclarationHead(list, callback);
	}

	protected void delete() {
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
		getBusinessService().deleteCustomsDeclarationHead(
				models2Ids(grid.getSelectionModel().getSelectedItems()),
				deleteCallback);
	}

	protected void resetState() {
		formPanel.setReadOnly(true);
		if (attachmentFormpanel != null) {
			attachmentFormpanel.setReadOnly(true);
		}
		saveButton.setVisible(false);
		cancelButton.setVisible(false);
		updateButton.setVisible(true);
		updateButton.disable();
		resetButton.setVisible(true);
		resetButton.disable();
		// loadPort.setAllowBlank(true);

		if (permitedEnterprise.getCanAdd()) {
			addButton.enable();
		} else {
			addButton.disable();
		}
		enableOperateButton();
		if (grid.getSelectionModel().getSelection().size() > 0) {
			BeanModel bm = grid.getSelectionModel().getSelection().get(0);
			formBindings.bind(bm);
			isPass=bm.get("pass");

			enableOperateButton();
		} else {
			formBindings.unbind();
			formPanel.reset();
			if (permitedEnterprise.getCanAdd()) {
				addButton.enable();
			} else {
				addButton.disable();
			}
			disableOperateButton();
		}
	}

	protected List<Long> models2Ids(List<BeanModel> models) {
		List<Long> ids = new ArrayList<Long>();
		for (Iterator<BeanModel> iterator = models.iterator(); iterator
				.hasNext();) {
			/*
			 * BeanModel beanModel = (BeanModel) iterator.next();
			 * ids.add(((CustomsDeclarationHead) beanModel.getBean()).getId());
			 */

			ModelData modelData = (ModelData) iterator.next();
			ids.add((Long) modelData.get("id"));
		}
		return ids;
	}

	protected ListStore<BeanModel> getCountryStore() {
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

	protected ListStore<BeanModel> getLoadPortStore() {
		final ListStore<BeanModel> loadPortStore = new ListStore<BeanModel>();
		AsyncCallback<List<LoadPort>> getCallback = new AsyncCallback<List<LoadPort>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<LoadPort> result) {
				Collections.sort(result, new Comparator<LoadPort>() {
					public int compare(LoadPort arg0, LoadPort arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<LoadPort> iterator = result.iterator(); iterator
						.hasNext();) {
					LoadPort loadPort = (LoadPort) iterator.next();
					beanModel = BeanModelLookup.get()
							.getFactory(LoadPort.class).createModel(loadPort);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					loadPortStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listLoadPort(getCallback);
		return loadPortStore;
	}

	protected ListStore<BeanModel> getTradeModeStore() {
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

	protected ListStore<BeanModel> getTaxKindStore() {
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
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					taxKindStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listTaxKind(getCallback);
		return taxKindStore;
	}

	protected ListStore<BeanModel> getUseageStore() {
		final ListStore<BeanModel> useageStore = new ListStore<BeanModel>();
		AsyncCallback<List<Useage>> getCallback = new AsyncCallback<List<Useage>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<Useage> result) {
				Collections.sort(result, new Comparator<Useage>() {
					public int compare(Useage arg0, Useage arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<Useage> iterator = result.iterator(); iterator
						.hasNext();) {
					Useage useage = (Useage) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(Useage.class)
							.createModel(useage);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					useageStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listUseage(getCallback);
		return useageStore;
	}

	protected ListStore<BeanModel> getDealModeStore() {
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

	protected ListStore<BeanModel> getCustomsStore() {
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

	protected ListStore<BeanModel> getWrapTypeStore() {
		final ListStore<BeanModel> wrapTypeStore = new ListStore<BeanModel>();
		AsyncCallback<List<WrapType>> getCallback = new AsyncCallback<List<WrapType>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<WrapType> result) {
				Collections.sort(result, new Comparator<WrapType>() {
					public int compare(WrapType arg0, WrapType arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<WrapType> iterator = result.iterator(); iterator
						.hasNext();) {
					WrapType wrapType = (WrapType) iterator.next();
					beanModel = BeanModelLookup.get()
							.getFactory(WrapType.class).createModel(wrapType);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					wrapTypeStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listWrapType(getCallback);
		return wrapTypeStore;
	}

	protected ListStore<BeanModel> getTransportModeStore() {
		final ListStore<BeanModel> transportModeStore = new ListStore<BeanModel>();
		AsyncCallback<List<TransportMode>> getCallback = new AsyncCallback<List<TransportMode>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<TransportMode> result) {
				Collections.sort(result, new Comparator<TransportMode>() {
					public int compare(TransportMode arg0, TransportMode arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<TransportMode> iterator = result.iterator(); iterator
						.hasNext();) {
					TransportMode transportMode = (TransportMode) iterator
							.next();
					beanModel = BeanModelLookup.get()
							.getFactory(TransportMode.class)
							.createModel(transportMode);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					transportModeStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listTransportMode(getCallback);
		return transportModeStore;
	}

	protected ListStore<BeanModel> getPortStore() {
		final ListStore<BeanModel> portStore = new ListStore<BeanModel>();
		AsyncCallback<List<Port>> getCallback = new AsyncCallback<List<Port>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<Port> result) {
				Collections.sort(result, new Comparator<Port>() {
					public int compare(Port arg0, Port arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<Port> iterator = result.iterator(); iterator
						.hasNext();) {
					Port port = (Port) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(Port.class)
							.createModel(port);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					portStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listPort(getCallback);
		return portStore;
	}

	protected ListStore<BeanModel> getDeclareEnterpriseStore() {
		final ListStore<BeanModel> declareEnterpriseStore = new ListStore<BeanModel>();
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
				BeanModelFactory bmf = BeanModelLookup.get().getFactory(
						Enterprise.class);
				for (Enterprise enterprise : result) {
					BeanModel bm = bmf.createModel(enterprise);
					bm.set("displayName", bm.get("tradeCode").toString()
							+ bm.get("registeName").toString());
					declareEnterpriseStore.add(bm);
				}
			}
		};
		getSystemService().listDeclareEnterprise(callback);
		return declareEnterpriseStore;
	}

	protected ListStore<BeanModel> getDistrictStore() {
		final ListStore<BeanModel> districtStore = new ListStore<BeanModel>();
		AsyncCallback<List<District>> getCallback = new AsyncCallback<List<District>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<District> result) {
				Collections.sort(result, new Comparator<District>() {
					public int compare(District arg0, District arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<District> iterator = result.iterator(); iterator
						.hasNext();) {
					District district = (District) iterator.next();
					beanModel = BeanModelLookup.get()
							.getFactory(District.class).createModel(district);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					districtStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listDistrict(getCallback);
		return districtStore;
	}

	protected ListStore<BeanModel> getPayWayStore() {
		final ListStore<BeanModel> payWayStore = new ListStore<BeanModel>();
		AsyncCallback<List<PayWay>> getCallback = new AsyncCallback<List<PayWay>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<PayWay> result) {
				Collections.sort(result, new Comparator<PayWay>() {
					public int compare(PayWay arg0, PayWay arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<PayWay> iterator = result.iterator(); iterator
						.hasNext();) {
					PayWay payWay = (PayWay) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(PayWay.class)
							.createModel(payWay);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					payWayStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listPayWay(getCallback);
		return payWayStore;
	}

	protected ListStore<BeanModel> getTruckStore() {
		final ListStore<BeanModel> truckStore = new ListStore<BeanModel>();
		AsyncCallback<List<Truck>> getCallback = new AsyncCallback<List<Truck>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<Truck> result) {
				Collections.sort(result, new Comparator<Truck>() {
					public int compare(Truck arg0, Truck arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<Truck> iterator = result.iterator(); iterator
						.hasNext();) {
					Truck truck = (Truck) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(Truck.class)
							.createModel(truck);
					beanModel.set("displayName", beanModel.get("code"));
					truckStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listTruck(getCallback);
		return truckStore;
	}
	
	protected ListStore<BeanModel> getDeclarePropertyStore() {
		final ListStore<BeanModel> declarePropertyStore = new ListStore<BeanModel>();
		AsyncCallback<List<DeclareProperty>> getCallback = new AsyncCallback<List<DeclareProperty>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<DeclareProperty> result) {
				Collections.sort(result, new Comparator<DeclareProperty>() {
					public int compare(DeclareProperty arg0, DeclareProperty arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<DeclareProperty> iterator = result.iterator(); iterator
						.hasNext();) {
					DeclareProperty declareProperty = (DeclareProperty) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(DeclareProperty.class)
							.createModel(declareProperty);
					beanModel.set("displayName", beanModel.get("code").toString()+beanModel.get("name").toString());
					declarePropertyStore.add(beanModel);
				}
			}
		};
		getDictionaryService().listDeclareProperty(getCallback);
		return declarePropertyStore;
	}

	protected ListStore<BeanModel> getAttachmentStore() {
		final ListStore<BeanModel> attachmentStore = new ListStore<BeanModel>();
		AsyncCallback<List<Attachment>> getCallback = new AsyncCallback<List<Attachment>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<Attachment> result) {
				Collections.sort(result, new Comparator<Attachment>() {
					public int compare(Attachment arg0, Attachment arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<Attachment> iterator = result.iterator(); iterator
						.hasNext();) {
					Attachment attachment = (Attachment) iterator.next();
					beanModel = BeanModelLookup.get()
							.getFactory(Attachment.class)
							.createModel(attachment);
					beanModel.set("displayName", beanModel.get("code"));
					attachmentStore.add(beanModel);
				}
				if (grid.getSelectionModel().getSelection().size() > 0) {
					getAttachmentWindow(true);
				}
			}

		};
		getDictionaryService().listAttachment(getCallback);
		return attachmentStore;
	}	

	protected void refleshPreEntryId(Long loadPortId) {
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(String result) {
				formBindings.getModel().set("preEntryId", result);
				save();
			}
		};
		getBusinessService().getPreEntryId(
				permitedEnterprise.getEnterpriseId(), loadPortId, true,
				callback);
	}

	@SuppressWarnings("rawtypes")
	protected Window getAttachmentWindow(boolean isNew) {
		if (!isNew) {
			if (attachmentWindow != null) {
				initAttachmentFormpanel();
				return attachmentWindow;
			}
		}

		attachmentWindow = new Window();
		attachmentWindow.setTabIndex(27);
		attachmentWindow.setIcon(Resources.ICONS.add());
		attachmentWindow.setResizable(false);
		attachmentWindow.setHeading("随附单据");
		attachmentWindow.setAutoHeight(true);
		attachmentFormpanel = new FormPanel();
		attachmentFormpanel.setLabelWidth(120);
		attachmentFormpanel.setWidth(300);
		attachmentFormpanel.setBodyBorder(false);
		attachmentFormpanel.setFrame(true);
		attachmentFormpanel.setHeaderVisible(false);

		
		FormData formData = new FormData("-20");

		String attachmentsString ="";
		if (formBindings.getModel()!=null) {
			attachmentsString = formBindings.getModel().get("certMark");
		}
		attachmentsString = attachmentsString == null ? "" : attachmentsString;
		Map<String, String> attachmentMap = MapUtil
				.stringToMap(attachmentsString);

		for (BeanModel beanModel : attachmentStore.getModels()) {
			TextField<String> attachment = new TextField<String>();
			attachment.setName(beanModel.get("code").toString());
			attachment.setFieldLabel(beanModel.get("name").toString());
			attachment.setValue(attachmentMap.get(beanModel.get("code")
					.toString()));
			attachmentFormpanel.add(attachment, formData);
		}
		attachmentFormpanel.setReadOnly(true);

		Button sureButton = new Button("确定");
		sureButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Map<String, String> map = new TreeMap<String, String>();
				for (Field field : attachmentFormpanel.getFields()) {
					String key = field.getName();
					String value = field.getValue() == null ? "" : field
							.getValue().toString();
					if (!value.trim().equals("")) {
						map.put(key, value);
					}
				}
				// formBindings.getModel().set("certMark",MapUtil.mapToString(map));
				Record record = store.getRecord((BeanModel) formBindings
						.getModel());
				if (record == null) {
					formBindings.getModel().set("certMark",
							MapUtil.mapToString(map));
				} else {
					record.set("certMark", MapUtil.mapToString(map));
				}
				attachmentWindow.hide();
				dealMode.focus();
			}
		});
		attachmentFormpanel.addButton(sureButton);

		attachmentFormpanel.setButtonAlign(HorizontalAlignment.CENTER);

		attachmentWindow.add(attachmentFormpanel);

		attachmentWindow.addListener(Events.AfterLayout,
				new Listener<ContainerEvent>() {
					@Override
					public void handleEvent(ContainerEvent be) {
						attachmentWindow.setWidth(attachmentFormpanel
								.getWidth());
					}
				});

		return attachmentWindow;
	}
	
	
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initAttachmentFormpanel() {
		String attachmentsString = formBindings.getModel().get("certMark");
		attachmentsString = attachmentsString == null ? "" : attachmentsString;
		Map<String, String> attachmentMap = MapUtil
				.stringToMap(attachmentsString);
		for (Field field : attachmentFormpanel.getFields()) {
			field.setValue(attachmentMap.get(field.getName()));
		}
	}
	
	protected void enableOperateButton(){
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
		if (isPass) {
			passButton.setText("退库");
		}else {
			passButton.setText("入库");
		}
		if (!isPass && permitedEnterprise.getCanPass()) {
			passButton.enable();
		} else if (isPass && permitedEnterprise.getCanDisPass()) {
			passButton.enable();
		} else {
			passButton.disable();
		}
		printButton.enable();
		declareButton.enable();
		declareDetailButton.enable();
		
	}
	
	protected void disableOperateButton(){
		editButton.disable();
		deleteButton.disable();
		printButton.disable();
		declareButton.disable();
		declareDetailButton.disable();
		passButton.disable();
	}
	
	protected void changePass() {
		String str=isPass?"退库":"入库";
		MessageBox.confirm("确认", "确定要"+str+"吗？",
			new Listener<MessageBoxEvent>() {
				@Override
				public void handleEvent(MessageBoxEvent be) {
					if (be.getButtonClicked().getText() == GXT.MESSAGES
							.messageBox_yes()) {
						formBindings.getModel().set("pass", !isPass);
						update();
					}
				}
			});
	}
	
	protected void addListener(CustomsDeclarationPanelListener listener) {
		this.listeners.add(listener);
	}
	
	protected void removeListener(CustomsDeclarationPanelListener listener) {
		this.listeners.remove(listener);
	}
	
	protected void notifyPermitedEnterpriseChange(PermitedEnterprise permitedEnterprise){
		for (CustomsDeclarationPanelListener listener : listeners) {
			listener.changePermitedEnterprise(permitedEnterprise);
			listener.changeCustomsDeclarationHead(null);
		}
	}
	
	protected void notifyCustomsDeclarationHeadChange(CustomsDeclarationHead selectedCustomsDeclarationHead){
		for (CustomsDeclarationPanelListener listener : listeners) {
			listener.changeCustomsDeclarationHead(selectedCustomsDeclarationHead);
		}
	}
	
	protected Window getSearchWindow(){
		if (searchWindow != null) {
			return searchWindow;
		}
		searchWindow = new Window();
		searchWindow.setResizable(false);
		searchWindow.setHeading("搜索报关单");
		searchWindow.setAutoHeight(true);
		FormPanel searchFormpanel = new FormPanel();
		searchFormpanel.setLabelWidth(70);
		searchFormpanel.setWidth(300);
		searchFormpanel.setBodyBorder(false);
		searchFormpanel.setFrame(true);
		searchFormpanel.setHeaderVisible(false);

		FormData formData = new FormData("-20");
		
		final TextField<String> preEntryIdField = new TextField<String>();
		preEntryIdField.setFieldLabel("预录入号");
		searchFormpanel.add(preEntryIdField, formData);
		
		final DateField startDate = new DateField();
		startDate.setFieldLabel("开始日期");
		searchFormpanel.add(startDate, formData);
		
		final DateField endDate = new DateField();
		endDate.setFieldLabel("结束日期");
		searchFormpanel.add(endDate, formData);
		
		
		final AsyncCallback<List<CustomsDeclarationHead>> getCallback = new AsyncCallback<List<CustomsDeclarationHead>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<CustomsDeclarationHead> result) {
				Collections.sort(result,
						new Comparator<CustomsDeclarationHead>() {
							public int compare(CustomsDeclarationHead arg0,
									CustomsDeclarationHead arg1) {
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
				store.removeAll();
				store.add(beanModelFactory.createModel(result));
			}
		};
		
		Button searchButton = new Button("搜索");
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				getBusinessService().listCustomsDeclarationHeadByConditions(
						permitedEnterprise.getEnterpriseId(), isExport,
						preEntryIdField.getValue(), startDate.getValue(),
						endDate.getValue(),getCallback);
				searchWindow.hide();
			}
		});
		searchFormpanel.addButton(searchButton);
		
		Button cancelButton = new Button("取消");
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				searchWindow.hide();
			}
		});
		searchFormpanel.addButton(cancelButton);

		searchFormpanel.setButtonAlign(HorizontalAlignment.CENTER);

		searchWindow.add(searchFormpanel);
		return searchWindow;
	}
	

}
