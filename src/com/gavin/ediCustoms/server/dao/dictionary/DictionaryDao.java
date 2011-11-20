package com.gavin.ediCustoms.server.dao.dictionary;


import java.util.List;

import com.gavin.ediCustoms.entity.edi.dictionary.BaseItem;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

public abstract class DictionaryDao<T extends BaseItem> extends BaseDaoHibernate<T> {
	@Override
	public Long save(T type) {
		if (find("code", type.getCode()).size() != 0) {
			return new Long(0);
		}
		return super.save(type);
	};
	
	public T get(String code) {
		List<T> list=find("code", code);
		if (list.size()!=0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	public String getCodeByName(String name){
		List<T> list=find("name", name);
		if (list.size()==0) {
			return null;
		}
		T t=list.get(0);
		if (t==null) {
			return null;
		}
		return t.getCode();	
	}
}
