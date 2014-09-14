package com.yixin.monitors.sdk.api;

/**
 * 核心接口，该接口用于连接设备
 * 
 * @author ChenRui
 * 
 */
public interface ApiMonitor extends Connectable {
	/**
	 * 设置蓝牙回调监听
	 * 
	 * @param listener
	 */
	void setBluetoothListener(BluetoothListener listener);
}
