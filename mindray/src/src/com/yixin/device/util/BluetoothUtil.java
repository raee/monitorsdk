package com.yixin.device.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

/**
 * 蓝牙自动配对
 * 
 * @author Chenrui
 */
public class BluetoothUtil
{
	
	public static String	ACTION_PAIRING_REQUEST	= "android.bluetooth.device.action.PAIRING_REQUEST";
	
	/**
	 * 与设备配对 参考源码：platform/packages/apps/Settings.git
	 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
	 */
	static public boolean createBond(Class<?> btClass, BluetoothDevice btDevice) throws Exception
	{
		Method createBondMethod = btClass.getMethod("createBond");
		Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}
	
	/**
	 * 与设备解除配对 参考源码：platform/packages/apps/Settings.git
	 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
	 */
	static public boolean removeBond(Class<?> btClass, BluetoothDevice btDevice) throws Exception
	{
		Method removeBondMethod = btClass.getMethod("removeBond");
		Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}
	
	/**
	 * 设置蓝牙配对码
	 * 
	 * @param btClass
	 * @param btDevice
	 * @param str
	 * @return
	 * @throws Exception
	 */
	static public boolean setPin(Class<?> btClass, BluetoothDevice btDevice, String str) throws Exception
	{
		try
		{
			boolean result = false;
			int count = 0;
			Method removeBondMethod = btClass.getDeclaredMethod("setPin", new Class[] { byte[].class });
			while (!result && count < 3)
			{
				result = (Boolean) removeBondMethod.invoke(btDevice, str.getBytes());
				
				Log.i("BluetoothUtil", "尝试配对次数：" + count + "；结果：" + result);
				count++;
			}
			
			Log.i("BluetoothUtil", btDevice.getName() + "蓝牙自动配对结果：" + result);
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
		
	}
	
	/**
	 * 蓝牙自动配对
	 * 
	 * @param devicesClass
	 * @param device
	 * @param pin
	 * @return
	 */
	static public boolean autoBond(Class<?> devicesClass, BluetoothDevice device, String pin)
	{
		try
		{
			// 判断是否已经配对过
			if (device.getBondState() != BluetoothDevice.BOND_BONDED)
			{
				boolean bonded = BluetoothUtil.createBond(device.getClass(), device); // 首先要进行配对
				boolean pined = BluetoothUtil.setPin(device.getClass(), device, pin);
				return bonded && pined;
			}
			else
			{
				Log.i("Bluetooth", device.getName() + "蓝牙已经配对！");
				return true;
				// 已经配对过
				//removeBond(devicesClass, device); // 先移除配对
				// boolean bonded = BluetoothUtil.createBond(device.getClass(),
				// device); // 首先要进行配对
				// boolean pined = BluetoothUtil.setPin(device.getClass(),
				// device, pin);
				// return bonded && pined;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	// 取消用户输入
	static public boolean cancelPairingUserInput(Class<?> btClass, BluetoothDevice device)
	{
		try
		{
			Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
			// cancelBondProcess()
			Boolean returnValue = (Boolean) createBondMethod.invoke(device);
			Log.i("BluetoothUtil", "取消配对输入框：" + returnValue);
			return returnValue.booleanValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	// 取消配对
	static public boolean cancelBondProcess(Class<?> btClass, BluetoothDevice device) throws Exception
	{
		Method createBondMethod = btClass.getMethod("cancelBondProcess");
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		return returnValue.booleanValue();
	}
	
	/**
	 * @param clsShow
	 */
	static public void printAllInform(Class<?> clsShow)
	{
		try
		{
			// 取得所有方法
			Method[] hideMethod = clsShow.getMethods();
			int i = 0;
			for (; i < hideMethod.length; i++)
			{
				Log.e("method name", hideMethod[i].getName() + ";and the i is:" + i);
			}
			// 取得所有常量
			Field[] allFields = clsShow.getFields();
			for (i = 0; i < allFields.length; i++)
			{
				Log.e("Field name", allFields[i].getName());
			}
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 注册蓝牙广播
	 * 
	 * @param context
	 * @param receiver
	 */
	static public void registerBluetoothRecevicer(Context context, BroadcastReceiver receiver)
	{
		IntentFilter intentFilter = new IntentFilter(); // 意图过滤
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); // 配对状态
		intentFilter.addAction(ACTION_PAIRING_REQUEST); // 配对
		intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
		context.registerReceiver(receiver, intentFilter); // 注册广播
	}
	
}
