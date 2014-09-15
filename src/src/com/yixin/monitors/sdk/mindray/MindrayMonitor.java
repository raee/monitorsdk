package com.yixin.monitors.sdk.mindray;

import android.content.Context;
import android.util.Log;

import com.yixin.monitors.sdk.api.ApiMonitor;
import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.bluetooth.MindrayBluetoothConnection;
import com.yixin.monitors.sdk.mindray.parser.CMSControl;
import com.yixin.monitors.sdk.model.DeviceInfo;

public class MindrayMonitor implements ApiMonitor {
	
	private static final String			Tag	= "MindrayMonitor";
	private Context						mContext;
	private MindrayBluetoothConnection	mConnection;
	
	public MindrayMonitor(Context context) {
		this.mContext = context;
		Log.i(Tag, "Connect Device is Mindray!");
		
	}
	
	@Override
	public void connect() {
		if (isConnected()) {
			Log.i(Tag, getDeviceInfo().getDeviceName() + "已经连接！不需要连接！");
			return;
		}
		if (mConnection != null) {
			mConnection.connect();
		}
	}
	
	@Override
	public void disconnect() {
		if (mConnection != null) {
			mConnection.disconnect();
		}
	}
	
	@Override
	public boolean isConnected() {
		if (mConnection == null) return false;
		return mConnection.isConnected();
	}
	
	@Override
	public void setBluetoothListener(BluetoothListener listener) {
		if (mConnection == null) {
			mConnection = new MindrayBluetoothConnection(mContext, listener);
			CMSControl mDataParser = new CMSControl(mConnection.getBluetoothSendableInterface());
			mConnection.setDataParser(mDataParser);
		}
	}
	
	private DeviceInfo	mDeviceInfo;
	
	@Override
	public DeviceInfo getDeviceInfo() {
		if (mDeviceInfo == null) {
			mDeviceInfo = new DeviceInfo();
			mDeviceInfo.setDevID("Mindray-H900");
			mDeviceInfo.setDeviceName("迈瑞");
		}
		return mDeviceInfo;
	}
}
