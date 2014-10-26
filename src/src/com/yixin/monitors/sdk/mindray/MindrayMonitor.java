package com.yixin.monitors.sdk.mindray;

import android.content.Context;
import android.util.Log;

import com.yixin.monitors.sdk.api.ApiMonitor;
import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.bluetooth.MindrayBluetoothConnection;
import com.yixin.monitors.sdk.mindray.parser.CMSControl;
import com.yixin.monitors.sdk.model.DeviceInfo;

public class MindrayMonitor implements ApiMonitor {
	public static String					DEVICE_NAME	= "mindray-ubicare";
	public static String					DEVICE_PIN	= "4321";
	private static final String				Tag			= "MindrayMonitor";
	protected Context						mContext;
	protected MindrayBluetoothConnection	mConnection;
	protected BluetoothListener				mBluetoothListener;

	public MindrayMonitor(Context context) {
		this.mContext = context;
		mDeviceInfo = new DeviceInfo();
		mDeviceInfo.setDeviceName(DEVICE_NAME);
		mDeviceInfo.setDevicePin(DEVICE_PIN);
	}

	@Override
	public void connect() {
		if (mBluetoothListener == null) {
			Log.e("apimonitor", "没有设置蓝牙监听，不发生连接！");
			return;
		}
		if (isConnected()) {
			Log.i(Tag, getDeviceInfo().getDeviceName() + "已经连接！不需要连接！");
			return;
		}
		if (mConnection == null) {
			mConnection = new MindrayBluetoothConnection(mContext,
					getDeviceInfo(), mBluetoothListener);
			CMSControl mDataParser = new CMSControl(
					mConnection.getBluetoothSendableInterface());
			mConnection.setDataParser(mDataParser);
		}

		mConnection.connect();
	}

	@Override
	public void disconnect() {
		if (mConnection != null) {
			mConnection.disconnect();
			mConnection = null;
			System.gc();
		}
	}

	@Override
	public boolean isConnected() {
		if (mConnection == null) return false;
		return mConnection.isConnected();
	}

	@Override
	public void setBluetoothListener(BluetoothListener listener) {
		this.mBluetoothListener = listener;
	}

	private DeviceInfo	mDeviceInfo;

	@Override
	public DeviceInfo getDeviceInfo() {
		return mDeviceInfo;
	}

	@Override
	public void configDevice(String deviceName, String devicePin) {
		DEVICE_NAME = deviceName;
		DEVICE_PIN = devicePin;
		mDeviceInfo.setDeviceName(deviceName);
		mDeviceInfo.setDevicePin(devicePin);
	}
}
