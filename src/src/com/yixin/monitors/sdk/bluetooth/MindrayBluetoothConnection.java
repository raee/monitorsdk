package com.yixin.monitors.sdk.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.AsyncTask.Status;
import android.util.Log;

import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.api.IBluetoothSendable;
import com.yixin.monitors.sdk.model.DeviceInfo;
import com.yixin.monitors.sdk.model.PackageModel;

public class MindrayBluetoothConnection extends BluetoothConnection {
	private MindraySocketConnection	mSocketConnection;
	private String					TAG	= "MindrayBluetoothConnection";
	
	/**
	 * 实例化后请记得设置数据解析接口，及socket连接
	 * 
	 * @param context
	 * @param listener
	 */
	public MindrayBluetoothConnection(Context context, DeviceInfo info, BluetoothListener listener) {
		super(context, info, listener);
		mSocketConnection = new MindraySocketConnection(this);
	}
	
	public IBluetoothSendable getBluetoothSendableInterface() {
		return mSocketConnection;
	}
	
	public void setSocketConnection(MindraySocketConnection conn) {
		this.mSocketConnection = conn;
	}
	
	@Override
	public boolean onFindBluetooth(BluetoothDevice device, boolean isBonded) {
		if (device.getName().equals(getDeviceName())) {
			if (isBonded) {
				connect(device);
				return true;
			}
		}
		return super.onFindBluetooth(device, isBonded);
	}
	
	private boolean	mIsStarted	= false;
	
	@Override
	public void onReceiving(byte[] data) {
		super.onReceiving(data);
		if (!mIsStarted) {
			onStartReceive();
			mIsStarted = true;
		}
		PackageModel result = mDataParser.parse(data);
		onReceived(result);
	}
	
	@Override
	public void onReceived(PackageModel model) {
		super.onReceived(model);
		if (model != null) {
			mIsStarted = false;
		}
	}
	
	@Override
	public void onError(int errorCode, String msg) {
		super.onError(errorCode, msg);
		mIsStarted = false;
		mIsConnected = false;
		
	}
	
	@Override
	public void onBluetoothBonded(BluetoothDevice device) {
		super.onBluetoothBonded(device);
		connect(device);
	}
	
	private void connect(BluetoothDevice device) {
		if (mSocketConnection == null) {
			mSocketConnection = new MindraySocketConnection(this);
		}
		if (mSocketConnection.getStatus() == Status.FINISHED) {
			mSocketConnection.disconnect();
			mSocketConnection = new MindraySocketConnection(this);
		}
		
		mSocketConnection.connect(device);
	}
	
	@Override
	public void onOpenBluetooth() {
		super.onOpenBluetooth();
		connect();
	}
	
	@Override
	public void disconnect() {
		super.disconnect();
		Log.i(TAG, "Connect Cancel: Mindray !");
		if (mSocketConnection != null) {
			mSocketConnection.disconnect();
			mSocketConnection = null;
		}
		
		getBluetoothManager().closeBluetooth(); //关闭 蓝牙
	}
	
}
