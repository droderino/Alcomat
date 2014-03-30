package com.xmppclient;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import com.xmppclient.Listeners.XMPPRosterListener;

import android.util.Log;

public class RosterManagerImpl implements RosterManager {

	private ConnectionManagerImpl cmanager = null;
	private Roster roster = null;
	private RosterListener rosterListener = null;
	
	public RosterManagerImpl(ConnectionManagerImpl cmanager)
	{
		this.cmanager = cmanager;
		
		this.roster = cmanager.getConnection().getRoster();
		rosterListener = new XMPPRosterListener();
		this.roster.addRosterListener(rosterListener);
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.RosterManager#addBuddy(java.lang.String, java.lang.String)
	 */
	@Override
	public void addBuddy(String user, String name) throws Exception
	{
		roster.createEntry(getExp(user), name, null);
		Log.d("_XMPP_", "Added: " + user);
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.RosterManager#removeBuddy(java.lang.String)
	 */
	@Override
	public void removeBuddy(String user) throws Exception
	{
		RosterEntry entry = roster.getEntry(getExp(user));
		if(entry != null)
			roster.removeEntry(entry);
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.RosterManager#getPresence(java.lang.String)
	 */
	@Override
	public Presence getPresence(String user)
	{
		return roster.getPresence(this.getExp(user));
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.RosterManager#getBuddies()
	 */
	@Override
	public Collection<RosterEntry> getBuddies()
	{
		return roster.getEntries();
	}
	
	/* (non-Javadoc)
	 * @see com.xmppclient.RosterManager#getPresences()
	 */
	@Override
	public Map<RosterEntry, Presence> getPresences()
	{
		Map<RosterEntry, Presence> presences = new HashMap<RosterEntry, Presence>();
		Collection<RosterEntry> buddies = roster.getEntries();
		
		Iterator<RosterEntry> iter = buddies.iterator();
		while(iter.hasNext())
		{
			RosterEntry entry = iter.next();
			presences.put(entry, this.getPresence(entry.getUser()));
		}
		
		return presences;
	}
	
	private String getExp(String prefix)
	{
		String suffix = "@" + cmanager.getServer();
		
		if(prefix.contains(suffix))
			return prefix;
		else
			return prefix + suffix;
	}
}
