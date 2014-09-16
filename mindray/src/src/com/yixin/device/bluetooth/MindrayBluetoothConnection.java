package com.yixin.device.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.AsyncTask.Status;

import com.yixin.device.core.IBluetoothParser;
import com.yixin.device.util.BluetoothUtil;

/**
 * 迈瑞设备蓝牙连接
 * 
 * @author ChenRui
 */
public class MindrayBluetoothConnection extends BluetoothConnection
{
	
	/**
	 * 迈瑞蓝牙设备名称
	 */
	public static final String			DEVICE_NAME	= "mindray-ubicare";
	
	/**
	 * 迈瑞设备配对密码
	 */
	public static final String			DEVICE_PIN	= "4321";
	
	private IBluetoothParser			mParser;
	private BluetoothSocketConnection	mBluetoothSocketConnection;
	
	public MindrayBluetoothConnection(Context context, BluetoothCallback callback, IBluetoothParser parser)
	{
		super(context);
		this.mParser = parser;
		setBluetoothCallback(callback);
		connect();// 建立连接
	}
	
	/**
	 * 开始连接蓝牙连接准备工作<br>
	 * 1、没注册蓝牙广播的注册蓝牙广播 <br>
	 * 2、打开蓝牙 <br>
	 * 3、扫描蓝牙设备
	 * 
	 * @see com.yixin.device.bluetooth.BluetoothConnection#connect()
	 */
	@Override
	public void connect()
	{
		if (mBluetoothSocketConnection != null && mBluetoothSocketConnection.getStatus() == Status.RUNNING)
		{
			return;// 已经正在连接则返回
		}
		super.connect();
	}
	
	@Override
	public void disconnect()
	{
		super.disconnect();
		
		if (mBluetoothSocketConnection != null)
		{
			mBluetoothSocketConnection.cancel(true);// 结束蓝牙线程
		}
		
		closeBluetooth();
	}
	
	@Override
	public boolean isConnected()
	{
		if (mBluetoothSocketConnection == null)
		{
			return false;
		}
		else if (mBluetoothSocketConnection.getStatus() != Status.RUNNING)
		{
			return false;
		}
		else
		{
			return super.isConnected();
		}
	}
	
	/**
	 * 发送数据到蓝牙设备中
	 * 
	 * @param data
	 */
	public void sendData(byte[] data)
	{
		mBluetoothSocketConnection.sendData(data);
	}
	
	@Override
	protected void onBluetoothSetPin(BluetoothDevice device)
	{
		mBluetoothCallback.onBluetoothBound(device, BluetoothDevice.BOND_BONDING, device.getName() + "正在配对PIN码"); // 通知蓝牙配对状态改变
		if (BluetoothUtil.autoBond(device.getClass(), device, DEVICE_PIN))
		{
			BluetoothUtil.cancelPairingUserInput(device.getClass(), device); // 取消用户输入框
		}
	}
	
	@Override
	protected void onBluetoothBondNone(BluetoothDevice device)
	{
		mBluetoothCallback.onBluetoothBound(device, BluetoothDevice.BOND_BONDING, device.getName() + "配对失败！"); // 通知蓝牙配对状态改变
	}
	
	@Override
	protected void onBluetoothBonding(BluetoothDevice device)
	{
		mBluetoothCallback.onBluetoothBound(device, BluetoothDevice.BOND_BONDING, "正在配对：" + device.getName()); // 通知蓝牙配对状态改变
	}
	
	@Override
	protected void onBluetoothBonded(BluetoothDevice device)
	{
		BluetoothUtil.cancelPairingUserInput(device.getClass(), device); // 取消输入框
		mBluetoothCallback.onBluetoothBound(device, BluetoothDevice.BOND_BONDING, device.getName() + "配对成功！"); // 通知蓝牙配对状态改变
	}
	
	@Override
	protected void onBluetoothFound(BluetoothDevice device)
	{
		if (DEVICE_NAME.equals(device.getName())) // 找到设备
		{
			mBluetoothSocketConnection = new BluetoothSocketConnection(mParser, mBluetoothCallback);
			mBluetoothSocketConnection.connect(device); // 线程连接
			stopDiscovery();
		}
	}
	
	@Override
	protected void onBluetoothStateChange(int state)
	{
		if (getBluetoothAdapter().getState() == BluetoothAdapter.STATE_ON)
		{
			connect(); // 蓝牙已经打开
		}
		else
		{
			disconnect();
		}
	}
	
	@Override
	protected void onBluetoothDiscoveryFinished()
	{
		if (!isConnected())
		{
			mBluetoothCallback.onBluetoothFail(BluetoothCallback.CODE_BLUETOOTH_NOT_FOUND, "没有找到设备!");// 没有找到设备
		}
		else
		{
			mBluetoothCallback.onFindFinshBluetooth(); // 回调给扫描完毕接口
		}
	}
	
	@Override
	protected void onBluetoothDiscoveryStart()
	{
		mBluetoothCallback.onFindBluetooth(); // 回调给开始扫描接口
	}
	
}
