package com.yixin.monitors.sdk.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.signove.health.service.OmronXmlParser;
import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.api.Connectable;

/**
 * 欧姆龙蓝牙连接
 * 
 * @author ChenRui
 * 
 */
public class OmronBluetoothConnection extends BluetoothConnection {

	public static final String DEVICE_NAME = "HEM-7081-IT";
	public static final String DEVICE_PIN = "";
	private Connectable mDeviceconConnectable;

	public OmronBluetoothConnection(Context context,
			BluetoothListener listener, Connectable connectable) {
		super(context, listener);
		this.mDeviceconConnectable = connectable; // 设置核心连接接口
		setDataParser(new OmronXmlParser()); // 设置数据解析接口
	}

	@Override
	public void disconnect() {
		super.disconnect();
//		getBluetoothManager().closeBluetooth(); // 关闭蓝牙
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
	public String getDeviceName() {
		return DEVICE_NAME;
	}

	@Override
	public String getDevicePin() {
		return DEVICE_PIN;
	}

}
