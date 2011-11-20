package com.gavin.ediCustoms.client;

import java.util.HashMap;
import java.util.Map;


import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DriverPaperPanel extends MyLayoutContainer {
	private FormPanel formPanel;
	
	public DriverPaperPanel(ListStore<BeanModel> permitedEnterpriseStore) {
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		ContentPanel content = new ContentPanel();
		content.setHeading("司机纸");
		content.setLayout(new FitLayout());

		LayoutContainer frame = new LayoutContainer();
		frame.setStyleAttribute("padding", "10px");
		frame.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame.setLayout(new FitLayout());

		content.add(frame);
		
		frame.add(createForm());

		// 添加工具栏
		ToolBar toolBar = new ToolBar();

		toolBar.add(new LabelToolItem("运输工具："));
		final TextField<String> transportTool=new TextField<String>();
		toolBar.add(transportTool);
		
		Button button = new Button("确定");
		button.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				AsyncCallback<Map<String, Object>> callback = new AsyncCallback<Map<String, Object>>() {
					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(Map<String, Object> result) {
						bindForm(result);
					}
					
				};
				getBusinessService().getDriverPaper(transportTool.getValue(), callback);
			}
		});
		toolBar.add(button);
		
		
		
		content.setTopComponent(toolBar);
		add(content);
	}
	
	private FormPanel createForm(){
		formPanel = new FormPanel();
		formPanel.setBodyBorder(false);
		formPanel.setHeaderVisible(false);
		
		LayoutContainer main = new LayoutContainer();
		main.setLayout(new ColumnLayout());

		LayoutContainer left = new LayoutContainer();
		left.setStyleAttribute("paddingRight", "10px");
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(80);
		left.setLayout(layout);

		LayoutContainer right = new LayoutContainer();
		right.setStyleAttribute("paddingLeft", "10px");
		layout = new FormLayout();
		layout.setLabelWidth(80);
		right.setLayout(layout);

		FormData formData = new FormData("-20");
		
		TextField<String> truckNo = new TextField<String>();
		truckNo.setName("truckNo");
		truckNo.setFieldLabel("内地车牌");
		left.add(truckNo, formData);
		
		TextField<String> hkTruckNo = new TextField<String>();
		hkTruckNo.setName("hkTruckNo");
		hkTruckNo.setFieldLabel("香港车牌");
		left.add(hkTruckNo, formData);
		
		TextField<String> declareTime = new TextField<String>();
		declareTime.setName("declareTime");
		declareTime.setFieldLabel("进/出境日期");
		left.add(declareTime, formData);
		
		TextField<String> ownerDistrict = new TextField<String>();
		ownerDistrict.setName("ownerDistrict");
		ownerDistrict.setFieldLabel("装货地点");
		left.add(ownerDistrict, formData);
		
		TextField<String> destinationPort = new TextField<String>();
		destinationPort.setName("destinationPort");
		destinationPort.setFieldLabel("卸货地点");
		left.add(destinationPort, formData);
		
		TextField<String> no = new TextField<String>();
		no.setName("no");
		no.setFieldLabel("项目");
		left.add(no, formData);
		
		TextField<String> goodNameAndModel = new TextField<String>();
		goodNameAndModel.setName("goodNameAndModel");
		goodNameAndModel.setFieldLabel("货物名称规格");
		left.add(goodNameAndModel, formData);
		
		TextField<String> andMore = new TextField<String>();
		andMore.setName("andMore");
		andMore.setFieldLabel("详见清单");
		left.add(andMore, formData);
		
		TextField<String> goodCode = new TextField<String>();
		goodCode.setName("goodCode");
		goodCode.setFieldLabel("标记及编号");
		left.add(goodCode, formData);
		
		TextField<String> wrapType = new TextField<String>();
		wrapType.setName("wrapType");
		wrapType.setFieldLabel("包装方式数量");
		left.add(wrapType, formData);
		
		TextField<String> netWeight = new TextField<String>();
		netWeight.setName("netWeight");
		netWeight.setFieldLabel("净重");
		left.add(netWeight, formData);
		
		TextField<String> totleMoney = new TextField<String>();
		totleMoney.setName("totleMoney");
		totleMoney.setFieldLabel("价格");
		left.add(totleMoney, formData);
		
		TextField<String> enterpriseName = new TextField<String>();
		enterpriseName.setName("enterpriseName");
		enterpriseName.setFieldLabel("付货人");
		left.add(enterpriseName, formData);
		
		TextField<String> foreignEnterpriseName = new TextField<String>();
		foreignEnterpriseName.setName("foreignEnterpriseName");
		foreignEnterpriseName.setFieldLabel("收货人");
		left.add(foreignEnterpriseName, formData);
		
		TextField<String> packNo = new TextField<String>();
		packNo.setName("packNo");
		packNo.setFieldLabel("总件数");
		right.add(packNo, formData);
		
		TextField<String> grossWeight = new TextField<String>();
		grossWeight.setName("grossWeight");
		grossWeight.setFieldLabel("总重量");
		right.add(grossWeight, formData);
		
		TextField<String> containerNo = new TextField<String>();
		containerNo.setName("containerNo");
		containerNo.setFieldLabel("集装箱编号");
		right.add(containerNo, formData);
		
		TextField<String> enterpriseName2 = new TextField<String>();
		enterpriseName2.setName("enterpriseName2");
		enterpriseName2.setFieldLabel("上列货物由");
		right.add(enterpriseName2, formData);
		
		TextField<String> truckCorporationName = new TextField<String>();
		truckCorporationName.setName("truckCorporationName");
		truckCorporationName.setFieldLabel("承运公司名称");
		right.add(truckCorporationName, formData);
		
		TextField<String> truckCorporationAddress = new TextField<String>();
		truckCorporationAddress.setName("truckCorporationAddress");
		truckCorporationAddress.setFieldLabel("承运公司地址");
		right.add(truckCorporationAddress, formData);
		
		TextField<String> truckCorporationTelephone = new TextField<String>();
		truckCorporationTelephone.setName("truckCorporationTelephone");
		truckCorporationTelephone.setFieldLabel("承运公司电话");
		right.add(truckCorporationTelephone, formData);
		
		TextField<String> truckDriverName = new TextField<String>();
		truckDriverName.setName("truckDriverName");
		truckDriverName.setFieldLabel("司机姓名");
		right.add(truckDriverName, formData);
		
		TextField<String> contractNo = new TextField<String>();
		contractNo.setName("contractNo");
		contractNo.setFieldLabel("合同号");
		right.add(contractNo, formData);
		
		TextField<String> tradeMode = new TextField<String>();
		tradeMode.setName("tradeMode");
		tradeMode.setFieldLabel("监管方式");
		right.add(tradeMode, formData);
		
		TextField<String> originCountry = new TextField<String>();
		originCountry.setName("originCountry");
		originCountry.setFieldLabel("产销国");
		right.add(originCountry, formData);
		
		TextField<String> truckCode = new TextField<String>();
		truckCode.setName("truckCode");
		truckCode.setFieldLabel("车辆海关编码");
		right.add(truckCode, formData);
		
		TextField<String> iePort = new TextField<String>();
		iePort.setName("iePort");
		iePort.setFieldLabel("进/出境地");
		right.add(iePort, formData);
		
		TextField<String> declareCustoms = new TextField<String>();
		declareCustoms.setName("declareCustoms");
		declareCustoms.setFieldLabel("指/启运地");
		right.add(declareCustoms, formData);
		
		main.add(left, new ColumnData(.5));
		main.add(right, new ColumnData(.5));

		formPanel.add(main, new FormData("100%"));
		
		Button printButton1= new Button("打印大陆司机纸",Resources.ICONS.printer());
		printButton1.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void componentSelected(ButtonEvent ce) {
				Map<String, Object> paramsMap=new HashMap<String, Object>();
				for(Field field:formPanel.getFields()){
					if (field.getName()!=null) {
						String key=field.getName();
						String value=field.getValue()==null?"":field.getValue().toString();	
						paramsMap.put(key, value);
					}									
				}
				openNewWindow("/driverPaper1.do",paramsMap);				
			}
		});	
		
		Button printButton2= new Button("打印香港司机纸",Resources.ICONS.printer());
		printButton2.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@SuppressWarnings("rawtypes")
			@Override
			public void componentSelected(ButtonEvent ce) {
				Map<String, Object> paramsMap=new HashMap<String, Object>();
				for(Field field:formPanel.getFields()){
					if (field.getName()!=null) {
						String key=field.getName();
						String value=field.getValue()==null?"":field.getValue().toString();	
						paramsMap.put(key, value);
					}									
				}
				openNewWindow("/driverPaper2.do",paramsMap);				
			}
		});	
		
		formPanel.addButton(printButton1);
		formPanel.addButton(printButton2);	
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);
		
		return formPanel;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void bindForm(Map<String, Object> map){
		if (map==null) {
			return;
		}
		for (Field field : formPanel.getFields()) {
			if (field.getName()!=null) {
				field.setValue(map.get(field.getName()));
			}
		}
	}
	
}
