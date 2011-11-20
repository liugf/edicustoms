package com.gavin.ediCustoms.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SafeSettingPanel extends MyLayoutContainer {
	
	TextField<String> password;

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

		password = new TextField<String>();
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

		Button updateButton = new Button("更改");
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
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);		

		frame.add(formPanel);
		content.add(frame);
		add(content);
	}

	private void update() {
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(String result) {
				MessageBox.info("提示", result, null);
			}
		};
		getSystemService().updatePassword(password.getValue(), callback);
	}
}
