package com.gavin.ediCustoms.server.Dispatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class MessageReceiver implements ServletContextListener {

	private Timer timer;
	
	private MessageProcesser messageProcesser;
	
	private String downloadTempDir;
	private String downloadDir;
	

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		WebApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(arg0.getServletContext());
		if (ctx == null) {
			throw new RuntimeException(
					"Check Your Web.Xml Setting, No Spring Context Configured");
		}
		messageProcesser = (MessageProcesser)ctx.getBean("messageProcesser");
		
		ResourceBundle bundle = ResourceBundle.getBundle("messageDispatcher");
		downloadTempDir = bundle.getString("message.downloadTemp");
		downloadDir = bundle.getString("message.download");

		timer=new Timer();
		timer.schedule(new GetMessageTask(), 0, 10000);

	}
	
	
	private class GetMessageTask extends TimerTask{
        @Override
        public void run() {
        	File dir = new File(downloadTempDir);
        	if (!dir.exists()) {
				dir.mkdir();
			}
    		File[] files = dir.listFiles();
    		for (int i = 0; i < files.length; i++) {
    			processMessage(files[i].getAbsolutePath());
    			File newFile=new File(downloadDir+"\\"+files[i].getName());
				if (rename(files[i], newFile)) {
					System.out.println("移动"+files[i].getAbsolutePath()+"到"+newFile.getAbsolutePath());
				}else {
					System.out.println("移动文件"+files[i].getAbsolutePath()+"失败");
				}
    		}
        }
	}
	
	private void processMessage(String address){
		System.out.println("处理报文"+address);
		messageProcesser.processReturnMessage(address);
	}
	
	private boolean rename(File file1,File file2){
		if (!file1.exists()) {
			return false;
		}
		try {
			if (file2.exists()) {
				file2.delete();
			}
			return file1.renameTo(file2);
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
			System.out.println("复制"+oldPath+"到"+newPath);
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			if (myDelFile.delete()) {
				System.out.println("删除文件"+filePathAndName);
			}else {
				System.out.println("删除文件操作出错");
			}
			
		} catch (Exception e) {
			System.out.println("删除文件过程出错");
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
