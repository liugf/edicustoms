package com.gavin.ediCustoms.client.contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyTabItem;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.entity.edi.Voucher;
import com.gavin.ediCustoms.entity.edi.contract.ContractMaterial;
import com.gavin.ediCustoms.entity.edi.dictionary.BringInMode;
import com.gavin.ediCustoms.entity.edi.dictionary.Country;
import com.gavin.ediCustoms.entity.edi.dictionary.TaxMode;
import com.gavin.ediCustoms.entity.edi.dictionary.Unit;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ContractMaterialTabItem extends MyTabItem {
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

	private FormPanel formPanel;
	private Grid<BeanModel> grid;

	private StoreFilterField<BeanModel> filter;
	private Long contractHeadId;
	
	private PermitedEnterprise permitedEnterprise;
	
	private ListStore<BeanModel> voucherStore;
	

	public ContractMaterialTabItem() {
		setText("料件");
		store = new ListStore<BeanModel>();
		store.sort("no", Style.SortDir.ASC);
		beanModelFactory = BeanModelLookup.get().getFactory(ContractMaterial.class);
	}

	public PermitedEnterprise getPermitedEnterprise() {
		return permitedEnterprise;
	}
	public void setPermitedEnterprise(PermitedEnterprise permitedEnterprise) {
		this.permitedEnterprise = permitedEnterprise;
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		
		setLayout(new FitLayout());		
		
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
				String no = record.get("no").toString().toLowerCase();
				String name = record.get("name").toString().toLowerCase();				
				if (no.indexOf(filter.toLowerCase()) != -1
						|| name.indexOf(filter.toLowerCase()) != -1) {
					return true;
				}
				return false;
			}
		};
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

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 330);
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);

		frame.add(grid, westData);
		frame.add(formPanel, centerData);

		content.add(frame);

		// 添加工具栏
		ToolBar toolBar = new ToolBar();		

		toolBar.add(new LabelToolItem("搜索："));
		toolBar.add(filter);

		toolBar.add(new SeparatorToolItem());

		addButton = new Button("添加", Resources.ICONS.add());
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				//查找最大no
				Integer maxNo=0;
				for(BeanModel bm:store.getModels()){
					if (maxNo<(Integer)bm.get("no")) {
						maxNo=(Integer)bm.get("no");
					}					
				}
				final Integer newNo=maxNo+1;
				beanModel = beanModelFactory.createModel(new ContractMaterial());
				formPanel.reset();
				formBindings.bind(beanModel);
				formBindings.addListener(Events.Bind,
						new Listener<BindingEvent>() {
							@Override
							public void handleEvent(BindingEvent be) {
								formBindings.getModel().set("no", newNo);
								formBindings.getModel().set("contractHeadId",
										contractHeadId);
								for (Field<?> field : formPanel.getFields()) {
									field.clearInvalid();
								}
								formBindings.removeAllListeners();

							}
						});
				formPanel.setReadOnly(false);
				formPanel.getFields().get(0).focus();
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
		toolBar.add(deleteButton);
		deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				MessageBox.confirm("确认", "确定要删除选中的条目吗？",
						new Listener<MessageBoxEvent>() {
							@Override
							public void handleEvent(MessageBoxEvent be) {
								if (be.getButtonClicked().getText() == GXT.MESSAGES.messageBox_yes()) {
									delete();
								}
							}
						});
			}
		});
		
		if (permitedEnterprise!=null) {
			if (permitedEnterprise.getCanAdd()) {
				addButton.enable();
			}else {
				addButton.disable();
			}
			if (permitedEnterprise.getCanUpdate()) {
				editButton.enable();
			}else {
				editButton.disable();
			}
			if (permitedEnterprise.getCanDelete()) {
				deleteButton.enable();
			}else {
				deleteButton.disable();
			}
		}

		content.setTopComponent(toolBar);
		add(content);
	}

	public void reflashStore(Long contractHeadId) {
		this.contractHeadId=contractHeadId;
		AsyncCallback<List<ContractMaterial>> getCallback = new AsyncCallback<List<ContractMaterial>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ContractMaterial> result) {
				Collections.sort(result, new Comparator<ContractMaterial>() {
					public int compare(ContractMaterial arg0, ContractMaterial arg1) {						
						return arg0.getNo()-arg1.getNo();
					}
				});
				store.removeAll();
				store.add(beanModelFactory.createModel(result));
			}
		};
		getBusinessService().listContractMaterial(contractHeadId, getCallback);
	}

	private Grid<BeanModel> createGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();

		column.setId("no");
		column.setHeader("序号");
		column.setWidth(100);
		column.setRowHeader(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("name");
		column.setHeader("商品名称");
		column.setWidth(226);
		configs.add(column);

		store.setMonitorChanges(true);
		Grid<BeanModel> grid = new Grid<BeanModel>(store, new ColumnModel(
				configs));
		grid.setBorders(true);
		return grid;
	}

	private FormPanel createForm() {
		final FormPanel panel = new FormPanel();
		panel.setHeaderVisible(false);		
		
		refleshVoucherStore();
		ComboBox<BeanModel> voucher = new ComboBox<BeanModel>();
		voucher.setForceSelection(true);
		voucher.setEmptyText("请选择...");
		voucher.setDisplayField("displayName");
		voucher.setFieldLabel("商品选择");
		voucher.setStore(voucherStore);
		voucher.setTypeAhead(true);
		voucher.setTriggerAction(TriggerAction.ALL);
		panel.add(voucher);
		
		final TextField<String> code = new TextField<String>();
		code.setAllowBlank(false);
		code.setName("code");
		code.setFieldLabel("商品编码");
		panel.add(code);

		final TextField<String> plusCode = new TextField<String>();
		plusCode.setName("plusCode");
		plusCode.setFieldLabel("附加编码");
		panel.add(plusCode);	
		
		final TextField<String> name = new TextField<String>();
		name.setAllowBlank(false);
		name.setName("name");
		name.setFieldLabel("商品名称");
		panel.add(name);
		
		voucher.addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<BeanModel>>() {
			@Override
			public void handleEvent(SelectionChangedEvent<BeanModel> be) {
				if (be.getSelectedItem()==null) {
					return;
				}
				formBindings.getModel().set("GoodClassificationId", be.getSelectedItem().get("id"));
				formBindings.getModel().set("code", be.getSelectedItem().get("code"));
				formBindings.getModel().set("plusCode", be.getSelectedItem().get("plusCode"));
				formBindings.getModel().set("name", be.getSelectedItem().get("name"));
				formBindings.getModel().set("declareUnit", be.getSelectedItem().get("declareUnit"));
				formBindings.getModel().set("unit1", be.getSelectedItem().get("unit1"));
				formBindings.getModel().set("unit2", be.getSelectedItem().get("unit2"));
			}
		});		
		

		TextField<String> goodModel = new TextField<String>();
		goodModel.setName("goodModel");
		goodModel.setFieldLabel("商品规格");
		panel.add(goodModel);
		
		final ListStore<BeanModel> unitStore =getUnitStore();
		ComboBox<BeanModel> declareUnit = new ComboBox<BeanModel>();
		declareUnit.setForceSelection(true);
		declareUnit.setAllowBlank(false);
		declareUnit.setName("declareUnit");
		declareUnit.setEmptyText("请选择...");
		declareUnit.setDisplayField("displayName");
		declareUnit.setFieldLabel("申报单位");
		declareUnit.setStore(unitStore);
		declareUnit.setTypeAhead(true);
		declareUnit.setTriggerAction(TriggerAction.ALL);
		panel.add(declareUnit);
		
		ComboBox<BeanModel> unit1 = new ComboBox<BeanModel>();
		unit1.setForceSelection(true);
		unit1.setName("unit1");
		unit1.setEmptyText("请选择...");
		unit1.setDisplayField("displayName");
		unit1.setFieldLabel("第一单位");
		unit1.setStore(unitStore);
		unit1.setTypeAhead(true);
		unit1.setTriggerAction(TriggerAction.ALL);
		panel.add(unit1);
		
		ComboBox<BeanModel> unit2 = new ComboBox<BeanModel>();
		unit2.setForceSelection(true);
		unit2.setName("unit2");
		unit2.setEmptyText("请选择...");
		unit2.setDisplayField("displayName");
		unit2.setFieldLabel("第二单位");
		unit2.setStore(unitStore);
		unit2.setTypeAhead(true);
		unit2.setTriggerAction(TriggerAction.ALL);
		panel.add(unit2);
		
		NumberField price=new NumberField();		
		price.setPropertyEditorType(Double.class);
		price.setFormat(NumberFormat.getFormat("#0.00"));
		price.setAllowBlank(false);
		price.setName("price");
		price.setFieldLabel("单价");
		panel.add(price);
		
		price.addListener(Events.Blur, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				countTotalPrise();				
			}			
		});
		
		NumberField quantity=new NumberField();		
		quantity.setPropertyEditorType(Double.class);
		quantity.setFormat(NumberFormat.getFormat("#0.0000"));
		quantity.setAllowBlank(false);
		quantity.setName("quantity");
		quantity.setFieldLabel("数量");
		panel.add(quantity);
		
		quantity.addListener(Events.Blur, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				countTotalPrise();				
			}			
		});
		
		NumberField totalPrice=new NumberField();		
		totalPrice.setFormat(NumberFormat.getFormat("#0.00"));
		totalPrice.setPropertyEditorType(Double.class);
		totalPrice.setAllowBlank(false);
		totalPrice.setName("totalPrice");
		totalPrice.setFieldLabel("总价");
		panel.add(totalPrice);
		
		totalPrice.addListener(Events.Focus, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				countTotalPrise();				
			}			
		});
		
		totalPrice.addListener(Events.Blur, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				countTotalPrise();				
			}			
		});
		
		final ListStore<BeanModel> countryStore =getCountryStore();
		ComboBox<BeanModel> originCountry = new ComboBox<BeanModel>();
		originCountry.setForceSelection(true);
		originCountry.setAllowBlank(false);
		originCountry.setName("originCountry");
		originCountry.setEmptyText("请选择...");
		originCountry.setDisplayField("displayName");
		originCountry.setFieldLabel("产销国");
		originCountry.setStore(countryStore);
		originCountry.setTypeAhead(true);
		originCountry.setTriggerAction(TriggerAction.ALL);
		panel.add(originCountry);
		
		final ListStore<BeanModel> taxModeStore =getTaxModeStore();
		ComboBox<BeanModel> taxMode = new ComboBox<BeanModel>();
		taxMode.setForceSelection(true);
		taxMode.setAllowBlank(false);
		taxMode.setName("taxMode");
		taxMode.setEmptyText("请选择...");
		taxMode.setDisplayField("displayName");
		taxMode.setFieldLabel("征免方式");
		taxMode.setStore(taxModeStore);
		taxMode.setTypeAhead(true);
		taxMode.setTriggerAction(TriggerAction.ALL);
		panel.add(taxMode);	
		
		final ListStore<BeanModel> bringInModeStore =getBringInModeStore();
		ComboBox<BeanModel> bringInMode = new ComboBox<BeanModel>();
		bringInMode.setForceSelection(true);
		bringInMode.setAllowBlank(false);
		bringInMode.setName("bringInMode");
		bringInMode.setEmptyText("请选择...");
		bringInMode.setDisplayField("displayName");
		bringInMode.setFieldLabel("进口方式");
		bringInMode.setStore(bringInModeStore);
		bringInMode.setTypeAhead(true);
		bringInMode.setTriggerAction(TriggerAction.ALL);
		panel.add(bringInMode);	
		
		NumberField taxScale=new NumberField();		
		taxScale.setPropertyEditorType(Double.class);
		taxScale.setFormat(NumberFormat.getFormat("#0.0000"));
		taxScale.setAllowBlank(false);
		taxScale.setName("taxScale");
		taxScale.setFieldLabel("征税比例");
		panel.add(taxScale);
		
		NumberField no=new NumberField();		
		no.setPropertyEditorType(Integer.class);
		no.setAllowBlank(false);
		no.setName("no");
		no.setFieldLabel("序号");
		panel.add(no);
		

		formBindings = new FormBinding(panel, true);		
		
		formBindings.getBinding(declareUnit).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return unitStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		
		formBindings.getBinding(unit1).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return unitStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		
		formBindings.getBinding(unit2).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return unitStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		
		formBindings.getBinding(originCountry).setConverter(new Converter() {
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
		
		formBindings.getBinding(taxMode).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return taxModeStore.findModel("code", (String) value);
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
					ContractMaterial newItem = (ContractMaterial) beanModel.getBean();
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
					}else {
						addButton.disable();
					}
					
				}
			}
		};

		getBusinessService().saveContractMaterial(
				(ContractMaterial) beanModel.getBean(), newCallback);
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
		List<ContractMaterial> list = new ArrayList<ContractMaterial>();
		list.add((ContractMaterial) grid.getSelectionModel().getSelection().get(0)
				.getBean());
		getBusinessService().updateContractMaterial(list, callback);
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
		getBusinessService().deleteContractMaterial(
				models2Ids(grid.getSelectionModel().getSelectedItems()),
				deleteCallback);
	}

	public void resetState() {
		formPanel.setReadOnly(true);
		saveButton.setVisible(false);
		cancelButton.setVisible(false);
		updateButton.setVisible(true);
		updateButton.disable();
		resetButton.setVisible(true);
		resetButton.disable();
		if (permitedEnterprise.getCanAdd()) {
			addButton.enable();
		}else {
			addButton.disable();
		}
		if (permitedEnterprise.getCanUpdate()) {
			editButton.enable();
		}else {
			editButton.disable();
		}
		if (permitedEnterprise.getCanDelete()) {
			deleteButton.enable();
		}else {
			deleteButton.disable();
		}
		formPanel.reset();
		if (grid.getSelectionModel().getSelection().size() > 0) {
			formBindings.bind((BeanModel) grid.getSelectionModel()
					.getSelection().get(0));
		} else {
			formBindings.unbind();
			if (permitedEnterprise.getCanAdd()) {
				addButton.enable();
			}else {
				addButton.disable();
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
			ids.add(((ContractMaterial) beanModel.getBean()).getId());
		}
		return ids;
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
	
	private ListStore<BeanModel> getTaxModeStore() {
		final ListStore<BeanModel> taxModeStore = new ListStore<BeanModel>();
		AsyncCallback<List<TaxMode>> getCallback = new AsyncCallback<List<TaxMode>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<TaxMode> result) {
				Collections.sort(result, new Comparator<TaxMode>() {
					public int compare(TaxMode arg0, TaxMode arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<TaxMode> iterator = result.iterator(); iterator
						.hasNext();) {
					TaxMode taxMode = (TaxMode) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(TaxMode.class)
							.createModel(taxMode);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					taxModeStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listTaxMode(getCallback);
		return taxModeStore;
	}
	
	
	private ListStore<BeanModel> getUnitStore() {
		final ListStore<BeanModel> unitStore = new ListStore<BeanModel>();
		AsyncCallback<List<Unit>> getCallback = new AsyncCallback<List<Unit>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<Unit> result) {
				Collections.sort(result, new Comparator<Unit>() {
					public int compare(Unit arg0, Unit arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<Unit> iterator = result.iterator(); iterator
						.hasNext();) {
					Unit unit = (Unit) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(Unit.class)
							.createModel(unit);
					beanModel.set("displayName", beanModel.get("code")
							.toString() + beanModel.get("name").toString());
					unitStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listUnit(getCallback);
		return unitStore;
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
					beanModel = BeanModelLookup.get().getFactory(BringInMode.class)
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
	
	public void refleshVoucherStore() {
		if (voucherStore==null) {
			voucherStore = new ListStore<BeanModel>();
		}
		if (permitedEnterprise==null) {
			return;
		}
		AsyncCallback<List<Voucher>> getCallback = new AsyncCallback<List<Voucher>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<Voucher> result) {
				Collections.sort(result, new Comparator<Voucher>() {
					public int compare(Voucher arg0, Voucher arg1) {
						return arg0.getNo()-arg1.getNo();
					}
				});
				voucherStore.removeAll();
				BeanModel beanModel;
				for (Iterator<Voucher> iterator = result.iterator(); iterator
						.hasNext();) {
					Voucher voucher = (Voucher) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(Voucher.class)
							.createModel(voucher);
					beanModel.set("displayName", beanModel.get("no")
							.toString() + beanModel.get("name").toString());
					voucherStore.add(beanModel);
				}
			}

		};
		getBusinessService().listVoucher(permitedEnterprise.getEnterpriseId(),getCallback);	
	}
	
	private void countTotalPrise(){
		double quantity=formBindings.getModel().get("quantity");
		double price=formBindings.getModel().get("price");
		formBindings.getModel().set("totalPrice", quantity*price);		
	}

}
