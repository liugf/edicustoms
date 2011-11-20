package com.gavin.ediCustoms.client;


import com.extjs.gxt.ui.client.widget.Text;
import com.gavin.ediCustoms.client.myUI.MyLayoutContainer;
import com.gavin.ediCustoms.shared.UserState;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class HeaderPanel extends MyLayoutContainer {
	
	private Text header;
	private Timer timer;
	
	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);		
		
		header = new Text();
		header.setText(getContent(""));		
		add(header);
		
		connect();
		
		timer=new Timer() {			
			@Override
			public void run() {
				connect();
			}
		};
		timer.scheduleRepeating(300000);
	}
	
	private void connect(){
		AsyncCallback<UserState> callback = new AsyncCallback<UserState>() {
			public void onFailure(Throwable caught) {
				//MessageBox.alert("警告", "发生错误，请刷新！", null);
				refresh();
			}
			public void onSuccess(UserState result) {
				if (result.getUserType()==UserState.UserType.USER) {
					header.setText(getContent(result.getName()));
				}else {
					header.setText(getContent("管理员"));
				}
				
			}
		};
		getSystemService().getState(callback);
	}
	
	private String getContent(String name){		
		return "<div style='float:left;'><img src='logo.gif' /></div><div style='float:right;margin:16px 16px 0 0;color:#5d6d88;font-size:12px;'>你好&nbsp,&nbsp"
		+(name!=null?name:"企业用户")
		+"<span style='color:#97999c'>&nbsp|&nbsp</span> <a href='/logout'>退出</a></div>";
	}
	
	private static native void refresh() /*-{
	  	window.location.reload();
	}-*/;
	
}
