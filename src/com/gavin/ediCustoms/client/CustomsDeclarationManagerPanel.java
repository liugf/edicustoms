package com.gavin.ediCustoms.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BeanModel;
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
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.google.gwt.user.client.Element;

public class CustomsDeclarationManagerPanel extends MyLayoutContainer {
	private PermitedEnterprise permitedEnterprise;
	private ListStore<BeanModel> permitedEnterpriseStore;
	private FormPanel formPanel;
	
	private TextField<String> contractNo;
	private CheckBox isExportCheckBox;
	private CheckBox isImportCheckBox;
	private DateField startDate;
	private DateField endDate;
	
	public CustomsDeclarationManagerPanel(ListStore<BeanModel> permitedEnterpriseStore) {
		this.permitedEnterpriseStore=permitedEnterpriseStore;
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		ContentPanel content = new ContentPanel();
		content.setHeading("报关单管理");
		content.setLayout(new FitLayout());

		LayoutContainer frame = new LayoutContainer();
		frame.setStyleAttribute("padding", "10px");
		frame.setStyleAttribute("backgroundColor", "#dfe8f6");
		//frame.setLayout(new FitLayout());

		content.add(frame);
		
		frame.add(createForm());

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
					}

				});
		toolBar.add(enterpriseComboBox);
		
		content.setTopComponent(toolBar);
		add(content);
	}
	
	@SuppressWarnings("deprecation")
	private FormPanel createForm(){
		formPanel = new FormPanel();
		formPanel.setBodyBorder(false);
		formPanel.setHeaderVisible(false);
		formPanel.setWidth(380);
		
		contractNo = new TextField<String>();
		contractNo.setFieldLabel("合同号");
		formPanel.add(contractNo);
		
		CheckBoxGroup checkBoxGroup=new CheckBoxGroup();
		checkBoxGroup.setFieldLabel("报关单类型");
		
		isExportCheckBox=new CheckBox();
		isExportCheckBox.setBoxLabel("出口");
		isExportCheckBox.setValue(true);
		
		isImportCheckBox=new CheckBox();
		isImportCheckBox.setBoxLabel("进口");
		isImportCheckBox.setValue(true);
		
		checkBoxGroup.add(isExportCheckBox);
		checkBoxGroup.add(isImportCheckBox);
		formPanel.add(checkBoxGroup);
		
		Date today=new Date();
		today.setDate(1);
		
		startDate = new DateField();
		startDate.setName("declareTime");
		startDate.setFieldLabel("开始日期");
		startDate.setValue(today);
		formPanel.add(startDate);        
		
		endDate = new DateField();
		endDate.setName("declareTime");
		endDate.setFieldLabel("结束日期");
		endDate.setValue(new Date());
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
					query("/customsDeclarationManager.do", null);
				}
			}
		});
		formPanel.addButton(queryButton);
		
		Button excelButton = new Button("导出excel");		
		excelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (permitedEnterprise==null) {
					MessageBox.alert("提示", "请选择用户单位", null);
					return;
				}
				if(formPanel.isValid()){
					query("/customsDeclarationExcel.do", "");
				}
			}
		});
		formPanel.addButton(excelButton);
		
		
		
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);
		
		return formPanel;
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
	
	@SuppressWarnings("deprecation")
	private void query(String url,String target){
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("enterpriseId", permitedEnterprise.getEnterpriseId());
		if (contractNo.getValue()!=null) {
			paramsMap.put("contractNo", contractNo.getValue());
		}		
		
		Boolean isExport=isExportCheckBox.getValue();
		Boolean isImport=isImportCheckBox.getValue();
		
		paramsMap.put("isExport", isExport);
		paramsMap.put("isImport", isImport);
		
		Date start = startDate.getValue();
		if (start!=null) {
			start.setHours(0);
			start.setMinutes(0);
			start.setSeconds(0);
			paramsMap.put("start", date2String(start));
		}
		Date end = endDate.getValue();
		if (end!=null) {
			end.setHours(23);
			end.setMinutes(59);
			end.setSeconds(59);
			paramsMap.put("end", date2String(end));
		}
		if (target!=null) {
			openNewWindow(url,target, paramsMap);
		}else {
			openNewWindow(url, paramsMap);
		}
		
	}
	
}
