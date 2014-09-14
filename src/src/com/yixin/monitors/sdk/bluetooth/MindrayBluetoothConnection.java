package com.yixin.monitors.sdk.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.AsyncTask.Status;
import android.util.Log;

import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.api.IBluetoothSendable;
import com.yixin.monitors.sdk.model.PackageModel;

public class MindrayBluetoothConnection extends BluetoothConnection {

	public static final String DEVICE_NAME = "mindray-ubicare";
	public static final String DEVICE_PIN = "4321";
	private MindraySocketConnection mSocketConnection;
	private String TAG = "MindrayBluetoothConnection";

	/**
	 * 实例化后请记得设置数据解析接口，及socket连接
	 * 
	 * @param context
	 * @param listener
	 */
	public MindrayBluetoothConnection(Context context,
			BluetoothListener listener) {
		super(context, listener);
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
				onBluetoothBonded(device);
				return true;
			}
		}
		return super.onFindBluetooth(device, isBonded);
	}

	@Override
	public void onReceiving(byte[] data) {
		super.onReceiving(data);
		PackageModel result = mDataParser.parse(data);
		onReceived(result);
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
		startDiscovery();
	}

	@Override
	public void disconnect() {
		super.disconnect();
		Log.i(TAG, "Connect Cancel: Mindray !");
		if (mSocketConnection != null) {
			mSocketConnection.disconnect();
			mSocketConnection = null;
		}
		getBluetoothManager().closeBluetooth();
	}

	@Override
	public String getDeviceName() {
		return DEVICE_NAME;
	}

	@Override
	public String getDevicePin() {
		return DEVICE_PIN;
	}

}
