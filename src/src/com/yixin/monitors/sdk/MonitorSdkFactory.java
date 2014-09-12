package com.yixin.monitors.sdk;

import android.content.Context;

import com.yixin.monitors.sdk.api.ApiMonitor;
import com.yixin.monitors.sdk.omron.OmronMonitor;

/**
 * 监测设备工厂，由该工厂获取监测设备接口
 * 
 * @author ChenRui
 * 
 */
public final class MonitorSdkFactory {
	/**
	 * 迈瑞监测设备
	 */
	public static final int	MINARAY	= 0;
	
	/**
	 * 欧姆龙监测设备
	 */
	public static final int	OMRON	= 1;
	
	/**
	 * 获取接口
	 * 
	 * @param context
	 * @return
	 */
	public static ApiMonitor getApiMonitor(Context context) {
		return new OmronMonitor(context);
	}
	
}
