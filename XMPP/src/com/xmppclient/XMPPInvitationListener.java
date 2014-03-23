package com.xmppclient;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.util.Log;

public class XMPPInvitationListener implements InvitationListener {

	@Override
	public void invitationReceived(Connection conn, String room, String inviter,
			String reason, String passwd, Message message) {
		MultiUserChat chat = new MultiUserChat(conn, room);
		try {
			if(passwd != null)
				chat.join(room, passwd);
			else
				chat.join(room);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("_XMPP_", "Muc Invitation: " + room + " by " + inviter + " " + reason);
	}

}
