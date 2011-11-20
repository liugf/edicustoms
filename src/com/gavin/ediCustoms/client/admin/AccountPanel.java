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
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.entity.edi.AccountRecord;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AccountPanel extends MyLayoutContainer {
	private ListStore<BeanModel> store;
	private BeanModelFactory beanModelFactory;
	private Enterprise currentEnterprise;
	private FormPanel formPanel;
	private NumberField moneyNumberField;
	private NumberField increaseNumberField;
	
	private FormBinding formBindings;
	
	public AccountPanel() {
		beanModelFactory = BeanModelLookup.get().getFactory(AccountRecord.class);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		ContentPanel content = new ContentPanel();
		content.setHeading("账户管理");
		content.setLayout(new FitLayout());

		LayoutContainer frame = new LayoutContainer();
		frame.setStyleAttribute("padding", "10px");
		frame.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame.setLayout(new BorderLayout());
		
		store = new ListStore<BeanModel>();
		store.sort("created", Style.SortDir.DESC);

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 350);
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);

		frame.add(createChargeContainer(), westData);
		frame.add(createChargeHistoryContainer(), centerData);

		content.add(frame);

		// 添加工具栏
		ToolBar toolBar = new ToolBar();

		toolBar.add(new LabelToolItem("企业名称："));
		ComboBox<BeanModel> enterpriseComboBox = new ComboBox<BeanModel>();
		enterpriseComboBox.setEmptyText("请选择...");
		enterpriseComboBox.setWidth(250);
		enterpriseComboBox.setDisplayField("displayName");
		enterpriseComboBox.setStore(getEnterpriseStore());
		enterpriseComboBox.setTypeAhead(true);
		enterpriseComboBox.setTriggerAction(TriggerAction.ALL);

		enterpriseComboBox.addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<BeanModel>>() {

					@Override
					public void handleEvent(SelectionChangedEvent<BeanModel> be) {
						if (be.getSelectedItem() == null) {
							return;
						}
						currentEnterprise = be.getSelectedItem().getBean();
						/*Double money= be.getSelectedItem().get("money");
						if (money == null) {
							money = (double) 0;
						}*/
						formBindings.addListener(Events.Bind,
								new Listener<BindingEvent>() {
									@Override
									public void handleEvent(BindingEvent be) {
										increaseNumberField.setValue(0);
									}
								});
						formBindings.bind(be.getSelectedItem());
						reflashStore();
						
					}

				});
		toolBar.add(enterpriseComboBox);

		content.setTopComponent(toolBar);
		add(content);
	}

	private LayoutContainer createChargeContainer() {
		LayoutContainer layoutContainer=new LayoutContainer();
		
		formPanel = new FormPanel();
		formPanel.setBodyBorder(false);
		formPanel.setHeaderVisible(false);
		
		//余额
		moneyNumberField = new NumberField();
		moneyNumberField.setName("money");
		moneyNumberField.setFieldLabel("余额");
		moneyNumberField.setPropertyEditorType(Double.class);
		moneyNumberField.setReadOnly(true);
		moneyNumberField.setFormat(NumberFormat.getFormat("#0.00"));
		formPanel.add(moneyNumberField);
		
		//充值金额
		increaseNumberField = new NumberField();
		increaseNumberField.setName("increase");
		increaseNumberField.setFieldLabel("充值金额");
		increaseNumberField.setPropertyEditorType(Double.class);
		increaseNumberField.setFormat(NumberFormat.getFormat("#0.00"));
		formPanel.add(increaseNumberField);
		
		final TextField<String> note = new TextField<String>();
		note.setFieldLabel("备注");
		note.setValue("充值");
		formPanel.add(note);
		
		Button chargeButton = new Button("充值");		
		chargeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (currentEnterprise==null) {
					MessageBox.alert("提示", "请选择用户单位", null);
					return;
				}
				if (!formPanel.isValid()){
					return;
				}
				if (increaseNumberField.getValue() == null){
					return;
				}
				if (increaseNumberField.getValue().equals(new Double(0))){
					return;
				}
					
				MessageBox.confirm("确认", "确定要为\""+currentEnterprise.getRegisteName()+"\"充值"+increaseNumberField.getValue()+"元么？",
						new Listener<MessageBoxEvent>() {
							@Override
							public void handleEvent(MessageBoxEvent be) {
								if (be.getButtonClicked().getText() == GXT.MESSAGES
										.messageBox_yes()) {
									charge(currentEnterprise.getId(), (Double)increaseNumberField.getValue(),note.getValue());
								}
							}
						});
			}
		});
		formPanel.addButton(chargeButton);
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);
		
		formBindings = new FormBinding(formPanel, true);
		
		layoutContainer.add(formPanel);
		return layoutContainer;
	}
	
	private Grid<BeanModel> createChargeHistoryContainer() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();

		column.setId("increase");
		column.setHeader("变动金额");
		column.setWidth(75);
		column.setRowHeader(true);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("moneyAfter");
		column.setHeader("余额");
		column.setWidth(75);
		column.setRowHeader(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("created");
		column.setHeader("操作时间");
		column.setWidth(150);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("note");
		column.setHeader("备注");
		column.setWidth(100);
		configs.add(column);

		store.setMonitorChanges(true);
		Grid<BeanModel> grid = new Grid<BeanModel>(store, new ColumnModel(
				configs));
		grid.setBorders(true);
		return grid;
	}
	
	private void reflashStore() {
		AsyncCallback<List<AccountRecord>> getCallback = new AsyncCallback<List<AccountRecord>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<AccountRecord> result) {
				Collections.sort(result, new Comparator<AccountRecord>() {
					public int compare(AccountRecord arg0, AccountRecord arg1) {
						if (arg0 == null) {
							return -1;
						}
						if (arg1 == null) {
							return 1;
						}
						return arg0.getCreated().compareTo(arg1.getCreated());
					}
				});
				store.removeAll();
				store.add(beanModelFactory.createModel(result));
			}
		};
		getSystemService().listAccountRecords(currentEnterprise.getId(), getCallback);
	}
	

	private ListStore<BeanModel> getEnterpriseStore() {
		final ListStore<BeanModel> enterpriseStore = new ListStore<BeanModel>();
		AsyncCallback<List<Enterprise>> getCallback = new AsyncCallback<List<Enterprise>>() {
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
				BeanModel beanModel;
				for (Iterator<Enterprise> iterator = result.iterator(); iterator
						.hasNext();) {
					Enterprise enterprise = (Enterprise) iterator.next();
					beanModel = BeanModelLookup.get()
							.getFactory(Enterprise.class)
							.createModel(enterprise);
					beanModel.set("displayName", beanModel.get("tradeCode")
							.toString()
							+ beanModel.get("registeName").toString());
					enterpriseStore.add(beanModel);
				}
			}

		};
		getSystemService().listEnterprise(getCallback);
		return enterpriseStore;
	}
	
	private void charge(long enterpriseId, double increase,String note) {
		AsyncCallback<Double> callback = new AsyncCallback<Double>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(Double result) {
				currentEnterprise.setMoney(result);
				moneyNumberField.setValue(result);
				increaseNumberField.setValue(0);
				reflashStore();
				MessageBox.info("提示", "充值成功！", null);
			}
		};
		getSystemService().charge(enterpriseId, increase,note, callback);
	}
}
