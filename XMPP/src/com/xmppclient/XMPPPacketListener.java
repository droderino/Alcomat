package com.xmppclient;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.util.Log;

public class XMPPPacketListener implements PacketListener{

	@Override
	public void processPacket(Packet packet) {
		// TODO Auto-generated method stub
		Message message = (Message)packet;
		if(message.getBody() != null)
		{
			String from = message.getFrom();
			String body = message.getBody();
			Log.d("_XMPP_", "Received: " + body + " from: " + from);
		}
	}

}
