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
	
	private static final String	DEVICE_NAME	= "HEM-7081-IT";
	public static final String	DEVICE_PIN	= "";
	private Connectable			mDeviceconConnectable;
	
	public OmronBluetoothConnection(Context context, BluetoothListener listener, Connectable connectable) {
		super(context, listener);
		this.mDeviceconConnectable = connectable; // 设置核心连接接口
		setDataParser(new OmronXmlParser()); // 设置数据解析接口
	}
	
	@Override
	public void connect() {
		getBluetoothManager().openBluetooth();
		startDiscovery();
	}
	
	@Override
	public void disconnect() {
		getBluetoothManager().unRegisterReceiver();
	}
	
	@Override
	public boolean isConnected() {
		return false;
	}
	
	@Override
	public void onCloseBluetooth() {
		super.onCloseBluetooth();
		mDeviceconConnectable.disconnect();
	}
	
	@Override
	public void onBluetoothBonded(BluetoothDevice device) {
		super.onBluetoothBonded(device);
		BluetoothManager.cancelPairingUserInput(device);
		mDeviceconConnectable.connect();// 连接成功
	}
	
	@Override
	public void onBluetoothSetPin(BluetoothDevice device) {
		super.onBluetoothSetPin(device);
		BluetoothManager.setPin(device, DEVICE_PIN);
	}
	
	@Override
	public boolean onFindBluetooth(BluetoothDevice device, boolean isBonded) {
		if (device.getName().equals(DEVICE_NAME)) {
			//TODO:已经配对处理
			if (isBonded) return true;
			BluetoothManager.autoCrateBondAndSetPin(device, DEVICE_PIN);
			return true;
		}
		else {
			return super.onFindBluetooth(device, isBonded);
		}
	}
	
}
