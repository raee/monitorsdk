package com.yixin.monitors.sdk.api;

import android.bluetooth.BluetoothDevice;

import com.yixin.monitors.sdk.model.PackageModel;

/**
 * 蓝牙回调接口
 * 
 * @author ChenRui
 * 
 */
public interface BluetoothListener {
	
	/**
	 * 蓝牙数据流关闭
	 */
	public static final int	ERROR_CODE_STREAM_CLOSE	= -1;
	
	/**
	 * 未知异常
	 */
	public static final int	ERROR_CODE_UNKNOWN		= 0;
	/**
	 * 没找到设备
	 */
	public static final int	ERROR_NOT_FOND			= -3;
	
	/**
	 * 连接超时
	 */
	public static final int	ERROR_TIME_OUT			= -4;
	/**
	 * 发送数据异常
	 */
	public static final int	ERROR_CODE_SEND_ERROR	= -2;
	
	/**
	 * 开始扫描
	 */
	void onStartDiscovery();
	
	/**
	 * 停止扫描
	 */
	void onStopDiscovery();
	
	/**
	 * 打开蓝牙时
	 */
	void onOpenBluetooth();
	
	/**
	 * 关闭蓝牙时
	 */
	void onCloseBluetooth();
	
	/**
	 * 蓝牙状态改变时
	 * 
	 * @param state
	 *            状态
	 * @param device
	 *            蓝牙设备，注意空值判断。
	 */
	void onBluetoothStateChange(int state, BluetoothDevice device);
	
	/**
	 * 扫描到设备时
	 * 
	 * @param device
	 *            设备
	 * @param isBonded
	 *            是否已经配对
	 * @return 真：找到想要的设备。
	 */
	boolean onFindBluetooth(BluetoothDevice device, boolean isBonded);
	
	/**
	 * 正在配对
	 * 
	 * @param device
	 */
	void onBluetoothBonding(BluetoothDevice device);
	
	/**
	 * 设置配对码时
	 * 
	 * @param device
	 */
	void onBluetoothSetPin(BluetoothDevice device);
	
	/**
	 * 配对成功
	 * 
	 * @param device
	 * @return 真：找到设备；假：没找到设备。
	 */
	void onBluetoothBonded(BluetoothDevice device);
	
	/**
	 * 配对失败
	 * 
	 * @param device
	 */
	void onBluetoothBondNone(BluetoothDevice device);
	
	/**
	 * 开始数据传输
	 */
	void onStartReceive();
	
	/**
	 * 正在传输过程
	 * 
	 * @param data
	 */
	void onReceiving(byte[] data);
	
	/**
	 * 传输完成
	 * 
	 * @param data
	 */
	void onReceived(byte[] data);
	
	/**
	 * 回调解析后的结果
	 * 
	 * @param model
	 *            数据
	 */
	void onReceived(PackageModel model);
	
	/**
	 * 当设备已经连接
	 * 
	 * @param device
	 */
	void onConnected(BluetoothDevice device);
	
	/**
	 * 当蓝牙发生错误时
	 * 
	 * @param errorCode
	 *            错误代码
	 * @param msg
	 *            错误信息
	 */
	void onError(int errorCode, String msg);
	
	/**
	 * 进行取消操作或者蓝牙已经结束时回调
	 */
	void onBluetoothCancle();
}
