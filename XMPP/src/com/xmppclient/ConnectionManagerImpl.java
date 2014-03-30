package com.xmppclient;

import org.jivesoftware.smack.AndroidConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

import com.xmppclient.Listeners.XMPPPacketListener;

import android.util.Log;

public class ConnectionManagerImpl implements ConnectionManager {

	private String server;
	private String confPrefix;
	private int port;
	
	private XMPPConnection connection;
	private PacketListener packetListener;
	
	private MultiUserChatManager mucManager = null;
	private SingleUserChatManager sucManager = null;
	private RosterManager rosterManager = null;
	
	public ConnectionManagerImpl(String server, int port, String confPrefix)
	{
		this.server = server;
		this.port = port;
		this.setConfPrefix(confPrefix);
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.ConnectionManager#init()
	 */
	@Override
	public void init() throws Exception
	{
		AndroidConnectionConfiguration config = new AndroidConnectionConfiguration(server, port);
		
		connection = new XMPPConnection(config);
		connection.connect();
		
		Log.d("_XMPP_", "Connected: " + connection.isConnected());
		
		packetListener = new XMPPPacketListener();
		connection.addPacketListener(packetListener, null);
		
		this.mucManager = new MultiUserChatManagerImpl(this);
		this.sucManager = new SingleUserChatManagerImpl(this);
		this.rosterManager = new RosterManagerImpl(this);
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.ConnectionManager#login(java.lang.String, java.lang.String)
	 */
	@Override
	public void login(String name, String passwd) throws Exception
	{
		if(connection != null && connection.isConnected())
			connection.login(name, passwd);
		Log.d("_XMPP_", "logged in: " + connection.isAuthenticated());
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.ConnectionManager#disconnect()
	 */
	@Override
	public void disconnect() {
		if(connection != null && connection.isConnected())
		{
			connection.removePacketListener(packetListener);
			connection.disconnect();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.ConnectionManager#setStatus(boolean, java.lang.String)
	 */
	@Override
	public void setStatus(boolean available, String status)
	{
		Presence.Type type;
		if(available)
			type = Type.available;
		else
			type = Type.unavailable;
		
		Presence presence = new Presence(type);
		presence.setStatus(status);
		connection.sendPacket(presence);
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getConfPrefix() {
		return confPrefix;
	}

	public void setConfPrefix(String confPrefix) {
		this.confPrefix = confPrefix;
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	/* (non-Javadoc)
	 * @see com.xmppclient.ConnectionManager#getMucManager()
	 */
	@Override
	public MultiUserChatManager getMucManager() {
		return mucManager;
	}

	/* (non-Javadoc)
	 * @see com.xmppclient.ConnectionManager#getSucManager()
	 */
	@Override
	public SingleUserChatManager getSucManager() {
		return sucManager;
	}

	/* (non-Javadoc)
	 * @see com.xmppclient.ConnectionManager#getRosterManager()
	 */
	@Override
	public RosterManager getRosterManager() {
		return rosterManager;
	}
}
