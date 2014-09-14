package com.yixin.monitors.sdk.bluetooth;

import java.lang.reflect.Method;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import com.yixin.monitors.sdk.api.BluetoothListener;

/**
 * 蓝牙连接管理
 * 
 * @author ChenRui
 * 
 */
public class BluetoothManager {
	/**
	 * 正在配对广播
	 */
	public static final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";

	/**
	 * 蓝牙接收数据完成广播
	 */
	public static final String ACTION_RECEIVED = "com.yixin.device.bluetooth.action.received";

	/**
	 * 正在接收数据广播
	 */
	public static final String ACTION_RECEIVEING = "com.yixin.device.bluetooth.action.receiving";
	/**
	 * 开始接收蓝牙数据广播
	 */
	public static final String ACTION_START_RECEIVE = "com.yixin.device.bluetooth.action.receive";

	/**
	 * 设备连接完成广播
	 */
	public static final String ACTION_CONNETED = "com.yixin.device.bluetooth.action.connected";

	/**
	 * 蓝牙数据
	 */
	public static String EXTRA_BLUETOOTH_DATA = "android.bluetooth.device.extra.data";

	private static String TAG = "BluetoothManager";

	/**
	 * 自动配对
	 * 
	 * @param device
	 * @param pin
	 * @return
	 */
	static boolean autoCrateBondAndSetPin(BluetoothDevice device, String pin) {
		// 判断是否已经配对过
		if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			boolean bonded = createBond(device); // 首先要进行配对
			boolean pined = setPin(device, pin);
			return bonded && pined;
		} else {
			Log.i("Bluetooth", device.getName() + "蓝牙已经配对！");
			return true;
		}
	}

	/**
	 * 取消配对
	 * 
	 * @param device
	 * @return
	 * @throws Exception
	 */
	static boolean cancelBondProcess(BluetoothDevice device) throws Exception {
		if (device == null) {
			return false;
		}
		Method createBondMethod = device.getClass().getMethod(
				"cancelBondProcess");
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		return returnValue.booleanValue();
	}

	/**
	 * 取消用户输入
	 * 
	 * @param device
	 * @return
	 */
	static boolean cancelPairingUserInput(BluetoothDevice device) {
		try {
			if (device == null) {
				return false;
			}
			Method createBondMethod = device.getClass().getMethod(
					"cancelPairingUserInput");
			Boolean returnValue = (Boolean) createBondMethod.invoke(device);
			Log.i("BluetoothUtil", "取消配对输入框：" + returnValue);
			return returnValue.booleanValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 与设备配对
	 */
	static boolean createBond(BluetoothDevice device) {
		try {
			if (device == null) {
				return false;
			}
			if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
				Log.i(TAG, "-- createBond --已经配对！" + device.getName());
				return true;
			}
			Method createBondMethod = device.getClass().getMethod("createBond");
			Boolean returnValue = (Boolean) createBondMethod.invoke(device);
			boolean val = returnValue.booleanValue();
			Log.d(TAG, device.getName() + "配对结果：" + val);
			return val;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 与设备解除配对
	 */
	static boolean removeBond(BluetoothDevice device) {
//		try {
//			if (device == null) {
//				return false;
//			}
//			Method removeBondMethod = device.getClass().getMethod("removeBond");
//			Boolean returnValue = (Boolean) removeBondMethod.invoke(device);
//			boolean val = returnValue.booleanValue();
//			Log.i(TAG, "移除配对：" + device.getName() + ":" + val);
//			return val;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return false;
	}

	/**
	 * 设置蓝牙配对码
	 * 
	 * @param device
	 * @param pin
	 * @return
	 */
	static boolean setPin(BluetoothDevice device, String pin) {
		try {
			if (device == null) {
				return false;
			}
			boolean result = false;
			int count = 0;
			Method removeBondMethod = device.getClass().getDeclaredMethod(
					"setPin", new Class[] { byte[].class });
			while (!result && count < 3) {
				result = (Boolean) removeBondMethod.invoke(device,
						pin.getBytes());

				Log.i("BluetoothUtil", "尝试配对次数：" + count + "；结果：" + result);
				count++;
			}

			Log.i("BluetoothUtil", device.getName() + "蓝牙自动配对结果：" + result);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	private String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED";

	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();

	private BluetoothBroadcastReceiver mBluetoothBroadcastReceiver;

	private Context mContext;

	public BluetoothManager(Context context) {
		this.mContext = context;
	}

	/**
	 * 关闭蓝牙
	 */
	public void closeBluetooth() {
		if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.disable();
		}
	}

	public BluetoothAdapter getBluetoothAdapter() {
		return mBluetoothAdapter;
	}

	private IntentFilter getIntentFilter() {
		IntentFilter intentFilter = new IntentFilter(); // 意图过滤
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND); // 发现设备
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);// 扫描完成
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);// 开始扫描
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);// 蓝牙状态改变
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); // 配对状态
		intentFilter.addAction(ACTION_PAIRING_REQUEST); // 配对
		intentFilter.addAction(ACTION_CONNECTION_STATE_CHANGED); // 连接状态改变

		intentFilter.addAction(ACTION_START_RECEIVE); // 开始接收数据
		intentFilter.addAction(ACTION_RECEIVEING); // 正在接收数据
		intentFilter.addAction(ACTION_RECEIVED); // 接收数据完成
		intentFilter.addAction(ACTION_CONNETED); // 接收数据完成
		return intentFilter;
	}

	/**
	 * 打开蓝牙
	 */
	public void openBluetooth() {
		if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}
	}

	/**
	 * 设置蓝牙回调，同时设置广播接收
	 * 
	 * @param l
	 */
	public void setBluetoothListener(BluetoothListener l) {
		unRegisterReceiver();
		registBroadcastReceiver(l);
	}

	/**
	 * 注册蓝牙广播监听
	 * 
	 * @param l
	 */
	public void registBroadcastReceiver(BluetoothListener l) {
		if (this.mBluetoothBroadcastReceiver == null) {
			this.mBluetoothBroadcastReceiver = new BluetoothBroadcastReceiver(l);
		}
		if (!this.mBluetoothBroadcastReceiver.isRegister()) {
			mContext.registerReceiver(mBluetoothBroadcastReceiver,
					getIntentFilter());
			mBluetoothBroadcastReceiver.setRegister(true);
		}
	}

	/**
	 * 结束蓝牙扫描
	 */
	public void stopDiscovery() {
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.cancelDiscovery();
		}
	}

	/**
	 * 取消蓝牙广播监听
	 */
	public void unRegisterReceiver() {
		if (mBluetoothBroadcastReceiver != null
				&& mBluetoothBroadcastReceiver.isRegister()) {
			mContext.unregisterReceiver(mBluetoothBroadcastReceiver); // 取消广播监听
			mBluetoothBroadcastReceiver = null;
		}
	}
}
