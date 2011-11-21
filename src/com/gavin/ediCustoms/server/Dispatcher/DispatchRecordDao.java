package com.gavin.ediCustoms.server.Dispatcher;


import org.springframework.stereotype.Repository;

import com.gavin.ediCustoms.entity.edi.DispatchRecord;
import com.gavin.ediCustoms.server.dao.base.BaseDaoHibernate;

@Repository("dispatchRecordDao")
public class DispatchRecordDao extends BaseDaoHibernate<DispatchRecord>{

}
