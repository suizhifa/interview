package com.lzkj.file.interview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.lzkj.file.interview.common.MultiThreadFileReader;
import com.lzkj.file.interview.common.ReadFileBuilder;
import com.lzkj.file.interview.dao.StartDateInfoDAO;
import com.lzkj.file.interview.entity.StartDateInfo;
import com.lzkj.file.interview.service.IHandle;
import com.lzkj.file.interview.service.SpecialFileHander;
import com.lzkj.file.interview.service.SpecialParseFileService;

public class MainTest {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		SpecialFileHander handle=new SpecialFileHander();
		ReadFileBuilder builder = new ReadFileBuilder(handle);
		final long startTime = System.currentTimeMillis();
		MultiThreadFileReader bigFileReader = builder.buildMultiThreadFileReader();
		bigFileReader.excute();
		final long useTime = System.currentTimeMillis()-startTime;
		System.out.println("整个文件解析入库耗时时间："+useTime);

		
	}

}
