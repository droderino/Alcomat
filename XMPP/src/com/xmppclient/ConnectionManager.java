package com.xmppclient;

public interface ConnectionManager {

	public abstract void init() throws Exception;

	public abstract void login(String name, String passwd) throws Exception;

	public abstract void disconnect();

	public abstract void setStatus(boolean available, String status);

	public abstract MultiUserChatManager getMucManager();

	public abstract SingleUserChatManager getSucManager();

	public abstract RosterManager getRosterManager();

}