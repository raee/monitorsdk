package com.yixin.monitors.sdk.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

	/**
	 * Socket 改变监听
	 * 
	 * @author ChenRui
	 * 
	 */
	public interface BluetoothSocketChangeListener {
		void onBluetoothDisconnect();
	}

	private String							uuid			= "00001101-0000-1000-8000-00805F9B34FB";
	private String							tag				= "BluetoothSocketConnection";
	private InputStream						mSocketInputStream;
	private OutputStream					mSocketOutputStream;
	private BluetoothSocket					mSocket;
	private BluetoothDevice					mDevice;

	private BluetoothListener				mListener;
	private String							msg;
	private int								errorCode;
	private boolean							mIsConnected	= false;
	private BluetoothSocketChangeListener	mBluetoothSocketChangeListener;

	public BluetoothSocketConnection(BluetoothListener listener) {
		super();
		this.mListener = listener;
	}

	public void setBluetoothListener(BluetoothListener l) {
		this.mListener = l;
	}

	public void setBluetoothSocketChangeListener(BluetoothSocketChangeListener l) {
		this.mBluetoothSocketChangeListener = l;
	}

	// public void connect(BluetoothDevice device) {
	// if (mIsConnected) {
	// Log.i(tag, "设备已经连接，不需要连接！");
	// return;
	// }
	//
	//
	// this.execute(device);
	// Log.i(tag, "开始连接！");
	// }

	public void disconnect() {
		try {
			mIsConnected = false;
			// 关闭输入流
			if (this.mSocketInputStream != null) {
				this.mSocketInputStream.close();
				this.mSocketInputStream = null;
			}
			// 关闭Socket连接
			if (this.mSocket != null) {
				this.mSocket.close();
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
			mListener.onStartConnection(mDevice);
			this.mSocket.connect();
			onConnected(mDevice); // 连接成功
			this.mSocketInputStream = mSocket.getInputStream();
			this.mSocketOutputStream = mSocket.getOutputStream();
			int len = -1;
			byte[] buffer = new byte[512];
			mIsConnected = true;
			while (mIsConnected) {
				len = mSocketInputStream.read(buffer);// 蓝牙是阻塞的接收数据
				byte[] recBuffer = Arrays.copyOf(buffer, len);
				this.publishProgress(recBuffer);
			}
			Log.i(tag, "蓝牙阻塞结束~~");
			onBluetoothCancle();

		}
		catch (Exception e) {
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
			// 取消配对
			if (mDevice != null && mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
				BluetoothManager.removeBond(mDevice);
				Log.i(tag, "移除配对。");
			}

		}
		finally {
			this.cancel(true);
			this.disconnect();
		}

		if (result == -1) {
			onBluetoothError(errorCode, msg);
		}
		if (mBluetoothSocketChangeListener != null) {
			mBluetoothSocketChangeListener.onBluetoothDisconnect();
		}
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		this.mIsConnected = false;
	}

	// @Override
	// protected void onPostExecute(Integer result) {
	// super.onPostExecute(result);
	// if (result == -1) {
	// onBluetoothError(errorCode, msg);
	// }
	// }

	/**
	 * 向蓝牙发送数据
	 */
	@Override
	public void send(byte[] data) {
		if (this.mSocket != null && this.mSocketOutputStream != null) {
			try {
				Log.i(tag, "发送数据：" + data.length);
				this.mSocket.getOutputStream().write(data, 0, data.length);

				mListener.onBluetoothSendData(data);
			}
			catch (IOException e) {
				onBluetoothError(-1, "蓝牙数据发送失败," + e.getMessage());
				disconnect();
			}
		}
		else {
			onBluetoothError(-1, "发送数据失败,未建立连接！");
			disconnect();
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
		mIsConnected = false;
	}

	protected void onBluetoothCancle() {
		mListener.onBluetoothCancle();
		mIsConnected = false;
	}

	protected void onConnected(BluetoothDevice device) {
		if (mIsConnected) {
			return;
		}
		mListener.onConnected(device);
		mIsConnected = true;
	}
}
