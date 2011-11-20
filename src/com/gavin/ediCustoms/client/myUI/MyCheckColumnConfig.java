package com.gavin.ediCustoms.client.myUI;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;

public class MyCheckColumnConfig extends CheckColumnConfig {
	private boolean isAble=true;
	
	public MyCheckColumnConfig(String string, String string2, int i) {
		super(string, string2, i);
	}
	
	public void enable(){
		isAble=true;		
	}
	
	public void disable(){
		isAble=false;
	}
	
	@Override
	protected void onMouseDown(GridEvent<ModelData> ge) {
		if (isAble) {
			super.onMouseDown(ge);
		}else {
			
		}
	}

}
