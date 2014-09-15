package com.yixin.monitors.sdk.model;

/**
 * 设备信息
 * 
 * @author ChenRui
 * 
 */
public class DeviceInfo {
	private String	devID;
	
	public String getDevID() {
		return devID;
	}
	
	public void setDevID(String devID) {
		this.devID = devID;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	private String	deviceName;
}
