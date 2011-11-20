package com.gavin.ediCustoms.server.dao;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.AccountRecord;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("accountRecordDao")
public class AccountRecordDao extends BaseDaoHibernate<AccountRecord>{
	@SuppressWarnings("unchecked")
	public List<AccountRecord> findByPeriod(final Long enterpriseId,
			final Date start, final Date end) {
		final String hql = "from AccountRecord entity where entity.enterpriseId = ? and " +
				"entity.created >= ? and entity.created <= ?";
		List<AccountRecord> list = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						return session.createQuery(hql).setParameter(0, enterpriseId)
						.setTimestamp(1, start).setTimestamp(2, end)
								.list();
					}
				});
		return list;
	}
}
