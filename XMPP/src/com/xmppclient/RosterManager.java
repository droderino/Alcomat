package com.xmppclient;

import java.util.Collection;
import java.util.Map;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

public interface RosterManager {

	public abstract void addBuddy(String user, String name) throws Exception;

	public abstract void removeBuddy(String user) throws Exception;

	public abstract Presence getPresence(String user);

	public abstract Collection<RosterEntry> getBuddies();

	public abstract Map<RosterEntry, Presence> getPresences();

}