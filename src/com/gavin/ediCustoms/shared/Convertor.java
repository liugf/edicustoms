package com.gavin.ediCustoms.shared;

public class Convertor {
	private static final String HAX_STRING="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static int string2int(String str){
		str=str.toUpperCase();
		int result=0;
		for (int i = 0; i < str.length() ; i++) {
			result+=Math.pow(36, str.length()-1-i)*HAX_STRING.indexOf(str.charAt(i));
		}
		return result;
	}
	public static String int2String(int i){
		String result="";
		int quotient;
		int remainder;
		while(i!=0){
			quotient=i/36;
			remainder=i-36*quotient;
			result=HAX_STRING.substring(remainder, remainder+1)+result;
			i=quotient;
		}
		return result;
	}
	
}
