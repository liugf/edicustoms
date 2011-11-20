package com.gavin.ediCustoms.server.dao.base;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public abstract class BaseDaoHibernate<T> extends HibernateDaoSupport implements
		BaseDao<T> {

	private Class<T> persistentClass;
	
	private String sqlOperator="><=";

	@SuppressWarnings("unchecked")
	public BaseDaoHibernate() {
		super();
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];		
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Long id) {
		return (T) getHibernateTemplate().get(getPersistentClass(), id);
	}

	@Override
	public Long save(T type) {
		return (Long) getHibernateTemplate().save(type);
	}

	@Override
	public void update(T type) {
		getHibernateTemplate().update(type);
	}

	@Override
	public void delete(Long id) {
		getHibernateTemplate().delete(get(id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list() {
		return (List<T>) getHibernateTemplate().loadAll(getPersistentClass());
	}

	@SuppressWarnings("unchecked")
	public List<Long> findIds(String property, final Object value) {
		final String hql = "select entity.id from "
				+ getPersistentClass().getSimpleName()
				+ " entity where entity." + property + " =?";
		List<Long> list = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						return session.createQuery(hql).setParameter(0, value)
								.list();
					}
				});
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Long> findIdsAnd(Map<String, Object> map) {

		String string = "select entity.id from " + getPersistentClass().getSimpleName()
				+ " entity where ";
		final Set entries = map.entrySet();
		if (entries != null) {
			for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
				Entry entry = (Entry) iterator.next();
				string += "entity." + entry.getKey() + " =? ";
				if (iterator.hasNext()) {
					string += "and ";
				}
			}
			final String hql = string;
			List<Long> list = getHibernateTemplate().executeFind(
					new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session.createQuery(hql);
							int i = 0;
							for (Iterator iterator = entries.iterator(); iterator
									.hasNext();) {
								Entry entry = (Entry) iterator.next();
								query.setParameter(i, entry.getValue());
								i++;
							}
							return query.list();
						}
					});
			return list;
		} else {
			return null;
		}

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Long> findIdsOr(Map<String, Object> map) {

		String string = "select entity.id from " + getPersistentClass().getSimpleName()
				+ " entity where ";
		final Set entries = map.entrySet();
		if (entries != null) {
			for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
				Entry entry = (Entry) iterator.next();
				string += "entity." + entry.getKey() + " =? ";
				if (iterator.hasNext()) {
					string += "or ";
				}
			}
			final String hql = string;
			List<Long> list = getHibernateTemplate().executeFind(
					new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session.createQuery(hql);
							int i = 0;
							for (Iterator iterator = entries.iterator(); iterator
									.hasNext();) {
								Entry entry = (Entry) iterator.next();
								query.setParameter(i, entry.getValue());
								i++;
							}
							return query.list();
						}
					});
			return list;
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<T> find(String property, final Object value) {
		final String hql = "from " + getPersistentClass().getSimpleName()
				+ " entity where entity." + property + " =?";
		List<T> list = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						return session.createQuery(hql).setParameter(0, value)
								.list();
					}
				});
		return list;
	}

	public List<T> findAnd(Map<String, Object> map) {
		return find(map, true, null,0);
	}

	public List<T> findOr(Map<String, Object> map) {
		return find(map, false, null,0);
	}

	public void delete(String property, final Object value) {
		final String hql = "delete from "
				+ getPersistentClass().getSimpleName()
				+ " entity where entity." + property + " =?";
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.createQuery(hql).setParameter(0, value)
						.executeUpdate();
			}
		});
	}

	@SuppressWarnings("rawtypes")
	public void deleteAnd(Map<String, Object> map) {
		String string = "delete from " + getPersistentClass().getSimpleName()
				+ " entity where ";
		final Set entries = map.entrySet();
		if (entries != null) {
			for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
				Entry entry = (Entry) iterator.next();
				string += "entity." + entry.getKey() + " =? ";
				if (iterator.hasNext()) {
					string += "and ";
				}
			}
			final String hql = string;
			getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query = session.createQuery(hql);
					int i = 0;
					for (Iterator iterator = entries.iterator(); iterator
							.hasNext();) {
						Entry entry = (Entry) iterator.next();
						query.setParameter(i, entry.getValue());
						i++;
					}
					query.executeUpdate();
					return null;
				}
			});
		}
	}

	@SuppressWarnings("rawtypes")
	public void deleteOr(Map<String, Object> map) {
		String string = "delete from " + getPersistentClass().getSimpleName()
				+ " entity where ";
		final Set entries = map.entrySet();
		if (entries != null) {
			for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
				Entry entry = (Entry) iterator.next();
				string += "entity." + entry.getKey() + " =? ";
				if (iterator.hasNext()) {
					string += "or ";
				}
			}
			final String hql = string;
			getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query = session.createQuery(hql);
					int i = 0;
					for (Iterator iterator = entries.iterator(); iterator
							.hasNext();) {
						Entry entry = (Entry) iterator.next();
						query.setParameter(i, entry.getValue());
						i++;
					}
					query.executeUpdate();
					return null;
				}
			});
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> find(Map<String, Object> conditions, boolean isAnd, String orderBy,final int limit) {
		String connector = isAnd ? " and " : " or ";
		String str = "from " + getPersistentClass().getSimpleName()
		+ " entity where ";
		final Set entries = conditions.entrySet();
		if (entries == null) {
			return null;
		}
		for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			if (entry.getKey() == null) {
				continue;
			}
			String key = entry.getKey().toString().trim();
			String lastOfKey = key.substring(key.length()-1, key.length());
			String operator ="";
			boolean isContainOperator = sqlOperator.contains(lastOfKey) || key.endsWith("like");
			operator = isContainOperator?" ":" = ";
						
			str += "entity." + entry.getKey() + operator + " ? ";
			if (iterator.hasNext()) {
				str += connector;
			}
		}
		if (orderBy!=null) {
			str += " order by "+ orderBy;
		}
		final String hql = str;
		List<T> list = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						int i = 0;
						for (Iterator iterator = entries.iterator(); iterator
								.hasNext();) {
							Entry entry = (Entry) iterator.next();
							if (entry.getValue() instanceof Date) {
								Date date = (Date) entry.getValue();
								query.setTimestamp(i, date);
							} else {
								query.setParameter(i, entry.getValue());
							}
							i++;
						}
						if (limit>0) {
							query.setMaxResults(limit);
						}
						return query.list();
					}
				});
		return list;
	}

}
