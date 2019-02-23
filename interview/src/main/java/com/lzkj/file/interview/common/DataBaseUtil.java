package com.lzkj.file.interview.common;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DataBaseUtil {
	
	private static SqlSessionFactory sqlSessionFactory;
	private static final ThreadLocal<SqlSession> t = new ThreadLocal<SqlSession>();
	static{
		try{			
			Reader resourceAsReader = Resources.getResourceAsReader("mybatis-config.xml");//后续按需修改
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsReader);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 暴露一个获取session方法
	 * suizf 20190222
	 */
	public static SqlSession getSqlSession(){
		SqlSession sqlSession = t.get();
		if(sqlSession==null){
			sqlSession = sqlSessionFactory.openSession();
			t.set(sqlSession);
		}
		return sqlSession;
	}
	/**
	 * 
	 * suizf 20190222
	 */
	public static void close(){
		SqlSession sqlSession = t.get();
		sqlSession.close();
		t.remove();
	}
	
	public static void commit(){
		getSqlSession().commit();
		close();
	}
	
	public static void rollback(){
		getSqlSession().rollback();
		close();	
	}
	

}
