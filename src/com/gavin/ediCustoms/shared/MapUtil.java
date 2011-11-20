package com.gavin.ediCustoms.shared;

import java.util.Map;
import java.util.TreeMap;

public class MapUtil {
	public static String mapToString(Map<String, String> map) {
		StringBuilder stringBuilder = new StringBuilder();

		for (String key : map.keySet()) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(",");
			}
			String value = map.get(key);
			stringBuilder.append((key != null ? key : ""));
			stringBuilder.append(":");
			stringBuilder.append(value != null ? value : "");
		}
		return stringBuilder.toString();
	}

	public static Map<String, String> stringToMap(String input) {
		input=input==null?"":input;
		Map<String, String> map = new TreeMap<String, String>();
		String[] nameValuePairs = input.split(",");
		for (String nameValuePair : nameValuePairs) {
			String[] nameValue = nameValuePair.split(":");
			if (nameValue.length ==2) {
				map.put(nameValue[0], nameValue[1]);
			}			
		}
		return map;
	}
}
