package com.gavin.ediCustoms.client.admin;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.gavin.ediCustoms.client.HeaderPanel;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.google.gwt.user.client.Element;

public class AdminPanel extends MyLayoutContainer {
	private HeaderPanel north;
	private ContentPanel west;
	private LayoutContainer center;

	private DictionaryPanel dictionaryPanel;
	private CompanyMessagePanel companyMessagePanel;
	private EnterprisePanel enterprisePanel;
	private ForeignEnterprisePanel foreignEnterprisePanel;
	private SafeSettingPanel safeSettingPanel;
	private UserPanel userPanel;
	//private DeclarantPanel declarantPanel;
	private RelationPanel relationPanel;
	private AccountPanel accountPanel;

	public AdminPanel() {
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		final BorderLayout layout = new BorderLayout();
		setLayout(layout);
		setStyleAttribute("padding", "5px");

		north = new HeaderPanel();
		west = new ContentPanel();

		VBoxLayout westLayout = new VBoxLayout();
		westLayout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		westLayout.setPadding(new Padding(10));
		west.setLayout(westLayout);

		VBoxLayoutData vBoxData = new VBoxLayoutData(0, 0, 10, 0);
		west.add(createToggleButton("企业信息", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (companyMessagePanel == null) {
					companyMessagePanel = new CompanyMessagePanel();
				}
				addToCenter(companyMessagePanel);
			}
		}), vBoxData);
		west.add(createToggleButton("用户单位", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (enterprisePanel == null) {
					enterprisePanel = new EnterprisePanel();
				}
				addToCenter(enterprisePanel);
			}
		}), vBoxData);
		west.add(createToggleButton("单位关系", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (relationPanel == null) {
					relationPanel = new RelationPanel();
				}
				addToCenter(relationPanel);
			}
		}), vBoxData);
		west.add(createToggleButton("外商公司", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (foreignEnterprisePanel == null) {
					foreignEnterprisePanel = new ForeignEnterprisePanel();
				}
				addToCenter(foreignEnterprisePanel);
			}
		}), vBoxData);
		/*west.add(createToggleButton("报关员", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (declarantPanel == null) {
					declarantPanel = new DeclarantPanel();
				}
				addToCenter(declarantPanel);
			}
		}), vBoxData);*/
		west.add(createToggleButton("录入人员", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (userPanel == null) {
					userPanel = new UserPanel();
				}
				addToCenter(userPanel);
			}
		}), vBoxData);
		west.add(createToggleButton("账户管理", new Listener<ButtonEvent>() {
			@Override
			public void handleEvent(ButtonEvent be) {
				if (!be.<ToggleButton> getComponent().isPressed()) {
					return;
				}
				if (accountPanel == null) {
					accountPanel = new AccountPanel();
				}
				addToCenter(accountPanel);
			}
		}), vBoxData);
		west.add(createToggleButton("数据字典", new Listener<ButtonEvent>() {
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

		center = new LayoutContainer();
		center.setLayout(new FitLayout());
		ContentPanel systemInfo = new ContentPanel();
		systemInfo.setHeading("系统信息");
		center.add(systemInfo);

		BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH,
				50);
		northData.setCollapsible(true);
		northData.setFloatable(true);
		northData.setMargins(new Margins(0, 0, 5, 0));

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 150);
		westData.setCollapsible(true);
		westData.setMargins(new Margins(0, 5, 0, 0));

		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerData.setMargins(new Margins(0));

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
}
