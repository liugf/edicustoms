package com.gavin.ediCustoms.client;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.google.gwt.user.client.Element;

public class TestPanel extends MyLayoutContainer {
	private ListStore<BeanModel> permitedEnterpriseStore;
	
	public TestPanel(ListStore<BeanModel> permitedEnterpriseStore) {
		this.permitedEnterpriseStore=permitedEnterpriseStore;
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		ContentPanel content = new ContentPanel();
		content.setHeading("已审凭证");
		content.setLayout(new FitLayout());

		

		// 添加工具栏
		ToolBar toolBar = new ToolBar();
		
		toolBar.add(new LabelToolItem("收发货单位："));
		ComboBox<BeanModel> permitedEnterpriseComboBox = new ComboBox<BeanModel>();
		permitedEnterpriseComboBox.setWidth(250);
		permitedEnterpriseComboBox.setEmptyText("请选择...");
		permitedEnterpriseComboBox.setDisplayField("displayName");
		permitedEnterpriseComboBox.setStore(permitedEnterpriseStore);
		permitedEnterpriseComboBox.setTypeAhead(true);
		permitedEnterpriseComboBox.setTriggerAction(TriggerAction.ALL);
		
		permitedEnterpriseComboBox.addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<BeanModel>>() {

			@Override
			public void handleEvent(SelectionChangedEvent<BeanModel> be) {
				if (be.getSelectedItem()==null) {
					return;
				}				
				
			}
			
		});
		toolBar.add(permitedEnterpriseComboBox);
		content.setTopComponent(toolBar);
		
		// 选择上传路径 form
		final FormPanel formPanel = new FormPanel ();
		// 设置form参数MULTIPART
		formPanel.setEncoding (FormPanel.Encoding.MULTIPART);
		// 设置提交方式POST
		formPanel.setMethod (FormPanel.Method.POST);
		// 设置上传请求地址
		formPanel.setAction ("/uploadVoucher.do");
		
		formPanel.addListener(Events.Submit, new Listener<FormEvent>() { 
            public void handleEvent(FormEvent be) { 
            	String response= be.getResultHtml();
                System.out.println(response);
                setIsWaiting(false);
            } 
        });  
		
		NumberField id=new NumberField();
		id.setVisible(false);
		id.setPropertyEditorType(Long.class);
		id.setName("enterpriseId");		
		id.setValue(10);
		formPanel.add(id);
		
		// 文件选择
		FileUploadField fileUploadField = new FileUploadField ();
		fileUploadField.setFieldLabel ("请选择上传文件");
		fileUploadField.setName ("file");
		
		
		
		Button button = new Button ("上传");
        button.addListener (Events.OnClick, new Listener <BaseEvent> ()
        {
            @Override
            public void handleEvent (BaseEvent be)
            {
                formPanel.submit ();
                setIsWaiting(true);
            }            
        });
        
        formPanel.add(fileUploadField);
        formPanel.addButton(button);
		
        content.add(formPanel);
		add(content);
	}


	
}
