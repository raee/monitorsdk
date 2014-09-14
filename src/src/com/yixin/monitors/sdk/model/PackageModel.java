package com.yixin.monitors.sdk.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 每一次蓝牙上传的包数据
 * 
 * @author ChenRui
 * 
 */
public class PackageModel implements Parcelable {
	public static Parcelable.Creator<PackageModel> CREATOR = new Creator<PackageModel>() {

		@Override
		public PackageModel[] newArray(int size) {
			return new PackageModel[size];
		}

		@Override
		public PackageModel createFromParcel(Parcel dest) {
			PackageModel model = new PackageModel();
			ClassLoader loader = List.class.getClassLoader();
			model.setSignDatas(dest.readArrayList(loader));
			return model;
		}

	};

	private List<SignDataModel> signDatas;

	/**
	 * 获取体征数据
	 * 
	 * @return
	 */
	public List<SignDataModel> getSignDatas() {
		return signDatas;
	}

	public void setSignDatas(List<SignDataModel> signDatas) {
		this.signDatas = signDatas;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(signDatas);
	}
}
