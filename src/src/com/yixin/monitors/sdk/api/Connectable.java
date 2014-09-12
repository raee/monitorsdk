package com.yixin.monitors.sdk.api;

public interface Connectable {
	void connect();
	
	void disconnect();
	
	boolean isConnected();
}
