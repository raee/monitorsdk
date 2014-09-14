package com.yixin.monitors.sdk.mindray.parser;

import java.util.List;

public class ECGGenelarData {
	private int iChannel;//ͨ����
	private int ECGLeadType;// ��������
	private int iFilterType; // �˲���ʽ
	private int iWaveName; // �˲����
	private int ECGwavegain; // ��������
	private Float ECGwavegaincoefficient;// �������淽ʽ
	private int iSampleRate;// ������
	private int iDataType;// �����������
	private List<Object[]> ECGWaveData; // �������

	public List<Object[]> getECGWaveData() {
		return ECGWaveData;
	}

	public void setECGWaveData(List<Object[]> eCGWaveData) {
		ECGWaveData = eCGWaveData;
	}

	public int getECGLeadType() {
		return ECGLeadType;
	}

	public void setECGLeadType(int eCGLeadType) {
		ECGLeadType = eCGLeadType;
	}

	public int getiChannel() {
		return iChannel;
	}

	public int getiFilterType() {
		return iFilterType;
	}

	public void setiFilterType(int iFilterType) {
		this.iFilterType = iFilterType;
	}

	public int getiWaveName() {
		return iWaveName;
	}

	public void setiWaveName(int iWaveName) {
		this.iWaveName = iWaveName;
	}

	public int getECGwavegain() {
		return ECGwavegain;
	}

	public void setECGwavegain(int eCGwavegain) {
		ECGwavegain = eCGwavegain;
	}

	public Float getECGwavegaincoefficient() {
		return ECGwavegaincoefficient;
	}

	public void setECGwavegaincoefficient(Float eCGwavegaincoefficient) {
		ECGwavegaincoefficient = eCGwavegaincoefficient;
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

	public void setiChannel(int iChannel) {
		this.iChannel = iChannel;
	}


}
