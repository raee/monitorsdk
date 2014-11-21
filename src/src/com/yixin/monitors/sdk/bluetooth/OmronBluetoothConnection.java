package com.yixin.monitors.sdk.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.signove.health.service.OmronXmlParser;
import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.api.Connectable;
import com.yixin.monitors.sdk.model.DeviceInfo;

/**
 * 欧姆龙蓝牙连接
 * 
 * @author ChenRui
 * 
 */
public class OmronBluetoothConnection extends BluetoothConnection {

	private Connectable	mDeviceconConnectable;

	public OmronBluetoothConnection(Context context, DeviceInfo info, BluetoothListener listener, Connectable connectable) {
		super(context, info, listener);
		this.mDeviceconConnectable = connectable; // 设置核心连接接口
		setDataParser(new OmronXmlParser()); // 设置数据解析接口
	}

	@Override
	public void disconnect() {
		super.disconnect();
		getBluetoothManager().closeBluetooth(); // 关闭蓝牙
	}

	@Override
	public void onCloseBluetooth() {
		super.onCloseBluetooth();
		mDeviceconConnectable.disconnect();
	}

	@Override
	public void onBluetoothBonded(BluetoothDevice device) {
		super.onBluetoothBonded(device);
		mDeviceconConnectable.connect();// 连接成功
	}

	@Override
	public void onOpenBluetooth() {
		super.onOpenBluetooth();
		mDeviceconConnectable.connect();
	}

	@Override
	public void onConnected(BluetoothDevice device) {
		super.onConnected(device);
		this.mDeviceconConnectable.connect(); // 重新连接
	}

	@Override
	public void onError(int errorCode, String msg) {
		super.onError(errorCode, msg);
		for (BluetoothDevice devices : getBluetoothManager().getBluetoothAdapter().getBondedDevices()) {
			if (getDeviceName().equals(devices.getName())) {
				BluetoothManager.removeBond(devices); // 移除
			}
		}
	}

}
