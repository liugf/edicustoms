package com.gavin.ediCustoms.server.freemarker;

import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class Number2StringMethod  implements TemplateMethodModel{

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arg0) throws TemplateModelException {
		if (arg0.size()==0) {
			return null;
		}
		Object object = arg0.get(0);
		return object.toString();
	}
	
}
