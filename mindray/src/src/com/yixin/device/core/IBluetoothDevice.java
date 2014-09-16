package com.yixin.device.core;

import com.yixin.device.bluetooth.BluetoothCallback;

/**
 * 监测设备接口,实例请使用 {@link BluetoothDeviceFactory} 工厂进行实例化
 * 
 * @author ChenRui
 */
public interface IBluetoothDevice extends IConnectionable
{
	
	/**
	 * 设置蓝牙回调监听
	 * 
	 * @param bluetoothCallback
	 */
	void setOnBluetoothDeviceListener(BluetoothCallback bluetoothCallback);
	
	/**
	 * 数据来源，设置蓝牙数据回调监听
	 * 
	 * @param l
	 */
	void setOnReceviceListener(DeviceReceviceCallback l);
	
	/**
	 * 是否正在连接
	 * 
	 * @return
	 */
	boolean isConnected();
}
