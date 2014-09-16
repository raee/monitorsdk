#家庭监测设备蓝牙数据传输接口
支持设备有：迈瑞H900、欧姆龙（HEM-7081-IT）、支持 IEEE 11073-20601 协议的设备。
----
##注意
		源码仅供学习，请勿用作商业用途！！
		欧姆龙底层协议根据[antidote](https://github.com/raee/antidote)
----
##需要使用的权限：
<div class="cnblogs_code">
<pre><span style="color: #0000ff;">&lt;</span><span style="color: #800000;">uses-permission </span><span style="color: #ff0000;">android:name</span><span style="color: #0000ff;">="android.permission.WRITE_EXTERNAL_STORAGE"</span> <span style="color: #0000ff;">/&gt;</span>
<span style="color: #0000ff;">&lt;</span><span style="color: #800000;">uses-permission </span><span style="color: #ff0000;">android:name</span><span style="color: #0000ff;">="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"</span> <span style="color: #0000ff;">/&gt;</span>
<span style="color: #0000ff;">&lt;</span><span style="color: #800000;">uses-permission </span><span style="color: #ff0000;">android:name</span><span style="color: #0000ff;">="android.permission.BLUETOOTH"</span> <span style="color: #0000ff;">/&gt;</span>
<span style="color: #0000ff;">&lt;</span><span style="color: #800000;">uses-permission </span><span style="color: #ff0000;">android:name</span><span style="color: #0000ff;">="android.permission.BLUETOOTH_ADMIN"</span> <span style="color: #0000ff;">/&gt;</span></pre>
</div>
<p>&nbsp;</p>

##欧姆龙设备需要的信息
A、底层JNI服务
<div class="cnblogs_code">
	<pre> <span style="color: #008000;">&lt;!--</span><span style="color: #008000;"> 欧姆龙核心服务 </span><span style="color: #008000;">--&gt;</span>
		<span style="color: #0000ff;">&lt;</span><span style="color: #800000;">service
	</span><span style="color: #ff0000;">android:name</span><span style="color: #0000ff;">="com.signove.health.service.HealthService"</span><span style="color: #ff0000;">
	android:enabled</span><span style="color: #0000ff;">="true"</span><span style="color: #ff0000;">
	android:label</span><span style="color: #0000ff;">="欧姆龙服务"</span><span style="color: #ff0000;">
	android:permission</span><span style="color: #0000ff;">="android.permission.BLUETOOTH"</span><span style="color: #ff0000;">
	android:stopWithTask</span><span style="color: #0000ff;">="false"</span> <span style="color: #0000ff;">&gt;</span>
	<span style="color: #0000ff;">&lt;/</span><span style="color: #800000;">service</span><span style="color: #0000ff;">&gt;</span>
	<span style="color: #008000;">&lt;!--</span><span style="color: #008000;"> 欧姆龙蓝牙服务 </span><span style="color: #008000;">--&gt;</span>
	<span style="color: #0000ff;">&lt;</span><span style="color: #800000;">service </span><span style="color: #ff0000;">android:name</span><span style="color: #0000ff;">="com.signove.health.service.BluetoothHDPService"</span> <span style="color: #0000ff;">/&gt;</span></pre>
</div>
<p>&nbsp;</p>
<br>
B、JNI库<br>
 <p>把libhealthd.so 添加到libs-armeabi中；</p>
 <p>添加aidl中的添加到build 中。</p>

----
##创建接口
		ApiMonitor mindrayApi = MonitorSdkFactory.getApiMonitor(this,MonitorSdkFactory.MINDRAY);
##数据回调
		// 数据回调将调用listener onReceived(PackageModel model); model为解析蓝牙数据后的体征数据。
		mindrayApi.setBluetoothListener(listener); 

##使用示例实例

		ApiMonitor mindrayApi = MonitorSdkFactory.getApiMonitor(this,MonitorSdkFactory.MINDRAY);
		mindrayApi.setBluetoothListener(this); //必须设置回调接口，否则会报错。
		if(!mindrayApi.isConnected()){ //是否已经建立连接
			mindrayApi.connect(); // 连接
		}
		mindrayApi.disconnect(); // 断开连接

----
