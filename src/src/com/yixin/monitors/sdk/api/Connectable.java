package com.yixin.monitors.sdk.api;

/**
 * 可连接的接口
 * 
 * @author ChenRui
 * 
 */
public interface Connectable {
	/**
	 * 连接
	 */
	void connect();
	
	/**
	 * 断开连接
	 */
	void disconnect();
	
	/**
	 * 是否已经连接
	 * 
	 * @return
	 */
	boolean isConnected();
}
