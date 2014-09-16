
迈瑞H900家庭监测设备蓝牙数据传输接口

适用迈瑞监测设备，蓝牙传输接口

##过期
该接口已经过期，已经移植到src目录中。

##注意
		源码仅供学习，请勿用作商业用途！！
----
需要使用的权限：
<div class="cnblogs_code">
<pre><span style="color: #0000ff;">&lt;</span><span style="color: #800000;">uses-permission </span><span style="color: #ff0000;">android:name</span><span style="color: #0000ff;">="android.permission.WRITE_EXTERNAL_STORAGE"</span> <span style="color: #0000ff;">/&gt;</span>
<span style="color: #0000ff;">&lt;</span><span style="color: #800000;">uses-permission </span><span style="color: #ff0000;">android:name</span><span style="color: #0000ff;">="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"</span> <span style="color: #0000ff;">/&gt;</span>
<span style="color: #0000ff;">&lt;</span><span style="color: #800000;">uses-permission </span><span style="color: #ff0000;">android:name</span><span style="color: #0000ff;">="android.permission.BLUETOOTH"</span> <span style="color: #0000ff;">/&gt;</span>
<span style="color: #0000ff;">&lt;</span><span style="color: #800000;">uses-permission </span><span style="color: #ff0000;">android:name</span><span style="color: #0000ff;">="android.permission.BLUETOOTH_ADMIN"</span> <span style="color: #0000ff;">/&gt;</span></pre>
</div>
<p>&nbsp;</p>
----
使用范例：

			/**
			 * 核心服务，设备接收
			 * 使用服务绑定的形式接收蓝牙数据，用于后台传输数据
			 * 您也可以在ACTIVITY 中实例化接口，通过IDevice的接口connect()方法连接设备，再设置接口回调device.setListener(listener);
			 * 
			 * @author Chenrui
			 * 
			 */
			public class CoreServerBinder extends Binder
			{
				/**
				 * 蓝牙正在连接
				 */
				public static final String			ACTION_BLUETOOTH_DEVICE_CONNECTING		= "com.yixin.nfyh.cloud.bluetoth.device.connecting.actoin";

				/**
				 * 蓝牙连接成功！
				 */
				public static final String			ACTION_BLUETOOTH_DEVICE_CONNECTED		= "com.yixin.nfyh.cloud.bluetoth.device.connected.actoin";
				
				/**
				 * 蓝牙连接失败
				 */
				public static final String			ACTION_BLUETOOTH_DEVICE_FAILD			= "com.yixin.nfyh.cloud.bluetoth.device.faild.actoin";
				
				/**
				 * 　蓝牙正在接收数据
				 */
				public static final String			ACTION_BLUETOOTH_DEVICE_RECEVICEING		= "com.yixin.nfyh.cloud.bluetoth.device.receviceing.actoin";
				
				/**
				 * 　蓝牙完成接收数据
				 */
				public static final String			ACTION_BLUETOOTH_DEVICE_RECEVICED		= "com.yixin.nfyh.cloud.bluetoth.device.receviced.actoin";
				/**
				 * 　蓝牙断开
				 */
				public static final String			ACTION_BLUETOOTH_DEVICE_DISCONNECTED	= "com.yixin.nfyh.cloud.bluetoth.device.disconnected.actoin";
				
				private IDevice						device; // 设备接口
				private String						tag										= "CoreServerBinder";
				private List<DeviceCallbakModel>	signModels;
				private Context						context;
				private IDeviceListener				listener;
				
				public CoreServerBinder(Context context)
				{
					this.context = context;
					
					signModels = new ArrayList<DeviceCallbakModel>(); // 解析蓝牙后的数据包
					device = new MindrayAPI(context); // 实例化迈瑞接口
					
				}
				
				public IDevice getDevice()
				{
					return device;
				}
				
				// 开始连接迈瑞设备
				public void conncet()
				{
					if (listener == null)
					{
						listener = new DeviceConnectListener();
						device.setListener(listener);
					}
					device.open();
					device.connect();
				}
				

				public void conncet(IDeviceListener l)
				{
					if (l != null)
					{
						device.setListener(l);
						device.open();
						device.connect();
					}
				}
				
				// 蓝牙设备数据回调监听
				private class DeviceConnectListener implements IDeviceListener
				{
					
					private Intent	intent;
					private int		connectTime;
					private int		maxConnectTime	= 1;	// 重试一次
															
					public DeviceConnectListener()
					{
						intent = new Intent();
					}
					
					/**
					 * 发送蓝牙广播
					 */
					private void sendIntent(String action, String msg)
					{
						this.intent.setAction(action);
						if (msg != null) this.intent.putExtra(Intent.EXTRA_TEXT, msg);
						context.sendBroadcast(intent);
					}
					
					/**
					 * 发送蓝牙广播
					 */
					private void sendIntent(String action)
					{
						this.intent.setAction(action);
						context.sendBroadcast(intent);
					}
					
					@Override
					public void onReceive(DeviceCallbakModel model)
					{
						signModels.add(model);
						show("当前接收-->" + model.getDataName() + ";" + model.getValue());
						sendIntent(ACTION_BLUETOOTH_DEVICE_RECEVICED, "等待传输数据..");
					}
					
					@Override
					public void onDisConnect()
					{
						show("设备断开连接");
						sendIntent(ACTION_BLUETOOTH_DEVICE_DISCONNECTED, "监测设备已经断开，请重新连接！");
					}
					
					@Override
					public void onProgress(int step, int progress)
					{
						show("开始接收数据：");
						signModels.clear();
						if (step == IDevice.STEP_SEND_DATA)
						{
							sendIntent(ACTION_BLUETOOTH_DEVICE_RECEVICED, "发送数据完毕");
						}
						else
						{
							sendIntent(ACTION_BLUETOOTH_DEVICE_RECEVICEING, "正在传输数据...");
						}
					}
					
					@Override
					public void onConnect()
					{
						show("设备开始连接");
						sendIntent(ACTION_BLUETOOTH_DEVICE_CONNECTING, "正在扫描设备...");
					}
					
					@Override
					public void onConnected()
					{
						show("设备连接成功");
						sendIntent(ACTION_BLUETOOTH_DEVICE_CONNECTED, "蓝牙设备连接成功！");
					}
					
					@Override
					public void onReceiveSuccess()
					{
						show("接收到数据");
						if (signModels.size() < 1)
						{
							show("没有接收到数据...");
							return;
						}
						for (DeviceCallbakModel m : signModels)
						{
							show(m.getDataName() + ";" + m.getValue());
						}
						Intent intent = new Intent(context, SignDetailActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Bundle bundle = new Bundle();
						bundle.putString(Intent.EXTRA_TEXT, "-1");
						bundle.putSerializable("data", (Serializable) signModels);
						intent.putExtras(bundle);
						context.startActivity(intent);
						
						intent.putExtras(bundle);
						sendIntent(ACTION_BLUETOOTH_DEVICE_RECEVICED);
						
					}
					
					@Override
					public void onConnectError(int code, String msg)
					{
						show("设备连接异常:" + msg);
						if (code == BluetoothCallback.CODE_BLUETOOTH_EXCTION)
						{
							if (connectTime < maxConnectTime)
							{
								show("重试连接：" + connectTime);
								connectTime++;
								new Timer().schedule(new TimerTask()
								{
									
									@Override
									public void run()
									{
										device.connect(); // 连接
									}
								}, 3000);
								
							}
						}
						else if (code == BluetoothCallback.CODE_BLUETOOTH_NOT_FOUND)
						{
						}
						else if (code == BluetoothCallback.CODE_BLUETOOTH_DISCONNETED)
						{
							msg = "设备已经断开连接，请重新连接！";
						}
						else
						{
							Log.e(tag, msg);
							msg = "设备连接出错,请尝试重新连接！";
						}
						sendIntent(ACTION_BLUETOOTH_DEVICE_FAILD, msg);
						
					}
					
					private void show(Object msg)
					{
						Log.i(tag, msg.toString());
					}
				}
			}
###
