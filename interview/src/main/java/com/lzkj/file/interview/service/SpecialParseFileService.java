package com.lzkj.file.interview.service;

import java.util.List;

import com.lzkj.file.interview.dao.StartDateInfoDAO;
import com.lzkj.file.interview.entity.StartDateInfo;

public class SpecialParseFileService {
	
	public  void insertStartDateInfoByBatch(List<StartDateInfo> startDateInfos)throws Exception{
		StartDateInfoDAO dao=new StartDateInfoDAO();
		dao.insertStartDateInfoByBatch(startDateInfos);
	}

}
