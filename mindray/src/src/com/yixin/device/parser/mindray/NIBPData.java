package com.yixin.device.parser.mindray;

import java.util.List;
/**
 * NIBP测量数据
 * @author xiaodeng
 *
 */
public class NIBPData {
	private int NIBPLength;//测量数据结构大小
	private List<NIBPTestData> listData;//测量数据
	public int getNIBPLength() {
		return NIBPLength;
	}
	public void setNIBPLength(int nIBPLength) {
		NIBPLength = nIBPLength;
	}
	public List<NIBPTestData> getListData() {
		return listData;
	}
	public void setListData(List<NIBPTestData> listData) {
		this.listData = listData;
	}
	
	public class NIBPTestData{
		private String dtDataTime;//测量的日期时间
		public String getDtDataTime() {
			return dtDataTime;
		}
		public void setDtDataTime(String dtDataTime) {
			this.dtDataTime = dtDataTime;
		}
		private int iMultiple;//参数传输时的放大倍数
		private int iDataType;//参数数据类型
		private int iNIBP_S;//收缩压
		private int iNIBP_M;//平均压
		private int iNIBP_D;//舒张压
	

		public int getiMultiple() {
			return iMultiple;
		}

		public void setiMultiple(int iMyltiple) {
			this.iMultiple = iMyltiple;
		}

		public int getiDataType() {
			return iDataType;
		}

		public void setiDataType(int iDataType) {
			this.iDataType = iDataType;
		}

		public int getiNIBP_S() {
			return iNIBP_S;
		}

		public void setiNIBP_S(int iNIBP_S) {
			this.iNIBP_S = iNIBP_S;
		}

		public int getiNIBP_M() {
			return iNIBP_M;
		}

		public void setiNIBP_M(int iNIBP_M) {
			this.iNIBP_M = iNIBP_M;
		}

		public int getiNIBP_D() {
			return iNIBP_D;
		}
		public void setiNIBP_D(int iNIBP_D) {
			this.iNIBP_D = iNIBP_D;
		}
	}
	
}
