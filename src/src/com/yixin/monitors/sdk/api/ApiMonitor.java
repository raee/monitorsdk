package com.yixin.monitors.sdk.api;

import com.yixin.monitors.sdk.MonitorBinder;
import com.yixin.monitors.sdk.MonitorSdkFactory;
import com.yixin.monitors.sdk.model.DeviceInfo;

/**
 * 核心接口，该接口用于连接设备<br>
 * 接口实例化请使用工厂{@link MonitorSdkFactory} getApiMonitor()方法。<br>
 * 服务绑定的请使用{@link MonitorBinder} 通过设置BluetoothListener()来获取回调监听。
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
	
	DeviceInfo getDeviceInfo();
	
	void configDevice(String deviceName, String devicePin);
}
