package com.yixin.monitors.sdk.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import com.yixin.monitors.sdk.model.PackageModel;

/**
 * 蓝牙广播发送统一规范
 * 
 * @author ChenRui
 * 
 */
public class SendBoradcastManager {
	private SendBoradcastManager() {
	}

	/**
	 * 
	 * @param model
	 */
	public static void sendReceived(Context context, PackageModel model) {
		Intent intent = new Intent(BluetoothManager.ACTION_RECEIVED);
		intent.putExtra(BluetoothManager.EXTRA_BLUETOOTH_DATA, model);
		context.sendBroadcast(intent);
	}

	public static void sendDeviceConnected(Context context,
			BluetoothDevice device) {
		Intent intent = new Intent(BluetoothManager.ACTION_CONNETED);
		intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
		context.sendBroadcast(intent);

	}
}
