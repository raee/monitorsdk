package com.yixin.monitors.sdk.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.api.IBluetoothSendable;

/**
 * 异步蓝牙连接
 * 
 * @author MrChenrui
 * 
 */
public class MindraySocketConnection extends AsyncTask<BluetoothDevice, byte[], Void> implements IBluetoothSendable {
	private String				uuid		= "00001101-0000-1000-8000-00805F9B34FB";
	private String				tag			= "MindraySocketConnection";
	private InputStream			mSocketInputStream;
	private OutputStream		mSocketOutputStream;
	private BluetoothSocket		mSocket;
	private BluetoothDevice		mDevice;
	private BluetoothListener	mListener;
	
	private Handler				mHandler	= new Handler(new Handler.Callback() {
												
												@Override
												public boolean handleMessage(Message msg) {
													switch (msg.what) {
														case -1:
															mListener.onError(msg.arg1, msg.obj.toString());
															break;
														case 1:
															mListener.onConnected(mDevice); // 通知连接成功！
															break;
														case 2:
															mListener.onBluetoothCancle(); // 结束蓝牙监听
															break;
														case 3:
															mListener.onStartReceive();
															break;
														default:
															break;
													}
													return false;
												}
											});
	
	/**
	 * 是否已经连接
	 * 
	 * @return
	 */
	public boolean isDisConnected() {
		return mSocket == null || mSocketInputStream == null;
	}
	
	public MindraySocketConnection(BluetoothListener l) {
		this.mListener = l;
		Log.i(tag, "新的迈瑞socket 连接线程！");
	}
	
	public void connect(BluetoothDevice device) {
		if (getStatus() == Status.PENDING) {
			this.execute(device);
			Log.i(tag, "开始连接迈瑞设备！");
		}
	}
	
	public void disconnect() {
		if (getStatus() != Status.PENDING) {
			cancleConnect();
		}
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		cancleConnect();
		
	}
	
	/**
	 * @throws IOException
	 */
	private void cancleConnect() {
		try {
			
			Log.i(tag, "取消迈瑞蓝牙监听！");
			if (this.mSocketInputStream != null && this.mSocketOutputStream != null) {
				this.mSocket.close();
				this.mSocketInputStream.close();
				this.mSocketOutputStream.close();
				this.mSocketInputStream = null;
				this.mSocketOutputStream = null;
				this.mSocket = null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.e(tag, "取消蓝牙线程失败：" + e.getMessage());
		}
	}
	
	// 蓝牙接收的主要线程方法
	@Override
	protected Void doInBackground(BluetoothDevice... params) {
		try {
			this.mDevice = params[0];
			int state = mDevice.getBondState();
			String s = state == 10 ? "没有绑定！" : state == 11 ? "正在绑定" : "已绑定";
			Log.i(tag, "蓝牙绑定状态：" + s);
			UUID uid = UUID.fromString(uuid);
			this.mSocket = this.mDevice.createRfcommSocketToServiceRecord(uid);
			Log.i(tag, "正准备Mindray Socket连接...");
			this.mSocket.connect();
			Message.obtain(mHandler, 1).sendToTarget(); // 通知连接成功
			this.mSocketOutputStream = mSocket.getOutputStream();
			this.mSocketInputStream = mSocket.getInputStream();
			int len = -1;
			byte[] buffer = new byte[512];
			while (!this.isCancelled()) {
				if (this.isCancelled()) {
					Log.e(tag, "接收时取消线程");
					mSocketInputStream.close();
					Message.obtain(mHandler, 2).sendToTarget(); // 通知取消
					break; // 取消接收数据
				}
				len = mSocketInputStream.read(buffer);// 蓝牙是阻塞的接收数据
				byte[] recBuffer = Arrays.copyOf(buffer, len);
				this.publishProgress(recBuffer);
			}
			
			Message.obtain(mHandler, 2).sendToTarget(); // 通知蓝牙取消监听
		}
		catch (IOException e) {
			e.printStackTrace();
			String msg = e.getMessage();
			int errorCode = BluetoothListener.ERROR_CODE_UNKNOWN;
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
			Message.obtain(mHandler, -1, errorCode, errorCode, msg).sendToTarget();
		}
		catch (Exception e) {
			e.printStackTrace();
			Message.obtain(mHandler, -1, BluetoothListener.ERROR_CODE_UNKNOWN, 0, "Mindray连接发生未知异常，请重新连接！");
		}
		finally {
			this.disconnect();
		}
		
		return null;
	}
	
	@Override
	protected void onProgressUpdate(byte[]... values) {
		byte[] data = values[0];
		mListener.onReceiving(data);
	}
	
	/**
	 * 向蓝牙发送数据
	 */
	@Override
	public void send(byte[] data) {
		if (this.mSocketOutputStream != null) {
			try {
				Log.i(tag, "发送数据：" + data.length);
				this.mSocketOutputStream.write(data, 0, data.length);
			}
			catch (IOException e) {
				Log.e(tag, "发送数据失败！");
				e.printStackTrace();
			}
		}
		else {
			
			Log.e(tag, "发送数据失败,蓝牙输出流为空。");
		}
	}
	
}
