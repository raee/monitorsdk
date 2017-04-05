# 家庭监测设备蓝牙数据传输接口

> 支持设备

- 迈瑞H900、欧姆龙（HEM-7081-IT）、支持 IEEE 11073-20601 协议的设备。
- 源码仅供学习，请勿用作商业用途！！
- 欧姆龙底层协议根据开源[antidote](https://github.com/raee/antidote)库进行二次修改。

## 需要使用的权限：
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
```

## 接入欧姆龙设备

- 先把libhealthd.so 添加到libs-armeabi中；
- 在把aidl中的添加到build 中

```java
 <!-- 欧姆龙核心服务 -->
 <service
	android:name="com.signove.health.service.HealthService"
	android:enabled="true"
	android:label="欧姆龙服务"
	android:permission="android.permission.BLUETOOTH"
	android:stopWithTask="false" >
</service>

<!-- 欧姆龙蓝牙服务 -->
<service android:name="com.signove.health.service.BluetoothHDPService" />

```

## 创建接口

```java
ApiMonitor mindrayApi = MonitorSdkFactory.getApiMonitor(this,MonitorSdkFactory.MINDRAY);
```

## 数据回调

```java
// 数据回调将调用listener onReceived(PackageModel model); model为解析蓝牙数据后的体征数据。
mindrayApi.setBluetoothListener(listener); 		
```

## 使用示例实例

```java
ApiMonitor mindrayApi = MonitorSdkFactory.getApiMonitor(this,MonitorSdkFactory.MINDRAY);
mindrayApi.setBluetoothListener(this); //必须设置回调接口，否则会报错。
if(!mindrayApi.isConnected()){ //是否已经建立连接
	mindrayApi.connect(); // 连接
}
mindrayApi.disconnect(); // 断开连接		
```

## 开源协议

请遵守[GPL](http://www.gnu.org/licenses/licenses.en.html)开源协议，禁止商业用途，如有疑问请联系raedev@qq.com
