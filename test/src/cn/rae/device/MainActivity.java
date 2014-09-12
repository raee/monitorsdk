package cn.rae.device;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.yixin.monitors.sdk.MonitorSdkFactory;
import com.yixin.monitors.sdk.api.ApiMonitor;
import com.yixin.monitors.sdk.api.BluetoothListener;
import com.yixin.monitors.sdk.model.PackageModel;
import com.yixin.monitors.sdk.model.SignDataModel;

public class MainActivity extends Activity implements OnClickListener, BluetoothListener {
	
	private ApiMonitor	api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btnoml).setOnClickListener(this);
		findViewById(R.id.btnmr).setOnClickListener(this);
		findViewById(R.id.btnCancle).setOnClickListener(this);
		api = MonitorSdkFactory.getApiMonitor(this);
		api.setBluetoothListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnoml:
				api.connect();
				break;
			case R.id.btnmr:
				api.connect();
				break;
			case R.id.btnCancle:
				api.disconnect();
				break;
			
			default:
				break;
		}
	}
	
	@Override
	public void onStartDiscovery() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStopDiscovery() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onOpenBluetooth() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onCloseBluetooth() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBluetoothStateChange(int state, BluetoothDevice device) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onFindBluetooth(BluetoothDevice device, boolean isBonded) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onBluetoothBonding(BluetoothDevice device) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBluetoothSetPin(BluetoothDevice device) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBluetoothBonded(BluetoothDevice device) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBluetoothBondNone(BluetoothDevice device) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStartReceive() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onReceiving(byte[] data) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onReceived(byte[] data) {
		
	}
	
	@Override
	public void onReceived(PackageModel model) {
		for (SignDataModel item : model.getSignDatas()) {
			Log.i("test", item.getDataName() + ":" + item.getValue());
		}
	}
	
	@Override
	public void onConnected(BluetoothDevice device) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onError(int errorCode, String msg) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBluetoothCancle() {
		// TODO Auto-generated method stub
		
	}
	
}
