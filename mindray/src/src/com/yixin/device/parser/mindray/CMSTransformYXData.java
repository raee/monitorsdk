package com.yixin.device.parser.mindray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;

import com.yixin.device.parser.mindray.NIBPData.NIBPTestData;
import com.yixin.device.parser.mindray.TestParameterData.RealTimeData;
import com.yx.model.DefiniteData;
import com.yx.model.DefiniteEcgData;
import com.yx.model.FinishPackageData;
import com.yx.model.FinishPackageData.litPackDataType;
import com.yx.model.PackageDatatype;

public class CMSTransformYXData {

	private static String[] itemsName = { "收缩压", "平均压", "舒张压", "脉搏", "体温",
			"血氧", "心电" };
	private static String[] itemsUnit = { "mmHg", "博/分", "°C", "%", "" };
	private static String[] dataType = { "NIBP", "Pulse", "ECG", "Oxygen",
			"Temperature" };
	private static String deviceName = "MINDRAY";

	/**
	 * 
	 * 转换设备的测量数据
	 * @param data
	 * @return
	 */
	public static List<litPackDataType> testParameterDataTransformDefiniteData(
			FinishPackageData packData, TestParameterData data) {
		List<litPackDataType> litPackDataType = new ArrayList<litPackDataType>();
		List<RealTimeData> list = data.getDataList();
		Iterator<?> itr = list.iterator();

		int NIBP_S = 0;
		int NIBP_M = 0;
		int NIBP_D = 0;
		while (itr.hasNext()) {
			RealTimeData realTimeData = (RealTimeData) itr.next();
			int dataValue = realTimeData.getiParaData();
			DefiniteData dd = null;
			litPackDataType litpackdatatype = null;
			PackageDatatype dataType = null;
			List<DefiniteData> litDD = null;
			if (dataValue == -100) {
				continue;
			}
			switch (realTimeData.getiParaType()) {
			case UbicareDefine.PARAMETER_TYPE_TEMP_T1:
				litpackdatatype = packData.new litPackDataType();
				litDD = new ArrayList<DefiniteData>();
				dd = getDefiniteData(4,
						String.valueOf(Double.valueOf(dataValue) / 10), 2);
				litDD.add(dd);
				dataType = getPackageDatatype(
						4,
						getTemperatureMonitorResult(Double.valueOf(dataValue) / 10),
						"", 0, getNowDate(), deviceName);
				litpackdatatype.setLitDefiniteData(litDD);
				litpackdatatype.setPackDataType(dataType);
				litPackDataType.add(litpackdatatype);

				break;

			case UbicareDefine.PARAMETER_TYPE_ECG_HR:
				litpackdatatype = packData.new litPackDataType();
				litDD = new ArrayList<DefiniteData>();
				dd = getDefiniteData(3, String.valueOf(dataValue), 1);
				litDD.add(dd);
				dataType = getPackageDatatype(1,
						getPulseMonitorResult(dataValue), "", 0, getNowDate(),
						deviceName);
				litpackdatatype.setLitDefiniteData(litDD);
				litpackdatatype.setPackDataType(dataType);
				litPackDataType.add(litpackdatatype);

				break;
			case UbicareDefine.PARAMETER_TYPE_SPO2_SPO2:
				litpackdatatype = packData.new litPackDataType();
				litDD = new ArrayList<DefiniteData>();
				dd = getDefiniteData(5, String.valueOf(dataValue), 3);
				litDD.add(dd);
				dataType = getPackageDatatype(3,
						getOxygenMonitorResult(dataValue), "", 0, getNowDate(),
						deviceName);
				litpackdatatype.setLitDefiniteData(litDD);
				litpackdatatype.setPackDataType(dataType);
				litPackDataType.add(litpackdatatype);
				break;
			case UbicareDefine.PARAMETER_TYPE_NIBP_NIBP_S:
				NIBP_S = dataValue;
				break;
			case UbicareDefine.PARAMETER_TYPE_NIBP_NIBP_M:
				NIBP_M = dataValue;
				break;
			case UbicareDefine.PARAMETER_TYPE_NIBP_NIBP_D:
				NIBP_D = dataValue;
				break;

			default:
				break;
			}
		}
		if (NIBP_S != 0 && NIBP_D != 0) {
			litPackDataType litpackdatatype = packData.new litPackDataType();
			List<DefiniteData> litDD = new ArrayList<DefiniteData>();
			DefiniteData DDS = getDefiniteData(0, String.valueOf(NIBP_S), 0);
			DefiniteData DDM = getDefiniteData(1, String.valueOf(NIBP_M), 0);
			DefiniteData DDD = getDefiniteData(2, String.valueOf(NIBP_M), 0);
			litDD.add(DDS);
			litDD.add(DDM);
			litDD.add(DDD);
			PackageDatatype dataType = getPackageDatatype(0,
					getNIBPMonitorResult(NIBP_S), "", 0, getNowDate(),
					deviceName);
			litpackdatatype.setLitDefiniteData(litDD);
			litpackdatatype.setPackDataType(dataType);
			litPackDataType.add(litpackdatatype);
		}
		return litPackDataType;
	}

	/**
	 * 转换成NIBP数据
	 * 
	 * @param data
	 * @return
	 */
	public static List<litPackDataType> NIBPDataTransformDefiniteData(
			FinishPackageData packData, NIBPData nibpData) {
		if (nibpData.getListData().get(0).getiNIBP_D() == -100) {
			return null;
		}
		List<litPackDataType> litPackDataType = new ArrayList<litPackDataType>();
		List<NIBPTestData> list = nibpData.getListData();
		Iterator<?> itr = list.iterator();
		while (itr.hasNext()) {
			NIBPTestData testData = (NIBPTestData) itr.next();
			int iNIBP_S = testData.getiNIBP_S();
			int iNIBP_M = testData.getiNIBP_M();
			int iNIBP_D = testData.getiNIBP_D();
			litPackDataType litpackdatatype = packData.new litPackDataType();
			List<DefiniteData> litDD = new ArrayList<DefiniteData>();
			DefiniteData DDS = getDefiniteData(0, String.valueOf(iNIBP_S), 0);
			DefiniteData DDM = getDefiniteData(1, String.valueOf(iNIBP_M), 0);
			DefiniteData DDD = getDefiniteData(2, String.valueOf(iNIBP_D), 0);
			litDD.add(DDS);
			litDD.add(DDM);
			litDD.add(DDD);
			PackageDatatype dataType = getPackageDatatype(0,
					getNIBPMonitorResult(iNIBP_S), "", 0, getNowDate(),
					deviceName);
			litpackdatatype.setLitDefiniteData(litDD);
			litpackdatatype.setPackDataType(dataType);
			litPackDataType.add(litpackdatatype);
		}
		return litPackDataType;
	}

	/**
	 * 转换成ECG数据
	 * 
	 * @param data
	 * @return
	 */
	public static List<litPackDataType> eCGDataTransformDefiniteEcgData(
			FinishPackageData packData, List<ECGGenelarData> litECGData) {
		List<litPackDataType> litPackDataType = new ArrayList<litPackDataType>();
		litPackDataType packdatatype = packData.new litPackDataType();
		List<DefiniteEcgData> litDD = new ArrayList<DefiniteEcgData>();
		Iterator<ECGGenelarData> it = litECGData.iterator();
		while (it.hasNext()) {
			ECGGenelarData ecgData = it.next();
			if (ecgData.getECGWaveData().size() != 0) {
				StringBuffer ecgStrbf = new StringBuffer();

				for (int i = 0; i < ecgData.getECGWaveData().size(); i++) {
					Object[] ob = ecgData.getECGWaveData().get(i);
					boolean isEmpty = true;
					for (Object object : ob) {
						if ((Double) object != 0.2048) {
							isEmpty = false;
							break;
						}
					}
					if (!isEmpty) {
						for (int j = 0; j < ob.length; j++) {
							ecgStrbf.append(ob[j]).append(",");
						}
					}

				}
				if (!"".equals(ecgStrbf.toString())) {
					DefiniteEcgData ded = getDefiniteEcgData(ecgData, ecgStrbf);
					litDD.add(ded);
				}

			}
		}

		if (litDD.size() > 0) {
			PackageDatatype dataType = getPackageDatatype(2, "", "", 1,
					getNowDate(), deviceName);
			packdatatype.setPackDataType(dataType);
			packdatatype.setLitECGData(litDD);
			litPackDataType.add(packdatatype);
			return litPackDataType;
		} else {
			return null;
		}

	}
	/**
	 * 得到ecgData
	 * @param ecgData
	 * @param ecgStrbf
	 * @return
	 */
	private static DefiniteEcgData getDefiniteEcgData(ECGGenelarData ecgData,
			StringBuffer ecgStrbf) {
		DefiniteEcgData ded = new DefiniteEcgData();
		ded.setChannelNum(ecgData.getiChannel());
		ded.setLeadType(ecgData.getECGLeadType());
		ded.setFilterType(String.valueOf(ecgData.getiFilterType()));
		ded.setWaveName(String.valueOf(ecgData.getiWaveName()));
		ded.setWaveGain(ecgData.getECGwavegain());
		ded.setWaveGainType(String.valueOf(ecgData.getECGwavegaincoefficient()));
		ded.setSampleRate(ecgData.getiSampleRate());
		ded.setDataType(ecgData.getiDataType());
		ded.setEcgData(ecgStrbf.toString());
		return ded;
	}

	/**
	 * 得到DefiniteData
	 * 
	 * @param itemName
	 * @param dataItemValue
	 * @param dataItemUnit
	 * @return
	 */
	private static DefiniteData getDefiniteData(int itemName,
			String dataItemValue, int dataItemUnit) {
		DefiniteData dd = new DefiniteData();
		dd.setItemTypeName(itemsName[itemName]);
		dd.setDataItemValue(dataItemValue);
		dd.setDataItemUnit(itemsUnit[dataItemUnit]);
		return dd;
	}

	/**
	 * 得到PackageDatatype
	 * 
	 * @param dataTypeName
	 * @param monitorResult
	 * @param monitorResulDescribe
	 * @param isEcgdata
	 * @param receiveDate
	 * @param deviceName
	 * @return
	 */
	private static PackageDatatype getPackageDatatype(int dataTypeName,
			String monitorResult, String monitorResulDescribe, int isEcgdata,
			String receiveDate, String deviceName) {
		PackageDatatype pdt = new PackageDatatype();
		pdt.setDataTypeName(dataType[dataTypeName]);
		pdt.setMonitorResult(monitorResult);
		pdt.setMonitorResulDescribe(monitorResulDescribe);
		pdt.setIsEcgdata(isEcgdata);
		pdt.setReceiveDate(receiveDate);
		pdt.setDeviceName(deviceName);
		return pdt;
	}

	/**
	 * 得到脉搏结果
	 * 
	 * @param dataValue
	 * @return
	 */
	private static String getPulseMonitorResult(int dataValue) {
		String monitor_result;
		if (dataValue < 60) {
			monitor_result = "脉搏过缓";
		} else if (dataValue > 100) {
			monitor_result = "脉搏过速";
		} else {
			monitor_result = "脉搏正常";
		}

		return monitor_result;
	}

	/**
	 * 得到体温结果
	 * 
	 * @param dataValue
	 * @return
	 */
	private static String getTemperatureMonitorResult(double dataValue) {
		String monitor_result;
		if (dataValue > 37 && dataValue < 39) {
			monitor_result = "轻度发烧";
		} else if (dataValue > 39) {
			monitor_result = "重度发烧";
		} else {
			monitor_result = "正常";
		}

		return monitor_result;
	}

	/**
	 * 得到血氧结果
	 * 
	 * @param dataValue
	 * @return
	 */
	private static String getOxygenMonitorResult(int dataValue) {
		String monitor_result;
		if (dataValue < 94 && dataValue > 90) {
			monitor_result = "血氧过低";
		} else if (dataValue < 90) {
			monitor_result = "";
		} else {
			monitor_result = "正常";
		}

		return monitor_result;
	}

	/**
	 * 得到血压结果
	 * 
	 * @param dataValue
	 * @return
	 */
	private static String getNIBPMonitorResult(int dataValue) {
		String monitor_result;
		if (dataValue >= 180) {
			monitor_result = "重度高血压";
		} else if (dataValue > 160 && dataValue < 179) {
			monitor_result = "中度";
		} else if (dataValue > 140 && dataValue < 159) {
			monitor_result = "轻度";
		} else if (dataValue > 120 && dataValue < 139) {
			monitor_result = "临界";
		} else if (dataValue < 90) {
			monitor_result = "低血压";
		} else {
			monitor_result = "正常";
		}
		return monitor_result;
	}

	/*
	 * 得到当前时间
	 */
	@SuppressLint("SimpleDateFormat")
	private static String getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

}
