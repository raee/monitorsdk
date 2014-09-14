package com.yixin.monitors.sdk.mindray.parser;

import java.util.List;
/**
 * 测试参数数据
 * @author xiaodeng
 *
 */
public class TestParameterData {
	
	private int RealtimeLengthl;//
	private List<RealTimeData> dataList;//
	
	public int getRealtimeLengthl() {
		return RealtimeLengthl;
	}

	public void setRealtimeLengthl(int realtimeLengthl) {
		RealtimeLengthl = realtimeLengthl;
	}

	public List<RealTimeData> getDataList() {
		return dataList;
	}

	public void setDataList(List<RealTimeData> dataList) {
		this.dataList = dataList;
	}

	public class RealTimeData{
		private int iParaType;//
		private int iMultiple;//
		private int iDataType;//
		private int iParaData;//
		public int getiParaType() {
			return iParaType;
		}
		public void setiParaType(int iParaType) {
			this.iParaType = iParaType;
		}
		public int getiMultiple() {
			return iMultiple;
		}
		public void setiMultiple(int iMultiple) {
			this.iMultiple = iMultiple;
		}
		
		public int getiDataType() {
			return iDataType;
		}
		public void setiDataType(int iDataType) {
			this.iDataType = iDataType;
		}
		public int getiParaData() {
			return iParaData;
		}
		public void setiParaData(int iParaData) {
			this.iParaData = iParaData;
		}
		
	}
	
	
}
