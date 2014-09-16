#家庭监测设备蓝牙数据传输接口
----
支持设备有：

迈瑞H900
欧姆龙（HEM-7081-IT）
支持 IEEE 11073-20601 协议的设备。
----
##注意
		源码仅供学习，请勿用作商业用途！！
		欧姆龙底层协议根据[antidote][A1]

[A1]:https://github.com/raee/antidote
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
B、JNI库
 把libhealthd.so 添加到libs-armeabi中；<br>
 添加aidl中的添加到build 中。

----
##接口调用
提供统一的 ApiMonitor 接口，该接口的实例从MonitorSdkFactory.getApiMonitor(Context context, int sdk) ;中获取。
该接口提供三个操作方法connect();disconnect();isConnected();核心回调通过setBluetoothListener();来获取整个接口的连接过程以及蓝牙数据回调。<br>
最好采用后台绑定服务来传输，保证可以随时接收数据。

----
void onReceived(PackageModel model); 为数据回调接口，通过该接口获取数据。

----
#使用示例实例

ApiMonitor mindrayApi = MonitorSdkFactory.getApiMonitor(this,MonitorSdkFactory.MINDRAY);
mindrayApi.setBluetoothListener(this); //必须设置回调接口，否则会报错。
if(!mindrayApi.isConnected()){ //是否已经建立连接
	mindrayApi.connect(); // 连接
}
mindrayApi.disconnect(); // 断开连接

----
###
