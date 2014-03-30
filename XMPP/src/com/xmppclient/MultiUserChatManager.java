package com.xmppclient;

import org.jivesoftware.smackx.muc.InvitationListener;

public interface MultiUserChatManager {

	public abstract void sendMessage(String room, String message)
			throws Exception;

	public abstract String createGroupChat(String room) throws Exception;

	public abstract String joinGroupChat(String room) throws Exception;

	public abstract void inviteToGroup(String room, String user, String reason);

	public abstract void addInvitationListener(InvitationListener listener);

}