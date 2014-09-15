package com.yixin.monitors.sdk.mindray.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.util.Log;

import com.yixin.monitors.sdk.mindray.parser.CMSPacket.Field;
import com.yixin.monitors.sdk.mindray.parser.ECGFilteringWay.FilteringWayData;
import com.yixin.monitors.sdk.mindray.parser.ECGGain.ECGWaveGain;
import com.yixin.monitors.sdk.mindray.parser.ECGGain.ECGWaveGainCoefficient;
import com.yixin.monitors.sdk.mindray.parser.ECGWaveformName.WaveName;
import com.yixin.monitors.sdk.mindray.parser.NIBPData.NIBPTestData;
import com.yixin.monitors.sdk.mindray.parser.TestParameterData.RealTimeData;
import com.yx.model.FinishPackageData;
import com.yx.model.FinishPackageData.litPackDataType;

/**
 * 解析cms包数据
 * 
 * @author xiaodeng
 * 
 */
public class ParseCMSPacket implements PackageParseIntf {
	final static String tag = "CMS";
	public Map<String, ECGGenelarData> gdMap;
	public List<ECGGenelarData> ECGgdlist;
	List<Object[]> ECGWaveData_1 = new ArrayList<Object[]>();
	List<Object[]> ECGWaveData_2 = new ArrayList<Object[]>();
	List<Object[]> ECGWaveData_3 = new ArrayList<Object[]>();

	private CMSPacketStream stream;

	/*
	 * 解析整个包
	 */
	@Override
	public FinishPackageData parsePacket(CMSPacketStream mStream) {
		this.stream = mStream;
		ECGLeadType generalData = null;
		ECGFilteringWay filteringWay = null;
		ECGGain gain = null;
		List<ECGWaveData> wdList = null;
		ECGWaveformName waveName = null;
		List<CMSPacket> packets = stream.getPackets();
		String minitor_pack_id = UUID.randomUUID().toString();
		FinishPackageData finishData = new FinishPackageData();
		List<litPackDataType> litPack = new ArrayList<FinishPackageData.litPackDataType>();
		for (int i = 0; i < packets.size(); i++) {
			CMSPacket pack = packets.get(i);
			if (pack.getCommandID() == CMSDefine.CMD_ID_MEASURE_PARA_LIST) {
				TestParameterData testData = parseTestParameterData(pack);
				if (testData.getDataList().size() != 0) {
					Log.w("tag", "SaveTestParameterData");
					List<litPackDataType> litTest = CMSTransformYXData
							.testParameterDataTransformDefiniteData(finishData,
									testData);
					copyListPack(litTest, litPack);
				}

			} else if (pack.getCommandID() == CMSDefine.CMD_ID_NIBP_DATA) {
				NIBPData nibpData = parseNIBPData(pack);
				List<litPackDataType> litNibp = CMSTransformYXData
						.NIBPDataTransformDefiniteData(finishData, nibpData);
				if (litNibp != null) {
					copyListPack(litNibp, litPack);
				}

			} else if (pack.getCommandID() == CMSDefine.CMD_ID_ECG_GENERAL_SET) {
				generalData = parseGeneralECG(pack);

			} else if (pack.getCommandID() == CMSDefine.CMD_ID_ECG_FILTER_TYPE) {
				filteringWay = parseECGFilteringWay(pack);
				for (int l = 0; l < filteringWay.getListData().size(); l++) {
					System.out.println("FilteringWayLength:"
							+ filteringWay.getECGFilteringWayLength()
							+ " iChanel:"
							+ filteringWay.getListData().get(l).getiChannel()
							+ " iFilterType:"
							+ filteringWay.getListData().get(l)
									.getiFilterType());
				}
			} else if (pack.getCommandID() == CMSDefine.CMD_ID_ECG_GAIN) {
				gain = parseECGGain(pack);
			} else if (pack.getCommandID() == CMSDefine.CMD_ID_WAVE_DATA) {
				wdList = parseECGwavedata(pack);
			} else if (pack.getCommandID() == CMSDefine.CMD_ID_WAVE_NAME) {
				waveName = parseWaveFormName(pack);
				for (int l = 0; l < waveName.getWaveNameList().size(); l++) {
					System.out.println("WaveNameLength:"
							+ waveName.getWaveNameLength() + " iChanel:"
							+ waveName.getWaveNameList().get(l).getiChannel()
							+ " iWaveName:"
							+ waveName.getWaveNameList().get(l).getiWaveName());
				}
			}

			if ((packets.size() - 1) == i) {
				List<ECGGenelarData> litECGData = parseGenelerData(
						minitor_pack_id, generalData, filteringWay, gain,
						wdList, waveName, true);
				List<litPackDataType> litECG = CMSTransformYXData
						.eCGDataTransformDefiniteEcgData(finishData, litECGData);
				if (litECG != null) {
					copyListPack(litECG, litPack);
				}

			}
		}

		finishData.setLitPackDataType(litPack);
		return finishData;
	}

	private List<litPackDataType> copyListPack(List<litPackDataType> srcList,
			List<litPackDataType> fromList) {
		List<litPackDataType> litPack = fromList;
		for (litPackDataType litPackDataType : srcList) {
			litPack.add(litPackDataType);
		}
		return litPack;
	}

	/**
	 * 解析整个心电包
	 * 
	 * @param generalData
	 * @param filteringWay
	 * @param gain
	 * @param edList
	 * @param waveName
	 * @param isEnd
	 */
	public List<ECGGenelarData> parseGenelerData(String minitor_pack_id,
			ECGLeadType generalData, ECGFilteringWay filteringWay,
			ECGGain gain, List<ECGWaveData> edList, ECGWaveformName waveName,
			Boolean isEnd) {
		ECGgdlist = judgeIndexGenelerData(generalData.getECGLeadType());
		if (generalData.getECGLeadType() == 1) {
			for (int i = 0; i < edList.size(); i++) {
				for (int j = 1; j < 4; j++) {
					if (edList.get(i).getiWaveType() == j) {
						ECGgdlist.get(j - 1).setECGLeadType(
								generalData.getECGLeadType());
						ECGgdlist.get(j - 1)
								.setiChannel(
										filteringWay.getListData().get(i)
												.getiChannel());
						ECGgdlist.get(j - 1).setiFilterType(
								filteringWay.getListData().get(i)
										.getiFilterType());
						ECGgdlist.get(j - 1).setiWaveName(
								waveName.getWaveNameList().get(i)
										.getiWaveName());
						ECGgdlist.get(j - 1).setECGwavegain(
								gain.getECGwavegain().get(i).getiGain());
						ECGgdlist.get(j - 1).setECGwavegaincoefficient(
								gain.getECGwavegaincoefficient().get(i)
										.getiGainCoef());
						ECGgdlist.get(j - 1).setiSampleRate(
								edList.get(i).getiSampleRate());
						ECGgdlist.get(j - 1).setiDataType(
								edList.get(i).getiDataType());
						switch (j) {
						case 1:
							ECGWaveData_1.add(edList.get(i).getpWaveData());
							break;
						case 2:
							ECGWaveData_2.add(edList.get(i).getpWaveData());
							break;
						case 3:
							ECGWaveData_3.add(edList.get(i).getpWaveData());
							break;
						default:
							break;
						}
					}
				}

			}

		}

		if (isEnd) {
			ECGgdlist.get(0).setECGWaveData(ECGWaveData_1);
			ECGgdlist.get(1).setECGWaveData(ECGWaveData_2);
			ECGgdlist.get(2).setECGWaveData(ECGWaveData_3);
			return ECGgdlist;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 有多少导联就返回多少个心电modle
	 * 
	 * @param LeadType
	 */
	public List<ECGGenelarData> judgeIndexGenelerData(int LeadType) {
		List<ECGGenelarData> gdList = new ArrayList<ECGGenelarData>();
		if (LeadType == CMSDefine.ECG_LEADTYPE_3) {
			for (int i = 0; i < 3; i++) {
				ECGGenelarData gd_1 = new ECGGenelarData();
				gdList.add(gd_1);
			}

			return gdList;
		} else if (LeadType == CMSDefine.ECG_LEADTYPE_5) {
			for (int i = 0; i < 5; i++) {
				ECGGenelarData gd = new ECGGenelarData();
				gdList.add(gd);
			}
		} else if (LeadType == CMSDefine.ECG_LEADTYPE_12) {
			for (int i = 0; i < 12; i++) {
				ECGGenelarData gd = new ECGGenelarData();
				gdList.add(gd);
			}
			return gdList;
		}
		return null;
	}

	/**
	 * 
	 * 解析波形名称
	 * 
	 * @param pack
	 * @return
	 */
	public ECGWaveformName parseWaveFormName(CMSPacket pack) {
		ECGWaveformName waveName = new ECGWaveformName();
		Iterator<?> itr = pack.fileds.iterator();
		while (itr.hasNext()) {
			CMSPacket.Field field = (Field) itr.next();
			if (field != null) {
				if (field.id == CMSDefine.FIELD_ID_WAVE_NAME_SIZE) {
					waveName.setWaveNameLength(CMSPacket.bufferToByte(
							field.value, 0));
				} else if (field.id == CMSDefine.FIELD_ID_WAVE_NAME) {
					List<WaveName> waveNameList = new ArrayList<WaveName>();
					for (int i = 0; i < waveName.getWaveNameLength(); i++) {
						WaveName wave = waveName.new WaveName();
						wave.setiChannel(CMSPacket.bufferToByte(field.value,
								i * 2 + 0));
						wave.setiWaveName(CMSPacket.bufferToByte(field.value,
								i * 2 + 1));
						waveNameList.add(wave);
					}
					waveName.setWaveNameList(waveNameList);
				}
			}
		}
		if (waveName.getWaveNameList() != null) {
			return waveName;
		}
		return null;
	}

	/*
	 * 
	 */
	public TestParameterData parseTestParameterData(CMSPacket pack) {
		Iterator<?> itr = pack.fileds.iterator();
		TestParameterData testData = new TestParameterData();
		while (itr.hasNext()) {
			CMSPacket.Field field = (Field) itr.next();
			if (field != null) {
				if (field.id == CMSDefine.FIELD_ID_PARA_DATA_SIZE) {
					testData.setRealtimeLengthl(CMSPacket.bufferToByte(
							field.value, 0));
				} else if (field.id == CMSDefine.FIELD_ID_PARA_DATA) {
					List<RealTimeData> dataList = new ArrayList<RealTimeData>();
					for (int i = 0; i < testData.getRealtimeLengthl(); i++) {
						RealTimeData realData = testData.new RealTimeData();
						realData.setiParaType(CMSPacket.bufferToByte(
								field.value, i * 5 + 0));
						realData.setiMultiple(CMSPacket.bufferToByte(
								field.value, i * 5 + 1));
						realData.setiDataType(CMSPacket.bufferToByte(
								field.value, i * 5 + 2));
						realData.setiParaData(CMSPacket.bufferToShort(
								field.value, i * 5 + 3));
						dataList.add(realData);
					}
					testData.setDataList(dataList);

				}

			}
		}
		if (testData.getDataList().size() <= 0) {
			return null;
		} else {
			return testData;
		}

	}

	/*
	 * 解析nibp数据
	 */
	public NIBPData parseNIBPData(CMSPacket pack) {
		Iterator<?> itr = pack.fileds.iterator();
		NIBPData nibpData = new NIBPData();
		while (itr.hasNext()) {
			CMSPacket.Field field = (Field) itr.next();
			if (field != null) {
				if (field.id == CMSDefine.FIELD_ID_NIBP_DATA_SIZE) {
					nibpData.setNIBPLength(CMSPacket.bufferToByte(field.value,
							0));
				} else if (field.id == CMSDefine.FIELD_ID_NIBP_DATA) {
					List<NIBPTestData> dataList = new ArrayList<NIBPTestData>();
					NIBPTestData nibpTestData = nibpData.new NIBPTestData();
					nibpTestData.setDtDataTime(this.transformDate(field.value,
							0));
					nibpTestData.setiMultiple(CMSPacket.bufferToByte(
							field.value, 7));
					nibpTestData.setiDataType(CMSPacket.bufferToByte(
							field.value, 8));
					nibpTestData.setiNIBP_S(CMSPacket.bufferToShort(
							field.value, 9));
					nibpTestData.setiNIBP_M(CMSPacket.bufferToShort(
							field.value, 11));
					nibpTestData.setiNIBP_D(CMSPacket.bufferToShort(
							field.value, 13));
					dataList.add(nibpTestData);

					nibpData.setListData(dataList);
				}

			}
		}
		if (nibpData.getListData().size() <= 0) {
			return null;
		} else {
			return nibpData;
		}
	}

	/*
	 * 解析导联类型
	 */
	public ECGLeadType parseGeneralECG(CMSPacket pack) {
		ECGLeadType ecgdata = new ECGLeadType();
		Iterator<?> itr = pack.fileds.iterator();
		while (itr.hasNext()) {
			CMSPacket.Field field = (Field) itr.next();
			if (field != null) {
				if (field.id == CMSDefine.FIELD_ID_ECG_LEAD_TYPE) {
					ecgdata.setECGLeadType(CMSPacket.bufferToByte(field.value,
							0));
				}
			}
		}
		return ecgdata;
	}

	/*
	 * 
	 */
	public ECGFilteringWay parseECGFilteringWay(CMSPacket pack) {
		ECGFilteringWay ECGdata = new ECGFilteringWay();
		Iterator<?> itr = pack.fileds.iterator();
		while (itr.hasNext()) {
			CMSPacket.Field field = (Field) itr.next();
			if (field != null) {
				if (field.id == CMSDefine.FIELD_ID_ECG_FILTER_SIZE) {
					ECGdata.setECGFilteringWayLength(CMSPacket.bufferToByte(
							field.value, 0));
				} else if (field.id == CMSDefine.FIELD_ID_ECG_FILTER_TYPE) {
					List<FilteringWayData> listdata = new ArrayList<FilteringWayData>();
					for (int i = 0; i < 3; i++) {
						FilteringWayData waydata = ECGdata.new FilteringWayData();
						waydata.setiChannel(CMSPacket.bufferToByte(field.value,
								i * 2 + 0));
						waydata.setiFilterType(CMSPacket.bufferToByte(
								field.value, i * 2 + 1));
						listdata.add(waydata);
					}
					ECGdata.setListData(listdata);
				}
			}
		}
		if (ECGdata.getListData().size() <= 0) {
			return null;
		}
		return ECGdata;
	}

	/*
	 * 
	 */
	public ECGGain parseECGGain(CMSPacket pack) {
		ECGGain ecgGain = new ECGGain();
		Iterator<?> itr = pack.fileds.iterator();
		while (itr.hasNext()) {
			CMSPacket.Field field = (Field) itr.next();
			if (field != null) {
				if (field.id == CMSDefine.FIELD_ID_ECG_GAIN_SIZE) {
					ecgGain.setECGGainLength(CMSPacket.bufferToByte(
							field.value, 0));
				} else if (field.id == CMSDefine.FIELD_ID_ECG_GAIN) {
					List<ECGWaveGain> waveGainList = new ArrayList<ECGWaveGain>();
					for (int i = 0; i < ecgGain.getECGGainLength(); i++) {
						ECGWaveGain waveGain = ecgGain.new ECGWaveGain();
						waveGain.setiChannel(CMSPacket.bufferToByte(
								field.value, i * 2 + 0));
						waveGain.setiGain(CMSPacket.bufferToByte(field.value,
								i * 2 + 1));
						waveGainList.add(waveGain);
					}
					ecgGain.setECGwavegain(waveGainList);
				} else if (field.id == CMSDefine.FIELD_ID_ECG_GAIN_FACTOR) {
					List<ECGWaveGainCoefficient> coefficientList = new ArrayList<ECGWaveGainCoefficient>();
					for (int i = 0; i < ecgGain.getECGGainLength(); i++) {
						ECGWaveGainCoefficient coefficient = ecgGain.new ECGWaveGainCoefficient();
						coefficient.setiChannel(CMSPacket.bufferToByte(
								field.value, i * 2 + 0));
						coefficient.setiGainCoef(CMSPacket.bufferToFloat(
								field.value, i * 2 + 1));
						coefficientList.add(coefficient);
					}
					ecgGain.setECGwavegaincoefficient(coefficientList);
				}
			}
		}
		if (ecgGain.getECGGainLength() <= 0) {
			return null;
		}
		return ecgGain;
	}

	/**
	 * 
	 * 
	 * @param pack
	 * @return
	 */
	public List<ECGWaveData> parseECGwavedata(CMSPacket pack) {
		List<ECGWaveData> wdList = new ArrayList<ECGWaveData>();
		Iterator<?> itr = pack.fileds.iterator();
		while (itr.hasNext()) {
			CMSPacket.Field field = (Field) itr.next();
			if (field != null && field.length > 0) {
				if (field.id == CMSDefine.FIELD_ID_WAVE_DATA) {
					int startLength = 0;
					if (startLength < field.length) {

						ECGWaveData ecgdata = new ECGWaveData();
						ecgdata.setiWaveType(CMSPacket.bufferToByte(
								field.value, startLength + 0));
						ecgdata.setiSampleRate(CMSPacket.bufferToShort(
								field.value, startLength + 1));
						ecgdata.setiDataType(CMSPacket.bufferToByte(
								field.value, startLength + 3));

						if (ecgdata.getiSampleRate() > 0) {
							byte[] DataValue = new byte[ecgdata
									.getiSampleRate()];
							System.arraycopy(field.value, startLength + 4,
									DataValue, 0, ecgdata.getiSampleRate());
							Object o[] = judgeType(DataValue,
									ecgdata.getiDataType());
							ecgdata.setpWaveData(o);
						}
						startLength += ecgdata.getiSampleRate() + 4;
						wdList.add(ecgdata);
					}

				}
			}

		}
		if (wdList.size() != 0) {
			return wdList;
		}
		return null;
	}

	/*
	 * 
	 */
	public Object[] judgeType(byte[] DataValue, int dataType) {
		int gainIndex = 10000;
		if (dataType == 0) {
			return null;
		} else if (dataType == 1) {
			Object[] list = new Object[(DataValue.length / 2)];
			int index = 0;
			for (int i = 0; i < DataValue.length; i++) {
				int idata = CMSPacket.bufferToByte(DataValue, i + 0);
				list[index] = idata;
				index++;
			}
			return list;
		} else if (dataType == 2) {
			Object[] list = new Object[DataValue.length];
			int index = 0;
			for (int i = 0; i < DataValue.length; i++) {
				int idata = CMSPacket.bufferToByte(DataValue, i + 0);
				list[index] = idata;
				index++;
			}
			return list;
		} else if (dataType == 3) {
			Object[] list = new Object[DataValue.length];
			int index = 0;
			for (int i = 0; i < DataValue.length; i++) {
				int idata = CMSPacket.bufferTobool(DataValue, i + 0);
				list[index] = idata;
				index++;
			}
			return list;
		} else if (dataType == 4) {
			Object[] list = new Object[(DataValue.length / 2)];
			int index = 0;
			for (int i = 0; i < DataValue.length; i = i + 2) {
				Short idata = (CMSPacket.bufferToShort(DataValue, i + 0));
				double data = idata;
				list[index] = data / gainIndex;
				index++;
			}
			return list;
		} else if (dataType == 5) {
			Object[] list = new Object[DataValue.length];
			int index = 0;
			for (int i = 0; i < DataValue.length; i = i + 2) {
				// 閺堝顑侀崣椋庣叚閺佹潙鐎�
				Short idata = CMSPacket.bufferToShort(DataValue, i + 0);
				list[index] = idata;
				index++;
			}
			return list;
		} else if (dataType == 6) {
			Object[] list = new Object[DataValue.length];
			int index = 0;
			for (int i = 0; i < DataValue.length; i = i + 4) {
				int idata = CMSPacket.bufferToInt(DataValue, i + 0);
				list[index] = idata;
				index++;
			}
			return list;
		} else if (dataType == 7) {
			Object[] list = new Object[DataValue.length];
			int index = 0;
			for (int i = 0; i < DataValue.length; i = i + 4) {
				int idata = CMSPacket.bufferToInt(DataValue, i + 0);
				list[index] = idata;
				index++;
			}
			return list;
		} else if (dataType == 8) {
			Object[] list = new Object[DataValue.length];
			int index = 0;
			for (int i = 0; i < DataValue.length; i++) {
				char idata = CMSPacket.buffertoChar(DataValue, i + 0);
				list[index] = idata;
				index++;
			}
			return list;
		} else if (dataType == 10) {
			Object[] list = new Object[DataValue.length];
			int index = 0;
			for (int i = 0; i < DataValue.length; i = i + 4) {
				Double idata = CMSPacket.buffertoDouble(DataValue, i + 0);
				list[index] = idata;
				index++;
			}
			return list;
		} else if (dataType == 11) {
			Object[] list = new Object[DataValue.length];
			int index = 0;
			for (int i = 0; i < DataValue.length; i = i + 8) {
				Double idata = CMSPacket.buffertoDouble(DataValue, i + 0);
				list[index] = idata;
				index++;
			}
			return list;
		} else {
			return null;
		}

	}

	/*
	 * 
	 */
	public String transformDate(byte[] data, int startLength) {
		if (data.length < startLength + 7) {
			return "";
		}
		int year = CMSPacket.bufferToShort(data, startLength + 0);
		int month = CMSPacket.bufferToByte(data, startLength + 2);
		int day = CMSPacket.bufferToByte(data, startLength + 3);
		int hour = CMSPacket.bufferToByte(data, startLength + 4);
		int minute = CMSPacket.bufferToByte(data, startLength + 5);
		int second = CMSPacket.bufferToByte(data, startLength + 6);

		return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":"
				+ second;
	}
}
