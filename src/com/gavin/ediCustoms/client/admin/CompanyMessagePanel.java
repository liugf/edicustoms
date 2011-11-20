package com.gavin.ediCustoms.client.admin;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.entity.edi.admin.CompanyMessage;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CompanyMessagePanel extends MyLayoutContainer {
	private BeanModelFactory beanModelFactory = BeanModelLookup.get()
			.getFactory(CompanyMessage.class);
	private BeanModel beanModel;
	private FormBinding binding;

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		ContentPanel content = new ContentPanel();
		content.setHeading("企业信息");		
		content.setLayout(new FitLayout());
		
		LayoutContainer frame=new LayoutContainer();
		frame.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame.setLayout(new CenterLayout());

		final FormPanel formPanel = new FormPanel();
		formPanel.setWidth(500);
		formPanel.setBodyBorder(false);
		formPanel.setFrame(true);
		formPanel.setHeaderVisible(false);
		
		FormData formData = new FormData("-20");

		TextField<String> companyName = new TextField<String>();
		companyName.setName("companyName");
		companyName.setFieldLabel("公司名称");
		companyName.setAllowBlank(false);
		formPanel.add(companyName, formData);

		TextField<String> address = new TextField<String>();
		address.setName("address");
		address.setFieldLabel("地址");
		formPanel.add(address, formData);

		TextField<String> telephone = new TextField<String>();
		telephone.setName("telephone");
		telephone.setFieldLabel("电话");
		formPanel.add(telephone, formData);

		TextField<String> faxField = new TextField<String>();
		faxField.setFieldLabel("传真");
		formPanel.add(faxField, formData);

		TextField<String> zip = new TextField<String>();
		zip.setName("zip");
		zip.setFieldLabel("邮编");
		formPanel.add(zip, formData);

		TextField<String> email = new TextField<String>();
		email.setName("email");
		email.setFieldLabel("邮箱");
		formPanel.add(email, formData);
		
		TextField<String> ediPlatformCode = new TextField<String>();
		ediPlatformCode.setAllowBlank(false);
		ediPlatformCode.setName("ediPlatformCode");
		ediPlatformCode.setFieldLabel("EDI平台号");
		ediPlatformCode.setMaxLength(4);
		formPanel.add(ediPlatformCode, formData);

		Button resetButton = new Button("重置");		
		resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				refleshBeanModel();
			}
		});
		Button updateButton = new Button("保存");		
		updateButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!formPanel.isValid())
					return;
				update();
			}
		});
		
		formPanel.addButton(updateButton);
		formPanel.addButton(resetButton);
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);

		binding = new FormBinding(formPanel,true);	

		refleshBeanModel();

		frame.add(formPanel);
		content.add(frame);
		add(content);
	
	}

	private void refleshBeanModel() {
		AsyncCallback<CompanyMessage> getCallback = new AsyncCallback<CompanyMessage>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(CompanyMessage result) {
				beanModel = beanModelFactory.createModel(result);
				binding.bind(beanModel);
			}

		};

		getSystemService().getCompanyMessage(getCallback);
	}

	private void update() {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(Void result) {
				MessageBox.info("提示", "保存成功！", null);
			}
		};
		getSystemService().updateCompanyMessage(
				(CompanyMessage) beanModel.getBean(), callback);
	}

}
