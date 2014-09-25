package com.yixin.monitors.sdk.manette;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.api.IBluetoothSendable;
import com.yixin.monitors.sdk.api.IDataParser;
import com.yixin.monitors.sdk.model.PackageModel;
import com.yixin.monitors.sdk.model.SignDataModel;

public class ManetteDataParser implements IDataParser {
	
	// 0xFB 6A 75 5A 55 BB BB BB 每隔1秒发送一次，发3次。
	private static byte[]	SEND_DATA	= null;
	
	static {
		SEND_DATA = getHexBytes("FB6A755A55BBBBBB");
	}
	
	/**
	 * 16进制字符串转byte
	 * 
	 * @param message
	 * @return
	 */
	private static byte[] getHexBytes(String message) {
		int len = message.length() / 2;
		char[] chars = message.toCharArray();
		String[] hexStr = new String[len];
		byte[] bytes = new byte[len];
		for (int i = 0, j = 0; j < len; i += 2, j++) {
			hexStr[j] = "" + chars[i] + chars[i + 1];
			bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
		}
		return bytes;
	}
	
	private IBluetoothSendable	mBluetoothSendable;
	private int					count	= 0;
	private BluetoothListener	mListener;
	
	public ManetteDataParser(IBluetoothSendable bluetoothSendable, BluetoothListener listener) {
		this.mBluetoothSendable = bluetoothSendable;
		this.mListener = listener;
	}
	
	@Override
	public PackageModel parse(byte[] data) {
		//		
		//		String datas = "data:";
		//		for (int i = 0; i < data.length; i++) {
		//			datas += data[i];
		//		}
		//		Log.i("data", datas);
		
		try {
			int len = data.length;
			if (len > 0 && data[0] != -2) { return null; } // 没有帧头
			if (len > 7) {
				StringBuilder sb = new StringBuilder();
				sb.append(data[4]);
				sb.append(data[5]);
				float value = Integer.valueOf(sb.toString()) / 18.00f;
				float val = Math.round(value * 100) / 100.0f;
				PackageModel model = new PackageModel();
				List<SignDataModel> signDatas = new ArrayList<SignDataModel>();
				SignDataModel m = new SignDataModel();
				m.setDataName("血糖");
				m.setUnit("mmol/L");
				m.setValue(String.valueOf(val));
				signDatas.add(m);
				model.setSignDatas(signDatas);
				sendDataToDevice();
				return model;
			}
			
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void sendDataToDevice() {
		
		byte[] data = null;
		mListener.onReceived(data);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (count > 2) {
					count = 0;
					this.cancel();
					
					byte[] data = null;
					mListener.onReceived(data);
					Log.i("data", "发送数据完成！");
					return;
				}
				mBluetoothSendable.send(SEND_DATA);
				count++;
			}
		}, 0, 1000);
		
	}
	
}
