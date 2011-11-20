package com.gavin.ediCustoms.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.gavin.ediCustoms.client.admin.AdminPanel;
import com.gavin.ediCustoms.client.myUI.MyViewport;
import com.gavin.ediCustoms.client.service.SystemService;
import com.gavin.ediCustoms.client.service.SystemServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class EdiCustoms implements EntryPoint {
	private MyViewport viewport; 
	private ContentPanel center;
	private SystemServiceAsync systemService = GWT.create(SystemService.class);

	public void onModuleLoad() {

		viewport = new MyViewport();
		viewport.setStyleAttribute("backgroundColor", "#eeeeee");
		viewport.setLayout(new CenterLayout());
		center = new ContentPanel();
		center.setHeading("新泽报关管理系统");
		center.setSize(1003, 670);
		center.setLayout(new FitLayout());

		boolean isDebug = false;

		if (isDebug) {
			ContentPanel contentPanel=new ContentPanel();
			contentPanel.setSize(100, 100);
			VBoxLayout layout = new VBoxLayout();
	        layout.setPadding(new Padding(5));
	        layout.setVBoxLayoutAlign(VBoxLayoutAlign.CENTER);
			contentPanel.setLayout(layout);
			Button userButton=new Button("user");
			userButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					login("user");					
				}
			});
			Button adminButton=new Button("admin");
			adminButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					login("admin");					
				}				
			});
			contentPanel.add(userButton);
			contentPanel.add(adminButton);
			viewport.add(contentPanel);
			
		} else {

			TabPanel loginTabPanel = new TabPanel();
			loginTabPanel.setWidth(300);
			loginTabPanel.setAutoHeight(true);

			final TabItem userTabItem = new TabItem("用户登录");
			userTabItem.addListener(Events.Select,
					new Listener<ComponentEvent>() {
						public void handleEvent(ComponentEvent be) {
							userTabItem.removeAll();
							userTabItem.add(createUserLoginPanel());
							userTabItem.layout();
						}
					});

			final TabItem adminTabItem = new TabItem("管理员登录");
			adminTabItem.addListener(Events.Select,
					new Listener<ComponentEvent>() {
						public void handleEvent(ComponentEvent be) {
							adminTabItem.removeAll();
							adminTabItem.add(createAdminLoginPanel());
							adminTabItem.layout();
						}
					});

			loginTabPanel.add(userTabItem);
			loginTabPanel.add(adminTabItem);
			viewport.add(loginTabPanel);
		}
		
		RootPanel.get().add(viewport);
	}

	private void addToCenter(LayoutContainer c) {
		viewport.removeAll();
		center.removeAll();
		center.add(c);
		viewport.add(center);
		viewport.layout();
	}

	private FormPanel createUserLoginPanel() {
		final FormPanel formPanel = new FormPanel();
		formPanel.setBodyBorder(false);
		formPanel.setHeaderVisible(false);

		FormData formData = new FormData("-20");

		final TextField<String> userName = new TextField<String>();
		userName.setFieldLabel("用户名");
		userName.setAllowBlank(false);

		final TextField<String> password = new TextField<String>();
		password.setPassword(true);
		password.setFieldLabel("密码");
		password.setAllowBlank(false);

		final TextField<String> checkCode = new TextField<String>();
		checkCode.setFieldLabel("验证码");
		checkCode.setAllowBlank(false);

		Text checkCodeImage = new Text();
		checkCodeImage.setText("<img src='checkCode.jpg' />");
		checkCodeImage.setStyleAttribute("textAlign", "center");

		formPanel.add(userName, formData);
		formPanel.add(password, formData);
		formPanel.add(checkCode, formData);
		formPanel.add(checkCodeImage, formData);

		final AsyncCallback<String> userLoginCallback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(String result) {
				if (result == null) {
					addToCenter(new MainPanel());
				} else {
					MessageBox.alert("提示", result, null);
				}

			}
		};

		Button loginButton = new Button("登录");
		loginButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!formPanel.isValid()) {
					return;
				}
				systemService.validateUser(userName.getValue(),
						password.getValue(), checkCode.getValue(),
						userLoginCallback);
			}
		});

		Button resetButton = new Button("重置");
		resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				formPanel.reset();
			}
		});

		formPanel.addButton(loginButton);
		formPanel.addButton(resetButton);
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);
		return formPanel;
	}

	private FormPanel createAdminLoginPanel() {
		final FormPanel formPanel = new FormPanel();
		formPanel.setBodyBorder(false);
		formPanel.setHeaderVisible(false);

		FormData formData = new FormData("-20");

		final TextField<String> adminName = new TextField<String>();
		adminName.setFieldLabel("管理员");
		adminName.setAllowBlank(false);

		final TextField<String> password = new TextField<String>();
		password.setPassword(true);
		password.setFieldLabel("密码");
		password.setAllowBlank(false);

		final TextField<String> checkCode = new TextField<String>();
		checkCode.setFieldLabel("验证码");
		checkCode.setAllowBlank(false);

		Text checkCodeImage = new Text();
		checkCodeImage.setText("<img src='checkCode.jpg' />");
		checkCodeImage.setStyleAttribute("textAlign", "center");

		formPanel.add(adminName, formData);
		formPanel.add(password, formData);
		formPanel.add(checkCode, formData);
		formPanel.add(checkCodeImage, formData);

		final AsyncCallback<String> adminLoginCallback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(String result) {
				if (result == null) {
					addToCenter(new AdminPanel());
				} else {
					MessageBox.alert("提示", result, null);
				}

			}
		};

		Button loginButton = new Button("登录");
		loginButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!formPanel.isValid()) {
					return;
				}
				systemService.validateAdmin(adminName.getValue(),
						password.getValue(), checkCode.getValue(),
						adminLoginCallback);
			}
		});

		Button resetButton = new Button("重置");
		resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				formPanel.reset();
			}
		});

		formPanel.addButton(loginButton);
		formPanel.addButton(resetButton);
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);
		return formPanel;
	}

	private void login(String type) {
		AsyncCallback<String> adminLoginCallback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(String result) {
				if (result == null) {
					addToCenter(new AdminPanel());
				} else {
					MessageBox.alert("提示", result, null);
				}

			}
		};
		AsyncCallback<String> userLoginCallback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(String result) {
				if (result == null) {
					addToCenter(new MainPanel());
				} else {
					MessageBox.alert("提示", result, null);
				}

			}
		};

		if (type.equals("admin")) {
			systemService.validateAdmin("master", "master", "",
					adminLoginCallback);
		} else {
			systemService.validateUser("gavin", "gavin", "", userLoginCallback);
		}
	}

}
