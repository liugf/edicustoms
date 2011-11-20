package com.gavin.ediCustoms.server.Dispatcher;

import java.io.File;

public interface MessageGenerator {
	/**
	 * @param id
	 * 	报关单的id
	 * @return
	 *  返回生成报文的物理地址
	 */
	File generateMessage(Long id);
}
