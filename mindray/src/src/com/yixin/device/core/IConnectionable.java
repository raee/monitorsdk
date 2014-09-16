package com.yixin.device.core;

/**
 * 可连接接口
 * 
 * @author ChenRui
 */
public interface IConnectionable
{
	/**
	 * 连接
	 */
	void connect();
	
	/**
	 * 取消连接
	 */
	void disconnect();
}
