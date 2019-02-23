package com.lzkj.file.interview.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.lzkj.file.interview.exception.LoadConfAndInitException;
import com.lzkj.file.interview.service.IHandle;


public class ReadFileBuilder {
	private static int threadSize=1;
	private static String charset=null;
	private static int bufferSize=1024*1024;
	private static String filePath;
	private IHandle handle;
		
	static{
		InputStream inputStream=null;
		try{
			inputStream = ClassLoader.getSystemResourceAsStream("conf.properties");  
			Properties prop=new Properties();
			prop.load(inputStream);
			threadSize=Integer.parseInt(prop.getProperty("thread_size"));
			charset=prop.getProperty("charset");
			bufferSize=Integer.parseInt(prop.getProperty("buffer_size"));
			filePath=prop.getProperty("file_path");
				
		}catch(Exception e){
			e.printStackTrace();
			LoadConfAndInitException.loadConfAndInitExceptionHandle(ConstantUtil.LOAD_CONF_EXCEPTION_CODE);
		}finally{
			if(inputStream!=null){
				try{
					inputStream.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public ReadFileBuilder(IHandle handle){
		this.handle = handle;
	}
	
	public MultiThreadFileReader buildMultiThreadFileReader(){
		return new MultiThreadFileReader(this.filePath,this.handle,this.charset,this.bufferSize,this.threadSize);
	}
}
