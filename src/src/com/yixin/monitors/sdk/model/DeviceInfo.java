package com.yixin.monitors.sdk.model;

/**
 * 设备信息
 * 
 * @author ChenRui
 * 
 */
public class DeviceInfo {
	private String	deviceName;
	private String	devicePin;
	
	public void setDevicePin(String devicePin) {
		this.devicePin = devicePin;
	}
	
	public String getDevicePin() {
		return devicePin;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
}
