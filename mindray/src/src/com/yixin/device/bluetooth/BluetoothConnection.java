package com.yixin.device.bluetooth;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yixin.device.util.BluetoothUtil;

/**
 * 蓝牙连接
 * 
 * @author ChenRui
 */
public class BluetoothConnection extends BroadcastReceiver
{
	private BluetoothAdapter	mBluetoothAdapter;
	private Context				mContext;
	private BroadcastReceiver	mBluetoothBroadcastReceiver;	// 蓝牙广播接收者
	protected BluetoothCallback	mBluetoothCallback;
	
	public BluetoothConnection(Context context)
	{
		this.mContext = context;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mBluetoothBroadcastReceiver = this;
	}
	
	public void setBluetoothCallback(BluetoothCallback callback)
	{
		this.mBluetoothCallback = callback;
	}
	
	public void connect()
	{
		openBluetooth();
		startDiscovery();
		registerBluetoothRecevicer();
	}
	
	public void disconnect()
	{
		unRegisterBluetoothRecevicer();
	}
	
	/**
	 * 注册蓝牙广播，设置之前请调用setBluetoothBroadcastReceiver()来设置广播接收者
	 */
	public void registerBluetoothRecevicer()
	{
		if (mBluetoothBroadcastReceiver != null)
		{
			BluetoothUtil.registerBluetoothRecevicer(mContext, mBluetoothBroadcastReceiver);
		}
	}
	
	/**
	 * 取消蓝牙广播接收
	 */
	public void unRegisterBluetoothRecevicer()
	{
		if (mBluetoothBroadcastReceiver != null)
		{
			mContext.unregisterReceiver(mBluetoothBroadcastReceiver);
			mBluetoothBroadcastReceiver = null;
		}
	}
	
	/**
	 * 设置蓝牙接收者，不设置将会采用默认的广播接收者
	 * 
	 * @param receiver
	 */
	public void setBluetoothBroadcastReceiver(BroadcastReceiver receiver)
	{
		this.mBluetoothBroadcastReceiver = receiver;
	}
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		
		if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
		{
			onBluetoothDiscoveryStart(); // 开始扫描蓝牙
			if (mBluetoothCallback != null)
			{
				mBluetoothCallback.onBluetoothDisconnect();
			}
		}
		if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
		{
			onBluetoothDiscoveryFinished(); // 蓝牙扫描完毕
			if (mBluetoothCallback != null)
			{
				mBluetoothCallback.onFindFinshBluetooth();
			}
		}
		if (BluetoothDevice.ACTION_FOUND.equals(action))
		{
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			onBluetoothFound(device); // 发现蓝牙设备
			if (mBluetoothCallback != null)
			{
				mBluetoothCallback.onBluetooth(device);
			}
		}
		if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
		{
			int state = mBluetoothAdapter.getState();
			onBluetoothStateChange(state); // 蓝牙状态改变
		}
		if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) // 配对状态
		{
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			int state = device.getBondState();
			String msg = device.getName();
			switch (state)
			{
				case BluetoothDevice.BOND_BONDED:
					onBluetoothBonded(device); // 蓝牙已经正在配对完成
					msg += "配对完成！";
					break;
				case BluetoothDevice.BOND_BONDING:
					onBluetoothBonding(device);// 蓝牙正在配对
					msg += "正在配对！";
					break;
				case BluetoothDevice.BOND_NONE:
					onBluetoothBondNone(device); // 蓝牙取消配对
					msg += "取消配对！";
					break;
				default:
					break;
			}
			if (mBluetoothCallback != null)
			{
				mBluetoothCallback.onBluetoothBound(device, state, msg);
			}
		}
		if (BluetoothUtil.ACTION_PAIRING_REQUEST.equals(action))
		{
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			onBluetoothSetPin(device); // 蓝牙正在弹出配对输入框
			if (mBluetoothCallback != null)
			{
				mBluetoothCallback.onBluetoothBound(device, 100, device.getName() + "正在配对...");
			}
		}
	}
	
	/**
	 * 蓝牙正在弹出配对输入框
	 * 
	 * @param device
	 */
	protected void onBluetoothSetPin(BluetoothDevice device)
	{
		
	}
	
	/**
	 * 蓝牙取消配对
	 * 
	 * @param device
	 */
	protected void onBluetoothBondNone(BluetoothDevice device)
	{
		
	}
	
	/**
	 * 蓝牙正在配对
	 * 
	 * @param device
	 */
	protected void onBluetoothBonding(BluetoothDevice device)
	{
		
	}
	
	/**
	 * 蓝牙已经配对完成
	 * 
	 * @param device
	 */
	protected void onBluetoothBonded(BluetoothDevice device)
	{
		
	}
	
	/**
	 * 发现蓝牙设备
	 * 
	 * @param device
	 */
	protected void onBluetoothFound(BluetoothDevice device)
	{
		
	}
	
	/**
	 * 蓝牙状态改变
	 * 
	 * @param state
	 */
	protected void onBluetoothStateChange(int state)
	{
		
	}
	
	/**
	 * 蓝牙扫描完毕
	 */
	protected void onBluetoothDiscoveryFinished()
	{
	}
	
	/**
	 * 开始扫描蓝牙
	 */
	protected void onBluetoothDiscoveryStart()
	{
	}
	
	protected Context getContext()
	{
		return mContext;
	}
	
	/**
	 * 获取蓝牙适配器
	 * 
	 * @return
	 */
	protected BluetoothAdapter getBluetoothAdapter()
	{
		return mBluetoothAdapter;
	}
	
	/**
	 * 打开蓝牙
	 */
	protected void openBluetooth()
	{
		if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled())
		{
			mBluetoothAdapter.enable();
		}
	}
	
	/**
	 * 关闭蓝牙
	 */
	protected void closeBluetooth()
	{
		if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled())
		{
			mBluetoothAdapter.disable();
		}
	}
	
	/**
	 * 开始扫描蓝牙设备
	 */
	protected void startDiscovery()
	{
		Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();// 迭代，得到所有本机已保存的配对的蓝牙适配器对象
		for (BluetoothDevice bluetoothDevice : devices)
		{
			onBluetoothFound(bluetoothDevice);
		}
		
		if (mBluetoothAdapter != null && !mBluetoothAdapter.isDiscovering())
		{
			mBluetoothAdapter.startDiscovery();// 查找设备
		}
	}
	
	/**
	 * 结束蓝牙扫描
	 */
	protected void stopDiscovery()
	{
		if (mBluetoothAdapter != null)
		{
			mBluetoothAdapter.cancelDiscovery();
		}
	}
	
	/**
	 * 是否连接
	 * 
	 * @return
	 */
	public boolean isConnected()
	{
		if (mBluetoothBroadcastReceiver == null)
		{
			return false;
		}
		else if (mBluetoothAdapter == null)
		{
			return false;
		}
		else if (!mBluetoothAdapter.isEnabled())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * 蓝牙自动配对
	 * 
	 * @param device
	 * @param pin 配对码
	 */
	protected boolean autoBond(BluetoothDevice device, String pin)
	{
		return BluetoothUtil.autoBond(device.getClass(), device, pin);
	}
}
