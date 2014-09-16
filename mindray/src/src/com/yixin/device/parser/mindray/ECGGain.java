package com.yixin.device.parser.mindray;

import java.util.List;

/**
 * 
 * @author xiaodeng
 *
 */
public class ECGGain {
	private int ECGGainLength;//
	public int getECGGainLength() {
		return ECGGainLength;
	}
	public void setECGGainLength(int eCGGainLength) {
		ECGGainLength = eCGGainLength;
	}
	public List<ECGWaveGain> getECGwavegain() {
		return ECGwavegain;
	}
	public void setECGwavegain(List<ECGWaveGain> eCGwavegain) {
		ECGwavegain = eCGwavegain;
	}
	public List<ECGWaveGainCoefficient> getECGwavegaincoefficient() {
		return ECGwavegaincoefficient;
	}
	public void setECGwavegaincoefficient(
			List<ECGWaveGainCoefficient> eCGwavegaincoefficient) {
		ECGwavegaincoefficient = eCGwavegaincoefficient;
	}
	private List<ECGWaveGain> ECGwavegain;//
	private List<ECGWaveGainCoefficient> ECGwavegaincoefficient;//
	/*
	 * 
	 */
	public class ECGWaveGain{
		private int iChannel;//	
		private int iGain;//
		public int getiChannel() {
			return iChannel;
		}
		public void setiChannel(int iChannel) {
			this.iChannel = iChannel;
		}
		public int getiGain() {
			return iGain;
		}
		public void setiGain(int iGain) {
			this.iGain = iGain;
		}
	}
	/*
	 * 濞夈垹鑸版晶鐐垫抄缁粯鏆�
	 */
	public class ECGWaveGainCoefficient{
		private int iChannel;//闁岸浜鹃崣锟�		
		private Float iGainCoef;//濞夈垹鑸版晶鐐垫抄缁粯鏆�
		public int getiChannel() {
			return iChannel;
		}
		public void setiChannel(int iChannel) {
			this.iChannel = iChannel;
		}
		public Float getiGainCoef() {
			return iGainCoef;
		}
		public void setiGainCoef(Float iGainCoef) {
			this.iGainCoef = iGainCoef;
		}
		
	}
	
}
