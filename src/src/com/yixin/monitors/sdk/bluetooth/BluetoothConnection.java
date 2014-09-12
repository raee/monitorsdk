package com.yixin.monitors.sdk.bluetooth;

import java.util.Set;

import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.api.Connectable;
import com.yixin.monitors.sdk.api.IDataParser;
import com.yixin.monitors.sdk.model.PackageModel;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

/**
 * 抽象蓝牙连接
 * 
 * @author ChenRui
 * 
 */
abstract class BluetoothConnection implements BluetoothListener, Connectable {
	
	private static final String	Tag	= "BluetoothConnection";
	private BluetoothManager	mBluetoothManager;
	private BluetoothListener	mBluetoothListener;
	private IDataParser			mDataParser;
	
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
	
	/**
	 * 开始扫描蓝牙设备
	 */
	protected void startDiscovery() {
		Set<BluetoothDevice> devices = mBluetoothManager.getBluetoothAdapter().getBondedDevices();// 迭代，得到所有本机已保存的配对的蓝牙适配器对象
		for (BluetoothDevice bluetoothDevice : devices) {
			if (onFindBluetooth(bluetoothDevice, true)) { return; }
		}
		
		if (mBluetoothManager.getBluetoothAdapter() != null && !mBluetoothManager.getBluetoothAdapter().isDiscovering()) {
			mBluetoothManager.getBluetoothAdapter().startDiscovery();// 查找设备
		}
	}
	
	@Override
	public void onStartDiscovery() {
		Log.d(Tag, "开始蓝牙扫描");
		mBluetoothListener.onStartDiscovery();
	}
	
	@Override
	public void onStopDiscovery() {
		Log.i(Tag, "蓝牙扫描完毕！");
		mBluetoothListener.onStopDiscovery();
	}
	
	@Override
	public void onOpenBluetooth() {
		Log.i(Tag, "蓝牙被打开了！");
		mBluetoothListener.onOpenBluetooth();
	}
	
	@Override
	public void onCloseBluetooth() {
		Log.i(Tag, "蓝牙关闭了！");
		mBluetoothListener.onCloseBluetooth();
	}
	
	@Override
	public void onBluetoothStateChange(int state, BluetoothDevice device) {
		mBluetoothListener.onBluetoothStateChange(state, device);
		Log.i(Tag, "蓝牙状态改变！状态：" + state);
	}
	
	@Override
	public boolean onFindBluetooth(BluetoothDevice device, boolean isBonded) {
		Log.i(Tag, "发现蓝牙设备：" + device.getName());
		return mBluetoothListener.onFindBluetooth(device, isBonded);
	}
	
	@Override
	public void onBluetoothBonding(BluetoothDevice device) {
		Log.i(Tag, "正在配对" + device.getName());
		mBluetoothListener.onBluetoothBonding(device);
	}
	
	@Override
	public void onBluetoothSetPin(BluetoothDevice device) {
		Log.i(Tag, "设置配对中" + device.getName());
		mBluetoothListener.onBluetoothSetPin(device);
	}
	
	@Override
	public void onBluetoothBonded(BluetoothDevice device) {
		Log.i(Tag, "配对完成" + device.getName());
		mBluetoothListener.onBluetoothBonded(device);
	}
	
	@Override
	public void onBluetoothBondNone(BluetoothDevice device) {
		Log.i(Tag, "取消配对！" + device.getName());
		mBluetoothListener.onBluetoothBondNone(device);
	}
	
	@Override
	public void onStartReceive() {
		Log.i(Tag, "开始接收蓝牙数据");
		mBluetoothListener.onStartReceive();
	}
	
	@Override
	public void onReceiving(byte[] data) {
		Log.i(Tag, "蓝牙数据接收中");
		mBluetoothListener.onReceiving(data);
	}
	
	@Override
	public void onReceived(byte[] data) {
		Log.i(Tag, "数据接收完成");
		mBluetoothListener.onReceived(data);
		if (data != null && data.length > 0) {
			PackageModel model = mDataParser.parse(data);
			onReceived(model);
		}
	}
	
	@Override
	public void onReceived(PackageModel model) {
		mBluetoothListener.onReceived(model);
		
	}
	
	@Override
	public void onConnected(BluetoothDevice device) {
		Log.i(Tag, "蓝牙设备连接完成！");
		mBluetoothListener.onConnected(device);
	}
	
	@Override
	public void onError(int errorCode, String msg) {
		Log.e(Tag, "蓝牙发生异常：" + msg);
		mBluetoothListener.onError(errorCode, msg);
	}
	
	@Override
	public void onBluetoothCancle() {
		Log.i(Tag, "蓝牙取消运行！");
		mBluetoothListener.onBluetoothCancle();
	}
	
}
