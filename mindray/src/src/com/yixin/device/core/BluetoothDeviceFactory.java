package com.yixin.device.core;

import com.yixin.device.omron.OmronDevice;

import android.content.Context;
import android.util.Log;

/**
 * 设备工厂，使用该工厂生产 {@link IBluetoothDevice} 接口的实例
 * 
 * <pre>
 * 产品参考工厂静态变量
 * </pre>
 * 
 * @author ChenRui
 */
public final class BluetoothDeviceFactory
{
	/**
	 * 迈瑞监测设备
	 */
	public static final int		PRODUCT_MINARAY	= 0;
	
	/**
	 * 欧姆龙监测设备
	 */
	public static final int		PRODUCT_OMRON	= 1;
	
	private static final String	TAG				= "BluetoothDeviceFactory";
	
	private BluetoothDeviceFactory()
	{
	}
	
	/**
	 * 获取设备
	 * 
	 * @param product 产品参考工厂静态变量
	 * @return
	 */
	public static IBluetoothDevice getDevice(Context context, int product)
	{
		IBluetoothDevice result;
		switch (product)
		{
			case PRODUCT_OMRON:
				result = new OmronDevice(context);
				Log.i(TAG, "Connection Device is Omron!");
				break;
			case PRODUCT_MINARAY:
			default:
				result = new MindrayDevice(context);
				Log.i(TAG, "Connection Device is Minaray!");
				break;
		}
		
		return result;
	}
}
