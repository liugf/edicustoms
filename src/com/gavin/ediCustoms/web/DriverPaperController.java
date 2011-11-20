package com.gavin.ediCustoms.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DriverPaperController {
	@SuppressWarnings("unchecked")
	@RequestMapping("/driverPaper1.do")
	public String driverPaper1(HttpServletRequest request,ModelMap map){
		Map<String, String[]> params = request.getParameterMap();
		map.putAll(params);
		return "driverPaper1";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/driverPaper2.do")
	public String driverPaper2(HttpServletRequest request,ModelMap map){
		Map<String, String[]> params = request.getParameterMap();
		map.putAll(params);
		return "driverPaper2";
	}
}
