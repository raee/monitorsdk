/*
 * Copyright (C) 2012 Signove Tecnologia
 */

package com.signove.health.service;


public class JniBridge
{
	static
	{
		System.loadLibrary("healthd");
	}
	HealthService	cb;
	
	boolean			finalized;
	
	JniBridge(HealthService pcb)
	{
		cb = pcb;
		finalized = false;
		healthd_init();
	}
	
	public synchronized void abortassoc(int context)
	{
		Cabortassoc(context);
	}
	
	public void associated(int context, String xml)
	{
		cb.associated(context, xml);
	}
	
	public native void Cabortassoc(int context);
	
	// manager part: called from C
	public void cancel_timer(int handle)
	{
		cb.cancel_timer(handle);
	}
	
	public native void Cchannelconnected(int context);
	
	public native void Cchanneldisconnected(int context);
	
	public native void Cdatareceived(int context, byte[] data);
	
	public native String Cgetconfig(int context);
	
	// communication part: called from service
	public synchronized void channel_connected(int context)
	{
		Cchannelconnected(context);
	}
	
	public synchronized void channel_disconnected(int context)
	{
		Cchanneldisconnected(context);
	}
	
	public native void Chealthdfinalize();
	
	public native void Chealthdinit(String tmp_path);
	
	public int create_timer(int milisseconds, int handle)
	{
		return cb.create_timer(milisseconds, handle);
	}
	
	public native void Creleaseassoc(int context);
	
	// FIXME implement PM-Store calls
	
	public native void Creqactivationscanner(int context, int handle);
	
	public native void Creqdeactivationscanner(int context, int handle);
	
	public native void Creqmdsattr(int context);
	
	public native void Creqmeasurement(int context);
	
	public native void Ctimeralarm(int handle);
	
	public synchronized void data_received(int context, byte[] data)
	{
		Cdatareceived(context, data);
	}
	
	public void deviceattributes(int context, String xml)
	{
		cb.deviceattributes(context, xml);
	}
	
	public void disassociated(int context)
	{
		cb.disassociated(context);
	}
	
	// communication part: called from C
	public void disconnect_channel(int context)
	{
		cb.disconnect_channel(context);
	}
	
	@Override
	protected void finalize()
	{
		if (!finalized)
		{
			healthd_finalize();
			finalized = true;
		}
	}
	
	public synchronized String getconfig(int context)
	{
		return Cgetconfig(context);
	}
	
	public synchronized void healthd_finalize()
	{
		Chealthdfinalize();
	}
	
	public synchronized void healthd_init()
	{
		Chealthdinit(cb.getApplicationContext().getFilesDir().toString());
	}
	
	public void measurementdata(int context, String xml)
	{
		cb.measurementdata(context, xml);
	}
	
	public synchronized void releaseassoc(int context)
	{
		Creleaseassoc(context);
	}
	
	public synchronized void reqactivationscanner(int context, int handle)
	{
		Creqactivationscanner(context, handle);
	}
	
	public synchronized void reqdeactivationscanner(int context, int handle)
	{
		Creqdeactivationscanner(context, handle);
	}
	
	public synchronized void reqmdsattr(int context)
	{
		Creqmdsattr(context);
	}
	
	public synchronized void reqmeasurement(int context)
	{
		Creqmeasurement(context);
	}
	
	public void send_data(int context, byte[] data)
	{
		cb.send_data(context, data);
	}
	
	// FIXME implement PM-Store calls
	
	// manager part: called from service
	public synchronized void timer_alarm(int handle)
	{
		Ctimeralarm(handle);
	}
};
