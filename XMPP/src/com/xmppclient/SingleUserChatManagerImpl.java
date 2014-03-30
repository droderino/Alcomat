package com.xmppclient;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;

import com.xmppclient.Listeners.XMPPMessageListener;

public class SingleUserChatManagerImpl implements SingleUserChatManager {

	private ConnectionManagerImpl cmanager = null;
	private ChatManager chatManager = null;
	private MessageListener messageListener = null;
	
	public SingleUserChatManagerImpl(ConnectionManagerImpl cmanager)
	{
		this.cmanager = cmanager;
		chatManager = cmanager.getConnection().getChatManager();
		messageListener = new XMPPMessageListener();
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.SingleUserChatManager#sendMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessage(String message, String to) throws Exception {
		Chat chat = chatManager.createChat(getDestExp(to), messageListener);
		chat.sendMessage(message);
	}
	
	private String getDestExp(String prefix)
	{
		String suffix = "@" + cmanager.getServer();
		
		if( prefix.contains(suffix) )
			return prefix;
		else
			return prefix + suffix;
	}
}
