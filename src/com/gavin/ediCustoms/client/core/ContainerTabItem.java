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
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.Converter;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.BindingEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.AggregationRenderer;
import com.extjs.gxt.ui.client.widget.grid.AggregationRowConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyTabItem;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.entity.edi.contract.ContractProduct;
import com.gavin.ediCustoms.entity.edi.core.Container;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.gavin.ediCustoms.entity.edi.dictionary.Bracket;
import com.gavin.ediCustoms.entity.edi.dictionary.ContainerSize;
import com.gavin.ediCustoms.entity.edi.dictionary.IronChest;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ContainerTabItem extends MyTabItem implements CustomsDeclarationPanelListener{
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

	private CustomsDeclarationHead selectedCustomsDeclarationHead;

	private PermitedEnterprise permitedEnterprise;

	private ListStore<BeanModel> goodStore;
	
	private ListStore<BeanModel> containerSizeStore;
	private ListStore<BeanModel> ironChestStore;
	private ListStore<BeanModel> bracketStore;

	public ContainerTabItem() {
		setText("集装箱");
		store = new ListStore<BeanModel>();
		store.sort("containerNo", Style.SortDir.ASC);
		beanModelFactory = BeanModelLookup.get().getFactory(Container.class);
		
		addListener(Events.Select,
				new Listener<ComponentEvent>() {
					public void handleEvent(ComponentEvent be) {
						if (selectedCustomsDeclarationHead == null) {
							MessageBox.alert("提示", "请选择报关单表头", null);
							be.cancelBubble();
						} else {
							refreshStore();
						}
					}
				});
	}

	public PermitedEnterprise getPermitedEnterprise() {
		return permitedEnterprise;
	}

	public void changePermitedEnterprise(PermitedEnterprise permitedEnterprise) {
		this.permitedEnterprise = permitedEnterprise;
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		
		containerSizeStore=getContainerSizeStore();
		ironChestStore=getIronChestStore();
		bracketStore=getBracketStore();

		ContentPanel content = new ContentPanel();
		content.setHeaderVisible(false);
		content.setBodyBorder(false);
		content.setLayout(new RowLayout(Orientation.VERTICAL));

		LayoutContainer frame1 = new LayoutContainer();
		frame1.setStyleAttribute("padding", "10px");
		frame1.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame1.setLayout(new BorderLayout());

		
		grid = createGrid();
		grid.getSelectionModel().addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<BeanModel>>() {
					public void handleEvent(SelectionChangedEvent<BeanModel> be) {
						store.rejectChanges();
						resetState();
					}
				});
		
		

		formPanel = createForm();

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 305);
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);

		frame1.add(grid, westData);
		frame1.add(formPanel, centerData);

		content.add(frame1);
		
		LayoutContainer frame2 = new LayoutContainer();
		frame2.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame2.setLayout(new RowLayout(Orientation.HORIZONTAL));
		
		RowData data1 = new RowData(.45, 1);
		data1.setMargins(new Margins(10));
		RowData data2 = new RowData(.55, 1);
		data2.setMargins(new Margins(10));
		frame2.add(createTypeGrid(), data1);
		frame2.add(createBracketGrid(), data2);
		
		RowData data = new RowData(1, 0.5);
		content.add(frame1, data);
		content.add(frame2, data);
		
		

		// 添加工具栏
		ToolBar toolBar = new ToolBar();		

		addButton = new Button("添加", Resources.ICONS.add());
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {				
				beanModel = beanModelFactory.createModel(new Container());
				formPanel.reset();
				formBindings.bind(beanModel);
				formBindings.addListener(Events.Bind,
						new Listener<BindingEvent>() {
							@Override
							public void handleEvent(BindingEvent be) {
								formBindings.getModel().set(
										"customsDeclarationHeadId",
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
								if (be.getButtonClicked().getText() == GXT.MESSAGES
										.messageBox_yes()) {
									delete();
								}
							}
						});
			}
		});
		
		

			
		if (permitedEnterprise != null) {
			if (permitedEnterprise.getCanAdd()) {
				addButton.enable();
			} else {
				addButton.disable();
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
		}

		content.setTopComponent(toolBar);
		add(content);
	}

	public void changeCustomsDeclarationHead(CustomsDeclarationHead selectedCustomsDeclarationHead) {
		this.selectedCustomsDeclarationHead = selectedCustomsDeclarationHead;
	}

	private Grid<BeanModel> createGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();

		column.setId("containerNo");
		column.setHeader("集装箱号");
		column.setWidth(100);
		column.setRowHeader(true);
		configs.add(column);
		
		GridCellRenderer<BeanModel> sizeCellRenderer=new GridCellRenderer<BeanModel>() {

			@Override
			public Object render(BeanModel model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BeanModel> store, Grid<BeanModel> grid) {
				String code= model.get(property);	
				ContainerSize containerSize=containerSizeStore.findModel("code", code).getBean();
				return containerSize.getSize();				
			}
		};

		column = new ColumnConfig();
		column.setRenderer(sizeCellRenderer);
		column.setId("size");
		column.setHeader("尺寸");
		column.setWidth(100);
		configs.add(column);
	
		column = new ColumnConfig();
		column.setId("valentNum");
		column.setHeader("标准箱个数");
		column.setWidth(100);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);  
		
		AggregationRowConfig<BeanModel> statistics = new AggregationRowConfig<BeanModel>();  
	    statistics.setHtml("containerNo", "统计");  
	    
	    statistics.setSummaryType("size", SummaryType.COUNT);  
	    statistics.setRenderer("size", new AggregationRenderer<BeanModel>() {  
	        public Object render(Number value, int colIndex, Grid<BeanModel> grid, ListStore<BeanModel> store) {  
	          return "集装箱"+ContainerTabItem.this.store.getCount()+"个";  
	        }  
	      });
	    
	    statistics.setSummaryType("valentNum", SummaryType.SUM);  
	    statistics.setRenderer("valentNum", new AggregationRenderer<BeanModel>() {  
	        public Object render(Number value, int colIndex, Grid<BeanModel> grid, ListStore<BeanModel> store) {  
		        if (value==null) {
					return "";
				}
	          return "标准箱"+value.intValue()+"个";  
	        }  
	      }); 
	    cm.addAggregationRow(statistics);  

		store.setMonitorChanges(true);
		Grid<BeanModel> grid = new Grid<BeanModel>(store, cm);
		grid.setView(new GridView(){
			@Override
			protected void afterRender() { 
				refresh(true);			    
			}
		});
		grid.setBorders(true);		
		
		return grid;
	}
	
	private Grid<BeanModel> createTypeGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();

		column.setId("code");
		column.setHeader("类型");
		column.setWidth(50);
		column.setRowHeader(true);
		configs.add(column);		
		
		column = new ColumnConfig();		
		column.setId("size");
		column.setHeader("尺寸");
		column.setWidth(100);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("description");
		column.setHeader("特征");
		column.setWidth(150);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("weight");
		column.setHeader("重量");
		column.setWidth(50);
		configs.add(column);

		Grid<BeanModel> grid = new Grid<BeanModel>(ironChestStore, new ColumnModel(
				configs));
		grid.setBorders(true);
		grid.setStyleAttribute("backgroundColor", "#dfe8f6");
		return grid;
	}
	
	private Grid<BeanModel> createBracketGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();

		column.setId("code");
		column.setHeader("托架代码");
		column.setWidth(50);
		column.setRowHeader(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("manufacturer");
		column.setHeader("生产厂家");
		column.setWidth(100);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("modelNo");
		column.setHeader("型号");
		column.setWidth(70);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("specification");
		column.setHeader("规格");
		column.setWidth(150);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("weight");
		column.setHeader("重量");
		column.setWidth(50);
		configs.add(column);

		Grid<BeanModel> grid = new Grid<BeanModel>(bracketStore, new ColumnModel(
				configs));
		grid.setBorders(true);
		return grid;
	}
	

	private FormPanel createForm() {
		final FormPanel panel = new FormPanel();
		panel.setLabelWidth(100);
		panel.setHeaderVisible(false);
		
		TextField<String> code = new TextField<String>();
		code.setName("containerNo");
		code.setFieldLabel("集装箱号");
		code.setAllowBlank(false);
		panel.add(code);
		
		final ComboBox<BeanModel> size = new ComboBox<BeanModel>();
		size.setTabIndex(9);
		size.setName("size");
		size.setForceSelection(true);
		size.setEmptyText("请选择...");
		size.setDisplayField("size");
		size.setFieldLabel("尺寸");
		size.setStore(containerSizeStore);
		size.setTypeAhead(true);
		size.setTriggerAction(TriggerAction.ALL);
		size.setAllowBlank(false);
		panel.add(size);		
		
		size.addListener(Events.Blur, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				formBindings.getModel().set("valentNum", size.getValue().get("valentNum"));
			}			
		});

		ComboBox<BeanModel> type = new ComboBox<BeanModel>();
		type.setTabIndex(9);
		type.setName("type");
		type.setForceSelection(true);
		type.setEmptyText("请选择...");
		type.setDisplayField("code");
		type.setFieldLabel("类型");
		type.setStore(ironChestStore);
		type.setTypeAhead(true);
		type.setTriggerAction(TriggerAction.ALL);
		panel.add(type);
		
		ComboBox<BeanModel> bracket = new ComboBox<BeanModel>();
		bracket.setTabIndex(9);
		bracket.setName("bracket");
		bracket.setForceSelection(true);
		bracket.setEmptyText("请选择...");
		bracket.setDisplayField("code");
		bracket.setFieldLabel("车架编号");
		bracket.setStore(bracketStore);
		bracket.setTypeAhead(true);
		bracket.setTriggerAction(TriggerAction.ALL);
		panel.add(bracket);


		formBindings = new FormBinding(panel, true);

		formBindings.getBinding(size).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return containerSizeStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(type).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return ironChestStore.findModel("code", (String) value);
				} else {
					return null;
				}
			}
		});
		formBindings.getBinding(bracket).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return ((BeanModel) value).get("code").toString();
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return bracketStore.findModel("code", (String) value);
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
					Container newItem = (Container) beanModel.getBean();
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

		getBusinessService().saveContainer((Container) beanModel.getBean(),
				newCallback);
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
		List<Container> list = new ArrayList<Container>();
		list.add((Container) grid.getSelectionModel().getSelection().get(0)
				.getBean());
		getBusinessService().updateContainer(list, callback);
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
		getBusinessService().deleteContainer(
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
		} else {
			addButton.disable();
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
		formPanel.reset();
		if (grid.getSelectionModel().getSelection().size() > 0) {
			formBindings.bind((BeanModel) grid.getSelectionModel()
					.getSelection().get(0));
		} else {
			formBindings.unbind();
			if (permitedEnterprise.getCanAdd()) {
				addButton.enable();
			} else {
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
			ids.add(((Container) beanModel.getBean()).getId());
		}
		return ids;
	}

	public void refleshGoodStore(Long contractHeadId) {
		if (goodStore == null) {
			goodStore = new ListStore<BeanModel>();
		}
		if (permitedEnterprise == null) {
			return;
		}
		AsyncCallback<List<ContractProduct>> getCallback = new AsyncCallback<List<ContractProduct>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ContractProduct> result) {
				Collections.sort(result, new Comparator<ContractProduct>() {
					public int compare(ContractProduct arg0,
							ContractProduct arg1) {
						return arg0.getNo() - arg1.getNo();
					}
				});
				goodStore.removeAll();
				BeanModel beanModel;
				for (Iterator<ContractProduct> iterator = result.iterator(); iterator
						.hasNext();) {
					ContractProduct contractProduct = (ContractProduct) iterator
							.next();
					beanModel = BeanModelLookup.get()
							.getFactory(ContractProduct.class)
							.createModel(contractProduct);
					beanModel.set("displayName", beanModel.get("no").toString()
							+ beanModel.get("name").toString());
					goodStore.add(beanModel);
				}
			}

		};
		getBusinessService().listContractProduct(contractHeadId, getCallback);
	}

	private ListStore<BeanModel> getContainerSizeStore() {
		final ListStore<BeanModel> containerSizeStore = new ListStore<BeanModel>();
		AsyncCallback<List<ContainerSize>> getCallback = new AsyncCallback<List<ContainerSize>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ContainerSize> result) {
				Collections.sort(result, new Comparator<ContainerSize>() {
					public int compare(ContainerSize arg0, ContainerSize arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<ContainerSize> iterator = result.iterator(); iterator
						.hasNext();) {
					ContainerSize containerSize = (ContainerSize) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(ContainerSize.class)
							.createModel(containerSize);
					containerSizeStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listContainerSize(getCallback);
		return containerSizeStore;
	}
	
	private ListStore<BeanModel> getIronChestStore() {
		final ListStore<BeanModel> ironChestStore = new ListStore<BeanModel>();
		AsyncCallback<List<IronChest>> getCallback = new AsyncCallback<List<IronChest>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<IronChest> result) {
				Collections.sort(result, new Comparator<IronChest>() {
					public int compare(IronChest arg0, IronChest arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<IronChest> iterator = result.iterator(); iterator
						.hasNext();) {
					IronChest ironChest = (IronChest) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(IronChest.class)
							.createModel(ironChest);					
					ironChestStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listIronChest(getCallback);
		return ironChestStore;
	}
	
	private ListStore<BeanModel> getBracketStore() {
		final ListStore<BeanModel> bracketStore = new ListStore<BeanModel>();
		AsyncCallback<List<Bracket>> getCallback = new AsyncCallback<List<Bracket>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<Bracket> result) {
				Collections.sort(result, new Comparator<Bracket>() {
					public int compare(Bracket arg0, Bracket arg1) {
						return arg0.getCode().compareTo(arg1.getCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<Bracket> iterator = result.iterator(); iterator
						.hasNext();) {
					Bracket bracket = (Bracket) iterator.next();
					beanModel = BeanModelLookup.get().getFactory(Bracket.class)
							.createModel(bracket);
					bracketStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listBracket(getCallback);
		return bracketStore;
	}
	
	private void refreshStore(){
		AsyncCallback<List<Container>> getCallback = new AsyncCallback<List<Container>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<Container> result) {
				Collections.sort(result, new Comparator<Container>() {
					public int compare(Container arg0, Container arg1) {
						return arg0.getContainerNo().compareTo(arg1.getContainerNo());
					}
				});
				store.removeAll();			
				store.add(beanModelFactory.createModel(result));		
			}
		};
		getBusinessService().listContainer(selectedCustomsDeclarationHead.getId(),
				getCallback);
	}
}
