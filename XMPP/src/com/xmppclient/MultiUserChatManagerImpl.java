package com.xmppclient;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.xmppclient.Listeners.XMPPInvitationListener;
import com.xmppclient.Listeners.XMPPInvitationRejectionListener;

import android.util.Log;

public class MultiUserChatManagerImpl implements MultiUserChatManager {

	private InvitationRejectionListener rejectionListener;
	private InvitationListener invitationListener;
	private ConnectionManagerImpl cmanager;
	private List<String> rooms;
	
	public MultiUserChatManagerImpl(ConnectionManagerImpl cmanager)
	{
		this.cmanager = cmanager;
		rooms = new ArrayList<String>();
		
		rejectionListener = new XMPPInvitationRejectionListener();
		invitationListener = new XMPPInvitationListener();
		this.addInvitationListener(invitationListener);
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.MultiUserChatManager#sendMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessage(String room, String message) throws Exception
	{
		String roomExp = this.getRommExpression(room);
		if( cmanager.getConnection() != null && rooms.contains(roomExp) )
		{
			MultiUserChat muc = new MultiUserChat(cmanager.getConnection(), roomExp);	
			muc.sendMessage(message);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.MultiUserChatManager#createGroupChat(java.lang.String)
	 */
	@Override
	public String createGroupChat(String room) throws Exception
	{
		String roomExp = this.getRommExpression(room);
		if(cmanager.getConnection() != null)
		{
			MultiUserChat muc = new MultiUserChat(cmanager.getConnection(), roomExp);
			muc.create(cmanager.getConnection().getUser());
			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			
			rooms.add(roomExp);

			Log.d("_XMPP_", "created room: " + muc.getRoom() + " " + muc.getNickname());
			
			return room;
		}
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see com.xmppclient.MultiUserChatManager#joinGroupChat(java.lang.String)
	 */
	@Override
	public String joinGroupChat(String room) throws Exception
	{
		String roomExp = this.getRommExpression(room);
		if(cmanager.getConnection() != null)
		{
			MultiUserChat muc = new MultiUserChat(cmanager.getConnection(), roomExp);
			muc.join(cmanager.getConnection().getUser());
			rooms.add(roomExp);
			return room;
		}
		else
			return null;
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.MultiUserChatManager#inviteToGroup(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void inviteToGroup(String room, String user, String reason)
	{
		String roomExp = this.getRommExpression(room);
		if(cmanager.getConnection() != null && rooms.contains(roomExp) )
		{
			MultiUserChat muc = new MultiUserChat(cmanager.getConnection(), roomExp);
			muc.addInvitationRejectionListener(rejectionListener);
			
			muc.invite(user + "@" + cmanager.getServer(), reason);
			Log.d("_XMPP", "invited: " + user);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.MultiUserChatManager#addInvitationListener(org.jivesoftware.smackx.muc.InvitationListener)
	 */
	@Override
	public void addInvitationListener(InvitationListener listener)
	{
		MultiUserChat.addInvitationListener(cmanager.getConnection(), listener);
	}
	
	private String getRommExpression(String room)
	{
		String suffix = "@" + cmanager.getConfPrefix() + "." + cmanager.getServer();
		if(room.contains(suffix))
			return room;
		else
			return room + suffix;
	}
}
