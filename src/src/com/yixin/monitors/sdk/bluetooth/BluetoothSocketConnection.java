package com.yixin.monitors.sdk.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.api.IBluetoothSendable;

/**
 * 蓝牙Socket连接父类
 * 
 * @author ChenRui
 * 
 */
public class BluetoothSocketConnection extends AsyncTask<BluetoothDevice, byte[], Integer> implements IBluetoothSendable {
	private String				uuid	= "00001101-0000-1000-8000-00805F9B34FB";
	private String				tag		= "BluetoothSocketConnection";
	private InputStream			mSocketInputStream;
	//	private OutputStream		mSocketOutputStream;
	private BluetoothSocket		mSocket;
	private BluetoothDevice		mDevice;
	
	private BluetoothListener	mListener;
	//	private Handler				mHandler	= new Handler(new Handler.Callback() {
	//												
	//												@Override
	//												public boolean handleMessage(Message msg) {
	//													switch (msg.what) {
	//														case -1:
	//															mListener.onError(msg.arg1, msg.obj.toString());
	//															break;
	//														case 1:
	//															mListener.onConnected(mDevice); // 通知连接成功！
	//															break;
	//														case 2:
	//															mListener.onBluetoothCancle(); // 结束蓝牙监听
	//															break;
	//														case 3:
	//															mListener.onStartReceive();
	//															break;
	//														default:
	//															break;
	//													}
	//													return false;
	//												}
	//											});
	private String				msg;
	private int					errorCode;
	
	public BluetoothSocketConnection(BluetoothListener listener) {
		super();
		this.mListener = listener;
	}
	
	public void setBluetoothListener(BluetoothListener l) {
		this.mListener = l;
	}
	
	public void connect(BluetoothDevice device) {
		if (getStatus() == Status.PENDING) {
			this.execute(device);
			Log.i(tag, "开始连接设备！");
		}
	}
	
	public void disconnect() {
		if (getStatus() != Status.PENDING) {
			cancleConnect();
		}
	}
	
	private void cancleConnect() {
		try {
			if (this.mSocketInputStream != null) {
				this.mSocket.close();
				this.mSocketInputStream.close();
				this.mSocketInputStream = null;
				this.mSocket = null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.e(tag, "取消蓝牙线程失败：" + e.getMessage());
		}
		finally {
			onBluetoothCancle();
		}
	}
	
	/**
	 * 是否已经连接
	 * 
	 * @return
	 */
	public boolean isDisConnected() {
		return mSocket == null || mSocketInputStream == null;
	}
	
	@Override
	protected Integer doInBackground(BluetoothDevice... params) {
		int result = 0;
		try {
			this.mDevice = params[0];
			UUID uid = UUID.fromString(uuid);
			this.mSocket = this.mDevice.createRfcommSocketToServiceRecord(uid);
			Log.i(tag, "正准备Bluetooth Socket连接...");
			this.mSocket.connect();
			onConnected(mDevice); // 连接成功
			this.mSocketInputStream = mSocket.getInputStream();
			int len = -1;
			byte[] buffer = new byte[512];
			while (!this.isCancelled()) {
				if (this.isCancelled()) {
					Log.e(tag, "接收时取消线程");
					mSocketInputStream.close();
					onBluetoothCancle();
					break; // 取消接收数据
				}
				len = mSocketInputStream.read(buffer);// 蓝牙是阻塞的接收数据
				byte[] recBuffer = Arrays.copyOf(buffer, len);
				this.publishProgress(recBuffer);
			}
			
			onBluetoothCancle();
		}
		catch (IOException e) {
			e.printStackTrace();
			msg = e.getMessage();
			errorCode = BluetoothListener.ERROR_CODE_UNKNOWN;
			msg = msg == null ? "" : msg;
			if (msg.contains("timeout")) {
				msg = "请确认迈瑞设备是否打开或尝试重新进行蓝牙配对！";
				errorCode = BluetoothListener.ERROR_TIME_OUT;
			}
			else if (msg.contains("closed")) {
				msg = "设备断开连接！";
				errorCode = BluetoothListener.ERROR_CODE_STREAM_CLOSE;
			}
			else {
				msg = "设备发生不可预知的异常！" + msg;
			}
			result = -1;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			msg = "蓝牙连接发生未知异常";
			errorCode = BluetoothListener.ERROR_CODE_UNKNOWN;
			result = -1;
		}
		finally {
			this.disconnect();
		}
		
		return result;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (result == -1) {
			onBluetoothError(errorCode, msg);
		}
	}
	
	/**
	 * 向蓝牙发送数据
	 */
	@Override
	public void send(byte[] data) {
		if (this.mSocket != null) {
			try {
				Log.i(tag, "发送数据：" + data.length);
				this.mSocket.getOutputStream().write(data, 0, data.length);
			}
			catch (IOException e) {
				onBluetoothError(-1, "蓝牙数据发送失败,请重新连接。");
				disconnect();
			}
		}
		else {
			onBluetoothError(-1, "发送数据失败,蓝牙输出流为空。");
		}
	}
	
	@Override
	protected void onProgressUpdate(byte[]... values) {
		byte[] data = values[0];
		onBluetoothReceived(data);
	}
	
	protected void onBluetoothReceived(byte[] data) {
		mListener.onReceiving(data);
	}
	
	protected void onBluetoothError(int errorCode, String msg) {
		mListener.onError(errorCode, msg);
		//		Message.obtain(mHandler, -1, errorCode, 0, msg).sendToTarget();
	}
	
	protected void onBluetoothCancle() {
		mListener.onBluetoothCancle();
		//		Message.obtain(mHandler, 2).sendToTarget(); // 通知蓝牙取消监听
	}
	
	protected void onConnected(BluetoothDevice device) {
		mListener.onConnected(device);
		//		Message.obtain(mHandler, 1).sendToTarget(); // 通知连接成功
	}
}
