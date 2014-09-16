package com.yixin.device.core;

/**
 * 蓝牙数据解析接口
 * 
 * @author MrChenrui
 * 
 */
public interface IBluetoothParser
{
	/**
	 * 解析数据
	 * @param data
	 * @param start
	 * @return
	 */
	int parser(byte[] data, int start);
}
