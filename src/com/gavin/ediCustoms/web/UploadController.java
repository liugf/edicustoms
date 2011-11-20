package com.gavin.ediCustoms.web;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gavin.ediCustoms.server.utils.ExcelResolver;

@Controller
public class UploadController {

	@SuppressWarnings("rawtypes")
	@RequestMapping("/uploadVoucher.do")
	public void uploadVoucher(HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
			return;
		}
		Iterator iter = items.iterator();
		
		FileItem item = (FileItem) iter.next();
		Long enterpriseId=new Long(item.getString());
		
		item = (FileItem) iter.next();
		String fileName = getFileName();
		File uploadedFile = new File(fileName);
		try {
			item.write(uploadedFile);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		String result=excelResolver.convert2Voucher(enterpriseId,fileName);
		out.println(result);
		uploadedFile.delete();		
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/uploadContract.do")
	public void uploadContract(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
			return;
		}
		Iterator iter = items.iterator();
		
		FileItem item = (FileItem) iter.next();
		Long enterpriseId=new Long(item.getString());
		
		item = (FileItem) iter.next();
		String fileName = getFileName();
		File uploadedFile = new File(fileName);
		try {
			item.write(uploadedFile);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		String result=excelResolver.convert2Contract(enterpriseId,fileName);
		out.println(result);
		uploadedFile.delete();
	}
	
	private String getFileName(){
		String basePath="temp-";
		Date date=new Date();
		int rand=(int) (Math.random()*10000);
		return basePath+date.getTime()+rand+".xls";
		
	}
	
	@Autowired
	private ExcelResolver excelResolver;
}
