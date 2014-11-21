package com.yixin.monitors.sdk;

import android.content.Context;
import com.yixin.monitors.sdk.api.ApiMonitor;
import com.yixin.monitors.sdk.manette.ManetteMonitor;
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

	// private static final SparseArray<ApiMonitor> Monitors = new
	// SparseArray<ApiMonitor>();

	/**
	 * 迈瑞监测设备
	 */
	public static final int	MINDRAY	= 0;

	/**
	 * 欧姆龙监测设备
	 */
	public static final int	OMRON	= 1;

	/**
	 * 测试设备
	 */
	public static final int	TEST	= -1;
	/**
	 * 玛奈特血糖
	 */
	public static final int	MANETTE	= 2;

	/**
	 * 获取接口，获取接口后调用configDevice(name,pin)来设置默认的设备名称和配对码。否则将会以默认的名称和配对码进行配对。
	 * 
	 * @param context
	 * @param sdk
	 *            实例化什么样的接口，参考该类静态变量
	 * @return
	 */
	public static ApiMonitor getApiMonitor(Context context, int sdk) {
		ApiMonitor result;
		switch (sdk) {
			case OMRON:
				result = getMonitor(sdk, new OmronMonitor(context));
				break;
			case TEST:
				result = getMonitor(sdk, new TestMonitor());
				break;
			case MANETTE:
				result = getMonitor(sdk, new ManetteMonitor(context));
				break;
			case MINDRAY:
				result = getMonitor(sdk, new MindrayMonitor(context));
				break;
			default:
				result = getMonitor(sdk, new MindrayMonitor(context));
				break;
		}
		return result;
	}

	private static ApiMonitor getMonitor(int sdk, ApiMonitor taget) {
		// ApiMonitor result = Monitors.get(sdk);
		// if (result == null) {
		// Monitors.put(sdk, taget);
		return taget;
		// }
		// else {
		// return result;
		// }
	}

}
