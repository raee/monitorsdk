package com.yixin.device.core;

import com.yx.model.FinishPackageData;

/**
 * 主要，蓝牙接收数据回调接口
 * 
 * @author MrChenrui
 * 
 */
public interface DeviceReceviceCallback
{
	/**
	 * 接收到数据接口
	 * 
	 * @param data
	 */
	void onRecevieData(FinishPackageData data);
}
