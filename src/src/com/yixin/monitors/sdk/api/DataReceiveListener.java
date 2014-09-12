package com.yixin.monitors.sdk.api;

import com.yixin.monitors.sdk.model.PackageModel;

/**
 * 蓝牙数据接收并解析完成后回调接口
 * 
 * @author ChenRui
 * 
 */
public interface DataReceiveListener {
	
	/**
	 * 回调结果
	 * 
	 * @param model
	 *            数据
	 */
	void onReceived(PackageModel model);
}
