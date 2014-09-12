package com.yixin.monitors.sdk.omron;

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
import com.yixin.monitors.sdk.api.ApiMonitor;
import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.bluetooth.OmronBluetoothConnection;

public class OmronMonitor implements ApiMonitor, ServiceConnection {
	OmronBluetoothConnection	mConnection;
	private Context				mContext;
	private BluetoothListener	mBluetoothListener;
	public static int[]			Specs	= { 0x1004 };
	private HealthServiceAPI	mApi;
	private HealthAgentAPI		mHealthAgentApiStub;		// 蓝牙设备接口
	private String				Tag		= "OmronDevice";
	private Intent				mHealthservice;
	private Intent				mBlueService;
	private boolean				mIsBinded;
	
	public OmronMonitor(Context context) {
		this.mContext = context;
		mHealthservice = new Intent(this.mContext, HealthService.class);
		mBlueService = new Intent(mContext, BluetoothHDPService.class);
	}
	
	@Override
	public void connect() {
		if (mConnection == null) {
			mConnection = new OmronBluetoothConnection(mContext, mBluetoothListener, this);
			if (mHealthAgentApiStub == null) {
				mHealthAgentApiStub = new HealthAgentApiStub(mConnection);
			}
		}
		mConnection.connect();
		startService();
	}
	
	/**
	 * 开启服务
	 */
	/**
	 * 
	 */
	private void startService() {
		if (mIsBinded) return;
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
	
	@Override
	public void disconnect() {
		if (mConnection != null) {
			mConnection.disconnect();
		}
		stopService();
		mIsBinded = false;
	}
	
	@Override
	public boolean isConnected() {
		return false;
	}
	
	@Override
	public void setBluetoothListener(BluetoothListener listener) {
		this.mBluetoothListener = listener;
	}
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mApi = HealthServiceAPI.Stub.asInterface(service); // 实例化接口
		try {
			mApi.ConfigurePassive(mHealthAgentApiStub, Specs);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onServiceDisconnected(ComponentName name) {
		this.disconnect(); // 关闭连接		
	}
	
}
