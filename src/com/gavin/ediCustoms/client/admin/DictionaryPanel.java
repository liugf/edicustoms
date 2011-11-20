package com.gavin.ediCustoms.client.admin;


import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.gavin.ediCustoms.client.admin.dictionary.AttachmentPanel;
import com.gavin.ediCustoms.client.admin.dictionary.BracketPanel;
import com.gavin.ediCustoms.client.admin.dictionary.BringInModePanel;
import com.gavin.ediCustoms.client.admin.dictionary.ContainerSizePanel;
import com.gavin.ediCustoms.client.admin.dictionary.CountryPanel;
import com.gavin.ediCustoms.client.admin.dictionary.CurrencyPanel;
import com.gavin.ediCustoms.client.admin.dictionary.CustomsPanel;
import com.gavin.ediCustoms.client.admin.dictionary.DeclarePropertyPanel;
import com.gavin.ediCustoms.client.admin.dictionary.DistrictPanel;
import com.gavin.ediCustoms.client.admin.dictionary.IronChestPanel;
import com.gavin.ediCustoms.client.admin.dictionary.PayWayPanel;
import com.gavin.ediCustoms.client.admin.dictionary.PortPanel;
import com.gavin.ediCustoms.client.admin.dictionary.TaxKindPanel;
import com.gavin.ediCustoms.client.admin.dictionary.DealModePanel;
import com.gavin.ediCustoms.client.admin.dictionary.GoodClassificationPanel;
import com.gavin.ediCustoms.client.admin.dictionary.InvestModePanel;
import com.gavin.ediCustoms.client.admin.dictionary.LoadPortPanel;
import com.gavin.ediCustoms.client.admin.dictionary.ProcessTypePanel;
import com.gavin.ediCustoms.client.admin.dictionary.TaxModePanel;
import com.gavin.ediCustoms.client.admin.dictionary.TradeModePanel;
import com.gavin.ediCustoms.client.admin.dictionary.TransportModePanel;
import com.gavin.ediCustoms.client.admin.dictionary.TruckPanel;
import com.gavin.ediCustoms.client.admin.dictionary.UnitPanel;
import com.gavin.ediCustoms.client.admin.dictionary.UseagePanel;
import com.gavin.ediCustoms.client.admin.dictionary.WrapTypePanel;
import com.google.gwt.user.client.Element;

public class DictionaryPanel extends LayoutContainer {

	private LayoutContainer center;

	private LoadPortPanel loadPortPanel;
	private WrapTypePanel wrapTypePanel;
	private CustomsPanel customsPanel;
	private CountryPanel countryPanel;
	private CurrencyPanel currencyPanel;
	private GoodClassificationPanel goodClassificationPanel;
	private UnitPanel unitPanel;
	private TradeModePanel tradeModePanel;
	private TaxKindPanel taxKindPanel;
	private ProcessTypePanel processTypePanel;
	private InvestModePanel investModePanel;
	private DealModePanel dealModePanel;
	private BringInModePanel bringInModePanel;
	private TaxModePanel taxModePanel;
	private TransportModePanel transportModePanel;
	private PortPanel portPanel;
	private DistrictPanel districtPanel;
	private PayWayPanel payWayPanel;
	private UseagePanel useagePanel;
	private BracketPanel bracketPanel;
	private IronChestPanel ironChestPanel;
	private ContainerSizePanel containerSizePanel;
	private TruckPanel truckPanel;
	private AttachmentPanel attachmentPanel;
	private DeclarePropertyPanel declarePropertyPanel;

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setLayout(new FitLayout());
		ContentPanel content = new ContentPanel();
		content.setHeading("数据字典");
		content.setLayout(new BorderLayout());

		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 150);
		westData.setMargins(new Margins(10, 5, 10, 10));

		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerData.setMargins(new Margins(10, 10, 10, 0));

		ListView<ListItem> west = new ListView<ListItem>();
		west.setStore(getDictionarys());
		west.getSelectionModel().addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<ListItem>>() {
					@Override
					public void handleEvent(SelectionChangedEvent<ListItem> be) {
						if (be.getSelectedItem()!=null) {
							addToCenter(getGridPanel(be.getSelectedItem()));
						}						
					}
				});
		west.setDisplayProperty("name");

		center = new LayoutContainer();
		center.setLayout(new FitLayout());
		center.add(new ContentPanel());
		content.add(west, westData);
		content.add(center, centerData);

		add(content);
	}

	private class ListItem extends BaseModel {
		public ListItem(int index, String name, String model) {
			set("index", index);
			set("name", name);
			set("model", model);
		}
		public String getName(){
			return (String)get("name");
		}
		public String getModel() {
			return (String)get("model");
		}
	}

	private ListStore<ListItem> getDictionarys() {
		ListStore<ListItem> store = new ListStore<ListItem>();
		store.add(new ListItem(1, "装卸港口表", "LoadPort"));
		store.add(new ListItem(2, "包装种类表", "WrapType"));
		store.add(new ListItem(3, "关区代码表", "Customs"));
		store.add(new ListItem(4, "国家地区表", "Country"));
		store.add(new ListItem(5, "币制表", "Currency"));
		store.add(new ListItem(6, "商品分类表", "GoodClassification"));
		store.add(new ListItem(7, "单位表", "Unit"));		
		store.add(new ListItem(8, "贸易方式表", "TradeMode"));
		store.add(new ListItem(9, "征免性质表", "TaxKind"));
		store.add(new ListItem(10, "征免税方式表", "TaxMode"));
		store.add(new ListItem(11, "加工种类表", "ProcessType"));
		store.add(new ListItem(12, "投资方式表", "InvestMode"));
		store.add(new ListItem(13, "成交方式表", "DealMode"));
		store.add(new ListItem(14, "引进方式表", "BringInMode"));
		store.add(new ListItem(15, "运输方式表", "TransportMode"));
		store.add(new ListItem(16, "港口代码表", "Port"));
		store.add(new ListItem(17, "国内地区表", "District"));
		store.add(new ListItem(18, "结汇方式表", "PayWay"));
		store.add(new ListItem(19, "用途代码表", "Useage"));
		store.add(new ListItem(20, "托架对照表", "Bracket"));
		store.add(new ListItem(21, "铁皮柜对照表", "IronChest"));
		store.add(new ListItem(22, "集装箱表", "Container"));
		store.add(new ListItem(23, "承运单位表", "Truck"));
		store.add(new ListItem(24, "随附单据表", "Attachment"));
		store.add(new ListItem(25, "报关形式表", "DeclareProperty"));

		return store;
	}

	private void addToCenter(LayoutContainer c) {
		center.removeAll();
		center.add(c);
		center.layout();
	}

	private LayoutContainer getGridPanel(ListItem listItem) {
		if (listItem.getModel() == "LoadPort") {
			if (loadPortPanel!=null){
				return loadPortPanel;
			}else {
				loadPortPanel=new LoadPortPanel(listItem.getName());
				return loadPortPanel;
			}
		}
		if (listItem.getModel() == "WrapType") {
			if (wrapTypePanel!=null){
				return wrapTypePanel;
			}else {
				wrapTypePanel=new WrapTypePanel(listItem.getName());
				return wrapTypePanel;
			}
		}	
		if (listItem.getModel() == "Customs") {
			if (customsPanel!=null){
				return customsPanel;
			}else {
				customsPanel=new CustomsPanel(listItem.getName());
				return customsPanel;
			}
		}
		if (listItem.getModel() == "Country") {
			if (countryPanel!=null){
				return countryPanel;
			}else {
				countryPanel=new CountryPanel(listItem.getName());
				return countryPanel;
			}
		}
		if (listItem.getModel() == "Currency") {
			if (currencyPanel!=null){
				return currencyPanel;
			}else {
				currencyPanel=new CurrencyPanel(listItem.getName());
				return currencyPanel;
			}
		}
		if (listItem.getModel() == "GoodClassification") {
			if (goodClassificationPanel!=null){
				return goodClassificationPanel;
			}else {
				goodClassificationPanel=new GoodClassificationPanel(listItem.getName());
				return goodClassificationPanel;
			}
		}
		if (listItem.getModel() == "Unit") {
			if (unitPanel!=null){
				return unitPanel;
			}else {
				unitPanel=new UnitPanel(listItem.getName());
				return unitPanel;
			}
		}
		
		if (listItem.getModel() == "TradeMode") {
			if (tradeModePanel!=null){
				return tradeModePanel;
			}else {
				tradeModePanel=new TradeModePanel(listItem.getName());
				return tradeModePanel;
			}
		}
		if (listItem.getModel() == "TaxKind") {
			if (taxKindPanel!=null){
				return taxKindPanel;
			}else {
				taxKindPanel=new TaxKindPanel(listItem.getName());
				return taxKindPanel;
			}
		}
		if (listItem.getModel() == "TaxMode") {
			if (taxModePanel!=null){
				return taxModePanel;
			}else {
				taxModePanel=new TaxModePanel(listItem.getName());
				return taxModePanel;
			}
		}
		if (listItem.getModel() == "ProcessType") {
			if (processTypePanel!=null){
				return processTypePanel;
			}else {
				processTypePanel=new ProcessTypePanel(listItem.getName());
				return processTypePanel;
			}
		}
		if (listItem.getModel() == "InvestMode") {
			if (investModePanel!=null){
				return investModePanel;
			}else {
				investModePanel=new InvestModePanel(listItem.getName());
				return investModePanel;
			}
		}
		if (listItem.getModel() == "DealMode") {
			if (dealModePanel!=null){
				return dealModePanel;
			}else {
				dealModePanel=new DealModePanel(listItem.getName());
				return dealModePanel;
			}
		}
		if (listItem.getModel() == "BringInMode") {
			if (bringInModePanel!=null){
				return bringInModePanel;
			}else {
				bringInModePanel=new BringInModePanel(listItem.getName());
				return bringInModePanel;
			}
		}
		if (listItem.getModel() == "TransportMode") {
			if (transportModePanel!=null){
				return transportModePanel;
			}else {
				transportModePanel=new TransportModePanel(listItem.getName());
				return transportModePanel;
			}
		}
		if (listItem.getModel() == "Port") {
			if (portPanel!=null){
				return portPanel;
			}else {
				portPanel=new PortPanel(listItem.getName());
				return portPanel;
			}
		}
		if (listItem.getModel() == "District") {
			if (districtPanel!=null){
				return districtPanel;
			}else {
				districtPanel=new DistrictPanel(listItem.getName());
				return districtPanel;
			}
		}
		if (listItem.getModel() == "PayWay") {
			if (payWayPanel!=null){
				return payWayPanel;
			}else {
				payWayPanel=new PayWayPanel(listItem.getName());
				return payWayPanel;
			}
		}
		if (listItem.getModel() == "Useage") {
			if (useagePanel!=null){
				return useagePanel;
			}else {
				useagePanel=new UseagePanel(listItem.getName());
				return useagePanel;
			}
		}
		if (listItem.getModel() == "Bracket") {
			if (bracketPanel!=null){
				return bracketPanel;
			}else {
				bracketPanel=new BracketPanel(listItem.getName());
				return bracketPanel;
			}
		}
		if (listItem.getModel() == "IronChest") {
			if (ironChestPanel!=null){
				return ironChestPanel;
			}else {
				ironChestPanel=new IronChestPanel(listItem.getName());
				return ironChestPanel;
			}
		}
		if (listItem.getModel() == "Container") {
			if (containerSizePanel!=null){
				return containerSizePanel;
			}else {
				containerSizePanel=new ContainerSizePanel(listItem.getName());
				return containerSizePanel;
			}
		}
		if (listItem.getModel() == "Truck") {
			if (truckPanel!=null){
				return truckPanel;
			}else {
				truckPanel=new TruckPanel(listItem.getName());
				return truckPanel;
			}
		}
		if (listItem.getModel() == "Attachment") {
			if (attachmentPanel!=null){
				return attachmentPanel;
			}else {
				attachmentPanel=new AttachmentPanel(listItem.getName());
				return attachmentPanel;
			}
		}
		if (listItem.getModel() == "DeclareProperty") {
			if (declarePropertyPanel!=null){
				return declarePropertyPanel;
			}else {
				declarePropertyPanel=new DeclarePropertyPanel(listItem.getName());
				return declarePropertyPanel;
			}
		}
		
		return new ContentPanel();

	}
}
