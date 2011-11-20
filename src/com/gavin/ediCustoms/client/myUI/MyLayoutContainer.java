package com.gavin.ediCustoms.client.myUI;

import java.util.Map;

import com.extjs.gxt.ui.client.js.JsUtil;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.gavin.ediCustoms.client.service.BusinessService;
import com.gavin.ediCustoms.client.service.BusinessServiceAsync;
import com.gavin.ediCustoms.client.service.SystemService;
import com.gavin.ediCustoms.client.service.SystemServiceAsync;
import com.gavin.ediCustoms.client.service.DictionaryService;
import com.gavin.ediCustoms.client.service.DictionaryServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class MyLayoutContainer extends LayoutContainer {
	private DictionaryServiceAsync dictionaryService = GWT.create(DictionaryService.class);
	private SystemServiceAsync systemService=GWT.create(SystemService.class);
	private BusinessServiceAsync businessService=GWT.create(BusinessService.class);
	
	public DictionaryServiceAsync getDictionaryService() {
		return dictionaryService;
	}

	public SystemServiceAsync getSystemService() {
		return systemService;
	}

	public BusinessServiceAsync getBusinessService() {
		return businessService;
	}	
	
	public void openNewWindow(String url, Map<String, Object> paramsMap) {
		openNewWindow(url,"new_blank", JsUtil.toJavaScriptObject(paramsMap));
	}
	
	public void openNewWindow(String url,String target, Map<String, Object> paramsMap) {
		if (target==null) {
			target="new_blank";
		}
		openNewWindow(url,target, JsUtil.toJavaScriptObject(paramsMap));
	}

	public static native void openNewWindow(String url,String target, JavaScriptObject params) /*-{
		var postForm = document.createElement("form");			
		document.body.appendChild(postForm);
		postForm.style.display="none";	
		postForm.id="postForm";
		postForm.method="post";
		postForm.target=target;
		postForm.action=url;		

		for(var p in params){  
			if(typeof(params[p])!="function"){     
			    var field = document.createElement("input");
				field.type="text";
				field.name=p;
				field.value=params[p];
				postForm.appendChild(field);
			}   
		}
		postForm.submit();
		document.body.removeChild(postForm);     
		
	}-*/;	
	
	private LayoutContainer mask;
	public void setIsWaiting(boolean isWaiting){
		if (mask==null) {
			mask=new LayoutContainer();
			mask.setStyleName("mask");
			RootPanel.get().add(mask);
		}		
		if (isWaiting) {
			mask.setVisible(true);
		}else {
			mask.setVisible(false);
		}
	}
}
