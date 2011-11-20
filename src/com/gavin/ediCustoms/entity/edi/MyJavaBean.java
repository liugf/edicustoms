package com.gavin.ediCustoms.entity.edi;

import java.util.HashMap;
import java.util.Map;

public class MyJavaBean {
	private Map<String, Object> map = new HashMap<String, Object>();
	public Object get(String key) {
		return map.get(key);
	}
	public void set(String key, Object value) {
		map.put(key, value);
	}
}
