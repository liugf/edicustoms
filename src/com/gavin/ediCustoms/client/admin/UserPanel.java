package com.gavin.ediCustoms.client.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.ButtonEvent;
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
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyCheckColumnConfig;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.Permission;
import com.gavin.ediCustoms.entity.share.User;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserPanel extends MyLayoutContainer {
	private ListStore<BeanModel> userStore;
	private FormBinding formBindings;
	private BeanModelFactory userBeanModelFactory;
	private BeanModelFactory permissionBeanModelFactory;
	private BeanModel beanModel;

	private Button saveButton;
	private Button cancelButton;

	private Button updateButton;
	private Button resetButton;

	private Button editButton;
	private Button addButton;

	private FormPanel userFormPanel;
	private Grid<BeanModel> userGrid;

	private TextField<String> password;
	private TextField<String> comfirdPassword;

	private StoreFilterField<BeanModel> filter;

	private ListStore<BeanModel> permissionStore;
	private ListStore<BeanModel> enterpriseStore;
	private ListStore<BeanModel> permitedStore;
	private ListStore<BeanModel> rejectedStore;

	private GridDragSource permitedDragSource;
	private GridDragSource rejectedDragSource;

	private EditorGrid<BeanModel> permitedGrid;
	private Grid<BeanModel> rejectedGrid;

	private MyCheckColumnConfig addCheckColumn;
	private MyCheckColumnConfig updateCheckColumn;
	private MyCheckColumnConfig deleteCheckColumn;
	private MyCheckColumnConfig passCheckColumn;
	private MyCheckColumnConfig disPassCheckColumn;

	public UserPanel() {
		userBeanModelFactory = BeanModelLookup.get().getFactory(User.class);
		permissionBeanModelFactory = BeanModelLookup.get().getFactory(
				Permission.class);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		ContentPanel content = new ContentPanel();
		content.setHeading("录入人员");
		content.setLayout(new RowLayout(Orientation.VERTICAL));

		LayoutContainer frame1 = new LayoutContainer();
		frame1.setStyleAttribute("padding", "10px");
		frame1.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame1.setLayout(new BorderLayout());

		reflashStore();

		userGrid = createGrid();
		userGrid.getSelectionModel().addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<BeanModel>>() {
					public void handleEvent(SelectionChangedEvent<BeanModel> be) {
						userStore.rejectChanges();
						resetState();
					}
				});

		userFormPanel = createForm();

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 350);
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);

		frame1.add(userGrid, westData);
		frame1.add(userFormPanel, centerData);

		LayoutContainer frame2 = new LayoutContainer();
		frame2.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame2.setLayout(new RowLayout(Orientation.HORIZONTAL));

		permitedStore = new ListStore<BeanModel>();
		rejectedStore = new ListStore<BeanModel>();
		permitedStore.sort("tradeCode", Style.SortDir.ASC);
		rejectedStore.sort("tradeCode", Style.SortDir.ASC);

		permitedGrid = createPermitedGrid();
		rejectedGrid = createRejectedGrid();
	

		ContentPanel permitedContentPanel = new ContentPanel();
		permitedContentPanel.setHeading("已授权单位");
		permitedContentPanel.setLayout(new FitLayout());
		ContentPanel rejectedContentPanel = new ContentPanel();
		rejectedContentPanel.setHeading("未授权单位");
		rejectedContentPanel.setLayout(new FitLayout());

		permitedContentPanel.add(permitedGrid);
		rejectedContentPanel.add(rejectedGrid);

		permitedDragSource = new GridDragSource(permitedGrid);
		rejectedDragSource = new GridDragSource(rejectedGrid);
		permitedDragSource.disable();
		rejectedDragSource.disable();

		GridDropTarget permitedDropTarget = new GridDropTarget(permitedGrid);
		permitedDropTarget.setAllowSelfAsSource(false);
		permitedDropTarget.setFeedback(Feedback.INSERT);

		GridDropTarget rejecteDropTarget = new GridDropTarget(rejectedGrid);
		rejecteDropTarget.setAllowSelfAsSource(false);
		rejecteDropTarget.setFeedback(Feedback.INSERT);

		RowData data1 = new RowData(.6, 1);
		data1.setMargins(new Margins(10));
		RowData data2 = new RowData(.4, 1);
		data2.setMargins(new Margins(10));
		frame2.add(permitedContentPanel, data1);
		frame2.add(rejectedContentPanel, data2);

		RowData data = new RowData(1, 0.5);
		content.add(frame1, data);
		content.add(frame2, data);

		// 添加工具栏
		ToolBar toolBar = new ToolBar();

		toolBar.add(new LabelToolItem("搜索："));
		toolBar.add(filter);

		toolBar.add(new SeparatorToolItem());

		addButton = new Button("添加", Resources.ICONS.add());
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				User newUser=new User();
				newUser.setId(new Long(0));
				beanModel = userBeanModelFactory.createModel(newUser);
				formBindings.bind(beanModel);
				calculateStores(newUser);

				userFormPanel.setReadOnly(false);
				permitedDragSource.enable();
				rejectedDragSource.enable();
				permitedGrid.enable();
				rejectedGrid.enable();
				addCheckColumn.enable();
				updateCheckColumn.enable();
				deleteCheckColumn.enable();
				passCheckColumn.enable();
				disPassCheckColumn.enable();

				saveButton.setVisible(true);
				cancelButton.setVisible(true);
				updateButton.setVisible(false);
				resetButton.setVisible(false);
				addButton.disable();
				password.setAllowBlank(false);

				userFormPanel.getFields().get(0).focus();
			}
		});
		toolBar.add(addButton);

		toolBar.add(new SeparatorToolItem());

		editButton = new Button("修改", Resources.ICONS.edit());
		editButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (userGrid.getSelectionModel().getSelection().size() > 0) {
					formBindings.bind((BeanModel) userGrid.getSelectionModel()
							.getSelection().get(0));

					userFormPanel.setReadOnly(false);
					permitedDragSource.enable();
					rejectedDragSource.enable();
					permitedGrid.enable();
					rejectedGrid.enable();
					addCheckColumn.enable();
					updateCheckColumn.enable();
					deleteCheckColumn.enable();
					passCheckColumn.enable();
					disPassCheckColumn.enable();
					

					updateButton.enable();
					resetButton.enable();
					editButton.disable();

					userFormPanel.getFields().get(0).focus();
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
								if (be.getButtonClicked().getText() == GXT.MESSAGES.messageBox_yes()) {
									delete();
								}
							}
						});
			}
		});
		content.setTopComponent(toolBar);

		// 添加底部按钮
		saveButton = new Button("保存");
		saveButton.setVisible(false);
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!userFormPanel.isValid())
					return;
				save();
			}
		});

		cancelButton = new Button("取消");
		cancelButton.setVisible(false);
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				resetState();
			}
		});

		updateButton = new Button("更新");
		updateButton.disable();
		updateButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!userFormPanel.isValid())
					return;
				update();
			}
		});

		resetButton = new Button("取消");
		resetButton.disable();
		resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				userStore.rejectChanges();
				resetState();
			}
		});

		content.setButtonAlign(HorizontalAlignment.CENTER);
		content.addButton(saveButton);
		content.addButton(cancelButton);
		content.addButton(updateButton);
		content.addButton(resetButton);

		add(content);
		
	
	}

	private void reflashStore() {
		// filter
		filter = new StoreFilterField<BeanModel>() {
			@Override
			protected boolean doSelect(Store<BeanModel> store,
					BeanModel parent, BeanModel record, String property,
					String filter) {
				String userName = record.get("userName");
				userName = userName.toLowerCase();
				String name = record.get("name");
				name = name.toLowerCase();
				if (userName.indexOf(filter.toLowerCase()) != -1
						|| name.indexOf(filter.toLowerCase()) != -1) {
					return true;
				}
				return false;
			}
		};

		// userStore
		// proxy and reader
		RpcProxy<List<User>> userProxy = new RpcProxy<List<User>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<List<User>> callback) {
				getSystemService().listUser(callback);
			}
		};
		BeanModelReader userReader = new BeanModelReader();
		// loader and store
		ListLoader<ListLoadResult<User>> userLoader = new BaseListLoader<ListLoadResult<User>>(
				userProxy, userReader);
		userLoader.setSortDir(Style.SortDir.ASC);
		userLoader.setSortField("userName");
		userStore = new ListStore<BeanModel>(userLoader);
		userLoader.load();
		userStore.sort("userName", Style.SortDir.ASC);
		filter.bind(userStore);

		// permissionStore
		// proxy and reader
		RpcProxy<List<Permission>> permissionProxy = new RpcProxy<List<Permission>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<List<Permission>> callback) {
				getSystemService().listPermission(callback);
			}
		};
		BeanModelReader permissionReader = new BeanModelReader();
		// loader and store
		ListLoader<ListLoadResult<Permission>> permissionLoader = new BaseListLoader<ListLoadResult<Permission>>(
				permissionProxy, permissionReader);
		permissionStore = new ListStore<BeanModel>(permissionLoader);
		permissionLoader.load();

		// enterpriseStore
		// proxy and reader
		RpcProxy<List<Enterprise>> enterpriseProxy = new RpcProxy<List<Enterprise>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<List<Enterprise>> callback) {
				getSystemService().listEnterprise(callback);
			}
		};
		BeanModelReader enterpriseReader = new BeanModelReader();
		// loader and store
		ListLoader<ListLoadResult<Enterprise>> enterpriseLoader = new BaseListLoader<ListLoadResult<Enterprise>>(
				enterpriseProxy, enterpriseReader);
		enterpriseStore = new ListStore<BeanModel>(enterpriseLoader);
		enterpriseLoader.load();
	}

	private Grid<BeanModel> createGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();

		column.setId("userName");
		column.setHeader("用户名");
		column.setWidth(150);
		column.setRowHeader(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("name");
		column.setHeader("姓名");
		column.setWidth(196);
		configs.add(column);

		userStore.setMonitorChanges(true);
		Grid<BeanModel> grid = new Grid<BeanModel>(userStore, new ColumnModel(
				configs));
		grid.setBorders(true);
		return grid;
	}

	private FormPanel createForm() {
		final FormPanel panel = new FormPanel();
		panel.setHeaderVisible(false);

		TextField<String> userName = new TextField<String>();
		userName.setAllowBlank(false);
		userName.setName("userName");
		userName.setFieldLabel("用户名");
		panel.add(userName);

		password = new TextField<String>();
		password.setName("password");
		password.setPassword(true);
		password.setFieldLabel("密码");
		panel.add(password);

		comfirdPassword = new TextField<String>();
		comfirdPassword.setPassword(true);
		comfirdPassword.setFieldLabel("确认密码");
		comfirdPassword.setValidator(new Validator() {
			@Override
			public String validate(Field<?> field, String value) {
				if (value.trim().equals(password.getValue().trim())) {
					return null;
				} else {
					return "两次密码输入必须一致且非空";
				}
			}
		});
		panel.add(comfirdPassword);

		password.addListener(Events.Blur, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				if ((password.getValue() == null)) {
					comfirdPassword.setAllowBlank(true);
				} else {
					comfirdPassword.setAllowBlank(false);
				}
			}
		});

		TextField<String> name = new TextField<String>();
		name.setName("name");
		name.setFieldLabel("姓名");
		panel.add(name);

		TextField<String> address = new TextField<String>();
		address.setName("phone");
		address.setFieldLabel("手机号码");
		panel.add(address);
		

		formBindings = new FormBinding(panel, true);

		formBindings.setStore((Store<BeanModel>) userGrid.getStore());

		panel.setReadOnly(true);

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
					
					User newItem = (User) beanModel.getBean();
					newItem.setId(result);
					userStore.insert(beanModel, 0);
					userStore.commitChanges();
					
					for (BeanModel beanModel : permitedStore.getModels()) {
						beanModel.set("userId", result);
					}
					permissionStore.add(permitedStore.getModels());
					
					MessageBox.info("提示", "添加成功！", null);

					userFormPanel.setReadOnly(true);
					saveButton.setVisible(false);
					cancelButton.setVisible(false);
					updateButton.setVisible(true);
					updateButton.disable();
					resetButton.setVisible(true);
					resetButton.disable();
					addButton.enable();
					password.setAllowBlank(true);
					comfirdPassword.setAllowBlank(true);
					password.setValue("");
					comfirdPassword.setValue("");
				}
			}
		};
		List<Permission> permissions = new ArrayList<Permission>();
		for (BeanModel beanModel : permitedStore.getModels()) {
			permissions.add((Permission) beanModel.getBean());
		}		
		getSystemService().saveUser((User) beanModel.getBean(),permissions, newCallback);		
		
	}

	private void update() {
		final User user = (User) userGrid.getSelectionModel().getSelection().get(0)
				.getBean();
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "更新用户发生错误，请刷新！", null);
			}
			
			public void onSuccess(Void result) {
				formBindings.getModel().set("password", "");
				userStore.commitChanges();
				
				for (BeanModel beanModel : permissionStore.getModels()) {
					if (beanModel.get("userId").equals(user.getId())) {
						permissionStore.remove(beanModel);
					}
				}
				permissionStore.add(permitedStore.getModels());
				
				resetState();
				
				MessageBox.info("提示", "修改成功！", null);
			}
		};
		List<Permission> permissions = new ArrayList<Permission>();
		for (BeanModel beanModel : permitedStore.getModels()) {
			permissions.add((Permission) beanModel.getBean());
		}
		getSystemService().updateUser(user, permissions,callback);
	}

	private void delete() {
		final List<Long> userIds=models2Ids(userGrid.getSelectionModel().getSelectedItems());
		final AsyncCallback<Void> deleteCallback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			@Override
			public void onSuccess(Void result) {
				for (Iterator<BeanModel> iterator = userGrid.getSelectionModel()
						.getSelectedItems().iterator(); iterator.hasNext();) {
					BeanModel beanModel = (BeanModel) iterator.next();
					userStore.remove(beanModel);
				}	
				
				for (BeanModel beanModel : permissionStore.getModels()) {					
					if (userIds.contains(beanModel.get("userId"))) {
						permissionStore.remove(beanModel);
					}
				}
				
				resetState();
			}
		};
		getSystemService().deleteUser(userIds,deleteCallback);
	}

	private void resetState() {
		saveButton.setVisible(false);
		cancelButton.setVisible(false);
		updateButton.setVisible(true);
		updateButton.disable();
		resetButton.setVisible(true);
		resetButton.disable();
		addButton.enable();
		editButton.enable();
		password.setAllowBlank(true);
		comfirdPassword.setAllowBlank(true);
		password.setValue("");
		comfirdPassword.setValue("");

		userFormPanel.setReadOnly(true);
		permitedDragSource.disable();
		rejectedDragSource.disable();
		addCheckColumn.disable();
		updateCheckColumn.disable();
		deleteCheckColumn.disable();
		passCheckColumn.disable();
		disPassCheckColumn.disable();

		if (userGrid.getSelectionModel().getSelection().size() > 0) {
			formBindings.bind((BeanModel) userGrid.getSelectionModel()
					.getSelection().get(0));
			User user = userGrid.getSelectionModel().getSelection().get(0)
					.getBean();
			calculateStores(user);
		} else {
			formBindings.unbind();
			permitedStore.removeAll();
			rejectedStore.removeAll();
		}
	}

	private List<Long> models2Ids(List<BeanModel> models) {
		List<Long> ids = new ArrayList<Long>();
		for (Iterator<BeanModel> iterator = models.iterator(); iterator
				.hasNext();) {
			BeanModel beanModel = (BeanModel) iterator.next();
			ids.add((Long) beanModel.get("id"));
		}
		return ids;
	}

	private void calculateStores(User user) {
		// 刷新 permitedStore & rejectedStore
		List<BeanModel> permitedList = permissionStore.findModels("userId",
				user.getId());
		List<BeanModel> rejectedList = new ArrayList<BeanModel>();
		for (BeanModel beanModel : enterpriseStore.getModels()) {
			Enterprise enterprise = beanModel.getBean();
			boolean isContain = false;
			for (Iterator<BeanModel> iterator = permitedList.iterator(); iterator
					.hasNext();) {
				BeanModel bm = (BeanModel) iterator.next();
				Long enterpriseId = bm.get("enterpriseId");
				if (enterprise.getId().equals(enterpriseId)) {
					bm.set("tradeCode", enterprise.getTradeCode());
					bm.set("registeName", enterprise.getRegisteName());
					isContain = true;
				}
			}
			if (!isContain) {
				BeanModel rejectedBeanModel = permissionBeanModelFactory
						.createModel(new Permission(user.getId(), enterprise
								.getId()));
				rejectedBeanModel.set("tradeCode", enterprise.getTradeCode());
				rejectedBeanModel.set("registeName",
						enterprise.getRegisteName());
				rejectedList.add(rejectedBeanModel);
			}
		}

		permitedStore.removeAll();
		permitedStore.add(permitedList);

		rejectedStore.removeAll();
		rejectedStore.add(rejectedList);

	}

	private EditorGrid<BeanModel> createPermitedGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
				
		ColumnConfig column = new ColumnConfig();
		column.setId("tradeCode");
		column.setHeader("公司编号");
		column.setWidth(120);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("registeName");
		column.setHeader("公司名称");
		column.setWidth(196);
		configs.add(column);

		CellEditor checkBoxEditor = new CellEditor(new CheckBox());
		checkBoxEditor.disableTextSelection(true);

		addCheckColumn = new MyCheckColumnConfig("canAdd", "录入", 35);
		addCheckColumn.setEditor(checkBoxEditor);
		addCheckColumn.disable();
		configs.add(addCheckColumn);
		
		

		updateCheckColumn = new MyCheckColumnConfig("canUpdate", "更新", 40);
		updateCheckColumn.setEditor(checkBoxEditor);
		updateCheckColumn.disable();
		configs.add(updateCheckColumn);

		deleteCheckColumn = new MyCheckColumnConfig("canDelete", "删除", 40);
		deleteCheckColumn.setEditor(checkBoxEditor);
		deleteCheckColumn.disable();
		configs.add(deleteCheckColumn);
		
		passCheckColumn = new MyCheckColumnConfig("canPass", "入库", 40);
		passCheckColumn.setEditor(checkBoxEditor);
		passCheckColumn.disable();
		configs.add(passCheckColumn);
		
		disPassCheckColumn = new MyCheckColumnConfig("canDisPass", "退库", 40);
		disPassCheckColumn.setEditor(checkBoxEditor);
		disPassCheckColumn.disable();
		configs.add(disPassCheckColumn);
		
		

		EditorGrid<BeanModel> grid = new EditorGrid<BeanModel>(permitedStore,
				new ColumnModel(configs));
		
		
		grid.setAutoExpandColumn("registeName");
		grid.setBorders(false);
		grid.addPlugin(addCheckColumn);
		grid.addPlugin(updateCheckColumn);
		grid.addPlugin(deleteCheckColumn);
		grid.addPlugin(passCheckColumn);
		grid.addPlugin(disPassCheckColumn);

		grid.setSelectionModel(new GridSelectionModel<BeanModel>());	

		return grid;
	}

	private Grid<BeanModel> createRejectedGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId("tradeCode");
		column.setHeader("公司编号");
		column.setWidth(120);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("registeName");
		column.setHeader("公司名称");
		column.setWidth(196);
		configs.add(column);

		Grid<BeanModel> grid = new Grid<BeanModel>(rejectedStore,
				new ColumnModel(configs));
		grid.setAutoExpandColumn("registeName");
		grid.setBorders(false);

		return grid;
	}

}
