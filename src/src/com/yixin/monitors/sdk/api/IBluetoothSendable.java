package com.yixin.monitors.sdk.api;

/**
 * 蓝牙发送数据接口
 * 
 * @author ChenRui
 * 
 */
public interface IBluetoothSendable {
	/**
	 * 发送二进制数据
	 * 
	 * @param data
	 */
	void send(byte[] data);
}
