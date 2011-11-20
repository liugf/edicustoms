package com.gavin.ediCustoms.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
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
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AccountPanel extends MyLayoutContainer {
	private ListStore<BeanModel> store;
	private BeanModelFactory beanModelFactory;
	private PermitedEnterprise permitedEnterprise;
	private FormPanel formPanel;
	private NumberField moneyNumberField;
	
	private DateField startDate;
	private DateField endDate;
	
	private ListStore<BeanModel> permitedEnterpriseStore;;
	
	public AccountPanel(ListStore<BeanModel> permitedEnterpriseStore) {
		beanModelFactory = BeanModelLookup.get().getFactory(AccountRecord.class);
		this.permitedEnterpriseStore=permitedEnterpriseStore;
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

		frame.add(leftContainer(), westData);
		frame.add(createChargeHistoryContainer(), centerData);

		content.add(frame);

		// 添加工具栏
		ToolBar toolBar = new ToolBar();

		toolBar.add(new LabelToolItem("企业名称："));
		ComboBox<BeanModel> enterpriseComboBox = new ComboBox<BeanModel>();
		enterpriseComboBox.setEmptyText("请选择...");
		enterpriseComboBox.setWidth(250);
		enterpriseComboBox.setDisplayField("displayName");
		enterpriseComboBox.setStore(permitedEnterpriseStore);
		enterpriseComboBox.setTypeAhead(true);
		enterpriseComboBox.setTriggerAction(TriggerAction.ALL);

		enterpriseComboBox.addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<BeanModel>>() {

					@Override
					public void handleEvent(SelectionChangedEvent<BeanModel> be) {
						if (be.getSelectedItem() == null) {
							return;
						}
						permitedEnterprise = be.getSelectedItem().getBean();			
						refreshEnterprise(permitedEnterprise.getEnterpriseId());
					}

				});
		toolBar.add(enterpriseComboBox);
		
		Button printButton = new Button("打印", Resources.ICONS.printer());
		toolBar.add(printButton);
		printButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@SuppressWarnings("deprecation")
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (permitedEnterprise==null) {
					return;
				}
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("enterpriseId", permitedEnterprise.getEnterpriseId());
				
				Date start = startDate.getValue();
				if (start!=null) {
					start.setHours(0);
					start.setMinutes(0);
					start.setSeconds(0);
				}
				Date end = endDate.getValue();
				if (end!=null) {
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
				}
				paramsMap.put("enterpriseId", permitedEnterprise.getEnterpriseId());
				paramsMap.put("start", date2String(start));
				paramsMap.put("end", date2String(end));
				openNewWindow("/accountDetail.do", paramsMap);
			}
		});

		content.setTopComponent(toolBar);
		add(content);
	}

	@SuppressWarnings("deprecation")
	private LayoutContainer leftContainer() {
		LayoutContainer layoutContainer=new LayoutContainer();
		
		formPanel = new FormPanel();
		formPanel.setBodyBorder(false);
		formPanel.setHeaderVisible(false);
		
		//余额
		moneyNumberField = new NumberField();
		moneyNumberField.setName("money");
		moneyNumberField.setFieldLabel("当前余额");
		moneyNumberField.setPropertyEditorType(Double.class);
		moneyNumberField.setReadOnly(true);
		moneyNumberField.setFormat(NumberFormat.getFormat("#0.00"));
		formPanel.add(moneyNumberField);
		
		Date today=new Date();
		
		startDate = new DateField();
		startDate.setAllowBlank(false);
		startDate.setFieldLabel("开始日期");
		startDate.setValue(new Date(today.getTime()-1000*3600*24));
		formPanel.add(startDate);        
		
		endDate = new DateField();
		endDate.setAllowBlank(false);
		endDate.setFieldLabel("结束日期");
		endDate.setValue(today);
		formPanel.add(endDate);
		
		Button queryButton = new Button("查询");		
		queryButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (permitedEnterprise==null) {
					MessageBox.alert("提示", "请选择用户单位", null);
					return;
				}
				if(formPanel.isValid()){
					Date start = startDate.getValue();
					start.setHours(0);
					start.setMinutes(0);
					start.setSeconds(0);
					Date end = endDate.getValue();
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					reflashStore(start, end);
				}
				
				
			}
		});
		formPanel.addButton(queryButton);
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);
		
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
		column.setWidth(180);
		configs.add(column);

		store.setMonitorChanges(true);
		Grid<BeanModel> grid = new Grid<BeanModel>(store, new ColumnModel(
				configs));
		grid.setBorders(true);
		return grid;
	}
	
	private void reflashStore(Date start,Date end) {
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
		getSystemService().listAccountRecordsByPeriod(permitedEnterprise.getEnterpriseId(),start,end, getCallback);
	}
	
	private void refreshEnterprise(long id){
		AsyncCallback<Enterprise> callback = new AsyncCallback<Enterprise>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(Enterprise result) {
				moneyNumberField.setValue(result.getMoney());
			}
		};
		getSystemService().getEnterprise(id, callback);
	}
	
	@SuppressWarnings("deprecation")
	private String date2String(Date date){
		if (date==null) {
			return null;
		}
		int year=date.getYear()+1900;
		int month=date.getMonth()+1;
		int day=date.getDate();
		int hour=date.getHours();
		int Minute=date.getMinutes();
		int second=date.getSeconds();
		String monthString=month>9?""+month:"0"+month;
		String daysString=day>9?""+day:"0"+day;
		String hourString=hour>9?""+hour:"0"+hour;
		String MinuteString=Minute>9?""+Minute:"0"+Minute;
		String secondString=second>9?""+second:"0"+second;
		return year+"-"+monthString+"-"+daysString+" "+hourString
		+":"+MinuteString+":"+secondString;
	}
	
}
