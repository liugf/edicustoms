package com.gavin.ediCustoms.server.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtil {
	static public String getTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }
}
