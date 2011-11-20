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
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyTabItem;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.entity.edi.contract.ContractConsume;
import com.gavin.ediCustoms.entity.edi.contract.ContractMaterial;
import com.gavin.ediCustoms.entity.edi.contract.ContractProduct;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ContractConsumeTabItem extends MyTabItem {
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
	private Long contractProductId;
	
	private PermitedEnterprise permitedEnterprise;
	
	private ListStore<BeanModel> contractProductStore;
	private ListStore<BeanModel> contractMaterialStore;
	
	private ComboBox<BeanModel> contractProductComboBox;
	

	public ContractConsumeTabItem() {
		setText("单耗");
		store = new ListStore<BeanModel>();
		store.sort("no", Style.SortDir.ASC);
		beanModelFactory = BeanModelLookup.get().getFactory(ContractConsume.class);
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
				
				ContractMaterial contractMaterial = contractMaterialStore.findModel("id", record.get("contractMaterialId")).getBean();
				String name = contractMaterial.getName().toLowerCase();	
				String no=contractMaterial.getNo()+"";
				if (no.indexOf(filter.toLowerCase()) != -1
						|| name.indexOf(filter.toLowerCase()) != -1) {
					return true;
				}
				return false;
			}
		};
		filter.bind(store);
		
		contractMaterialStore=new ListStore<BeanModel>();
		contractProductStore=new ListStore<BeanModel>();

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
		
		
		toolBar.add(new LabelToolItem("成品名称："));
		contractProductComboBox = new ComboBox<BeanModel>();
		contractProductComboBox.setEmptyText("请选择...");
		contractProductComboBox.setDisplayField("displayName");
		contractProductComboBox.setStore(contractProductStore);
		contractProductComboBox.setTypeAhead(true);
		contractProductComboBox.setTriggerAction(TriggerAction.ALL);

		contractProductComboBox.addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<BeanModel>>() {

					@Override
					public void handleEvent(SelectionChangedEvent<BeanModel> be) {
						if (be.getSelectedItem() == null) {
							return;
						}
						contractProductId=be.getSelectedItem().get("id");
						reflashStore();
						if (permitedEnterprise.getCanAdd()) {
							addButton.enable();
						}else {
							addButton.disable();
						}
						editButton.disable();
						deleteButton.disable();
					}

				});
		toolBar.add(contractProductComboBox);

		toolBar.add(new SeparatorToolItem());

		toolBar.add(new LabelToolItem("搜索："));
		toolBar.add(filter);

		toolBar.add(new SeparatorToolItem());

		addButton = new Button("添加", Resources.ICONS.add());
		addButton.disable();
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				beanModel = beanModelFactory.createModel(new ContractConsume());
				formPanel.reset();
				formBindings.bind(beanModel);
				formBindings.addListener(Events.Bind,
						new Listener<BindingEvent>() {
							@Override
							public void handleEvent(BindingEvent be) {
								formBindings.getModel().set("contractProductId",
										contractProductId);
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
								if (be.getButtonClicked().getText() == GXT.MESSAGES.messageBox_yes()) {
									delete();
								}
							}
						});
			}
		});

		content.setTopComponent(toolBar);
		add(content);
	}

	private void reflashStore() {
		AsyncCallback<List<ContractConsume>> getCallback = new AsyncCallback<List<ContractConsume>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ContractConsume> result) {
				/*Collections.sort(result, new Comparator<ContractConsume>() {
					public int compare(ContractConsume arg0, ContractConsume arg1) {						
						return arg0.getNo()-arg1.getNo();
					}
				});*/
				store.removeAll();
				store.add(beanModelFactory.createModel(result));
			}
		};
		getBusinessService().listContractConsumeByProductId(contractProductId, getCallback);
	}

	private Grid<BeanModel> createGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();

		GridCellRenderer<BeanModel> contractMaterialNoCellRenderer=new GridCellRenderer<BeanModel>() {
			@Override
			public Object render(BeanModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BeanModel> store, Grid<BeanModel> grid) {
				Long contractMaterialId= model.get(property);				
				return contractMaterialStore.findModel("id", contractMaterialId).get("no");
			}
		};
		column.setId("contractMaterialId");
		column.setRenderer(contractMaterialNoCellRenderer);
		column.setHeader("料件序号");
		column.setWidth(100);
		column.setRowHeader(true);
		configs.add(column);

		
		GridCellRenderer<BeanModel> contractMaterialNameCellRenderer=new GridCellRenderer<BeanModel>() {
			@Override
			public Object render(BeanModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BeanModel> store, Grid<BeanModel> grid) {
				Long contractMaterialId= model.get(property);				
				return contractMaterialStore.findModel("id", contractMaterialId).get("name");
			}
		};
		
		column = new ColumnConfig();
		column.setId("contractMaterialId");
		column.setRenderer(contractMaterialNameCellRenderer);
		column.setHeader("料件名称");
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
		
		ComboBox<BeanModel> contractMaterial = new ComboBox<BeanModel>();
		contractMaterial.setName("contractMaterialId");
		contractMaterial.setForceSelection(true);
		contractMaterial.setEmptyText("请选择...");
		contractMaterial.setDisplayField("displayName");
		contractMaterial.setFieldLabel("料件名称");
		contractMaterial.setStore(contractMaterialStore);
		contractMaterial.setTypeAhead(true);
		contractMaterial.setTriggerAction(TriggerAction.ALL);
		panel.add(contractMaterial);		
		
		NumberField used=new NumberField();		
		used.setPropertyEditorType(Double.class);
		used.setFormat(NumberFormat.getFormat("#0.0000"));
		used.setAllowBlank(false);
		used.setName("used");
		used.setFieldLabel("单耗");
		panel.add(used);
		
		NumberField wasted=new NumberField();		
		wasted.setPropertyEditorType(Double.class);
		wasted.setFormat(NumberFormat.getFormat("#0.0000"));
		wasted.setAllowBlank(false);
		wasted.setName("wasted");
		wasted.setFieldLabel("损耗(%)");
		panel.add(wasted);
		

		formBindings = new FormBinding(panel, true);		
		
		formBindings.getBinding(contractMaterial).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("id");
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return contractMaterialStore.findModel("id", (Long) value);
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
					MessageBox.alert("提醒", "已存在相同条目，添加失败", null);
				} else {
					ContractConsume newItem = (ContractConsume) beanModel.getBean();
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

		getBusinessService().saveContractConsume(
				(ContractConsume) beanModel.getBean(), newCallback);
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
		List<ContractConsume> list = new ArrayList<ContractConsume>();
		list.add((ContractConsume) grid.getSelectionModel().getSelection().get(0)
				.getBean());
		getBusinessService().updateContractConsume(list, callback);
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
		getBusinessService().deleteContractConsume(
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
	
	private void reset(){
		if (contractProductComboBox.getValue()!=null) {
			return;
		}
		contractProductComboBox.reset();
		store.removeAll();
		formPanel.reset();
		formPanel.setReadOnly(true);
		addButton.disable();
		editButton.disable();
		deleteButton.disable();
	}

	private List<Long> models2Ids(List<BeanModel> models) {
		List<Long> ids = new ArrayList<Long>();
		for (Iterator<BeanModel> iterator = models.iterator(); iterator
				.hasNext();) {
			BeanModel beanModel = (BeanModel) iterator.next();
			ids.add(((ContractConsume) beanModel.getBean()).getId());
		}
		return ids;
	}
	
	public void refleshProductAndMaterialStore(Long contractHeadId){		
		AsyncCallback<List<ContractProduct>> contractProductCallback = new AsyncCallback<List<ContractProduct>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ContractProduct> result) {
				Collections.sort(result, new Comparator<ContractProduct>() {
					public int compare(ContractProduct arg0, ContractProduct arg1) {
						return arg0.getNo()-arg1.getNo();
					}
				});
				contractProductStore.removeAll();
				BeanModel beanModel;
				for (Iterator<ContractProduct> iterator = result.iterator(); iterator
						.hasNext();) {
					ContractProduct contractProduct = (ContractProduct) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(ContractProduct.class)
							.createModel(contractProduct);
					beanModel.set("displayName", beanModel.get("no")
							.toString() + beanModel.get("name").toString());
					contractProductStore.add(beanModel);
				}				
			}

		};
		getBusinessService().listContractProduct(contractHeadId,contractProductCallback);	
		
		AsyncCallback<List<ContractMaterial>> contractMaterialCallback = new AsyncCallback<List<ContractMaterial>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ContractMaterial> result) {
				Collections.sort(result, new Comparator<ContractMaterial>() {
					public int compare(ContractMaterial arg0, ContractMaterial arg1) {
						return arg0.getNo()-arg1.getNo();
					}
				});
				contractMaterialStore.removeAll();
				BeanModel beanModel;
				for (Iterator<ContractMaterial> iterator = result.iterator(); iterator
						.hasNext();) {
					ContractMaterial contractMaterial = (ContractMaterial) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(ContractMaterial.class)
							.createModel(contractMaterial);
					beanModel.set("displayName", beanModel.get("no")
							.toString() + beanModel.get("name").toString());
					contractMaterialStore.add(beanModel);
				}
				reset();
			}

		};
		getBusinessService().listContractMaterial(contractHeadId,contractMaterialCallback);
	}
	
	

}
