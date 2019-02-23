package com.lzkj.file.interview.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import com.lzkj.file.interview.bean.SignStartEndBean;
import com.lzkj.file.interview.common.MultiThreadFileReader;
import com.lzkj.file.interview.entity.StartDateInfo;

/*
 * 对文件进行解析处理，切片任务执行
 * suizf 20190222
 */
public class ReadFileHandleTask implements Runnable{

	private long start;
	private long sliceSize;//单片文件长度
	private byte[] readBuff;//缓冲读取区
	private RandomAccessFile rAccessFile;//文件多点处理
	private int bufferSize;
	private String charset;
	private IHandle handle;
	
	public ReadFileHandleTask(long start,long end,int bufferSize,String filePath,IHandle handle) {
		this.start = start;
		this.sliceSize = end-start+1;
		this.bufferSize=bufferSize;
		this.readBuff = new byte[bufferSize];
		this.handle=handle;
		try {
			File file = new File(filePath);
			if(!file.exists()){
				throw new IllegalArgumentException("文件不存在！");
			}
			this.rAccessFile = new RandomAccessFile(file,"r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		MappedByteBuffer mapBuffer=null;
		ByteArrayOutputStream bos=null;			
		try {
			mapBuffer = rAccessFile.getChannel().map(MapMode.READ_ONLY,start, this.sliceSize);
			bos = new ByteArrayOutputStream();
			List<StartDateInfo> startDateInfos=new ArrayList<StartDateInfo>();
			SpecialParseFileService specialParseFileService=new SpecialParseFileService();
			for(int offset=0;offset<sliceSize;offset+=bufferSize){
				int readLength;
				if(offset+bufferSize<=sliceSize){
					readLength = bufferSize;
				}else{
					readLength = (int) (sliceSize-offset);
				}
				mapBuffer.get(readBuff, 0, readLength);
				for(int i=0;i<readLength;i++){
					byte tmp = readBuff[i];
					if(tmp=='\n' || tmp=='\r'){		
						StartDateInfo startDateInfo=handle.handleByLine(bos.toByteArray(),charset);//处理可扩展，不知道这样算不算
						startDateInfos.add(startDateInfo);
						bos.reset();
					}else{
						bos.write(tmp);
					}
					//每一千行提交一次
					if(startDateInfos.size()>1000){
						specialParseFileService.insertStartDateInfoByBatch(startDateInfos);
						startDateInfos.clear();
					}
				}
			}
			if(bos.size()>0){
				StartDateInfo startDateInfo=handle.handleByLine(bos.toByteArray(),charset);//处理可扩展，不知道这样算不算
				startDateInfos.add(startDateInfo);
			}
			specialParseFileService.insertStartDateInfoByBatch(startDateInfos);
			startDateInfos.clear();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null!= bos){
				try{
					bos.close();
				}catch(Exception e){
					e.printStackTrace();
				}				
			}
			if(null!=mapBuffer){
				mapBuffer.clear();
			}
		}
	}


}
