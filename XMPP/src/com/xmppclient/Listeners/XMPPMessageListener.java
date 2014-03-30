package com.xmppclient.Listeners;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import android.util.Log;

public class XMPPMessageListener implements MessageListener{

	@Override
	public void processMessage(Chat chat, Message msg) {
		String from = msg.getFrom();
		String body = msg.getBody();
		Log.d("_XMPP_", "Received: " + body + " from: " + from);
	}

}
