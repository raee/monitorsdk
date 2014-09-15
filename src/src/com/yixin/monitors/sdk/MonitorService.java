package com.yixin.monitors.sdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MonitorService extends Service {
	
	private MonitorBinder	mMonitorBinder;
	
	@Override
	public IBinder onBind(Intent intent) {
		return mMonitorBinder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mMonitorBinder = new MonitorBinder();
	}
}
