package com.alcomat.service;

import com.xmppclient.ConnectionManager;
import com.xmppclient.ConnectionManagerImpl;
import com.xmppclient.SingleUserChatManager;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class AlcomatService extends Service{

	private final IBinder binder = new AlcomatServiceBinder();
	private ConnectionManager connManager;
	
	public static final String SERVER = "server";
	public static final String PORT = "port";
	public static final String CONFPREFIX = "confPrefix";
	
	@Override
	public IBinder onBind(Intent intent) {
		String server = intent.getStringExtra(SERVER);
		int port = Integer.parseInt( intent.getStringExtra(PORT) );
		String confPrefix = intent.getStringExtra(CONFPREFIX);
		
		try {
			connManager = new ConnectionManagerImpl(server, port, confPrefix);
			connManager.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return binder;
	}
	
	public void login(String name, String passwd) throws Exception
	{
		if(connManager != null)
			connManager.login(name, passwd);
	}
	
	public void setStatus(boolean available, String status)
	{
		if(connManager != null)
			connManager.setStatus(available, status);
	}
	
	public SingleUserChatManager getSucManager()
	{
		return connManager.getSucManager();
	}
	
	public class AlcomatServiceBinder extends Binder {
		public AlcomatService getService()
		{
			return AlcomatService.this;
		}
	}
}
