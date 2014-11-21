package com.yixin.monitors.sdk.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.AsyncTask.Status;
import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.model.DeviceInfo;
import com.yixin.monitors.sdk.model.PackageModel;

/**
 * 迈瑞蓝牙连接线程
 * 
 * @author ChenRui
 * 
 */
public class MindrayBluetoothConnection extends BluetoothConnection {
	private BluetoothSocketConnection	mSocketConnection;
	private boolean						mIsStarted	= false;
//	private String						tag			= "MindrayBluetoothConnection";
	private BluetoothListener			mListener;

	/**
	 * 实例化后请记得设置数据解析接口，及socket连接
	 * 
	 * @param context
	 * @param listener
	 */
	public MindrayBluetoothConnection(Context context, DeviceInfo info, BluetoothListener listener) {
		super(context, info, listener);
		this.mListener = listener;
		mSocketConnection = new BluetoothSocketConnection(this);
	}

	public BluetoothSocketConnection getBluetoothSocketConnection() {
		return mSocketConnection;
	}

	@Override
	public boolean onFindBluetooth(BluetoothDevice device, boolean isBonded) {
		if (getDeviceName().equals(device.getName())) {
			if (isBonded) {
				connect(device);
				return true;
			}
		}
		return super.onFindBluetooth(device, isBonded);
	}

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
		getBluetoothManager().closeBluetooth(); // 发生错误则关闭蓝牙。
	}

	@Override
	public void onBluetoothBonded(BluetoothDevice device) {
		super.onBluetoothBonded(device);
		connect(device);
	}

	@Override
	public void onConnected(BluetoothDevice device) {
		super.onConnected(device);
		BluetoothManager.cancelPairingUserInput(device, mContext);
	}

	private void connect(BluetoothDevice device) {
		if (mSocketConnection.getStatus() == Status.RUNNING) {
			return;
		}
		else if (mSocketConnection.getStatus() == Status.FINISHED) {
			mSocketConnection.cancel(true);
			mSocketConnection = null;
			mSocketConnection = new BluetoothSocketConnection(mListener);
		}

		mSocketConnection.execute(device);
	}

	@Override
	public void onOpenBluetooth() {
		super.onOpenBluetooth();
		connect();
	}

	@Override
	public boolean isConnected() {
		return !mSocketConnection.isDisConnected();
	}

	@Override
	public void disconnect() {
		super.disconnect();
		// if (mSocketConnection != null) {
		mSocketConnection.disconnect();
		// mSocketConnection = null;
		// }

		getBluetoothManager().closeBluetooth(); // 关闭 蓝牙
	}

}
