package com.yixin.monitors.sdk.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.AsyncTask.Status;

import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.api.IBluetoothSendable;
import com.yixin.monitors.sdk.model.DeviceInfo;
import com.yixin.monitors.sdk.model.PackageModel;

/**
 * 迈瑞蓝牙连接线程
 * 
 * @author ChenRui
 * 
 */
public class MindrayBluetoothConnection extends BluetoothConnection {
	private BluetoothSocketConnection mSocketConnection;

	/**
	 * 实例化后请记得设置数据解析接口，及socket连接
	 * 
	 * @param context
	 * @param listener
	 */
	public MindrayBluetoothConnection(Context context, DeviceInfo info,
			BluetoothListener listener) {
		super(context, info, listener);
		mSocketConnection = new BluetoothSocketConnection(this);
	}

	public IBluetoothSendable getBluetoothSendableInterface() {
		return mSocketConnection;
	}

	@Override
	public boolean onFindBluetooth(BluetoothDevice device, boolean isBonded) {
		if (device == null) {
			return false;
		}

		if (getDeviceName().equals(device.getName())) {
			if (isBonded) {
				connect(device);
				return true;
			}
		}
		return super.onFindBluetooth(device, isBonded);
	}

	private boolean mIsStarted = false;

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
	public void onBluetoothSendData(byte[] data) {
		super.onBluetoothSendData(data);
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
			mSocketConnection = new BluetoothSocketConnection(this);
		}
		if (mSocketConnection.getStatus() == Status.FINISHED) {
			mSocketConnection.disconnect();
			mSocketConnection = new BluetoothSocketConnection(this);
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
		if (mSocketConnection != null) {
			mSocketConnection.disconnect();
			mSocketConnection = null;
		}
		getBluetoothManager().closeBluetooth(); // 关闭 蓝牙
	}


}
