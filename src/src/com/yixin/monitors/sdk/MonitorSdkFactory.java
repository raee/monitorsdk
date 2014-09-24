package com.yixin.monitors.sdk;

import android.content.Context;
import android.util.SparseArray;

import com.yixin.monitors.sdk.api.ApiMonitor;
import com.yixin.monitors.sdk.mindray.MindrayMonitor;
import com.yixin.monitors.sdk.omron.OmronMonitor;
import com.yixin.monitors.sdk.test.TestMonitor;

/**
 * 监测设备工厂，由该工厂获取监测设备接口
 * 
 * @author ChenRui
 * 
 */
public final class MonitorSdkFactory {
	
	private static final SparseArray<ApiMonitor>	Monitors	= new SparseArray<ApiMonitor>();
	
	/**
	 * 迈瑞监测设备
	 */
	public static final int							MINDRAY		= 0;
	
	/**
	 * 欧姆龙监测设备
	 */
	public static final int							OMRON		= 1;
	
	public static final int							TEST		= -1;
	
	/**
	 * 获取接口
	 * 
	 * @param context
	 * @param sdk
	 *            实例化什么样的接口，参考该类静态变量
	 * @return
	 */
	public static ApiMonitor getApiMonitor(Context context, int sdk) {
		ApiMonitor result = Monitors.get(sdk);
		switch (sdk) {
			case OMRON:
				if (result == null) {
					result = new OmronMonitor(context);
					Monitors.put(sdk, result);
				}
				break;
			case TEST:
				if (result == null) {
					result = new TestMonitor();
					Monitors.put(sdk, result);
				}
				break;
			case MINDRAY:
			default:
				if (result == null) {
					result = new MindrayMonitor(context);
					Monitors.put(sdk, result);
				}
				break;
		}
		return result;
	}
	
}
