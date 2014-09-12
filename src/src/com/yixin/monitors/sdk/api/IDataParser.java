package com.yixin.monitors.sdk.api;

import com.yixin.monitors.sdk.model.PackageModel;

/**
 * 蓝牙数据解析接口
 * 
 * @author ChenRui
 * 
 */
public interface IDataParser {
	/**
	 * 解析数据
	 * 
	 * @param data
	 *            数据
	 * @return
	 */
	PackageModel parse(byte[] data);
}
