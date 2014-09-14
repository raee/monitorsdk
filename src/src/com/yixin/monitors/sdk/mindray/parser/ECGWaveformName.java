package com.yixin.monitors.sdk.mindray.parser;

import java.util.List;

/**
 * �������
 * @author xiaodeng
 *
 */
public class ECGWaveformName {
	private int waveNameLength;
	private List<WaveName> WaveNameList;
	
	public int getWaveNameLength() {
		return waveNameLength;
	}

	public void setWaveNameLength(int waveNameLength) {
		this.waveNameLength = waveNameLength;
	}

	public List<WaveName> getWaveNameList() {
		return WaveNameList;
	}

	public void setWaveNameList(List<WaveName> waveNameList) {
		WaveNameList = waveNameList;
	}

	public class WaveName{
		private int iChannel;//通道�?
		private int iWaveName;//波形名称
		public int getiChannel() {
			return iChannel;
		}
		public void setiChannel(int iChannel) {
			this.iChannel = iChannel;
		}
		public int getiWaveName() {
			return iWaveName;
		}
		public void setiWaveName(int iWaveName) {
			this.iWaveName = iWaveName;
		}
	}
	
	
}
