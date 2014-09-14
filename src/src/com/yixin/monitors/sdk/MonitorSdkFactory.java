package com.yixin.monitors.sdk;

import android.content.Context;

import com.yixin.monitors.sdk.api.ApiMonitor;
import com.yixin.monitors.sdk.mindray.MindrayMonitor;
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
	public static final int MINARAY = 0;

	/**
	 * 欧姆龙监测设备
	 */
	public static final int OMRON = 1;

	/**
	 * 获取接口
	 * 
	 * @param context
	 * @param sdk
	 *            实例化什么样的接口，参考该类静态变量
	 * @return
	 */
	public static ApiMonitor getApiMonitor(Context context, int sdk) {
		switch (sdk) {
		case OMRON:
			return new OmronMonitor(context);
		case MINARAY:
		default:
			return new MindrayMonitor(context);
		}
	}

}
