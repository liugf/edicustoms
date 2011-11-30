package com.gavin.ediCustoms.client.admin;

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
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
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
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
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
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.dictionary.Customs;
import com.gavin.ediCustoms.resources.Resources;
import com.gavin.ediCustoms.shared.Convertor;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EnterprisePanel extends MyLayoutContainer {
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

	private FormPanel formPanel;
	private Grid<BeanModel> grid;

	private StoreFilterField<BeanModel> filter;


	public EnterprisePanel() {
		beanModelFactory = BeanModelLookup.get().getFactory(Enterprise.class);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		ContentPanel content = new ContentPanel();
		content.setHeading("用户单位");
		content.setLayout(new FitLayout());

		LayoutContainer frame = new LayoutContainer();
		frame.setStyleAttribute("padding", "10px");
		frame.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame.setLayout(new BorderLayout());

		reflashStore();

		grid = createGrid();
		grid.getSelectionModel().addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<BeanModel>>() {
					public void handleEvent(SelectionChangedEvent<BeanModel> be) {
						store.rejectChanges();
						resetState();
					}
				});

		formPanel = createForm();

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 270);
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
				beanModel = beanModelFactory.createModel(new Enterprise());
				formPanel.reset();
				formBindings.bind(beanModel);
				formBindings.addListener(Events.Bind,
						new Listener<BindingEvent>() {
							@Override
							public void handleEvent(BindingEvent be) {
								formBindings.getModel().set("ediCode",
										getNewEdiCode());
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

		Button deleteButton = new Button("删除", Resources.ICONS.delete());
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
		content.setTopComponent(toolBar);
		add(content);
	}

	private void reflashStore() {
		// filter
		filter = new StoreFilterField<BeanModel>() {
			@Override
			protected boolean doSelect(Store<BeanModel> store,
					BeanModel parent, BeanModel record, String property,
					String filter) {
				String tradeCode = record.get("tradeCode");
				tradeCode = tradeCode.toLowerCase();
				String registeName = record.get("registeName");
				registeName = registeName.toLowerCase();
				if (tradeCode.indexOf(filter.toLowerCase()) != -1
						|| registeName.indexOf(filter.toLowerCase()) != -1) {
					return true;
				}
				return false;
			}
		};

		// proxy and reader
		RpcProxy<List<Enterprise>> proxy = new RpcProxy<List<Enterprise>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<List<Enterprise>> callback) {
				getSystemService().listEnterprise(callback);
			}
		};
		BeanModelReader reader = new BeanModelReader();
		// loader and store
		ListLoader<ListLoadResult<Enterprise>> loader = new BaseListLoader<ListLoadResult<Enterprise>>(
				proxy, reader);
		loader.setSortDir(Style.SortDir.ASC);
		loader.setSortField("tradeCode");
		store = new ListStore<BeanModel>(loader);
		loader.load();
		store.sort("tradeCode", Style.SortDir.ASC);
		filter.bind(store);
	}

	private Grid<BeanModel> createGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();

		column.setId("tradeCode");
		column.setHeader("单位代码");
		column.setWidth(80);
		column.setRowHeader(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("registeName");
		column.setHeader("单位名称");
		column.setWidth(266);
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
		layout.setLabelWidth(80);
		layout.setDefaultWidth(150);
		left.setLayout(layout);

		LayoutContainer right = new LayoutContainer();
		right.setStyleAttribute("paddingLeft", "10px");
		layout = new FormLayout();
		layout.setLabelWidth(80);
		layout.setDefaultWidth(150);
		right.setLayout(layout);

		TextField<String> ownerCode = new TextField<String>();
		ownerCode.setAllowBlank(false);
		ownerCode.setName("ownerCode");
		ownerCode.setFieldLabel("组织代码");
		left.add(ownerCode);

		TextField<String> tradeCode = new TextField<String>();
		tradeCode.setName("tradeCode");
		tradeCode.setFieldLabel("交易代码");
		left.add(tradeCode);

		TextField<String> registeName = new TextField<String>();
		registeName.setAllowBlank(false);
		registeName.setName("registeName");
		registeName.setFieldLabel("单位名称");
		left.add(registeName);

		TextField<String> address = new TextField<String>();
		address.setName("address");
		address.setFieldLabel("单位地址");
		left.add(address);

		Radio declareYes = new Radio();
		declareYes.setBoxLabel("是");
		declareYes.setValue(true);

		Radio declareNo = new Radio();
		declareNo.setBoxLabel("否");

		RadioGroup radioGroup = new RadioGroup();
		radioGroup.setFieldLabel("申报单位");
		radioGroup.add(declareYes);
		radioGroup.add(declareNo);
		left.add(radioGroup);

		TextField<String> contact = new TextField<String>();
		contact.setName("contact");
		contact.setFieldLabel("联系人");
		left.add(contact);

		TextField<String> legalPersonCode = new TextField<String>();
		legalPersonCode.setName("legalPersonCode");
		legalPersonCode.setFieldLabel("法人代码");
		left.add(legalPersonCode);

		TextField<String> telephone = new TextField<String>();
		telephone.setName("telephone");
		telephone.setFieldLabel("联系电话");
		left.add(telephone);

		TextField<String> fax = new TextField<String>();
		fax.setName("fax");
		fax.setFieldLabel("传真");
		left.add(fax);

		TextField<String> bank = new TextField<String>();
		bank.setName("bank");
		bank.setFieldLabel("开户银行");
		left.add(bank);

		TextField<String> bankAccount = new TextField<String>();
		bankAccount.setName("bankAccount");
		bankAccount.setFieldLabel("银行账号");
		left.add(bankAccount);

		final ListStore<BeanModel> customStore = getCustomsStore();

		ComboBox<BeanModel> customsCode = new ComboBox<BeanModel>();
		customsCode.setForceSelection(true);
		customsCode.setAllowBlank(false);
		customsCode.setName("customsCode");
		customsCode.setEmptyText("请选择...");
		customsCode.setDisplayField("displayName");
		customsCode.setFieldLabel("所属注册海关");
		customsCode.setStore(customStore);
		customsCode.setTypeAhead(true);
		customsCode.setTriggerAction(TriggerAction.ALL);
		left.add(customsCode);
		
		NumberField proxyCostPerDeal = new NumberField();
		proxyCostPerDeal.setName("proxyCostPerDeal");
		proxyCostPerDeal.setFieldLabel("每单代理费");
		proxyCostPerDeal.setPropertyEditorType(Double.class);
		proxyCostPerDeal.setFormat(NumberFormat.getFormat("#0.00"));
		left.add(proxyCostPerDeal);
		
		NumberField serviceCostPerDeal = new NumberField();
		serviceCostPerDeal.setName("serviceCostPerDeal");
		serviceCostPerDeal.setFieldLabel("每单服务费");
		serviceCostPerDeal.setPropertyEditorType(Double.class);
		serviceCostPerDeal.setFormat(NumberFormat.getFormat("#0.00"));
		left.add(serviceCostPerDeal);

		TextField<String> ediCode = new TextField<String>();
		ediCode.setAllowBlank(false);
		ediCode.setName("ediCode");
		ediCode.setFieldLabel("EDI用户编码");
		right.add(ediCode);

		TextField<String> jichengtongId = new TextField<String>();
		jichengtongId.setName("jichengtongId");
		jichengtongId.setFieldLabel("集成通平台号");
		right.add(jichengtongId);

		TextField<String> quyujiedian = new TextField<String>();
		quyujiedian.setName("quyujiedian");
		quyujiedian.setFieldLabel("区域节点名称");
		right.add(quyujiedian);
		
		TextField<String> userPrivateKey = new TextField<String>();
		userPrivateKey.setName("userPrivateKey");
		userPrivateKey.setFieldLabel("平台用户私钥");
		right.add(userPrivateKey);
		
		TextField<String> icCardNo = new TextField<String>();
		icCardNo.setAllowBlank(false);
		icCardNo.setName("icCardNo");
		icCardNo.setFieldLabel("IC卡号");
		right.add(icCardNo);
		
		TextField<String> certificateNo = new TextField<String>();
		certificateNo.setName("certificateNo");
		certificateNo.setFieldLabel("用户证书号");
		right.add(certificateNo);
		
		TextField<String> bpNo = new TextField<String>();
		bpNo.setName("bpNo");
		bpNo.setFieldLabel("业务流程编号");
		right.add(bpNo);
				
		main.add(left, new ColumnData(.5));
		main.add(right, new ColumnData(.5));
		
		panel.add(main, new FormData("100%"));
		
		formBindings = new FormBinding(panel, true);
		formBindings.getBinding(customsCode).setConverter(new Converter() {
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
		});

		formBindings.addFieldBinding(new FieldBinding(declareYes, "isDeclare"));
		formBindings.addFieldBinding(new FieldBinding(declareNo, "isDeclare"));
		formBindings.getBinding(declareNo).setConverter(new Converter() {
			@Override
public Object convertFieldValue(Object value) {
 if (value == null) {return null;}
				return !(Boolean) value;
			}

			@Override
			public Object convertModelValue(Object value) {
				if (value != null) {
					return !(Boolean) value;
				} else {
					return true;
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
					MessageBox
							.alert("提醒", "已存在相同条目(组织代码或EDI用户编码相同)，添加失败", null);
				} else {
					Enterprise newItem = (Enterprise) beanModel.getBean();
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
					addButton.enable();
				}
			}
		};

		getSystemService().saveEnterprise((Enterprise) beanModel.getBean(),
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
		List<Enterprise> list = new ArrayList<Enterprise>();
		list.add((Enterprise) grid.getSelectionModel().getSelection().get(0)
				.getBean());
		getSystemService().updateEnterprise(list, callback);
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
		getSystemService().deleteEnterprise(
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
		addButton.enable();
		editButton.enable();

		if (grid.getSelectionModel().getSelection().size() > 0) {
			formBindings.bind((BeanModel) grid.getSelectionModel()
					.getSelection().get(0));
		} else {
			formBindings.unbind();
		}
	}

	private List<Long> models2Ids(List<BeanModel> models) {
		List<Long> ids = new ArrayList<Long>();
		for (Iterator<BeanModel> iterator = models.iterator(); iterator
				.hasNext();) {
			BeanModel beanModel = (BeanModel) iterator.next();
			ids.add(((Enterprise) beanModel.getBean()).getId());
		}
		return ids;
	}

	private ListStore<BeanModel> getCustomsStore() {
		final ListStore<BeanModel> customStore = new ListStore<BeanModel>();
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
					customStore.add(beanModel);
				}
			}

		};
		getDictionaryService().listCustoms(getCallback);
		return customStore;
	}

	private String getNewEdiCode() {
		int maxNo = 0;
		int ediNo;
		for (BeanModel bm : store.getModels()) {
			ediNo = Convertor.string2int((String) bm.get("ediCode"));
			if (maxNo < ediNo) {
				maxNo = ediNo;
			}
		}

		String result = Convertor.int2String(maxNo + 1);
		result = "0000" + result;
		return result.substring(result.length() - 4, result.length());
	}

	/*private Window getMoreWindow() {
		if (moreWindow != null) {
			return moreWindow;
		}

		moreWindow = new Window();
		moreWindow.setHeading("更多信息");
		moreWindow.setWidth(450);
		moreWindow.setHeight(120);
		moreWindow.setLayout(new FitLayout());

		FormPanel moreFormPanel = new FormPanel();
		moreFormPanel.setHeaderVisible(false);
		moreFormPanel.setBodyBorder(false);
		
		
		moreWindow.add(moreFormPanel);
		moreWindow.setButtonAlign(HorizontalAlignment.CENTER);

		moreWindow.addButton(new Button("关闭",
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						moreWindow.hide();
					}
				}));

		return moreWindow;
	}*/

}
