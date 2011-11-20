package com.gavin.ediCustoms.shared;

import com.google.gwt.i18n.client.CurrencyData;

public class MyCurrencyData implements CurrencyData {

	public MyCurrencyData(){
		
	}
	
	@Override
	public String getCurrencyCode() {
		return "";
	}

	@Override
	public String getCurrencySymbol() {
		return "";
	}

	@Override
	public int getDefaultFractionDigits() {
		return 2;
	}

	@Override
	public String getPortableCurrencySymbol() {
		return "";
	}

	@Override
	public boolean isDeprecated() {
		return false;
	}

	@Override
	public boolean isSpaceForced() {
		return false;
	}

	@Override
	public boolean isSpacingFixed() {
		return false;
	}

	@Override
	public boolean isSymbolPositionFixed() {
		return false;
	}

	@Override
	public boolean isSymbolPrefix() {
		return false;
	}

}
