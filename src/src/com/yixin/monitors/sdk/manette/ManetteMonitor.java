package com.yixin.monitors.sdk.manette;

import android.content.Context;
import android.util.Log;

import com.yixin.monitors.sdk.api.IDataParser;
import com.yixin.monitors.sdk.bluetooth.MindrayBluetoothConnection;
import com.yixin.monitors.sdk.mindray.MindrayMonitor;

/**
 * 玛奈特血糖仪接口
 * 
 * @author ChenRui
 * 
 */
public class ManetteMonitor extends MindrayMonitor {
	public static String DEVICE_NAME = "TZ100";
	public static String DEVICE_PIN = "1234";

	public ManetteMonitor(Context context) {
		super(context);
		getDeviceInfo().setDeviceName(DEVICE_NAME);
		getDeviceInfo().setDevicePin(DEVICE_PIN);
	}

	@Override
	public void connect() {
		if (mBluetoothListener == null) {
			Log.e("apimonitor", "没有设置蓝牙监听，不发生连接！");
			return;
		}
		if (isConnected()) {
			return;
		}
		if (mConnection == null) {
			mConnection = new MindrayBluetoothConnection(mContext,
					getDeviceInfo(), mBluetoothListener);
			IDataParser mDataParser = new ManetteDataParser(
					mConnection.getBluetoothSocketConnection(),
					mBluetoothListener);
			mConnection.setDataParser(mDataParser);
		}
		mConnection.connect();
	}

	@Override
	public void configDevice(String deviceName, String devicePin) {
		getDeviceInfo().setDeviceName(deviceName);
		getDeviceInfo().setDevicePin(devicePin);
		DEVICE_NAME = deviceName;
		DEVICE_PIN = devicePin;
	}

}
