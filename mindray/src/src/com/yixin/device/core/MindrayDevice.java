package com.yixin.device.core;

import android.content.Context;

import com.yixin.device.bluetooth.BluetoothCallback;
import com.yixin.device.bluetooth.MindrayBluetoothConnection;
import com.yixin.device.parser.mindray.CMSControl;
import com.yixin.device.parser.mindray.CMSControl.CMSControlHandler;
import com.yixin.device.parser.mindray.CMSPacketStream;
import com.yixin.device.parser.mindray.PackageParseIntf;
import com.yixin.device.parser.mindray.ParseCMSPacket;
import com.yx.model.FinishPackageData;

public class MindrayDevice implements IBluetoothDevice
{
	private MindrayBluetoothConnection	mBluetoothConnection;
	private CMSControl					mParser;
	private Context						mContext;
	private DeviceReceviceCallback		mListener;
	private BluetoothCallback			mBluetoothCallback;
	
	public MindrayDevice(Context context)
	{
		this.mContext = context;
		mParser = new CMSControl();
		mParser.SetCMSControlHandler(new MindrayBluetoothReceiveHandler());
	}
	
	@Override
	public void connect()
	{
		if (mBluetoothConnection == null)
		{
			mBluetoothConnection = new MindrayBluetoothConnection(mContext, mBluetoothCallback, mParser);
		}
		mBluetoothConnection.connect();
	}
	
	@Override
	public boolean isConnected()
	{
		if (mBluetoothConnection == null)
			return false;
		return mBluetoothConnection.isConnected();
	}
	
	@Override
	public void disconnect()
	{
		if (mBluetoothConnection != null)
		{
			mBluetoothConnection.disconnect();
		}
	}
	
	private class MindrayBluetoothReceiveHandler implements CMSControlHandler
	{
		
		@Override
		public void onDeviceBaseInfo(CMSPacketStream packetStream, int nDeviceType)
		{
			CMSPacketStream pksReturn = mParser.getSysdatePackets(null);
			if (pksReturn != null)
			{
				byte[] buf = pksReturn.toBuffer();
				mBluetoothConnection.sendData(buf); // 发送数据
			}
		}
		
		@Override
		public void onDeviceResult(CMSPacketStream packetStream)
		{
			CMSPacketStream pksReturn = mParser.getConfirmPackets(packetStream);
			if (pksReturn != null)
			{
				byte[] buf = pksReturn.toBuffer();
				mBluetoothConnection.sendData(buf); // 发送数据
			}
			PackageParseIntf parsePacket = new ParseCMSPacket();
			FinishPackageData packData = parsePacket.parsePacket(packetStream);
			mListener.onRecevieData(packData); // 回调给数据接收接口
		}
	}
	
	@Override
	public void setOnBluetoothDeviceListener(BluetoothCallback bluetoothCallback)
	{
		this.mBluetoothCallback = bluetoothCallback;
	}
	
	@Override
	public void setOnReceviceListener(DeviceReceviceCallback l)
	{
		this.mListener = l;
	}
	
}
