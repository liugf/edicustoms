package com.gavin.ediCustoms.server.utils;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Logout extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getSession().setAttribute("userState",null);	
		PrintStream out=new PrintStream(response.getOutputStream());
		out.println("<script language='javascript' type='text/javascript'>");
		out.println("window.location.href='/'");
		out.println("</script>");
	}
}
