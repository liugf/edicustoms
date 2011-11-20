package com.gavin.ediCustoms.client.myUI;

import com.extjs.gxt.ui.client.widget.Viewport;
import com.google.gwt.user.client.Window;

public class MyViewport extends Viewport {
	private int minWidth=1003;
	private int minHeight=680;
	
	@Override
	protected void onLoad() {
		int width=getWidth();
		int height=getHeight();
		width = width > minWidth ? width : minWidth;
		height = height > minHeight ? height : minHeight;
		setWidth(width);
		setHeight(height);
		Window.enableScrolling(true);
		super.onLoad();
	}
	
	@Override
	protected void onWindowResize(int width, int height) {
		width = width > minWidth ? width : minWidth;
		height = height > minHeight ? height : minHeight;		
		super.onWindowResize(width, height);
	}
}
