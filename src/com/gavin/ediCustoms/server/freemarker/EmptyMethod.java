package com.gavin.ediCustoms.server.freemarker;

import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class EmptyMethod  implements TemplateMethodModel{

	@SuppressWarnings("rawtypes")
	@Override
	
	public Object exec(List arg0) throws TemplateModelException {
		if (arg0.size()==0) {
			return true;
		}
		Object object = arg0.get(0);
		if (object == null) {
			return true;
		}else if ((object instanceof String) && object.toString().trim().length()==0) {
			return true;
		}else {
			return false;
		}
	}
	
}
