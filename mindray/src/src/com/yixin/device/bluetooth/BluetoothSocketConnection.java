package com.yixin.device.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import com.yixin.device.core.IBluetoothParser;

/**
 * 异步蓝牙连接
 * 
 * @author MrChenrui
 * 
 */
class BluetoothSocketConnection extends
		AsyncTask<BluetoothDevice, byte[], Void>
{
	
	private String				uuid	= "00001101-0000-1000-8000-00805F9B34FB";
	private String				tag		= "BluetoothSocketConnection";
	private InputStream			mSocketInputStream;
	private OutputStream		mSocketOutputStream;
	private IBluetoothParser	mParser;
	private BluetoothSocket		mSocket;
	private BluetoothDevice		mDevice;
	private BluetoothCallback	mListener;
	
	public BluetoothSocketConnection(IBluetoothParser parser,
			BluetoothCallback l)
	{
		this.mParser = parser;
		this.mListener = l;
	}
	
	public void connect(BluetoothDevice device)
	{
		this.execute(device);
	}
	
	@Override
	protected void onCancelled()
	{
		super.onCancelled();
		Log.i(tag, "取消蓝牙监听！");
		try
		{
			if (this.mSocketInputStream != null
					&& this.mSocketOutputStream != null)
			{
				this.mSocketInputStream.close();
				this.mSocketOutputStream.close();
				this.mSocketInputStream = null;
				this.mSocketOutputStream = null;
			}
		}
		catch (Exception e1)
		{
			Log.e(tag, "取消蓝牙线程失败：" + e1.getMessage());
			e1.printStackTrace();
		}
		finally
		{
			//this.mListener.onBluetoothDisconnect(); // 关闭蓝牙
		}
	}
	
	@Override
	protected Void doInBackground(BluetoothDevice... params)
	{
		
		// 蓝牙接收的主要线程方法
		
		try
		{
			
			this.mDevice = params[0];
			UUID uid = UUID.fromString(uuid);
			this.mSocket = this.mDevice.createRfcommSocketToServiceRecord(uid);
			this.mSocket.connect();
			mListener.onBluetooth(mSocket.getRemoteDevice());
			this.mSocketOutputStream = mSocket.getOutputStream();
			this.mSocketInputStream = mSocket.getInputStream();
			int len = -1;
			byte[] buffer = new byte[512];
			while (!this.isCancelled())
			{
				if (this.isCancelled())
				{
					Log.e(tag, "接收时取消线程");
					mSocketInputStream.close();
					this.mListener.onBluetoothDisconnect(); //结束蓝牙监听
					break; //取消接收数据
				}
				len = mSocketInputStream.read(buffer);// 蓝牙是阻塞的接收数据
				
				byte[] recBuffer = Arrays.copyOf(buffer, len);
				this.publishProgress(recBuffer);
			}
		}
		catch (IOException e)
		{
			String msg = e.getMessage();
			int code = BluetoothCallback.CODE_BLUETOOTH_EXCTION;
			if (msg != null && msg.contains("closed"))
			{
				msg = "设备断开连接！";
				code = BluetoothCallback.CODE_BLUETOOTH_DISCONNETED;
			}
			else
			{
				msg = "设备发生不可预知的异常！" + msg;
			}
			this.mListener.onBluetoothFail(code, msg);
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.mListener.onBluetoothFail(
					BluetoothCallback.CODE_BLUETOOTH_EXCTION,
					"蓝牙发生异常：" + e.getMessage());
		}
		finally
		{
			this.cancel(true); // 关闭线程
		}
		
		return null;
	}
	
	@Override
	protected void onProgressUpdate(byte[]... values)
	{
		byte[] data = values[0];
		this.mListener.onBluetoothRecevice(data);
		mListener.onBluetoothReceviceing();
		mParser.parser(data, 0);		// 解析包 
	}
	
	/**
	 * 向蓝牙发送数据
	 */
	public void sendData(byte[] data)
	{
		if (this.mSocketOutputStream != null)
		{
			try
			{
				Log.i(tag, "发送数据：" + data.length);
				this.mSocketOutputStream.write(data, 0, data.length);
				mListener.onBluetoothSendData(data); // 调用发送接口
			}
			catch (IOException e)
			{
				Log.e(tag, "发送数据失败！");
				this.mListener.onBluetoothFail(
						BluetoothCallback.CODE_BLUETOOTH_SEND_FAILD,
						"发送蓝牙数据失败：" + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
}
