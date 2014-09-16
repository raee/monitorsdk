package com.yixin.device.parser.mindray;
/**
 * 娉㈠舰鏁版嵁
 * @author xiaodeng
 *
 */
public class ECGWaveData {
	private int iWaveType;//濞夈垹鑸扮猾璇茬�
	private int iSampleRate;//闁插洦鐗遍悳锟�	
	private int iDataType;//濮ｅ繋閲滃▔銏犺埌閺佺増宓侀惃鍕殶閹诡喚琚崹锟�	
	private Object[] pWaveData;//濞夈垹鑸伴弫鐗堝祦
	public Object[] getpWaveData() {
		return pWaveData;
	}
	public void setpWaveData(Object[] pWaveData) {
		this.pWaveData = pWaveData;
	}
	public int getiWaveType() {
		return iWaveType;
	}
	public void setiWaveType(int iWaveType) {
		this.iWaveType = iWaveType;
	}
	public int getiSampleRate() {
		return iSampleRate;
	}
	public void setiSampleRate(int iSampleRate) {
		this.iSampleRate = iSampleRate;
	}
	public int getiDataType() {
		return iDataType;
	}
	public void setiDataType(int iDataType) {
		this.iDataType = iDataType;
	}
	
	
}
