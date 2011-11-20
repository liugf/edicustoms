package com.gavin.ediCustoms.client.core;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.gavin.ediCustoms.client.myUI.MyTabItem;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TransitInformationTabItem extends MyTabItem implements CustomsDeclarationPanelListener{
	private FormBinding formBindings;
	private FormPanel formPanel;
	private BeanModel beanModel;
	private CustomsDeclarationHead selectedCustomsDeclarationHead;
	
	public TransitInformationTabItem() {
		setText("转关信息");	
		
		addListener(Events.Select,
				new Listener<ComponentEvent>() {
					public void handleEvent(ComponentEvent be) {
						if (selectedCustomsDeclarationHead == null) {
							MessageBox.alert("提示", "请选择报关单表头", null);
							be.cancelBubble();
						} else {
							if (isDirty) {
								refresh();
								isDirty=false;
							}
							
						}
					}
				});
	}
	
	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		
		LayoutContainer frame=new LayoutContainer();
		frame.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame.setLayout(new CenterLayout());
		
		formPanel=createFormPanel();
		frame.add(formPanel);
		add(frame);
		
	}
	
	private FormPanel createFormPanel(){
		final FormPanel formPanel=new FormPanel();
		
		FormData formData = new FormData("-20");
		
		formPanel.setBodyBorder(false);
		formPanel.setWidth(500);
		formPanel.setBodyBorder(false);
		formPanel.setFrame(true);
		formPanel.setHeaderVisible(false);
		formPanel.setLabelWidth(250);
		
		TextField<String> organizationCode = new TextField<String>();
		organizationCode.setName("organizationCode");
		organizationCode.setFieldLabel("承运单位组织机构代码");
		formPanel.add(organizationCode,formData);
		
		TextField<String> corporationName = new TextField<String>();
		corporationName.setName("corporationName");
		corporationName.setFieldLabel("承运单位名称");
		formPanel.add(corporationName,formData);
		
		TextField<String> meansOfTransportCode = new TextField<String>();
		meansOfTransportCode.setName("meansOfTransportCode");
		meansOfTransportCode.setFieldLabel("进出境运输工具编号(汽车则添加司机本号)");
		formPanel.add(meansOfTransportCode,formData);
		
		TextField<String> meansOfTransportName = new TextField<String>();
		meansOfTransportName.setName("meansOfTransportName");
		meansOfTransportName.setFieldLabel("进出境运输工具名称(汽车则添加车牌号)");
		formPanel.add(meansOfTransportName,formData);
		
		TextField<String> meansOfTransportId = new TextField<String>();
		meansOfTransportId.setName("meansOfTransportId");
		meansOfTransportId.setFieldLabel("进出境运输工具航次(汽车则添加日期)");
		formPanel.add(meansOfTransportId,formData);
		
		TextField<String> billOfLadingNo = new TextField<String>();
		billOfLadingNo.setName("billOfLadingNo");
		billOfLadingNo.setFieldLabel("进出境提单号(汽车则添加车牌号)");
		formPanel.add(billOfLadingNo,formData);
		
		NumberField localMeansOfTransportMode = new NumberField();
		localMeansOfTransportMode.setPropertyEditorType(Integer.class);
		localMeansOfTransportMode.setName("localMeansOfTransportMode");
		localMeansOfTransportMode.setFieldLabel("境内运输方式");
		formPanel.add(localMeansOfTransportMode,formData);
		
		TextField<String> localMeansOfTransportCode = new TextField<String>();
		localMeansOfTransportCode.setName("localMeansOfTransportCode");
		localMeansOfTransportCode.setFieldLabel("境内运输工具编号(汽车则添加司机本号)");
		formPanel.add(localMeansOfTransportCode,formData);
		
		TextField<String> localMeansOfTransportName = new TextField<String>();
		localMeansOfTransportName.setName("localMeansOfTransportName");
		localMeansOfTransportName.setFieldLabel("境内运输工具名称(汽车则添加车牌号)");
		formPanel.add(localMeansOfTransportName,formData);
		
		TextField<String> localMeansOfTransportId = new TextField<String>();
		localMeansOfTransportId.setName("localMeansOfTransportId");
		localMeansOfTransportId.setFieldLabel("境内运输工具航次(汽车则添加日期)");
		formPanel.add(localMeansOfTransportId,formData);
		
		TextField<String> iccardNo = new TextField<String>();
		iccardNo.setName("iccardNo");
		iccardNo.setFieldLabel("IC卡号");
		formPanel.add(iccardNo,formData);
		
		formBindings = new FormBinding(formPanel, true);
		
		Button updateButton = new Button("保存");
		updateButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!formPanel.isValid()){
					return;
				}
				update();
			}
		});
		formPanel.addButton(updateButton);
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);

		/*Button resetButton = new Button("重置");
		resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				store.rejectChanges();
			}
		});
		formPanel.addButton(resetButton);
		*/
		
		return formPanel;
	}

	private void update() {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(Void result) {
				MessageBox.info("提示", "修改成功！", null);
			}
		};
		List<CustomsDeclarationHead> list = new ArrayList<CustomsDeclarationHead>();
		list.add((CustomsDeclarationHead) beanModel.getBean());
		getBusinessService().updateCustomsDeclarationHead(list, callback);
	}
	
	private void refresh(){
		AsyncCallback<CustomsDeclarationHead> callback = new AsyncCallback<CustomsDeclarationHead>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(CustomsDeclarationHead result) {
				selectedCustomsDeclarationHead = result;
				BeanModelFactory beanModelFactory=BeanModelLookup.get().getFactory(CustomsDeclarationHead.class);
				beanModel = beanModelFactory.createModel(result);
				formBindings.bind(beanModel);
			}
		};
		getBusinessService().getCustomsDeclarationHead(selectedCustomsDeclarationHead.getId(), callback);
	}

	@Override
	public void changePermitedEnterprise(PermitedEnterprise permitedEnterprise) {
		
	}

	@Override
	public void changeCustomsDeclarationHead(
			CustomsDeclarationHead selectedCustomsDeclarationHead) {
		this.selectedCustomsDeclarationHead=selectedCustomsDeclarationHead;
		isDirty=true;
	}

	
}
