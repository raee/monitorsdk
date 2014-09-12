package com.yixin.monitors.sdk.model;

import java.io.Serializable;

/**
 * 体征数据实体
 * 
 * @author ChenRui
 * 
 */
public class SignDataModel implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3981448226950291143L;
	
	/**
	 * 数据类型名称
	 */
	private String				dataName;
	
	// 数据类型
	private int					dataType;
	
	/**
	 * 接收数据日期
	 */
	private String				date;
	
	/**
	 * 单位
	 */
	private String				unit;
	
	// 数据值
	private Object				value;
	
	/**
	 * 获取dataName
	 * 
	 * @return the dataName
	 */
	public String getDataName() {
		return dataName;
	}
	
	/**
	 * 获取dataType
	 * 
	 * @return the dataType
	 */
	public int getDataType() {
		return dataType;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getUnit() {
		return unit;
	}
	
	/**
	 * 获取value
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * 设置 dataName
	 * 
	 * @param dataName
	 *            设置 dataName 的值
	 */
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	
	/**
	 * 设置 dataType
	 * 
	 * @param dataType
	 *            设置 dataType 的值
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	/**
	 * 设置 value
	 * 
	 * @param value
	 *            设置 value 的值
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
}