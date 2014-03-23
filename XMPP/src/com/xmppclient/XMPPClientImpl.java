package com.xmppclient;

import org.jivesoftware.smack.AndroidConnectionConfiguration;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.util.Log;

public class XMPPClientImpl {

	private String server;
	private int port;
	
	private XMPPConnection connection;
	private ChatManager chatManager;
	private MessageListener messageListener;
	private InvitationRejectionListener rejectionListener;
	private InvitationListener invitationListener;
	private PacketListener packetListener;
	
	public XMPPClientImpl(String server, int port)
	{
		this.server = server;
		this.port = port;
	}
	
	public void init() throws Exception
	{
		AndroidConnectionConfiguration config = new AndroidConnectionConfiguration(server, port);
		
		connection = new XMPPConnection(config);
		connection.connect();
		
		Log.d("_XMPP_", "Connected: " + connection.isConnected());
		
		chatManager = connection.getChatManager();
		messageListener = new XMPPMessageListener();
		rejectionListener = new XMPPInvitationRejectionListener();
		invitationListener = new XMPPInvitationListener();
		packetListener = new XMPPPacketListener();
		
		connection.addPacketListener(packetListener, null);
		MultiUserChat.addInvitationListener(connection, invitationListener);
	}
	
	public void login(String name, String passwd) throws Exception
	{
		if(connection != null && connection.isConnected())
			connection.login(name, passwd);
		Log.d("_XMPP_", "logged in: " + connection.isAuthenticated());
	}
	
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

	public void disconnect() {
		if(connection != null && connection.isConnected())
		{
			connection.removePacketListener(packetListener);
			connection.disconnect();
		}
	}

	public void sendMessage(String message, String to) throws Exception {
		Chat chat = chatManager.createChat(to + "@" + server, messageListener);
		chat.sendMessage(message);
	}
	
	public void sendMucMessage(MultiUserChat muc, String message) throws Exception
	{
		muc.sendMessage(message);
	}
	
	public void addBuddy(String user, String name) throws Exception
	{
		Roster roster = connection.getRoster();
		roster.createEntry(user + "@" + server, name, null);
		Log.d("_XMPP_", "Added: " + user);
	}
	
	public MultiUserChat createGroupChat(String room) throws Exception
	{
		if(connection != null)
		{
			MultiUserChat muc = new MultiUserChat(connection, room + "@conference." + server);
			muc.create(connection.getUser());
			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));

			Log.d("_XMPP_", "created room: " + muc.getRoom() + " " + muc.getNickname());
			
			return muc;
		}
		else
			return null;
	}

	public MultiUserChat joinGroupChat(String room) throws Exception
	{
		MultiUserChat muc = new MultiUserChat(connection, room);
		muc.join(connection.getUser());
		
		return muc;
	}
	
	public void inviteToGroup(MultiUserChat muc, String user, String reason)
	{
		muc.addInvitationRejectionListener(rejectionListener);
		
		muc.invite(user + "@" + server, reason);
		Log.d("_XMPP", "invited: " + user);
	}
	
	public void addMUCInvitationListener(InvitationListener listener)
	{
		MultiUserChat.addInvitationListener(connection, listener);
	}
}
