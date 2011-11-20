package com.gavin.ediCustoms.client.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.entity.edi.Enterprise;
import com.gavin.ediCustoms.entity.edi.RunRelation;
import com.gavin.ediCustoms.entity.edi.SendRelation;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RelationPanel extends MyLayoutContainer {
	
	private ListStore<BeanModel> enterpriseStore;
	private ListStore<BeanModel> sendEnterpriseStore;
	private ListStore<BeanModel> nonSendEnterpriseStore;
	private ListStore<BeanModel> receiveEnterpriseStore;
	private ListStore<BeanModel> nonReceiveEnterpriseStore;
	private ListStore<BeanModel> runEnterpriseStore;
	private ListStore<BeanModel> nonRunEnterpriseStore;
	
	private Button editButton;
	
	private Button updateButton;
	private Button cancelButton;
	
	private Long currentEnterpriseId;
	
	private GridDragSource sendEnterpriseDragSource;
	private GridDragSource nonSendEnterpriseDragSource;
	private GridDragSource receiveEnterpriseDragSource;
	private GridDragSource nonReceiveEnterpriseDragSource;
	private GridDragSource runEnterpriseDragSource;
	private GridDragSource nonRunEnterpriseDragSource;

	public RelationPanel() {
		
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		ContentPanel content = new ContentPanel();
		content.setHeading("单位关系");
		content.setLayout(new RowLayout(Orientation.VERTICAL));
		
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();		
		ColumnConfig column = new ColumnConfig();
		column.setId("tradeCode");
		column.setHeader("公司编号");
		column.setWidth(120);
		configs.add(column);
		column = new ColumnConfig();
		column.setId("registeName");
		column.setHeader("公司名称");
		column.setWidth(256);
		configs.add(column);
		
		LayoutContainer frame1 = new LayoutContainer();
		frame1.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame1.setLayout(new RowLayout(Orientation.HORIZONTAL));		
		sendEnterpriseStore=new ListStore<BeanModel>();
		nonSendEnterpriseStore=new ListStore<BeanModel>();
		Grid<BeanModel> sendEnterpriseGrid=new Grid<BeanModel>(sendEnterpriseStore, new ColumnModel(configs));
		Grid<BeanModel> nonSendEnterpriseGrid=new Grid<BeanModel>(nonSendEnterpriseStore, new ColumnModel(configs));
		ContentPanel sendEnterprisePanel = new ContentPanel();
		sendEnterprisePanel.setHeading("发货单位");
		sendEnterprisePanel.setLayout(new FitLayout());
		ContentPanel nonSendEnterprisePanel = new ContentPanel();
		nonSendEnterprisePanel.setHeading("非发货单位");
		nonSendEnterprisePanel.setLayout(new FitLayout());
		sendEnterprisePanel.add(sendEnterpriseGrid);
		nonSendEnterprisePanel.add(nonSendEnterpriseGrid);
		sendEnterpriseDragSource = new GridDragSource(sendEnterpriseGrid);
		nonSendEnterpriseDragSource = new GridDragSource(nonSendEnterpriseGrid);
		sendEnterpriseDragSource.disable();
		nonSendEnterpriseDragSource.disable();	
		sendEnterpriseDragSource.setGroup("group1");
		nonSendEnterpriseDragSource.setGroup("group1");
		GridDropTarget sendEnterpriseDropTarget = new GridDropTarget(sendEnterpriseGrid);
		sendEnterpriseDropTarget.setAllowSelfAsSource(false);
		sendEnterpriseDropTarget.setFeedback(Feedback.INSERT);
		sendEnterpriseDropTarget.setGroup("group1");
		GridDropTarget nonSendEnterpriseDropTarget = new GridDropTarget(nonSendEnterpriseGrid);
		nonSendEnterpriseDropTarget.setAllowSelfAsSource(false);
		nonSendEnterpriseDropTarget.setFeedback(Feedback.INSERT);	
		nonSendEnterpriseDropTarget.setGroup("group1");
		RowData data1 = new RowData(.5, 1);
		data1.setMargins(new Margins(10));
		RowData data2 = new RowData(.5, 1);
		data2.setMargins(new Margins(10));
		frame1.add(sendEnterprisePanel, data1);
		frame1.add(nonSendEnterprisePanel, data2);
		
		LayoutContainer frame2 = new LayoutContainer();
		frame2.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame2.setLayout(new RowLayout(Orientation.HORIZONTAL));		
		receiveEnterpriseStore=new ListStore<BeanModel>();
		nonReceiveEnterpriseStore=new ListStore<BeanModel>();
		Grid<BeanModel> receiveEnterpriseGrid=new Grid<BeanModel>(receiveEnterpriseStore, new ColumnModel(configs));
		Grid<BeanModel> nonReceiveEnterpriseGrid=new Grid<BeanModel>(nonReceiveEnterpriseStore, new ColumnModel(configs));
		ContentPanel receiveEnterprisePanel = new ContentPanel();
		receiveEnterprisePanel.setHeading("收货单位");
		receiveEnterprisePanel.setLayout(new FitLayout());
		ContentPanel nonReceiveEnterprisePanel = new ContentPanel();
		nonReceiveEnterprisePanel.setHeading("非收货单位");
		nonReceiveEnterprisePanel.setLayout(new FitLayout());
		receiveEnterprisePanel.add(receiveEnterpriseGrid);
		nonReceiveEnterprisePanel.add(nonReceiveEnterpriseGrid);
		receiveEnterpriseDragSource = new GridDragSource(receiveEnterpriseGrid);
		nonReceiveEnterpriseDragSource = new GridDragSource(nonReceiveEnterpriseGrid);
		receiveEnterpriseDragSource.disable();
		nonReceiveEnterpriseDragSource.disable();	
		receiveEnterpriseDragSource.setGroup("group2");
		nonReceiveEnterpriseDragSource.setGroup("group2");
		GridDropTarget receiveEnterpriseDropTarget = new GridDropTarget(receiveEnterpriseGrid);
		receiveEnterpriseDropTarget.setAllowSelfAsSource(false);
		receiveEnterpriseDropTarget.setFeedback(Feedback.INSERT);
		receiveEnterpriseDropTarget.setGroup("group2");
		GridDropTarget nonReceiveEnterpriseDropTarget = new GridDropTarget(nonReceiveEnterpriseGrid);
		nonReceiveEnterpriseDropTarget.setAllowSelfAsSource(false);
		nonReceiveEnterpriseDropTarget.setFeedback(Feedback.INSERT);
		nonReceiveEnterpriseDropTarget.setGroup("group2");
		data1 = new RowData(.5, 1);
		data1.setMargins(new Margins(10));
		data2 = new RowData(.5, 1);
		data2.setMargins(new Margins(10));
		frame2.add(receiveEnterprisePanel, data1);
		frame2.add(nonReceiveEnterprisePanel, data2);
		
		LayoutContainer frame3 = new LayoutContainer();
		frame3.setStyleAttribute("backgroundColor", "#dfe8f6");
		frame3.setLayout(new RowLayout(Orientation.HORIZONTAL));		
		runEnterpriseStore=new ListStore<BeanModel>();
		nonRunEnterpriseStore=new ListStore<BeanModel>();
		Grid<BeanModel> runEnterpriseGrid=new Grid<BeanModel>(runEnterpriseStore, new ColumnModel(configs));
		Grid<BeanModel> nonRunEnterpriseGrid=new Grid<BeanModel>(nonRunEnterpriseStore, new ColumnModel(configs));
		ContentPanel runEnterprisePanel = new ContentPanel();
		runEnterprisePanel.setHeading("经营单位");
		runEnterprisePanel.setLayout(new FitLayout());
		ContentPanel nonRunEnterprisePanel = new ContentPanel();
		nonRunEnterprisePanel.setHeading("非经营单位");
		nonRunEnterprisePanel.setLayout(new FitLayout());
		runEnterprisePanel.add(runEnterpriseGrid);
		nonRunEnterprisePanel.add(nonRunEnterpriseGrid);
		runEnterpriseDragSource = new GridDragSource(runEnterpriseGrid);
		nonRunEnterpriseDragSource = new GridDragSource(nonRunEnterpriseGrid);
		runEnterpriseDragSource.disable();
		nonRunEnterpriseDragSource.disable();	
		runEnterpriseDragSource.setGroup("group3");
		nonRunEnterpriseDragSource.setGroup("group3");
		GridDropTarget runEnterpriseDropTarget = new GridDropTarget(runEnterpriseGrid);
		runEnterpriseDropTarget.setAllowSelfAsSource(false);
		runEnterpriseDropTarget.setFeedback(Feedback.INSERT);
		runEnterpriseDropTarget.setGroup("group3");
		GridDropTarget nonRunEnterpriseDropTarget = new GridDropTarget(nonRunEnterpriseGrid);
		nonRunEnterpriseDropTarget.setAllowSelfAsSource(false);
		nonRunEnterpriseDropTarget.setFeedback(Feedback.INSERT);
		runEnterpriseDropTarget.setGroup("group3");
		data1 = new RowData(.5, 1);
		data1.setMargins(new Margins(10));
		data2 = new RowData(.5, 1);
		data2.setMargins(new Margins(10));
		frame3.add(runEnterprisePanel, data1);
		frame3.add(nonRunEnterprisePanel, data2);
		
		
		
		RowData data = new RowData(1, 0.3333333333333333333333334);
		content.add(frame1, data);
		content.add(frame2, data);
		content.add(frame3, data);
		

		enterpriseStore=getEnterpriseStore();
		
		// 添加工具栏
		ToolBar toolBar = new ToolBar();
		
		toolBar.add(new LabelToolItem("企业名称："));
		ComboBox<BeanModel> enterpriseComboBox = new ComboBox<BeanModel>();
		enterpriseComboBox.setWidth(300);
		enterpriseComboBox.setForceSelection(true);
		enterpriseComboBox.setEmptyText("请选择...");
		enterpriseComboBox.setDisplayField("displayName");
		enterpriseComboBox.setStore(enterpriseStore);
		enterpriseComboBox.setTypeAhead(true);
		enterpriseComboBox.setTriggerAction(TriggerAction.ALL);

		enterpriseComboBox.addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<BeanModel>>() {

					@Override
					public void handleEvent(SelectionChangedEvent<BeanModel> be) {
						if (be.getSelectedItem() == null) {
							return;
						}
						currentEnterpriseId = be.getSelectedItem().get("id");	
						refleshStores();
						resetState();
					}

				});
		toolBar.add(enterpriseComboBox);

		toolBar.add(new SeparatorToolItem());
		
		editButton = new Button("修改", Resources.ICONS.edit());
		editButton.disable();
		editButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				editButton.disable();
				updateButton.enable();
				cancelButton.enable();
				sendEnterpriseDragSource.enable();
				nonSendEnterpriseDragSource.enable();
				receiveEnterpriseDragSource.enable();
				nonReceiveEnterpriseDragSource.enable();
				runEnterpriseDragSource.enable();
				nonRunEnterpriseDragSource.enable();				
			}
		});
		toolBar.add(editButton);
		content.setTopComponent(toolBar);
		
		// 添加底部按钮
		updateButton = new Button("更新");
		updateButton.disable();
		updateButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				update();
			}
		});
		cancelButton = new Button("取消");
		cancelButton.disable();
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				resetState();
			}
		});
		
		content.setButtonAlign(HorizontalAlignment.CENTER);
		content.addButton(updateButton);
		content.addButton(cancelButton);
		
		add(content);
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
	
	private void refleshStores(){
		AsyncCallback<List<Enterprise>> callback = new AsyncCallback<List<Enterprise>>() {
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
				sendEnterpriseStore.removeAll();
				sendEnterpriseStore.add(BeanModelLookup.get()
							.getFactory(Enterprise.class).createModel(result));
				nonSendEnterpriseStore.removeAll();
				for (BeanModel beanModel : enterpriseStore.getModels()) {
					if (sendEnterpriseStore.findModel("id", beanModel.get("id"))==null) {
						if (!currentEnterpriseId.equals(beanModel.get("id"))) {
							nonSendEnterpriseStore.add(beanModel);
						}						
					}
				}
			}

		};
		getSystemService().listSendEnterprise(currentEnterpriseId,callback);
		
		callback = new AsyncCallback<List<Enterprise>>() {
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
				receiveEnterpriseStore.removeAll();
				receiveEnterpriseStore.add(BeanModelLookup.get()
							.getFactory(Enterprise.class).createModel(result));	
				nonReceiveEnterpriseStore.removeAll();
				for (BeanModel beanModel : enterpriseStore.getModels()) {
					BeanModel bm=receiveEnterpriseStore.findModel("id", beanModel.get("id"));
					if (bm==null) {
						if (!currentEnterpriseId.equals(beanModel.get("id"))) {
							nonReceiveEnterpriseStore.add(beanModel);
						}	
					}
				}
			}
		};
		getSystemService().listReceiveEnterprise(currentEnterpriseId,callback);
		
		callback = new AsyncCallback<List<Enterprise>>() {
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
				runEnterpriseStore.removeAll();
				runEnterpriseStore.add(BeanModelLookup.get()
							.getFactory(Enterprise.class).createModel(result));	
				nonRunEnterpriseStore.removeAll();
				for (BeanModel beanModel : enterpriseStore.getModels()) {
					BeanModel bm=runEnterpriseStore.findModel("id", beanModel.get("id"));
					if (bm==null) {
						nonRunEnterpriseStore.add(beanModel);
					}
				}
				
			}
		};
		getSystemService().listRunEnterprise(currentEnterpriseId,callback);		
	}
	
	private void update() {
		List<SendRelation> sendRelations=new ArrayList<SendRelation>();
		for(BeanModel beanModel :sendEnterpriseStore.getModels()){
			sendRelations.add(new SendRelation((Long)beanModel.get("id"), currentEnterpriseId));
		}
		for(BeanModel beanModel :receiveEnterpriseStore.getModels()){
			sendRelations.add(new SendRelation(currentEnterpriseId,(Long)beanModel.get("id")));
		}
		List<RunRelation> runRelations=new ArrayList<RunRelation>();
		for(BeanModel beanModel :runEnterpriseStore.getModels()){
			runRelations.add(new RunRelation((Long)beanModel.get("id"), currentEnterpriseId));
		}
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "更新用户发生错误，请刷新！", null);
			}
			
			public void onSuccess(Void result) {				
				resetState();
				MessageBox.info("提示", "修改成功！", null);
			}
		};
		getSystemService().updateRelation(currentEnterpriseId, sendRelations, runRelations, callback);
	}

	private void resetState(){
		refleshStores();
		editButton.enable();
		
		updateButton.disable();
		cancelButton.disable();
		sendEnterpriseDragSource.disable();
		nonSendEnterpriseDragSource.disable();
		receiveEnterpriseDragSource.disable();
		nonReceiveEnterpriseDragSource.disable();
		runEnterpriseDragSource.disable();
		nonRunEnterpriseDragSource.disable();
	}
	
}
