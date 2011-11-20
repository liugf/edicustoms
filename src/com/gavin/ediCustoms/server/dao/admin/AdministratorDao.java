package com.gavin.ediCustoms.server.dao.admin;

import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.admin.Administrator;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("administratorDao")
public class AdministratorDao extends BaseDaoHibernate<Administrator>{

}
