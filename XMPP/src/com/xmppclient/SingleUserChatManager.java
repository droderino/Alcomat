package com.xmppclient;

public interface SingleUserChatManager {

	public abstract void sendMessage(String message, String to)
			throws Exception;

}