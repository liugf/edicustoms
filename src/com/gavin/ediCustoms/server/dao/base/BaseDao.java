package com.gavin.ediCustoms.server.dao.base;

import java.util.List;

public interface BaseDao<T> {
	T get(Long id);
	Long save(T type);
	void update(T type);
	void delete(Long id);
	List<T> list();
	List<T> find(String property, final Object value);
}
