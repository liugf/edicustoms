package com.gavin.ediCustoms.client.core;

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
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
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
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyTabItem;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationGood;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.core.PackingItem;
import com.gavin.ediCustoms.entity.edi.dictionary.Unit;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PackingItemTabItem extends MyTabItem implements CustomsDeclarationPanelListener{
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
	private CustomsDeclarationHead selectedCustomsDeclarationHead;
	
	private PermitedEnterprise permitedEnterprise;
	
	private ListStore<BeanModel> goodStore;
	

	public PackingItemTabItem() {
		setText("录入装箱明细");
		store = new ListStore<BeanModel>();
		store.sort("no", Style.SortDir.ASC);
		beanModelFactory = BeanModelLookup.get().getFactory(PackingItem.class);
		
		addListener(Events.Select,
				new Listener<ComponentEvent>() {
					public void handleEvent(ComponentEvent be) {
						if (selectedCustomsDeclarationHead == null) {
							MessageBox.alert("提示", "请选择报关单表头", null);
							be.cancelBubble();
						} else {
							if (isDirty) {
								refreshStore();
								refreshGoodStore();
								resetState();
								isDirty=false;
							}
								
						}
					}
				});
	
	}
	
	public void changePermitedEnterprise(PermitedEnterprise permitedEnterprise) {
		this.permitedEnterprise = permitedEnterprise;
		isDirty=true;
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
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

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 280);
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
				beanModel = beanModelFactory.createModel(new PackingItem());
				formPanel.reset();
				formBindings.bind(beanModel);
				formBindings.addListener(Events.Bind,
						new Listener<BindingEvent>() {
							@Override
							public void handleEvent(BindingEvent be) {
								formBindings.getModel().set("no", newNo);
								formBindings.getModel().set("customsDeclarationHeadId",
										selectedCustomsDeclarationHead.getId());
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

	public void refreshStore() {
		AsyncCallback<List<PackingItem>> getCallback = new AsyncCallback<List<PackingItem>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<PackingItem> result) {
				Collections.sort(result, new Comparator<PackingItem>() {
					public int compare(PackingItem arg0, PackingItem arg1) {						
						return arg0.getNo()-arg1.getNo();
					}
				});
				store.removeAll();
				store.add(beanModelFactory.createModel(result));
			}
		};
		getBusinessService().listPackingItem(selectedCustomsDeclarationHead.getId(), getCallback);
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
		column.setWidth(176);
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
		panel.setLabelWidth(70);
		
		final ListStore<BeanModel> unitStore =getUnitStore();
		
		LayoutContainer main = new LayoutContainer();
		main.setLayout(new ColumnLayout());

		LayoutContainer left = new LayoutContainer();
		left.setStyleAttribute("paddingRight", "10px");
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(70);
		left.setLayout(layout);

		LayoutContainer right = new LayoutContainer();
		right.setStyleAttribute("paddingLeft", "10px");
		layout = new FormLayout();
		layout.setLabelWidth(70);
		right.setLayout(layout);	
		
		FormData formData = new FormData("-20");
		
		NumberField no = new NumberField();
		no.setPropertyEditorType(Integer.class);
		no.setName("no");
		no.setFieldLabel("序号");
		right.add(no, formData);
		
		goodStore=new ListStore<BeanModel>();
		ComboBox<BeanModel> good = new ComboBox<BeanModel>();
		good.setForceSelection(true);
		good.setEmptyText("请选择...");
		good.setDisplayField("displayName");
		good.setFieldLabel("选择货名");
		good.setStore(goodStore);
		good.setTypeAhead(true);
		good.setTriggerAction(TriggerAction.ALL);
		left.add(good,formData);
		
		final TextField<String> name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel("商品名称");
		right.add(name,formData);
			
		
		NumberField packNo=new NumberField();	
		packNo.setPropertyEditorType(Integer.class);
		packNo.setName("packNo");
		packNo.setFieldLabel("箱(件)数");
		left.add(packNo,formData);
		
		NumberField quantity=new NumberField();	
		quantity.setPropertyEditorType(Double.class);
		quantity.setFormat(NumberFormat.getFormat("#0.0000"));
		quantity.setName("quantity");
		quantity.setFieldLabel("包装数量");
		left.add(quantity,formData);
		
		ComboBox<BeanModel> declareUnit = new ComboBox<BeanModel>();
		declareUnit.setForceSelection(true);
		declareUnit.setName("declareUnit");
		declareUnit.setEmptyText("请选择...");
		declareUnit.setDisplayField("displayName");
		declareUnit.setFieldLabel("申报单位");
		declareUnit.setStore(unitStore);
		declareUnit.setTypeAhead(true);
		declareUnit.setTriggerAction(TriggerAction.ALL);
		right.add(declareUnit,formData);
		
		
		
		NumberField grossWeight=new NumberField();	
		grossWeight.setPropertyEditorType(Double.class);
		grossWeight.setFormat(NumberFormat.getFormat("#0.0000"));
		grossWeight.setName("grossWeight");
		grossWeight.setFieldLabel("总毛重");
		left.add(grossWeight,formData);
		
		NumberField netWeight=new NumberField();
		netWeight.setPropertyEditorType(Double.class);
		netWeight.setFormat(NumberFormat.getFormat("#0.0000"));
		netWeight.setName("netWeight");
		netWeight.setFieldLabel("总净重");
		left.add(netWeight,formData);
		
		NumberField grossWeightPerPack=new NumberField();	
		grossWeightPerPack.setPropertyEditorType(Double.class);
		grossWeightPerPack.setFormat(NumberFormat.getFormat("#0.0000"));
		grossWeightPerPack.setName("grossWeightPerPack");
		grossWeightPerPack.setFieldLabel("每箱毛重");
		left.add(grossWeightPerPack,formData);
		
		NumberField netWeightPerPack=new NumberField();
		netWeightPerPack.setPropertyEditorType(Double.class);
		netWeightPerPack.setFormat(NumberFormat.getFormat("#0.0000"));
		netWeightPerPack.setName("netWeightPerPack");
		netWeightPerPack.setFieldLabel("每箱净重");
		left.add(netWeightPerPack,formData);
		
		
		main.add(left, new ColumnData(.5));
		main.add(right, new ColumnData(.5));

		panel.add(main, new FormData("100%"));
		
		good.addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<BeanModel>>() {
			@Override
			public void handleEvent(SelectionChangedEvent<BeanModel> be) {
				if (be.getSelectedItem()==null) {
					return;
				}
				formBindings.getModel().set("name", be.getSelectedItem().get("name"));
				formBindings.getModel().set("declareUnit", be.getSelectedItem().get("declareUnit"));
			}
		});	

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
					PackingItem newItem = (PackingItem) beanModel.getBean();
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

		getBusinessService().savePackingItem(
				(PackingItem) beanModel.getBean(), newCallback);
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
		List<PackingItem> list = new ArrayList<PackingItem>();
		list.add((PackingItem) grid.getSelectionModel().getSelection().get(0)
				.getBean());
		getBusinessService().updatePackingItem(list, callback);
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
		getBusinessService().deletePackingItem(
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
			ids.add(((PackingItem) beanModel.getBean()).getId());
		}
		return ids;
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
	
	public void refreshGoodStore() {
		if (goodStore==null) {
			goodStore = new ListStore<BeanModel>();
		}
		if (permitedEnterprise==null) {
			return;
		}		
		AsyncCallback<List<CustomsDeclarationGood>> customsdeclarationGoodCallback = new AsyncCallback<List<CustomsDeclarationGood>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<CustomsDeclarationGood> result) {
				Collections.sort(result, new Comparator<CustomsDeclarationGood>() {
					public int compare(CustomsDeclarationGood arg0,
							CustomsDeclarationGood arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				goodStore.removeAll();
				BeanModel beanModel;				
				for (CustomsDeclarationGood customsDeclarationGood:result) {
					beanModel = BeanModelLookup.get()
							.getFactory(CustomsDeclarationGood.class)
							.createModel(customsDeclarationGood);
					beanModel.set("displayName", beanModel.get("name").toString());
					goodStore.add(beanModel);
				}
			}
		};
		getBusinessService().listCustomsDeclarationGood(selectedCustomsDeclarationHead.getId(),customsdeclarationGoodCallback);
	}
	
	public void changeCustomsDeclarationHead(CustomsDeclarationHead selectedCustomsDeclarationHead){
		this.selectedCustomsDeclarationHead = selectedCustomsDeclarationHead;
		isDirty=true;
	}

	
	

}
