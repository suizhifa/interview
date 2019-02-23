package com.lzkj.file.interview.dao;


import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.lzkj.file.interview.common.ConstantUtil;
import com.lzkj.file.interview.common.DataBaseUtil;
import com.lzkj.file.interview.entity.StartDateInfo;
import com.lzkj.file.interview.exception.DaoExceptionHandle;
import com.lzkj.file.interview.mapper.StartDateInfoMapper;

public class StartDateInfoDAO {	
    
	public StartDateInfo queryStartDateInfoByid(int id)throws Exception{
		StartDateInfo startDateInfo=null;
		try{
			SqlSession sqlSession = DataBaseUtil.getSqlSession();
		    StartDateInfoMapper startDateInfoMapper = sqlSession.getMapper(StartDateInfoMapper.class);
		    startDateInfo=startDateInfoMapper.findOne(id);
		}catch(Exception e){
			e.printStackTrace();
		}
					
		return startDateInfo;
	}
	/**
	 * 批量插入start_date_info表
	 * @param startDateInfos
	 * @throws Exception
	 * suizf 20190222
	 */
	public void insertStartDateInfoByBatch(List<StartDateInfo> startDateInfos)throws Exception{
		try{
			SqlSession sqlSession = DataBaseUtil.getSqlSession();
		    StartDateInfoMapper startDateInfoMapper = sqlSession.getMapper(StartDateInfoMapper.class);
		    startDateInfoMapper.insertStartDateInfoByBatch(startDateInfos);
		    DataBaseUtil.commit();
		}catch(Exception e){
			e.printStackTrace();
			DataBaseUtil.rollback();//数据回滚
			DaoExceptionHandle.daoExceptionHandle(ConstantUtil.DAO_EXCEPTION_CODE);//交给异常处理
		}
	}

}
