package com.gavin.ediCustoms.client.admin.dictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.entity.edi.dictionary.BaseItem;
import com.gavin.ediCustoms.resources.Resources;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class CommonDictionaryPanel<T extends BaseItem> extends MyLayoutContainer {
	protected String heading="";
	protected BeanModelFactory beanModelFactory;
	private ListStore<BeanModel> store;
	protected CheckBoxSelectionModel<BeanModel> selectionMode=new CheckBoxSelectionModel<BeanModel>();
	private Window newWindow;
	private T newItem;
	
	protected abstract List<ColumnConfig> getColumnConfigs() ;
	protected abstract void list(AsyncCallback<List<T>> callback);
	protected abstract void save(T t,AsyncCallback<Long> callback);
	protected abstract void delete(List<Long> ids,AsyncCallback<Void> callback);
	protected abstract void update(List<T> dictionarys,AsyncCallback<Void> callback);
	protected abstract FormCallback<T> initNewItemFormPanel(FormPanel newItemFormPanel);
	
	private FormPanel newItemFormPanel;
	
	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		ContentPanel contentPanel = new ContentPanel();
		contentPanel.setHeading(heading);
		contentPanel.setIcon(Resources.ICONS.table());
		contentPanel.setLayout(new FitLayout());
		
		//添加表格
		reflashStore();
		EditorGrid<BeanModel> grid=new EditorGrid<BeanModel>(store,new ColumnModel(getColumnConfigs()));
		grid.setStyleAttribute("borderTop", "none");
		grid.setClicksToEdit(ClicksToEdit.TWO);
		grid.setSelectionModel(selectionMode);
		grid.addPlugin(selectionMode);
		contentPanel.add(grid);
		
		//添加工具栏
		ToolBar toolBar = new ToolBar();
		Button addButton = new Button("添加", Resources.ICONS.add());
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				getNewWindow().show();
			}
		});
		toolBar.add(addButton);
		toolBar.add(new SeparatorToolItem());
		Button deleteButton = new Button("删除", Resources.ICONS.delete());
		toolBar.add(deleteButton);
		deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (selectionMode.getSelectedItems().size()==0) {
					MessageBox.alert("提示", "请选择要删除的条目！", null);
					return;
				}
				MessageBox.confirm("确认", "确定要删除选中的条目吗？", new Listener<MessageBoxEvent>() {
					@Override
					public void handleEvent(MessageBoxEvent be) {
						if (be.getButtonClicked().getText()==GXT.MESSAGES.messageBox_yes()) {
							delete();
						}
					}
				});
			}
		});
		
		//底部按钮
		final Button updateButton=new Button("更新");
		updateButton.disable();
		updateButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				MessageBox.confirm("确认", "确定要保存变更的条目吗？", new Listener<MessageBoxEvent>() {
					@Override
					public void handleEvent(MessageBoxEvent be) {
						if (be.getButtonClicked().getText()==GXT.MESSAGES.messageBox_yes()) {
							update();
						}
					}
				});
			}
		});
		contentPanel.addButton(updateButton);
		
		contentPanel.setTopComponent(toolBar);
		final Button resetButton=new Button("重置");
		resetButton.disable();
		resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				store.rejectChanges();				
			}
		});
		contentPanel.addButton(resetButton);
		
		contentPanel.setButtonAlign(HorizontalAlignment.CENTER);		
		
		add(contentPanel);
		
		store.addStoreListener(new StoreListener<BeanModel>(){
			@Override
			public void handleEvent(StoreEvent<BeanModel> e) {
				super.handleEvent(e);
				if (e.getType()==Store.Update) {
					if (store.getModifiedRecords().size()!=0) {						
						resetButton.enable();
						updateButton.enable();
					}else {
						resetButton.disable();
						updateButton.disable();
					}
				}
			}
		});
		
		getNewWindow();
	}
	
	private void reflashStore() {
		// proxy and reader
		RpcProxy<List<T>> proxy = new RpcProxy<List<T>>() {
			@Override
			public void load(Object loadConfig,
					AsyncCallback<List<T>> callback) {
				list(callback);
			}
		};
		BeanModelReader reader = new BeanModelReader();
		// loader and store
		ListLoader<ListLoadResult<T>> loader = new BaseListLoader<ListLoadResult<T>>(
				proxy, reader);
		loader.setSortDir(Style.SortDir.ASC);
		loader.setSortField("code");
		store = new ListStore<BeanModel>(loader);
		loader.load();	
		store.sort("code", Style.SortDir.ASC);			
	}
	
	@SuppressWarnings("rawtypes")
	private Window getNewWindow() {
		if (newWindow != null) {
			return newWindow;
		}
		newWindow = new Window();
		newWindow.setIcon(Resources.ICONS.add());
		newWindow.setResizable(false);
		newWindow.setHeading("新建条目");
		newWindow.setAutoHeight(true);		
		newItemFormPanel = new FormPanel();		
		newItemFormPanel.setWidth(300);
		newItemFormPanel.setBodyBorder(false);
		newItemFormPanel.setFrame(true);
		newItemFormPanel.setHeaderVisible(false);
		
		final FormCallback<T> formCallback=initNewItemFormPanel(newItemFormPanel);	
		
		Button saveNewItemButton = new Button("保存");
		newItemFormPanel.setButtonAlign(HorizontalAlignment.CENTER);
		
		final AsyncCallback<Long> newCallback = new AsyncCallback<Long>() {
			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("提醒", "发生错误，请刷新", null);
			}

			@Override
			public void onSuccess(Long result) {
				if (result==0) {
					MessageBox.alert("提醒", "已存在相同条目(编号相同)，添加失败", null);
				}else{
					newItem.setId(result);
					store.insert(beanModelFactory.createModel(newItem), 0);
				}
			}
		};
		
		saveNewItemButton
				.addSelectionListener(new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						if (!newItemFormPanel.isValid())
							return;
						newItem=formCallback.getBean();	
						save(newItem, newCallback);
						newItemFormPanel.reset();
						newWindow.hide();
					}
				});
		newItemFormPanel.addButton(saveNewItemButton);

		Button cancelNewItemButton = new Button("取消");
		cancelNewItemButton
				.addSelectionListener(new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						newItemFormPanel.reset();
						newWindow.hide();
					}
				});
		newItemFormPanel.addButton(cancelNewItemButton);
		newWindow.add(newItemFormPanel);
		
		newWindow.addListener(Events.AfterLayout, new Listener<ContainerEvent>() {
			@Override
			public void handleEvent(ContainerEvent be) {
				newWindow.setWidth(newItemFormPanel.getWidth());	
			}
		});		
		
		return newWindow;		
	}		
	
	private void delete(){		
		final AsyncCallback<Void> deleteCallback = new AsyncCallback<Void>() {			
			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			@Override
			public void onSuccess(Void result) {
				for (Iterator<BeanModel> iterator = selectionMode.getSelectedItems().iterator(); iterator
						.hasNext();) {
					BeanModel beanModel = (BeanModel) iterator.next();
					store.remove(beanModel);
				}
			}
		};
		delete(models2Ids(selectionMode.getSelectedItems()), deleteCallback);
	}
	
	private void update(){
		final AsyncCallback<Void> updateCallback = new AsyncCallback<Void>() {			
			@Override
			public void onFailure(Throwable caught) {
				com.google.gwt.user.client.Window.alert("fail");
			}

			@Override
			public void onSuccess(Void result) {
				store.commitChanges();
			}
		};
		update(records2Beans(store.getModifiedRecords()), updateCallback);
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> models2Ids(List<BeanModel> models){
		List<Long> ids=new ArrayList<Long>();
		for (Iterator<BeanModel> iterator = models.iterator(); iterator.hasNext();) {
			BeanModel beanModel = (BeanModel) iterator.next();
			ids.add(((T)beanModel.getBean()).getId());
		}
		return ids;
	}
	
	@SuppressWarnings("unchecked")
	private List<T> records2Beans(List<Record> records){
		List<T> beans=new ArrayList<T>();
		for (Iterator<Record> iterator = records.iterator(); iterator.hasNext();) {
			Record record = (Record) iterator.next();
			beans.add((T)((BeanModel)record.getModel()).getBean());
		}
		return beans;
	}
			
}
