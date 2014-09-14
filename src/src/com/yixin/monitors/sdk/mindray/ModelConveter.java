package com.yixin.monitors.sdk.mindray;

import java.util.ArrayList;
import java.util.List;

import com.yixin.monitors.sdk.model.PackageModel;
import com.yixin.monitors.sdk.model.SignDataModel;
import com.yx.model.DefiniteData;
import com.yx.model.DefiniteEcgData;
import com.yx.model.FinishPackageData;
import com.yx.model.FinishPackageData.litPackDataType;

/**
 * 实体转换
 * 
 * @author ChenRui
 * 
 */
public class ModelConveter {
	private ModelConveter() {
	}

	public static PackageModel conver(FinishPackageData data) {
		if (data == null) {
			return null;
		}
		PackageModel result = new PackageModel();

		List<SignDataModel> signDatas = new ArrayList<SignDataModel>();
		for (litPackDataType m : data.getLitPackDataType()) {
			List<DefiniteData> definite = m.getLitDefiniteData();
			List<DefiniteEcgData> ecg = m.getLitECGData();

			// 普通包
			if (definite != null) {
				for (DefiniteData item : definite) {
					if (item == null) {
						continue;
					}
					SignDataModel model = new SignDataModel();
					model.setDataName(item.getItemTypeName());
					model.setDataType(item.getPackageDataTypeID());
					model.setDate(m.getPackDataType().getReceiveDate());
					model.setValue(item.getDataItemValue());
					model.setUnit(item.getDataItemUnit());
					signDatas.add(model);
				}
			}

			// 心电包
			if (ecg != null) {

				for (DefiniteEcgData item : ecg) {
					if (item == null) {
						continue;
					}
					String name = "心电";
					String val = item.getEcgData(); // 心电数组，格式：
													// 0.2072,0.2072,0.2072,0.2072,

					SignDataModel model = new SignDataModel();
					model.setDataName(name);
					model.setDataType(item.getPackageDataTypeID());
					model.setDate(m.getPackDataType().getReceiveDate());
					model.setValue(val);
					signDatas.add(model);
				}
			}
		}
		result.setSignDatas(signDatas);
		return result;
	}
}
