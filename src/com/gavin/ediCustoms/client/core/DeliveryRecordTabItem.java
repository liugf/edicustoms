package com.gavin.ediCustoms.client.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyTabItem;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationGood;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.core.DeliveryRecord;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeliveryRecordTabItem extends MyTabItem implements CustomsDeclarationPanelListener {
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
	private Button printButton;

	private FormPanel formPanel;
	private Grid<BeanModel> grid;

	private CustomsDeclarationHead selectedCustomsDeclarationHead;
	
	private PermitedEnterprise permitedEnterprise;
		
	private ListStore<BeanModel> receiveEnterpriseStore;
	private ListStore<BeanModel> customsDeclarationGoodStore;
	

	public DeliveryRecordTabItem() {
		setText("收货单位");
		store = new ListStore<BeanModel>();
		store.sort("destinationEnterpriseId", Style.SortDir.ASC);
		beanModelFactory = BeanModelLookup.get().getFactory(DeliveryRecord.class);
		receiveEnterpriseStore=new ListStore<BeanModel>();
		customsDeclarationGoodStore=new ListStore<BeanModel>();
		
		addListener(Events.Select,
				new Listener<ComponentEvent>() {
					public void handleEvent(ComponentEvent be) {
						if (selectedCustomsDeclarationHead == null) {
							MessageBox.alert("提示", "请选择报关单表头", null);
							be.cancelBubble();
						} else {
							if (isDirty) {
								refreshStores();
								resetState();
								isDirty=false;
							}
							
						}
					}
				});
	}

	public PermitedEnterprise getPermitedEnterprise() {
		return permitedEnterprise;
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

		grid = createGrid();
		grid.getSelectionModel().addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<BeanModel>>() {
					public void handleEvent(SelectionChangedEvent<BeanModel> be) {
						store.rejectChanges();
						resetState();
					}
				});

		formPanel = createForm();

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 430);
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);

		frame.add(grid, westData);
		frame.add(formPanel, centerData);

		content.add(frame);

		// 添加工具栏
		ToolBar toolBar = new ToolBar();	

		addButton = new Button("添加", Resources.ICONS.add());
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {				
				beanModel = beanModelFactory.createModel(new DeliveryRecord());
				formPanel.reset();
				formBindings.bind(beanModel);
				formBindings.addListener(Events.Bind,
						new Listener<BindingEvent>() {
							@Override
							public void handleEvent(BindingEvent be) {
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
		
		toolBar.add(new SeparatorToolItem());
		
		Button saveAllButton = new Button("保存",Resources.ICONS.save());
		toolBar.add(saveAllButton);
		saveAllButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if(!checkBanlence()){
					MessageBox.alert("警告", "发货总量不平衡，请检查！", null);
					return;
				}
				
				final AsyncCallback<Void> saveAllCallback = new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						MessageBox.alert("警告", "发生错误，请刷新！", null);
					}

					@Override
					public void onSuccess(Void result) {
						MessageBox.info("提示", "保存成功", null);
					}
				};
				List<DeliveryRecord> list=new ArrayList<DeliveryRecord>();
				for(BeanModel bm:store.getModels()){
					DeliveryRecord deliveryRecord=bm.getBean();
					list.add(deliveryRecord);
				}
				getBusinessService().saveDeliveryRecords(selectedCustomsDeclarationHead.getId(), list, saveAllCallback);
			}
		});
		
		
		toolBar.add(new SeparatorToolItem());
		
		printButton = new Button("打印", Resources.ICONS.printer());
		toolBar.add(printButton);
		printButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("id", selectedCustomsDeclarationHead.getId());
				openNewWindow("/delivery.do", paramsMap);
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

	public void changeCustomsDeclarationHead(CustomsDeclarationHead selectedCustomsDeclarationHead) {
		this.selectedCustomsDeclarationHead=selectedCustomsDeclarationHead;
		isDirty=true;
	}
	
	private void refreshStores(){
		AsyncCallback<List<DeliveryRecord>> getCallback = new AsyncCallback<List<DeliveryRecord>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<DeliveryRecord> result) {
				Collections.sort(result, new Comparator<DeliveryRecord>() {
					public int compare(DeliveryRecord arg0, DeliveryRecord arg1) {						
						return arg0.getDestinationEnterpriseId().compareTo(arg1.getDestinationEnterpriseId());
					}
				});
				store.removeAll();
				store.add(beanModelFactory.createModel(result));
			}
		};
		getBusinessService().listDeliveryRecord(selectedCustomsDeclarationHead.getId(), getCallback);
		
		AsyncCallback<List<CustomsDeclarationGood>> goodCallback = new AsyncCallback<List<CustomsDeclarationGood>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<CustomsDeclarationGood> result) {
				Collections.sort(result, new Comparator<CustomsDeclarationGood>() {
					public int compare(CustomsDeclarationGood arg0, CustomsDeclarationGood arg1) {						
						return arg0.getNo()-arg1.getNo();
					}
				});
				customsDeclarationGoodStore.removeAll();
				BeanModelFactory bmf=BeanModelLookup.get().getFactory(CustomsDeclarationGood.class);	
				for(CustomsDeclarationGood customsDeclarationGood:result){
					BeanModel bm=bmf.createModel(customsDeclarationGood);
					bm.set("displayName",bm.get("no").toString()
							+ bm.get("name").toString());
					customsDeclarationGoodStore.add(bm);
				}				
			}
		};
		getBusinessService().listCustomsDeclarationGood(selectedCustomsDeclarationHead.getId(), goodCallback);
		
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
				receiveEnterpriseStore.removeAll();
				BeanModelFactory bmf=BeanModelLookup.get().getFactory(Enterprise.class);				
				for(Enterprise enterprise:result){
					BeanModel bm=bmf.createModel(enterprise);
					bm.set("displayName",bm.get("tradeCode").toString()
							+ bm.get("registeName").toString());
					receiveEnterpriseStore.add(bm);
				}
			}
		};
		getSystemService().listReceiveEnterprise(permitedEnterprise.getEnterpriseId(),callback);
	}

	private Grid<BeanModel> createGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();
		
		GridCellRenderer<BeanModel> enterpriseCellRenderer=new GridCellRenderer<BeanModel>() {
			@Override
			public Object render(BeanModel model, String property,
					com.extjs.gxt.ui.client.widget.grid.ColumnData config,
					int rowIndex, int colIndex, ListStore<BeanModel> store,
					Grid<BeanModel> grid) {
				Long id= model.get(property);
				if (receiveEnterpriseStore.getCount()==0) {
					return id;
				}
				Enterprise enterprise=receiveEnterpriseStore.findModel("id", id).getBean();				
				return enterprise.getRegisteName();
			}		
		};

		column.setId("destinationEnterpriseId");
		column.setRenderer(enterpriseCellRenderer);
		column.setHeader("收货单位");
		column.setWidth(200);
		column.setRowHeader(true);
		configs.add(column);
		
		GridCellRenderer<BeanModel> goodCellRenderer=new GridCellRenderer<BeanModel>() {
			@Override
			public Object render(BeanModel model, String property,
					com.extjs.gxt.ui.client.widget.grid.ColumnData config,
					int rowIndex, int colIndex, ListStore<BeanModel> store,
					Grid<BeanModel> grid) {
				Long id= model.get(property);
				if (customsDeclarationGoodStore.getCount()==0) {
					return id;
				}
				CustomsDeclarationGood customsDeclarationGood=customsDeclarationGoodStore.findModel("id", id).getBean();				
				return customsDeclarationGood.getName();
			}		
		};

		column = new ColumnConfig();
		column.setRenderer(goodCellRenderer);
		column.setId("customsDeclarationGoodId");
		column.setHeader("商品名称");
		column.setWidth(156);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("quantity");
		column.setHeader("数量");
		column.setWidth(70);
		configs.add(column);

		store.setMonitorChanges(true);
		Grid<BeanModel> grid = new Grid<BeanModel>(store, new ColumnModel(
				configs));
		grid.setBorders(true);
		return grid;
	}

	private FormPanel createForm() {
		final FormPanel panel = new FormPanel();
		panel.setLabelWidth(100);
		panel.setFieldWidth(250);
		panel.setHeaderVisible(false);	
		
		ComboBox<BeanModel> destinationEnterpriseId = new ComboBox<BeanModel>();
		destinationEnterpriseId.setAllowBlank(false);
		destinationEnterpriseId.setName("destinationEnterpriseId");
		destinationEnterpriseId.setForceSelection(true);
		destinationEnterpriseId.setEmptyText("请选择...");
		destinationEnterpriseId.setDisplayField("displayName");
		destinationEnterpriseId.setFieldLabel("收货单位");
		destinationEnterpriseId.setStore(receiveEnterpriseStore);
		destinationEnterpriseId.setTypeAhead(true);
		destinationEnterpriseId.setTriggerAction(TriggerAction.ALL);
		panel.add(destinationEnterpriseId);
		
		ComboBox<BeanModel> customsDeclarationGoodId = new ComboBox<BeanModel>();
		customsDeclarationGoodId.setAllowBlank(false);
		customsDeclarationGoodId.setName("customsDeclarationGoodId");
		customsDeclarationGoodId.setForceSelection(true);
		customsDeclarationGoodId.setEmptyText("请选择...");
		customsDeclarationGoodId.setDisplayField("displayName");
		customsDeclarationGoodId.setFieldLabel("发货商品");
		customsDeclarationGoodId.setStore(customsDeclarationGoodStore);
		customsDeclarationGoodId.setTypeAhead(true);
		customsDeclarationGoodId.setTriggerAction(TriggerAction.ALL);
		panel.add(customsDeclarationGoodId);
		
		NumberField quantity = new NumberField();
		quantity.setAllowBlank(false);
		quantity.setFormat(NumberFormat.getFormat("#0.0000"));
		quantity.setPropertyEditorType(Double.class);
		quantity.setName("quantity");
		quantity.setFieldLabel("数量");
		panel.add(quantity);
		
		NumberField packNo = new NumberField();
		packNo.setAllowBlank(false);
		packNo.setPropertyEditorType(Integer.class);
		packNo.setName("packNo");
		packNo.setFieldLabel("件数/包装");
		panel.add(packNo);
		
		NumberField grossWeight = new NumberField();
		grossWeight.setAllowBlank(false);
		grossWeight.setFormat(NumberFormat.getFormat("#0.0000"));
		grossWeight.setPropertyEditorType(Double.class);
		grossWeight.setName("grossWeight");
		grossWeight.setFieldLabel("毛重");
		panel.add(grossWeight);
		
		NumberField netWeight = new NumberField();
		netWeight.setAllowBlank(false);
		netWeight.setFormat(NumberFormat.getFormat("#0.0000"));
		netWeight.setPropertyEditorType(Double.class);
		netWeight.setName("netWeight");
		netWeight.setFieldLabel("净重");
		panel.add(netWeight);
		
		NumberField volume = new NumberField();
		volume.setFormat(NumberFormat.getFormat("#0.0000"));
		volume.setPropertyEditorType(Double.class);
		volume.setName("volume");
		volume.setFieldLabel("体积");
		panel.add(volume);

		formBindings = new FormBinding(panel, true);	
		
		formBindings.getBinding(customsDeclarationGoodId).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("id");
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return customsDeclarationGoodStore.findModel("id",
							value);
				} else {
					return null;
				}
			}
		});
		
		formBindings.getBinding(destinationEnterpriseId).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("id");
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return receiveEnterpriseStore.findModel("id",
							value);
				} else {
					return null;
				}
			}
		});
	

		formBindings.setStore((Store<BeanModel>) grid.getStore());

		panel.setReadOnly(true);

		panel.setButtonAlign(HorizontalAlignment.CENTER);

		saveButton = new Button("添加");
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

		updateButton = new Button("更改");
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
		store.insert(beanModel, 0);
		store.commitChanges();				

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

	private void update() {
		store.commitChanges();
		resetState();
	}

	private void delete() {
		for (Iterator<BeanModel> iterator = grid.getSelectionModel()
				.getSelectedItems().iterator(); iterator.hasNext();) {
			BeanModel beanModel = (BeanModel) iterator.next();
			store.remove(beanModel);
		}
		resetState();
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
			formPanel.reset();
			if (permitedEnterprise.getCanAdd()) {
				addButton.enable();
			}else {
				addButton.disable();
			}
			editButton.disable();
			deleteButton.disable();
		}
	}	

	
	private boolean checkBanlence(){
		Map<Long, Double> map=new HashMap<Long, Double>();
		for(BeanModel bm:customsDeclarationGoodStore.getModels()){
			CustomsDeclarationGood customsDeclarationGood=bm.getBean();
			map.put(customsDeclarationGood.getId(),customsDeclarationGood.getQuantity());
		}
		double totleNetWeight=0;
		double totleGrossWeight=0;
		for(BeanModel bm:store.getModels()){
			DeliveryRecord deliveryRecord=bm.getBean();
			double newValue=(double)map.get(deliveryRecord.getCustomsDeclarationGoodId())-deliveryRecord.getQuantity();
			map.put(deliveryRecord.getCustomsDeclarationGoodId(),newValue);
			totleNetWeight+=deliveryRecord.getNetWeight();
			totleGrossWeight+=deliveryRecord.getGrossWeight();
		}
		
		boolean result=true;
		double zero=0;
		for(Object o : map.keySet()){			
		    if (!map.get(o).equals(zero)) {
		    	result=false;
			}
		}
		if (selectedCustomsDeclarationHead.getNetWeight()!=totleNetWeight) {
			result=false;
		}
		if (selectedCustomsDeclarationHead.getGrossWeight()!=totleGrossWeight) {
			result=false;
		}
		return result;		
	}

}
