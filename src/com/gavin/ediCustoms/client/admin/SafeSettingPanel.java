package com.gavin.ediCustoms.client.admin;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.entity.edi.admin.Administrator;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SafeSettingPanel extends MyLayoutContainer {
	private BeanModelFactory beanModelFactory = BeanModelLookup.get()
			.getFactory(Administrator.class);
	private BeanModel beanModel;
	private FormBinding binding;

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		ContentPanel content = new ContentPanel();
		content.setHeading("安全设置");
		content.setLayout(new FitLayout());

		LayoutContainer frame = new LayoutContainer();
		frame.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame.setLayout(new CenterLayout());

		final FormPanel formPanel = new FormPanel();
		formPanel.setWidth(500);
		formPanel.setBodyBorder(false);
		formPanel.setFrame(true);
		formPanel.setHeaderVisible(false);

		FormData formData = new FormData("-20");

		TextField<String> adminName = new TextField<String>();
		adminName.setFieldLabel("管理员");
		formPanel.add(adminName, formData);

		final TextField<String> password = new TextField<String>();
		password.setPassword(true);
		password.setFieldLabel("密码");

		formPanel.add(password, formData);

		final TextField<String> comfirdPassword = new TextField<String>();
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
		formPanel.add(comfirdPassword, formData);

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

		Button resetButton = new Button("重置");
		resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				refleshBeanModel();
				comfirdPassword.setAllowBlank(true);
				comfirdPassword.setValue("");
			}
		});
		Button updateButton = new Button("保存");
		updateButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!formPanel.isValid())
					return;
				if ((password.getValue() == null)) {
					MessageBox.alert("提示", "密码为空，不作任何修改", null);
				} else {
					update();
				}
			}
		});

		formPanel.addButton(updateButton);
		formPanel.addButton(resetButton);
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);

		binding = new FormBinding(formPanel);
		binding.addFieldBinding(new FieldBinding(adminName, "adminName"));
		binding.addFieldBinding(new FieldBinding(password, "password"));
		binding.autoBind();

		refleshBeanModel();
		frame.add(formPanel);
		content.add(frame);
		add(content);
	}

	private void refleshBeanModel() {
		AsyncCallback<Administrator> getCallback = new AsyncCallback<Administrator>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(Administrator result) {
				beanModel = beanModelFactory.createModel(result);
				binding.bind(beanModel);
			}

		};

		getSystemService().getAdministrator(getCallback);
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
		getSystemService().updateAdministrator(
				(Administrator) beanModel.getBean(), callback);
	}
}
