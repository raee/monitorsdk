package com.yixin.monitors.sdk.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yixin.monitors.sdk.api.BluetoothListener;

/**
 * 蓝牙广播接收
 * 
 * @author ChenRui
 * 
 */
class BluetoothBroadcastReceiver extends BroadcastReceiver {

	BluetoothListener mBluetoothListener;
	private boolean isRegister = false;

	public boolean isRegister() {
		return isRegister;
	}

	public void setRegister(boolean isRegister) {
		this.isRegister = isRegister;
	}

	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();

	public BluetoothBroadcastReceiver(BluetoothListener listener) {
		setBluetoothListener(listener);
	}

	public void setBluetoothListener(BluetoothListener listener) {
		this.mBluetoothListener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		// 开始扫描
		if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
			mBluetoothListener.onStartDiscovery();
		}

		// 蓝牙扫描完毕
		if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			mBluetoothListener.onStopDiscovery();
		}

		// 发现蓝牙设备
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if (mBluetoothListener.onFindBluetooth(device,
					device.getBondState() == BluetoothDevice.BOND_BONDED)) {
				return;
			}
		}

		// 蓝牙状态改变
		if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
			int state = mBluetoothAdapter.getState();
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			mBluetoothListener.onBluetoothStateChange(state, device);
			if (BluetoothAdapter.STATE_OFF == state) {
				mBluetoothListener.onCloseBluetooth();
			}
			if (BluetoothAdapter.STATE_ON == state) {
				mBluetoothListener.onOpenBluetooth();
			}
		}

		// 蓝牙配对状态
		if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			int state = device.getBondState();
			switch (state) {
			case BluetoothDevice.BOND_BONDED:// 蓝牙已经正在配对完成
				mBluetoothListener.onBluetoothBonded(device);
				break;
			case BluetoothDevice.BOND_BONDING:// 蓝牙正在配对
				mBluetoothListener.onBluetoothBonding(device);
				break;
			case BluetoothDevice.BOND_NONE: // 蓝牙取消配对
				mBluetoothListener.onBluetoothBondNone(device);
				break;
			default:
				break;
			}
		}

		// 设置配对码状态
		if (BluetoothManager.ACTION_PAIRING_REQUEST.equals(action)) {
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			mBluetoothListener.onBluetoothSetPin(device); // 蓝牙正在弹出配对输入框
		}

		// 开始数据接收
		if (BluetoothManager.ACTION_START_RECEIVE.equals(action)) {
			mBluetoothListener.onStartReceive();
		}

		// 正在数据接收
		if (BluetoothManager.ACTION_RECEIVEING.equals(action)) {
			byte[] data = intent
					.getByteArrayExtra(BluetoothManager.EXTRA_BLUETOOTH_DATA);
			mBluetoothListener.onReceiving(data);
		}

		// 接收完成
		if (BluetoothManager.ACTION_RECEIVED.equals(action)) {
			byte[] data = intent
					.getByteArrayExtra(BluetoothManager.EXTRA_BLUETOOTH_DATA);
			mBluetoothListener.onReceived(data);
		}

		// 蓝牙建立连接完成
		if (BluetoothManager.ACTION_CONNETED.equals(action)) {
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			mBluetoothListener.onConnected(device);
		}
	}

}
