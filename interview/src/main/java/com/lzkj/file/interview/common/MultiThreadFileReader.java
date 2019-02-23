package com.lzkj.file.interview.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import com.lzkj.file.interview.service.IHandle;
import com.lzkj.file.interview.service.ReadFileHandleTask;
import com.lzkj.file.interview.bean.SignStartEndBean;
import com.lzkj.file.interview.exception.ParseFileAndHandleException;

/*
 * 多线程文件读取类
 * suizf 2019022
 */
public class MultiThreadFileReader {
	private int threadSize;
	private String charset;
	private int bufferSize;
	private IHandle handle;//文件处理类
	private ExecutorService  executorService;
	private long fileLength;
	private String filePath;
	private RandomAccessFile rAccessFile;//文件多点处理
	private Set<SignStartEndBean> signStartEndBeans;//存储文件所有的开始结束位置
	public static AtomicLong counter = new AtomicLong(0);//记录处理文件总行数
	
	MultiThreadFileReader(String filePath,IHandle handle,String charset,int bufferSize,int threadSize){
		this.handle = handle;
		this.charset = charset;
		this.bufferSize = bufferSize;
		this.threadSize = threadSize;
		this.filePath=filePath;
		try {
			File file = new File(filePath);
			if(!file.exists()){
				throw new IllegalArgumentException("文件不存在！");
			}
				
			this.fileLength = file.length();
			this.rAccessFile = new RandomAccessFile(file,"r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.executorService = Executors.newFixedThreadPool(threadSize);
		signStartEndBeans = new HashSet<SignStartEndBean>();
	}
	
	public void excute(){
		try{
			start();//开始执行多行程解析			
		}catch(Exception e){
			e.printStackTrace();
			ParseFileAndHandleException.parseFileAndHandleExceptionHandle(ConstantUtil.PARSE_HANDEL_EXCEPTION_CODE);
		}finally{
			shutdown();
		}
	}
	/**
	 * 处理解析
	 */
	public void start(){
		long everySize = this.fileLength/this.threadSize;//每个线程需要处理的时间长度
		try {
			calculateStartEnd(0, everySize);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
					
		for(SignStartEndBean pair:signStartEndBeans){
			this.executorService.execute(new ReadFileHandleTask(pair.getStart(),pair.getEnd(),bufferSize,filePath,handle));
		}
	}
	/**
	 * 根据设置的分片计算文件开始结束位置，标记处位置以便多线程处理,采用递归的方式获取
	 * 文件每行字符数不同，以换行符结束，这个文件还挺坑的
	 * @param start
	 * @param size
	 * @throws IOException
	 * suizf 20190222
	 */
	private void calculateStartEnd(long start,long size) throws IOException{
		if(start>fileLength-1){
			return;
		}
		SignStartEndBean pair = new SignStartEndBean();//设置一个开始结束标记对
		pair.setStart(start);
		long endPosition = start+size-1;//结束位置是开始位置+分片字符数-1
		if(endPosition>=fileLength-1){
			long end=fileLength-1;
			pair.setEnd(end);
			signStartEndBeans.add(pair);
			return;
		}
		
		rAccessFile.seek(endPosition);
		byte tmp =(byte) rAccessFile.read();
		while(tmp!='\n' && tmp!='\r'){
			endPosition++;
			if(endPosition>=fileLength-1){
				endPosition=fileLength-1;
				break;
			}
			rAccessFile.seek(endPosition);
			tmp =(byte) rAccessFile.read();
		}
		pair.setEnd(endPosition);
		signStartEndBeans.add(pair);
		
		calculateStartEnd(endPosition+1, size);//递归调用	
	}
	public void shutdown(){
		try {
			this.rAccessFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.executorService.shutdown();
	}
	

}
