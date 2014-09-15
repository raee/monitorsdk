package com.yixin.monitors.sdk;

import android.os.Binder;

import com.yixin.monitors.sdk.api.ApiMonitor;
import com.yixin.monitors.sdk.api.BluetoothListener;

/**
 * 设备服务绑定
 * 
 * @author ChenRui
 * 
 */
public class MonitorBinder extends Binder {
	private ApiMonitor	mApiMonitor;
	
	public void setBluetoothListener(BluetoothListener l) {
		mApiMonitor.setBluetoothListener(l);
	}
	
	public ApiMonitor getApiMonitor() {
		return mApiMonitor;
	}
	
	/**
	 * 设置接口
	 * 
	 * @param api
	 */
	public void setApiMonitor(ApiMonitor api) {
		this.mApiMonitor = api;
	}
}
