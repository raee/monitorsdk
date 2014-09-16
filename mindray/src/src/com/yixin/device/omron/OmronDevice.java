package com.yixin.device.omron;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.signove.health.service.BluetoothHDPService;
import com.signove.health.service.HealthAgentAPI;
import com.signove.health.service.HealthService;
import com.signove.health.service.HealthServiceAPI;
import com.signove.health.service.OrmonXmlParser;
import com.yixin.device.bluetooth.BluetoothCallback;
import com.yixin.device.bluetooth.OrmonBluetoothConnection;
import com.yixin.device.core.DeviceReceviceCallback;
import com.yixin.device.core.IBluetoothDevice;
import com.yx.model.FinishPackageData;

public class OmronDevice implements IBluetoothDevice, ServiceConnection {
	class HealthAgentApiStub extends HealthAgentAPI.Stub {

		@Override
		public void Associated(String dev, String xmldata)
				throws RemoteException {
			Log.d(Tag, "Associated");
			mBluetoothCallback.onBluetoothReceviceing();
		}

		@Override
		public void Connected(String dev, String addr) throws RemoteException {
			Log.d(Tag, "Connected");
			mBluetoothCallback.onBluetoothReceviceing();
		}

		@Override
		public void DeviceAttributes(String dev, String xmldata)
				throws RemoteException {
			Log.d(Tag, "DeviceAttributes");
			mBluetoothCallback.onBluetoothReceviceing();
		}

		@Override
		public void Disassociated(String dev) throws RemoteException {
			Log.d(Tag, "Disassociated");
			mBluetoothCallback.onBluetoothReceviceing();
		}

		@Override
		public void Disconnected(String dev) throws RemoteException {
			Log.d(Tag, "Disconnected");
			mBluetoothCallback.onBluetoothRecevice(dev.getBytes());
		}

		@Override
		public void MeasurementData(String dev, String xmldata)
				throws RemoteException {
			FinishPackageData result = mOrmonXmlParser.parser(xmldata);
			mDeviceReceviceCallback.onRecevieData(result);
			mBluetoothCallback.onBluetoothRecevice(xmldata.getBytes());
		}

	}

	public static int[] Specs = { 0x1004 };
	private HealthServiceAPI mApi;
	private String Tag = "OmronDevice";
	private Intent mBlueService;
	private Context mContext;
	private DeviceReceviceCallback mDeviceReceviceCallback;
	private HealthAgentAPI mHealthAgentApiStub = new HealthAgentApiStub(); // 蓝牙设备接口
	private Intent mHealthservice;
	private boolean mIsBinded;
	private OrmonXmlParser mOrmonXmlParser = new OrmonXmlParser(); // 数据解析
	private OrmonBluetoothConnection mBluetoothConnection; // 蓝牙连接
	private BluetoothCallback mBluetoothCallback;

	public OmronDevice(Context context) {
		this.mContext = context;

	}

	@Override
	public void connect() {
		mBluetoothConnection.connect();
		startService();
	}

	@Override
	public void disconnect() {
		mBluetoothConnection.disconnect();
		stopService(); // 停止服务
		if (mApi != null) {
			try {
				mApi.Unconfigure(mHealthAgentApiStub); // Jni取消监听
				mApi = null;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		mIsBinded = false;
		Log.d(Tag, "停止欧姆龙设备服务");
	}

	@Override
	public boolean isConnected() {
		boolean bluetoothOn = BluetoothAdapter.getDefaultAdapter().isEnabled();
		if (bluetoothOn && mApi != null) {
			return true;
		}
		return bluetoothOn;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mApi = HealthServiceAPI.Stub.asInterface(service); // 实例化接口
		try {
			mApi.ConfigurePassive(mHealthAgentApiStub, Specs);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		this.disconnect(); // 关闭连接
	}

	@Override
	public void setOnBluetoothDeviceListener(BluetoothCallback callback) {
		this.mBluetoothCallback = callback;
		this.mBluetoothConnection = new OrmonBluetoothConnection(mContext,
				callback);
		this.mBluetoothConnection.setOnBluetoothChangeListener(this);
	}

	@Override
	public void setOnReceviceListener(DeviceReceviceCallback l) {
		this.mDeviceReceviceCallback = l;
	}

	/**
	 * 开启服务
	 */
	/**
	 * 
	 */
	private void startService() {
		mHealthservice = new Intent(this.mContext, HealthService.class);
		mBlueService = new Intent(mContext, BluetoothHDPService.class);
		mContext.bindService(mHealthservice, this, Context.BIND_AUTO_CREATE);
		mContext.startService(mBlueService);
		this.mIsBinded = true;
		Log.d(Tag, "开始欧姆龙设备服务");
	}

	/**
	 * 停止服务
	 */
	private void stopService() {

		if (mHealthservice != null) {
			mContext.stopService(mHealthservice);
		}
		if (mBlueService != null) {
			mContext.stopService(mBlueService);
		}
		if (mIsBinded) {
			mContext.unbindService(this);
		}
	}

}
