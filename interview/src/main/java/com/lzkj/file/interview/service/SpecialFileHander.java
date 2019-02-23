package com.lzkj.file.interview.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.lzkj.file.interview.common.ConstantUtil;
import com.lzkj.file.interview.entity.StartDateInfo;

/**
 * 可扩展处理解析类
 * 原题中要求解析处理需要可扩展，此处为实际的解析处理类
 * @author suizhifa
 *
 */
public class SpecialFileHander implements IHandle{

	public StartDateInfo handleByLine(byte[] bytes,String charset) throws Exception{
		// TODO Auto-generated method stub
		return parseFileByLine(bytes,charset);
	}
	/**
	 * 按行解析数据
	 * @param bytes
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public StartDateInfo parseFileByLine(byte[] bytes,String charset)throws Exception{
		String line = null;
		StartDateInfo startDateInfo=null;
		if(charset==null){
			line = new String(bytes);
		}else{
			line = new String(bytes,charset);
		}
		if(null!=line && !"".equals(line)){
			startDateInfo = parseFileByLine(line);
		}
		return startDateInfo;
		
	}
	
	public StartDateInfo parseFileByLine(String line)throws Exception{
		StartDateInfo startDateInfo=new StartDateInfo();
		String str[]=line.split(ConstantUtil.FILE_SPILT_CODE);
		if(null==str || str.length!=3){
			throw new Exception("此行格式不正确，请核对，内容为："+str);
		}else{
			startDateInfo.setId(Integer.parseInt(str[0]));
			startDateInfo.setType(Integer.parseInt(str[1]));
			startDateInfo.setStartDate(converStrToDate(str[1],str[2]));
		}		
		return startDateInfo;
	}
	
	public Date converStrToDate(String type,String strDate)throws Exception{
		Date date=new Date();
		if("1".equals(type)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");//2019年02月13日
			date=format.parse(strDate);
		}else if("2".equals(type)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//02-16
			Calendar cal=Calendar.getInstance();
			int y=cal.get(Calendar.YEAR);
			strDate=String.valueOf(y)+"-"+strDate;
			date=format.parse(strDate);
		}else if("3".equals(type)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");//04月19日
			Calendar cal=Calendar.getInstance();
			int y=cal.get(Calendar.YEAR);
			strDate=String.valueOf(y)+"年"+strDate;
			date=format.parse(strDate);
		}else if("4".equals(type)){
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");//0229
			Calendar cal=Calendar.getInstance();
			int y=cal.get(Calendar.YEAR);
			strDate=String.valueOf(y)+strDate;
			date=format.parse(strDate);
		}else{
			throw new Exception("没有匹配的类型"+type);
		}
		return date;
	}

}
