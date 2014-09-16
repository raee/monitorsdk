package com.yixin.device.parser.mindray;

import java.util.List;

/**
 * 
 * @author xiaodeng
 *
 */
public class ECGFilteringWay {
	private int ECGFilteringWayLength;
	private List<FilteringWayData> listData;
	
	public int getECGFilteringWayLength() {
		return ECGFilteringWayLength;
	}

	public void setECGFilteringWayLength(int eCGFilteringWayLength) {
		ECGFilteringWayLength = eCGFilteringWayLength;
	}
	
	public List<FilteringWayData> getListData() {
		return listData;
	}

	public void setListData(List<FilteringWayData> listData) {
		this.listData = listData;
	}


	public class FilteringWayData{
		private int iChannel;//	
		private int iFilterType;//
		
		public int getiChannel() {
			return iChannel;
		}
		public void setiChannel(int iChannel) {
			this.iChannel = iChannel;
		}
		public int getiFilterType() {
			return iFilterType;
		}
		public void setiFilterType(int iFilterType) {
			this.iFilterType = iFilterType;
		}
	}

	
}
