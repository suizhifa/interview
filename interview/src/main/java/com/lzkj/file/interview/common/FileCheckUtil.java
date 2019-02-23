package com.lzkj.file.interview.common;

public class FileCheckUtil {
	
	public static void checkByLine(String line)throws Exception{
		String str[]=line.split(ConstantUtil.FILE_SPILT_CODE);
		if(null!=str && str.length==3){
			
		}else{
			throw new Exception("此行格式不正确，请核对，内容为："+str);
		}
	}

}
