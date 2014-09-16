package com.yixin.device.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * 蓝牙回调接口
 * 
 * @author MrChenrui
 * 
 */
public interface BluetoothCallback
{
	/**
	 * 蓝牙捕捉的异常
	 */
	public static final int	CODE_BLUETOOTH_EXCTION		= 500;
	
	/**
	 * 蓝牙断开异常
	 */
	public static final int	CODE_BLUETOOTH_DISCONNETED	= 505;
	
	/**
	 * 没有发现蓝牙设备
	 */
	public static final int	CODE_BLUETOOTH_NOT_FOUND	= 404;
	
	/**
	 * 蓝牙发送数据失败
	 */
	public static final int	CODE_BLUETOOTH_SEND_FAILD	= 600;
	
	/**
	 * 开始查找蓝牙设备
	 */
	void onFindBluetooth();
	
	/**
	 * 蓝牙查找完毕
	 */
	void onFindFinshBluetooth();
	
	/**
	 * 蓝牙连接成功返回蓝牙设备
	 * 
	 * @param device
	 *            蓝牙设备
	 */
	void onBluetooth(BluetoothDevice device);
	
	/**
	 * 蓝牙失败接口
	 */
	void onBluetoothFail(int code, String msg);
	
	/**
	 * 蓝牙接收到数据
	 * 
	 * @param data
	 */
	void onBluetoothRecevice(byte[] data);
	
	/**
	 * 正在接收蓝牙数据
	 */
	void onBluetoothReceviceing();
	
	/**
	 * 蓝牙断开
	 */
	void onBluetoothDisconnect();
	
	/**
	 * 蓝牙断开
	 */
	void onBluetoothSendData(byte[] data);
	
	/**
	 * 蓝牙配对状态回调
	 * 
	 * @param device
	 * @param state
	 *            配对状态，参考BluetoothDevice的蓝牙状态
	 * @param msg
	 *            消息
	 */
	void onBluetoothBound(BluetoothDevice device, int state, String msg);
	
}
