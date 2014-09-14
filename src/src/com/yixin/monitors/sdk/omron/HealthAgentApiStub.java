package com.yixin.monitors.sdk.omron;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.signove.health.service.HealthAgentAPI;
import com.yixin.monitors.sdk.api.BluetoothListener;

public class HealthAgentApiStub extends HealthAgentAPI.Stub {

	private static final String Tag = "HealthAgentApiStub";
	private BluetoothListener mBluetoothListener;
	private boolean isDisconnected = false;
	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mBluetoothListener.onStartReceive();
				break;
			case 1:

				byte[] model = null;
				if (msg.obj != null) {
					model = (byte[]) msg.obj;
				}
				mBluetoothListener.onReceived(model);

			default:
				break;
			}
			return false;
		}
	});

	public HealthAgentApiStub(BluetoothListener listener) {
		this.mBluetoothListener = listener;
	}

	@Override
	public void Associated(String dev, String xmldata) throws RemoteException {
		Log.d(Tag, "Associated");
		Message.obtain(handler, 0).sendToTarget();
	}

	@Override
	public void Connected(String dev, String addr) throws RemoteException {
		Log.d(Tag, "Connected");
		Message.obtain(handler, 0).sendToTarget();
		isDisconnected = false;
	}

	@Override
	public void DeviceAttributes(String dev, String xmldata)
			throws RemoteException {
		Log.d(Tag, "DeviceAttributes");
		Message.obtain(handler, 0).sendToTarget();
	}

	@Override
	public void Disassociated(String dev) throws RemoteException {
		Log.d(Tag, "Disassociated");
		Message.obtain(handler, 0).sendToTarget();
	}

	@Override
	public void Disconnected(String dev) throws RemoteException {
		Log.d(Tag, "Disconnected");
		if (!isDisconnected) {
			Message.obtain(handler, 1, null).sendToTarget();
			isDisconnected = true;
		}

	}

	@Override
	public void MeasurementData(String dev, String xmldata)
			throws RemoteException {
		Message.obtain(handler, 1, xmldata.getBytes()).sendToTarget();
		isDisconnected = true;
	}

}
