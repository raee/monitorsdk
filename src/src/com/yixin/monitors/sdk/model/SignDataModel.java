package com.yixin.monitors.sdk.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 体征数据实体
 * 
 * @author ChenRui
 * 
 */
public class SignDataModel implements Parcelable {
	/**
	 * 
	 */
	//	private static final long	serialVersionUID	= 3981448226950291143L;
	
	public static Parcelable.Creator<SignDataModel>	CREATOR	= new Creator<SignDataModel>() {
																
																@Override
																public SignDataModel[] newArray(int size) {
																	return new SignDataModel[size];
																}
																
																@Override
																public SignDataModel createFromParcel(Parcel source) {
																	SignDataModel model = new SignDataModel();
																	source.readString();
																	model.setDataName(source.readString());
																	model.setValue(source.readString());
																	model.setUnit(source.readString());
																	model.setDate(source.readString());
																	model.setDataType(source.readInt());
																	
																	return model;
																}
															};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(dataName);
		dest.writeString(value);
		dest.writeString(unit);
		dest.writeString(date);
		dest.writeInt(dataType);
	}
	
	/**
	 * 数据类型名称
	 */
	private String	dataName;
	
	// 数据类型
	private int		dataType;
	
	/**
	 * 接收数据日期
	 */
	private String	date;
	
	/**
	 * 单位
	 */
	private String	unit;
	
	// 数据值
	private String	value;
	
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
	
	@SuppressLint("SimpleDateFormat")
	public String getDate() {
		if (date == null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.format(new Date());
		}
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
	public String getValue() {
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
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
}