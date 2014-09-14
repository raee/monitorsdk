package com.yixin.monitors.sdk.bluetooth;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.api.Connectable;
import com.yixin.monitors.sdk.api.IDataParser;
import com.yixin.monitors.sdk.model.PackageModel;

/**
 * 抽象蓝牙连接
 * 
 * @author ChenRui
 * 
 */
abstract class BluetoothConnection implements BluetoothListener, Connectable {

	private static final String TAG = "BluetoothConnection";
	private BluetoothManager mBluetoothManager;
	private BluetoothListener mBluetoothListener;
	protected IDataParser mDataParser;
	private boolean mIsConnected = false; // 是否连接上设备！
	private BluetoothDevice mCurrentDevice;

	public abstract String getDeviceName();

	public abstract String getDevicePin();

	public BluetoothConnection(Context context, BluetoothListener listener) {
		mBluetoothManager = new BluetoothManager(context);
		mBluetoothManager.setBluetoothListener(this);
		setBluetoothListener(listener);
	}

	public void setBluetoothListener(BluetoothListener listener) {
		this.mBluetoothListener = listener;
	}

	public void setDataParser(IDataParser dataparser) {
		this.mDataParser = dataparser;
	}

	public BluetoothManager getBluetoothManager() {
		return mBluetoothManager;
	}

	@Override
	public void connect() {
		mBluetoothManager.registBroadcastReceiver(this);
		mBluetoothManager.openBluetooth();
		startDiscovery();// 开始扫描蓝牙
		Log.d(TAG, "蓝牙准备连接！");
	}

	@Override
	public void disconnect() {
		mBluetoothManager.unRegisterReceiver();
		onBluetoothCancle(); // 通知被取消
	}

	@Override
	public boolean isConnected() {
		return mIsConnected;
	}

	/**
	 * 开始扫描蓝牙设备
	 */
	protected void startDiscovery() {
		Set<BluetoothDevice> devices = mBluetoothManager.getBluetoothAdapter()
				.getBondedDevices();// 迭代，得到所有本机已保存的配对的蓝牙适配器对象
		for (BluetoothDevice bluetoothDevice : devices) {
			if (onFindBluetooth(bluetoothDevice, true)) {
				Log.i(TAG, "发现可用蓝牙，不开始扫描！");
				mCurrentDevice = bluetoothDevice;
				onStopDiscovery();
				return;
			}
		}

		BluetoothAdapter bluetoothAdapter = mBluetoothManager
				.getBluetoothAdapter();
		Log.i(TAG, "是否正在扫描：" + bluetoothAdapter.isDiscovering());
		if (bluetoothAdapter != null && !bluetoothAdapter.isDiscovering()) {
			bluetoothAdapter.startDiscovery();// 查找设备
			Log.d(TAG, "开始扫描蓝牙！");
		}
	}

	@Override
	public void onStartDiscovery() {
		Log.d(TAG, "开始蓝牙扫描...");
		mBluetoothListener.onStartDiscovery();
	}

	@Override
	public void onStopDiscovery() {
		Log.i(TAG, "蓝牙扫描完毕！");
		mBluetoothListener.onStopDiscovery();

		if (mCurrentDevice == null) {
			onError(0, getDeviceName() + "设备没有发现，请打开监测设备再试！");
		}

		// else if (!isConnected()) {
		// Set<BluetoothDevice> devices = mBluetoothManager
		// .getBluetoothAdapter().getBondedDevices();// 迭代，得到所有本机已保存的配对的蓝牙适配器对象
		// for (BluetoothDevice bluetoothDevice : devices) {
		// if (bluetoothDevice.getName().equals(getDeviceName())) {
		// Log.i(TAG, "没有连接，重新配对连接...");
		// BluetoothManager.removeBond(bluetoothDevice);
		// // 扫描完成后还是没有连接上，并且已经配对了，取消后再次尝试。
		// startDiscovery();
		//
		// break;
		// }
		// }
		//
		// onError(ERROR_CODE_UNKNOWN, "设备连接失败!");
		// }

	}

	@Override
	public void onOpenBluetooth() {
		Log.i(TAG, "蓝牙被打开了！");
		mBluetoothListener.onOpenBluetooth();
	}

	@Override
	public void onCloseBluetooth() {
		Log.i(TAG, "蓝牙关闭了！");
		mBluetoothListener.onCloseBluetooth();
	}

	@Override
	public void onBluetoothStateChange(int state, BluetoothDevice device) {
		mBluetoothListener.onBluetoothStateChange(state, device);
		Log.i(TAG, "蓝牙状态改变！状态：" + state);
	}

	@Override
	public boolean onFindBluetooth(BluetoothDevice device, boolean isBonded) {
		Log.i(TAG, "发现蓝牙设备" + device.getName());
		if (device.getName().equals(getDeviceName())) {
			mCurrentDevice = device;
			if (!getBluetoothManager().getBluetoothAdapter().isDiscovering()) {
				onStopDiscovery();
			} else {
				getBluetoothManager().stopDiscovery();
			}
			if (isBonded) {
				return true;
			}
			Log.i(TAG, "发现蓝牙设备,并开始配对：" + device.getName());
			BluetoothManager.autoCrateBondAndSetPin(device, getDevicePin()); // 自动配对
			return true;
		}
		return mBluetoothListener.onFindBluetooth(device, isBonded);
	}

	@Override
	public void onBluetoothBonding(BluetoothDevice device) {
		Log.i(TAG, "正在配对" + device.getName());
		mBluetoothListener.onBluetoothBonding(device);
	}

	@Override
	public void onBluetoothSetPin(BluetoothDevice device) {
		Log.i(TAG, "设置配对中" + device.getName());
		BluetoothManager.setPin(device, getDevicePin());
		mBluetoothListener.onBluetoothSetPin(device);
	}

	@Override
	public void onBluetoothBonded(BluetoothDevice device) {
		Log.i(TAG, device.getName() + "配对成功！");
		BluetoothManager.cancelPairingUserInput(device);
		mBluetoothListener.onBluetoothBonded(device);
	}

	@Override
	public void onBluetoothBondNone(BluetoothDevice device) {
		Log.i(TAG, "取消配对！" + device.getName());
		mBluetoothListener.onBluetoothBondNone(device);
	}

	@Override
	public void onStartReceive() {
		Log.i(TAG, "开始接收蓝牙数据");
		mBluetoothListener.onStartReceive();
	}

	@Override
	public void onReceiving(byte[] data) {
		Log.i(TAG, "蓝牙数据接收中");
		mBluetoothListener.onReceiving(data);
	}

	@Override
	public void onReceived(byte[] data) {
		Log.i(TAG, "数据接收完成");
		mBluetoothListener.onReceived(data);
		if (mDataParser == null) {
			return;
		}
		if (data != null && data.length > 0) {
			PackageModel model = mDataParser.parse(data);
			onReceived(model);
		}
	}

	@Override
	public void onReceived(PackageModel model) {
		if (model != null) {
			mBluetoothListener.onReceived(model);
		}
	}

	@Override
	public void onConnected(BluetoothDevice device) {
		Log.i(TAG, "蓝牙设备连接完成！");
		mIsConnected = true;
		mCurrentDevice = device;
		mBluetoothListener.onConnected(device);
	}

	@Override
	public void onError(int errorCode, String msg) {
		Log.e(TAG, "蓝牙发生异常：" + msg);
		mBluetoothListener.onError(errorCode, msg);
	}

	@Override
	public void onBluetoothCancle() {
		Log.i(TAG, "蓝牙取消运行！");
		mBluetoothListener.onBluetoothCancle();
	}

}
