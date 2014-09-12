package com.yixin.monitors.sdk.api;

/**
 * 核心接口，该接口用于连接设备
 * 
 * @author ChenRui
 * 
 */
public interface ApiMonitor extends Connectable {
	void setBluetoothListener(BluetoothListener listener);
}
