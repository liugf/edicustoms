package com.gavin.ediCustoms.client.contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gavin.ediCustoms.client.myUI.MyTabItem;
import com.gavin.ediCustoms.entity.edi.contract.ContractProduct;
import com.gavin.ediCustoms.resources.Resources;
import com.gavin.ediCustoms.shared.MyCurrencyData;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ContractProcessFeeTabItem extends MyTabItem {
	private BeanModelFactory beanModelFactory;
	private ListStore<BeanModel> store;
	private CheckBoxSelectionModel<BeanModel> selectionMode=new CheckBoxSelectionModel<BeanModel>();
	private Grid<BeanModel> grid;

	public ContractProcessFeeTabItem() {
		setText("工缴费");	
		store=new ListStore<BeanModel>();
		beanModelFactory = BeanModelLookup.get().getFactory(ContractProduct.class);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		
		ContentPanel content = new ContentPanel();
		content.setHeaderVisible(false);
		content.setBodyBorder(false);
		content.setLayout(new FitLayout());
		
		grid=createGrid();		
		content.add(grid);
		
		// 添加工具栏
		ToolBar toolBar = new ToolBar();
		Button printButton= new Button("打印",Resources.ICONS.printer());
		toolBar.add(printButton);
		
		content.setTopComponent(toolBar);
		add(content);
		
	}
	
	private Grid<BeanModel> createGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();
		
		configs.add(selectionMode.getColumn());
		
		column.setId("no");
		column.setHeader("序号");
		column.setWidth(100);
		column.setRowHeader(true);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("name");
		column.setHeader("成品名称");
		column.setWidth(226);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("goodModel");
		column.setHeader("规格、型号");
		column.setWidth(226);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setNumberFormat(NumberFormat.getCurrencyFormat(new MyCurrencyData()));
		column.setId("processFee");
		column.setHeader("加工费(单价)");
		column.setWidth(100);
		configs.add(column);

		store.setMonitorChanges(true);
		Grid<BeanModel> grid = new Grid<BeanModel>(store, new ColumnModel(
				configs));
		grid.setSelectionModel(selectionMode);
		grid.addPlugin(selectionMode);
		return grid;
	}

	public void refleshStore(Long contractHeadId){
		AsyncCallback<List<ContractProduct>> getCallback = new AsyncCallback<List<ContractProduct>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
			}

			public void onSuccess(List<ContractProduct> result) {
				Collections.sort(result, new Comparator<ContractProduct>() {
					public int compare(ContractProduct arg0, ContractProduct arg1) {						
						return arg0.getNo()-arg1.getNo();
					}
				});
				store.removeAll();
				store.add(beanModelFactory.createModel(result));
			}
		};
		getBusinessService().listContractProduct(contractHeadId, getCallback);	
		
	}

}
