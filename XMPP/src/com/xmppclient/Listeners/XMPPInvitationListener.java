package com.xmppclient.Listeners;

import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.util.Log;

public class XMPPInvitationListener implements InvitationListener {

	private List<String> rooms = null;
	
	public XMPPInvitationListener()
	{
		
	}
	
	public XMPPInvitationListener(List<String> rooms)
	{
		this.rooms = rooms;
	}
	
	@Override
	public void invitationReceived(Connection conn, String room, String inviter,
			String reason, String passwd, Message message) {
		MultiUserChat chat = new MultiUserChat(conn, room);
		try {
			if(passwd != null)
				chat.join(room, passwd);
			else
				chat.join(room);
			
			if(rooms != null)
				rooms.add(chat.getRoom());
			
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("_XMPP_", "Muc Invitation: " + chat.getRoom() + " by " + inviter + " " + reason);
	}

}
