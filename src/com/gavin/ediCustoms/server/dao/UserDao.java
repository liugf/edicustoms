package com.gavin.ediCustoms.server.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.share.User;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("userDao")
public class UserDao extends BaseDaoHibernate<User>{	
	@Autowired
	public void setHibernateTemplate(@Qualifier("shareHibernateTemplate") HibernateTemplate hibernateTemplate){
		this.hibernateTemplate = hibernateTemplate;
	}
	
	@Override
	public Long save(User user) {
		if (find("userName", user.getUserName()).size() != 0) {
			return new Long(0);
		}
		return super.save(user);
	}
}
