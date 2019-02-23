package com.lzkj.file.interview.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lzkj.file.interview.entity.StartDateInfo;

public interface StartDateInfoMapper {
	public StartDateInfo findOne(@Param("id") int id);
	public void insertStartDateInfoByBatch(@Param("startDateInfos")List<StartDateInfo> startDateInfos);

}
