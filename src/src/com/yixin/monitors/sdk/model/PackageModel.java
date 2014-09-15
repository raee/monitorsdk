package com.yixin.monitors.sdk.model;

import java.util.List;

/**
 * 每一次蓝牙上传的包数据
 * 
 * @author ChenRui
 * 
 */
public class PackageModel {
	
	private List<SignDataModel>	signDatas;
	
	/**
	 * 获取体征数据
	 * 
	 * @return
	 */
	public List<SignDataModel> getSignDatas() {
		return signDatas;
	}
	
	public void setSignDatas(List<SignDataModel> signDatas) {
		this.signDatas = signDatas;
	}
}
