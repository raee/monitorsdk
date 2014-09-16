package com.yixin.device.bluetooth;

import com.yixin.device.core.IConnectionable;
import com.yixin.device.util.BluetoothUtil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

/**
 * 欧姆龙蓝牙设备连接
 * 
 * @author ChenRui
 */
public class OrmonBluetoothConnection extends BluetoothConnection {
	private String Tag = "OrmonBluetoothConnection";

	/**
	 * 迈瑞蓝牙设备名称
	 */
	public static final String DEVICE_NAME = "HEM-7081-IT";
	private IConnectionable mConncetinListener;

	public OrmonBluetoothConnection(Context context, BluetoothCallback callback) {
		super(context);
		setBluetoothCallback(callback);
	}

	public void setOnBluetoothChangeListener(IConnectionable connectionable) {
		this.mConncetinListener = connectionable;
	}

	@Override
	protected void onBluetoothStateChange(int state) {
		if (getBluetoothAdapter().getState() == BluetoothAdapter.STATE_OFF
				&& mConncetinListener != null) {
			mConncetinListener.disconnect(); // 关闭连接
			Log.d(Tag, "关闭欧姆龙连接！");
		}
	}

	@Override
	protected void onBluetoothSetPin(BluetoothDevice device) {
		Log.d(Tag, "设置配对码中！");
		if (autoBond(device, "")) {
			cancelUserInput(device);
		}
	}

	@Override
	protected void onBluetoothFound(BluetoothDevice device) {
		if (device.getName().equals(DEVICE_NAME)) {
			Log.d(Tag, "发现欧姆龙设备，正在配对！");
			autoBond(device, ""); // 自动配对
		}
	}

	@Override
	protected void onBluetoothBonded(BluetoothDevice device) {
		cancelUserInput(device);
		Log.d(Tag, "欧姆龙配对成功！");
		if (mConncetinListener != null) {
			mConncetinListener.connect();// 连接设备
		}
	}

	void cancelUserInput(BluetoothDevice device) {
		BluetoothUtil.cancelPairingUserInput(device.getClass(), device); // 取消输入框
	}

}
