package com.gavin.ediCustoms.client;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.gavin.ediCustoms.client.contract.ContractPanel;
import com.gavin.ediCustoms.client.core.ExportCustomsDeclarationPanel;
import com.gavin.ediCustoms.client.core.ImportCustomsDeclarationPanel;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MainPanel extends MyLayoutContainer {
	private HeaderPanel north;
	private ContentPanel west;
	private LayoutContainer center;
	private ListStore<BeanModel> permitedEnterpriseStore;

	private VoucherPanel voucherPanel;
	private ContractPanel contractPanel;
	private ExportCustomsDeclarationPanel exportExportCustomsDeclarationPanel;
	private ImportCustomsDeclarationPanel importCustomsDeclarationPanel;
	private DriverPaperPanel driverPaperPanel;
	private CustomsDeclarationManagerPanel customsDeclarationManagerPanel;
	private AccountPanel accountPanel;
	private SafeSettingPanel safeSettingPanel;
	
	
	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		final BorderLayout layout = new BorderLayout();
		setLayout(layout);
		setStyleAttribute("padding", "5px");		

		north = new HeaderPanel();

		west = new ContentPanel();

		VBoxLayoutData vBoxData = new VBoxLayoutData(0, 0, 10, 0);
		VBoxLayout vBoxLayout = new VBoxLayout();
		vBoxLayout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		vBoxLayout.setPadding(new Padding(10));
		
		west.setLayout(vBoxLayout);
		

		west.add(createToggleButton("凭证管理", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (voucherPanel == null) {
					voucherPanel = new VoucherPanel(permitedEnterpriseStore);
				}
				addToCenter(voucherPanel);
			}
		}), vBoxData);
		west.add(createToggleButton("合同管理", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (contractPanel == null) {
					contractPanel = new ContractPanel(permitedEnterpriseStore);
				}
				addToCenter(contractPanel);
			}
		}), vBoxData);
		
		west.add(createToggleButton("出口报关单", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (exportExportCustomsDeclarationPanel == null) {
					exportExportCustomsDeclarationPanel = new ExportCustomsDeclarationPanel(
							permitedEnterpriseStore);
				}
				addToCenter(exportExportCustomsDeclarationPanel);
			}
		}), vBoxData);
		west.add(createToggleButton("进口报关单", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (importCustomsDeclarationPanel == null) {
					importCustomsDeclarationPanel = new ImportCustomsDeclarationPanel(
							permitedEnterpriseStore);
				}
				addToCenter(importCustomsDeclarationPanel);
			}
		}), vBoxData);
		
		
		west.add(createToggleButton("报关单管理", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (customsDeclarationManagerPanel == null) {
					customsDeclarationManagerPanel = new CustomsDeclarationManagerPanel(
							permitedEnterpriseStore);
				}
				addToCenter(customsDeclarationManagerPanel);
			}
		}), vBoxData);
		
		west.add(createToggleButton("司机纸", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (driverPaperPanel == null) {
					driverPaperPanel = new DriverPaperPanel(
							permitedEnterpriseStore);
				}
				addToCenter(driverPaperPanel);
			}
		}), vBoxData);
		
		west.add(createToggleButton("账户管理", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (accountPanel == null) {
					accountPanel = new AccountPanel(
							permitedEnterpriseStore);
				}
				addToCenter(accountPanel);
			}
		}), vBoxData);
		
		west.add(createToggleButton("安全设置", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (safeSettingPanel == null) {
					safeSettingPanel = new SafeSettingPanel();
				}
				addToCenter(safeSettingPanel);
			}
		}), vBoxData);
		
		
		/*west.add(createToggleButton("数据字典", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (dictionaryPanel == null) {
					dictionaryPanel = new DictionaryPanel();
				}
				addToCenter(dictionaryPanel);
			}
		}), vBoxData);*/
		
		/*west.add(createToggleButton("测试版块", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (testPanel == null) {
					testPanel = new TestPanel(permitedEnterpriseStore);
				}
				addToCenter(testPanel);
			}
		}), vBoxData);*/
		

		center = new LayoutContainer();
		center.setLayout(new FitLayout());
		ContentPanel systemInfo = new ContentPanel();
		systemInfo.setHeading("系统信息");
		/*String information="2011-3-31<br/>加入了报关单打印功能<br/>总清单导入报关单功能<br/>出口总清单导入入口总清单功能<br/>后台添加用户单位关系模块";
		information="2011-4-6<br/> 修复一些bug，完成大部分单据的打印功能。<br/><br/>"+information;
		information="2011-4-21<br/> 加入凭证、合同的excel导入功能，加入装箱单录入、导入、打印功能。<br/>根据您提出的要求修改了打印格式、排序以及其他一些地方。<br/><br/>"+information;
		systemInfo.addText(information);*/
		
		center.add(systemInfo);

		BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH,
				44);
		northData.setCollapsible(true);
		northData.setFloatable(true);
		northData.setMargins(new Margins(0, 0, 5, 0));

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 120);
		westData.setCollapsible(true);
		westData.setMargins(new Margins(0, 5, 0, 0));

		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerData.setMargins(new Margins(0));
		
		refleshStore();

		add(north, northData);
		add(west, westData);
		add(center, centerData);
	}

	private ToggleButton createToggleButton(String name, Listener<ButtonEvent> l) {
		ToggleButton button = new ToggleButton(name);
		button.setHeight(30);
		button.setToggleGroup("vboxlayoutbuttons");
		button.addListener(Events.Toggle, l);
		button.setAllowDepress(false);
		return button;
	}

	private void addToCenter(LayoutContainer c) {
		center.removeAll();
		center.add(c);
		center.layout();
	}

	private void refleshStore() {
		permitedEnterpriseStore = new ListStore<BeanModel>();
		AsyncCallback<List<PermitedEnterprise>> getCallback = new AsyncCallback<List<PermitedEnterprise>>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("警告", "发生错误，请刷新！", null);
				
			}

			public void onSuccess(List<PermitedEnterprise> result) {

				Collections.sort(result, new Comparator<PermitedEnterprise>() {
					public int compare(PermitedEnterprise arg0,
							PermitedEnterprise arg1) {
						return arg0.getEnterpriseCode().compareTo(
								arg1.getEnterpriseCode());
					}
				});
				BeanModel beanModel;
				for (Iterator<PermitedEnterprise> iterator = result.iterator(); iterator
						.hasNext();) {
					PermitedEnterprise permitedEnterprise = (PermitedEnterprise) iterator
							.next();
					beanModel = BeanModelLookup.get()
							.getFactory(PermitedEnterprise.class)
							.createModel(permitedEnterprise);
					beanModel.set("displayName",
							beanModel.get("enterpriseCode").toString()
									+ beanModel.get("enterpriseName")
											.toString());
					permitedEnterpriseStore.add(beanModel);
				}
			}
		};
		getSystemService().listPermitedEnterprise(getCallback);
	}

}
