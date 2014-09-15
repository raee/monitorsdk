/*
 * Copyright (C) 2012 Signove Tecnologia Parts based on Bluetooth HDP example
 * from Android SDK Copyright (C) 2011 The Android Open Source Project
 */

package com.signove.health.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.yixin.monitors.sdk.bluetooth.OmronBluetoothConnection;
import com.yixin.monitors.sdk.bluetooth.SendBoradcastManager;

@SuppressLint("HandlerLeak")
public class HealthService extends Service {
	private static final int[]					HEALTH_PROFILE_SOURCE_DATA_TYPES	= { 0x1004, 0x1007, 0x1029, 0x100f };
	private HashMap<String, Integer>			addr_ctx							= new HashMap<String, Integer>();
	private List<HealthAgentAPI>				agents								= new ArrayList<HealthAgentAPI>();
	JniBridge									antidote;
	
	private HealthServiceAPI.Stub				apiEndpoint							= new HealthServiceAPI.Stub() {
																						//																						// FIXME add to AIDL
																						//																						// @Override
																						//																						public void AbortAssociation(String dev) throws RemoteException {
																						//																							Log.i(TAG, "Aborting association (asked by client)");
																						//																							int context = get_context(dev);
																						//																							antidote.abortassoc(context);
																						//																						}
																						
																						@Override
																						public void ConfigurePassive(HealthAgentAPI agt, int[] specs) throws RemoteException {
																							Log.i(TAG, "ConfigurePassive");
																							agents.add(agt);
																							Log.i(TAG, "Configured agent " + agt);
																						}
																						
																						@Override
																						public String GetConfiguration(String dev) throws RemoteException {
																							Log.i(TAG, "Returning config");
																							int context = get_context(dev);
																							return antidote.getconfig(context);
																						}
																						
																						// @Override
																						//																						public void ReleaseAssociation(String dev) throws RemoteException {
																						//																							Log.i(TAG, "Releasing association (asked by client)");
																						//																							int context = get_context(dev);
																						//																							antidote.releaseassoc(context);
																						//																						}
																						
																						@Override
																						public void RequestDeviceAttributes(String dev) throws RemoteException {
																							Log.i(TAG, "Asking deviceAttributes");
																							int context = get_context(dev);
																							antidote.reqmdsattr(context);
																						}
																						
																						@Override
																						public void Unconfigure(HealthAgentAPI agt) throws RemoteException {
																							Log.i(TAG, "Unconfigure");
																							agents.remove(agt);
																							Log.i(TAG, "Unconfigured agent " + agt);
																						}
																					};
	
	int											context_id							= 0;
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, BluetoothDevice>	ctx_dev								= new HashMap<Integer, BluetoothDevice>();
	private BluetoothAdapter					mBluetoothAdapter;
	private boolean								mIsSendConnectedBroadcast			= false;									// 是否已经发送了通知连接成功的状态广播
																																
	// Sets up communication with {@link BluetoothHDPService}.
	private ServiceConnection					mConnection							= new ServiceConnection() {
																						@Override
																						public void onServiceConnected(ComponentName name, IBinder service) {
																							Log.i(TAG, "HDP service connected");
																							mHealthServiceBound = true;
																							mIsSendConnectedBroadcast = false;
																							Message msg = Message.obtain(null, BluetoothHDPService.MSG_REG_CLIENT);
																							msg.replyTo = mMessenger;
																							mHealthService = new Messenger(service);
																							sendConnectedBroadcast();
																							try {
																								mHealthService.send(msg);
																							}
																							catch (RemoteException e) {
																								Log.i(TAG, "Unable to register client to service.");
																								e.printStackTrace();
																							}
																							for (int type : HEALTH_PROFILE_SOURCE_DATA_TYPES) {
																								sendMessage(BluetoothHDPService.MSG_REG_HEALTH_APP, type);
																							}
																						}
																						
																						@Override
																						public void onServiceDisconnected(ComponentName name) {
																							mHealthService = null;
																							mHealthServiceBound = false;
																							Log.i(TAG, "HealthService 取消连接！");
																						}
																					};
	
	private Messenger							mHealthService;
	private boolean								mHealthServiceBound;
	// Handles events sent by {@link HealthHDPService}.
	private Handler								mIncomingHandler					= new Handler() {
																						@Override
																						public void handleMessage(Message msg) {
																							int context;
																							BluetoothDevice mDevice;
																							
																							switch (msg.what) {
																							
																								case BluetoothHDPService.STATUS_HEALTH_APP_REG:
																									Log.i(TAG, "HDP Registered");
																									sendConnectedBroadcast();
																									break;
																								
																								case BluetoothHDPService.STATUS_HEALTH_APP_UNREG:
																									Log.i(TAG, "HDP Unegistered");
																									break;
																								
																								case BluetoothHDPService.STATUS_READ_DATA:
																									Log.i(TAG, "HDP data");
																									Object[] pair = (Object[]) msg.obj;
																									context = get_context((BluetoothDevice) pair[0]);
																									if (context > 0 && antidote != null) {
																										antidote.data_received(context, (byte[]) pair[1]);
																									}
																									break;
																								
																								case BluetoothHDPService.STATUS_READ_DATA_DONE:
																									Log.i(TAG, "HDP closed channel");
																									mDevice = (BluetoothDevice) msg.obj;
																									context = get_context(mDevice);
																									if (context > 0 && antidote != null) {
																										antidote.channel_disconnected(context);
																										sendDisconnected(context);
																									}
																									break;
																								
																								case BluetoothHDPService.STATUS_CREATE_CHANNEL:
																									Log.i(TAG, "HDP create channel complete");
																									mDevice = (BluetoothDevice) msg.obj;
																									context = get_context(mDevice);
																									if (context > 0 && antidote != null) {
																										antidote.channel_connected(context);
																										sendConnected(context, mDevice);
																									}
																									break;
																								case BluetoothHDPService.STATUS_DESTROY_CHANNEL:
																									Log.i(TAG, "HDP destroy channel complete");
																									mDevice = (BluetoothDevice) msg.obj;
																									context = get_context(mDevice);
																									if (context > 0 && antidote != null) {
																										antidote.channel_disconnected(context);
																										sendDisconnected(context);
																									}
																									break;
																							}
																						}
																						
																					};
	
	private final Messenger						mMessenger							= new Messenger(mIncomingHandler);
	private final BroadcastReceiver				mReceiver							= new BroadcastReceiver() {
																						@Override
																						public void onReceive(Context context, Intent intent) {
																							final String action = intent.getAction();
																							if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
																								if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR) == BluetoothAdapter.STATE_ON) {
																									// 当蓝牙打开时，启动蓝牙服务
																									initialize();
																								}
																							}
																						}
																					};
	private HashMap<String, Integer>			path_ctx							= new HashMap<String, Integer>();
	
	String										PATH_PREFIX							= "/com/signove/health/device/";
	
	String										TAG									= "HealthService";
	
	int											timer_id							= 0;
	
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Runnable>			timers								= new HashMap<Integer, Runnable>();
	
	Handler										tm;
	
	public void associated(int context, String xml) {
		sendAssociated(context, xml);
	}
	
	// called by Antidote
	public synchronized void cancel_timer(int timer_id) {
		Runnable r = timers.get(timer_id);
		if (r != null) {
			Log.i(TAG, "Cancelling timer " + timer_id);
			tm.removeCallbacks(r);
			timers.remove(timer_id);
		}
		else {
			Log.i(TAG, "Tried to cancel unknown timer " + timer_id);
		}
	}
	
	public synchronized int create_timer(int milisseconds, int context) {
		final int ctx = context;
		
		if (++timer_id > 0x7ffffffe) {
			timer_id = 1;
		}
		
		Log.i(TAG, "Creating timer " + timer_id + " timeout " + milisseconds + "ms");
		
		final Runnable task = new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "Timer callback " + timer_id + " ctx " + ctx);
				antidote.timer_alarm(ctx);
			}
		};
		
		tm.postDelayed(task, milisseconds);
		timers.put(timer_id, task);
		
		return timer_id;
	}
	
	/**
	 * 发送连接成功广播
	 * 
	 * @param msg
	 */
	private void sendConnectedBroadcast() {
		if (!mIsSendConnectedBroadcast) {
			
			BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
				if (OmronBluetoothConnection.DEVICE_NAME.equals(device.getName())) {
					// 发送蓝牙广播，告知已经完成连接！
					SendBoradcastManager.sendDeviceConnected(getApplicationContext(), device);
					mIsSendConnectedBroadcast = true;
					break;
				}
			}
		}
	}
	
	public void deviceattributes(int context, String xml) {
		sendDeviceAttributes(context, xml);
	}
	
	public void disassociated(int context) {
		sendDisassociated(context);
	}
	
	// called by Antidote, we forward to HDP
	public void disconnect_channel(int context) {
		disconnectChannel(context);
	}
	
	private void disconnectChannel(int context) {
		BluetoothDevice dev = get_device(context);
		if (dev != null) sendMessageWithDevice(BluetoothHDPService.MSG_DISCONNECT_CHANNEL, dev);
	}
	
	public int get_context(BluetoothDevice dev) {
		String addr = dev.getAddress();
		
		if (!addr_ctx.containsKey(addr)) {
			insert_context(dev);
		}
		
		return addr_ctx.get(addr);
	}
	
	public int get_context(String path) {
		if (!path_ctx.containsKey(path)) {
			Log.i(TAG, "Path " + path + " has no associated context");
			return 0;
		}
		return path_ctx.get(path);
	}
	
	public BluetoothDevice get_device(int context) {
		if (!ctx_dev.containsKey(context)) {
			Log.i(TAG, "Context " + context + " has no associated device");
			return null;
		}
		return ctx_dev.get(context);
	}
	
	/**
	 * 启动蓝牙监听服务
	 */
	private void initialize() {
		Log.i(TAG, "开始启动BluetoothHDPService蓝牙监听服务...");
		Intent intent = new Intent(this, BluetoothHDPService.class);
		startService(intent);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE); // 绑定服务
	}
	
	// Intent filter and broadcast receive to handle Bluetooth on event.
	private IntentFilter initIntentFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		return filter;
	}
	
	private synchronized int insert_context(BluetoothDevice dev) {
		String addr = dev.getAddress();
		
		if (addr_ctx.containsKey(addr)) { throw new AssertionError("Trying to reinsert context for " + addr); }
		int context = new_context();
		
		ctx_dev.put(context, dev);
		addr_ctx.put(addr, context);
		path_ctx.put(PATH_PREFIX + context, context);
		
		return context;
	}
	
	public void measurementdata(int context, String xml) {
		sendMeasurementData(context, xml);
	}
	
	private int new_context() {
		++context_id;
		return context_id;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return apiEndpoint;
	}
	
	@Override
	public void onCreate() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Log.i(TAG, "Bluetooth not available");
			return;
		}
		
		if (mBluetoothAdapter.isEnabled()) {
			initialize();
		}
		
		mHealthServiceBound = false;
		registerReceiver(mReceiver, initIntentFilter()); // 注册蓝牙广播
		tm = new Handler();
		antidote = new JniBridge(this);
		Log.i(TAG, "HealthService Crated!!");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "HealthService服务停止了！");
		antidote.finalize();
		antidote = null;
		if (mHealthServiceBound) {
			unbindService(mConnection);
			Log.d(TAG, "BluetoothHDPService服务取消绑定!");
		}
		unregisterReceiver(mReceiver);
		mHealthService = null;
		mHealthServiceBound = false;
	}
	
	// called by Antidote, we forward to HDP
	public void send_data(int context, byte[] data) {
		sendData(context, data);
	}
	
	private void sendAssociated(int context, String xml_associated) {
		List<HealthAgentAPI> tmp = new ArrayList<HealthAgentAPI>(agents);
		for (HealthAgentAPI agent : tmp) {
			try {
				agent.Associated(PATH_PREFIX + context, xml_associated);
				Log.i(TAG, "Sent associated to " + agent);
			}
			catch (RemoteException e) {
				Log.i(TAG, "Failed to comm with listener " + agent);
				agents.remove(agent);
			}
		}
	}
	
	private void sendConnected(int context, BluetoothDevice dev) {
		List<HealthAgentAPI> tmp = new ArrayList<HealthAgentAPI>(agents);
		for (HealthAgentAPI agent : tmp) {
			try {
				agent.Connected(PATH_PREFIX + context, dev.getAddress());
				Log.i(TAG, "Sent connected to " + agent);
			}
			catch (RemoteException e) {
				Log.i(TAG, "Failed to comm with listener " + agent);
				agents.remove(agent);
			}
		}
	}
	
	private void sendData(int context, byte[] data) {
		BluetoothDevice dev = get_device(context);
		if (dev != null) {
			Object[] pair = new Object[2];
			pair[0] = dev;
			pair[1] = data;
			sendMessageWithData(BluetoothHDPService.MSG_SEND_DATA, pair);
		}
	}
	
	private void sendDeviceAttributes(int context, String xml_attributes) {
		List<HealthAgentAPI> tmp = new ArrayList<HealthAgentAPI>(agents);
		for (HealthAgentAPI agent : tmp) {
			try {
				agent.DeviceAttributes(PATH_PREFIX + context, xml_attributes);
				Log.i(TAG, "Sent device attributes to " + agent);
			}
			catch (RemoteException e) {
				agents.remove(agent);
				Log.i(TAG, "Failed to comm with listener " + agent);
			}
		}
		
	}
	
	private void sendDisassociated(int context) {
		List<HealthAgentAPI> tmp = new ArrayList<HealthAgentAPI>(agents);
		for (HealthAgentAPI agent : tmp) {
			try {
				agent.Disassociated(PATH_PREFIX + context);
				Log.i(TAG, "Sent disassociated to " + agent);
			}
			catch (RemoteException e) {
				agents.remove(agent);
				Log.i(TAG, "Failed to comm with listener " + agent);
			}
		}
	}
	
	private void sendDisconnected(int context) {
		List<HealthAgentAPI> tmp = new ArrayList<HealthAgentAPI>(agents);
		for (HealthAgentAPI agent : tmp) {
			try {
				agent.Disconnected(PATH_PREFIX + context);
				Log.i(TAG, "Sent disconnected to " + agent);
			}
			catch (RemoteException e) {
				agents.remove(agent);
				Log.i(TAG, "Failed to comm with listener " + agent);
			}
		}
	}
	
	private void sendMeasurementData(int context, String xml_measurement) {
		List<HealthAgentAPI> tmp = new ArrayList<HealthAgentAPI>(agents);
		for (HealthAgentAPI agent : tmp) {
			try {
				agent.MeasurementData(PATH_PREFIX + context, xml_measurement);
				Log.i(TAG, "Sent measurement to " + agent);
			}
			catch (RemoteException e) {
				Log.i(TAG, "Failed to comm with listener " + agent);
				agents.remove(agent);
			}
		}
	}
	
	// Sends a message to {@link BluetoothHDPService}.
	private void sendMessage(int what, int value) {
		if (mHealthService == null) {
			Log.i(TAG, "Health Service not connected.");
			return;
		}
		
		try {
			mHealthService.send(Message.obtain(null, what, value, 0));
		}
		catch (RemoteException e) {
			Log.i(TAG, "Unable to reach service.");
			e.printStackTrace();
		}
	}
	
	private void sendMessageWithData(int what, Object[] pair) {
		if (mHealthService == null) {
			Log.d(TAG, "Health Service not connected.");
			return;
		}
		
		try {
			mHealthService.send(Message.obtain(null, BluetoothHDPService.MSG_SEND_DATA, pair));
		}
		catch (RemoteException e) {
			Log.i(TAG, "Unable to reach service.");
			e.printStackTrace();
		}
	}
	
	// Sends an update message, along with an HDP BluetoothDevice object, to
	// {@link BluetoothHDPService}. The BluetoothDevice object is needed by the
	// channel creation
	// method.
	private void sendMessageWithDevice(int what, BluetoothDevice mDevice) {
		if (mHealthService == null) {
			Log.d(TAG, "Health Service not connected.");
			return;
		}
		
		try {
			mHealthService.send(Message.obtain(null, what, mDevice));
		}
		catch (RemoteException e) {
			Log.i(TAG, "Unable to reach service.");
			e.printStackTrace();
		}
	}
}
