package com.lzkj.file.interview.service;

import com.lzkj.file.interview.entity.StartDateInfo;

/**
 * 作为可扩展的解析处理接口
 * @author suizhifa
 *
 */
public interface IHandle{
	public StartDateInfo handleByLine(byte[] bytes,String charset)throws Exception;

}
