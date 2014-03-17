
package org.jivesoftware.openfire.plugin;

/**
 * Broadcast service plugin. It accepts messages and broadcasts them out to
 * groups of connected users. The address <tt>all@[serviceName].[server]</tt> is
 * reserved for sending a broadcast message to all connected users. Otherwise,
 * broadcast messages can be sent to groups of users by using addresses
 * in the form <tt>[group]@[serviceName].[server]</tt>.
 *
 * @author Andreas Braun, Christian Tiefenau
 */
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.PropertyEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

import alcomat.src.java.org.jivesoftware.openfire.plugin.mysql.MySQLConnector;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * A sample plugin for Openfire.
 */
public class AlcomatPlugin implements Plugin, Component, PropertyEventListener {
	private String serviceName;
	private ComponentManager componentManager;
	private PluginManager pluginManager;
	private static final Logger Log = LoggerFactory.getLogger(AlcomatPlugin.class);
	private MySQLConnector connector = null;

	public AlcomatPlugin(){
		serviceName = "alcomat";
	}

	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		pluginManager = manager;
		componentManager = ComponentManagerFactory.getComponentManager();
		try {
			componentManager.addComponent(serviceName, this);
			Log.info(componentManager.getServerName());
		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		PropertyEventDispatcher.addListener(this);

		try{
			connector = new MySQLConnector();
		} catch (Exception e){
			Log.error("Could not instantiate MYSQL");
		}

	}

	@Override
	public void processPacket(Packet packet) {
		String toNode = packet.getTo().getNode();
		// Check if user is allowed to send packet to this service[+group]

		if (packet instanceof Message) {
			processMessage((Message)packet);
		}
		else if (packet instanceof Presence) {
			processPresence((Presence) packet);
		}
		else if (packet instanceof IQ) {
			// Handle disco packets
			IQ iq = (IQ) packet;
			// Ignore IQs of type ERROR or RESULT
			if (IQ.Type.error == iq.getType() || IQ.Type.result == iq.getType()) {
				return;
			}
			processIQ(iq);
		}	
	}

	private void processIQ(IQ iq) {

	}

	private void processPresence(Presence packet) {

	}

	private void processMessage(Message message){
		if(connector!=null){
			try{
				List<String> result = connector.getUserRooms(Integer.parseInt(message.getBody()));
				Message reply = null;
				for(String x : result){
					reply = new Message();
					reply.setID(message.getID());
					reply.setFrom(message.getTo());
					reply.setTo(message.getFrom());
					reply.setBody(x);
					componentManager.sendPacket(this, reply);
				}
			} catch (Exception e) {}
		}
	}

	public void destroyPlugin() {
		PropertyEventDispatcher.removeListener(this);
		// Unregister component.
		if (componentManager != null) {
			try {
				componentManager.removeComponent(serviceName);
			}
			catch (Exception e) {
				Log.error(e.getMessage(), e);
			}
		}
		componentManager = null;
		pluginManager = null;
	}

	public void propertySet(String property, Map<String, Object> params) {
		// Hier koennen Einstellungen geladen werden
		/*if (property.equals("plugin.broadcast.groupMembersAllowed")) {
            this.groupMembersAllowed = Boolean.parseBoolean((String)params.get("value"));
        }
        else if (property.equals("plugin.broadcast.disableGroupPermissions")) {
            this.disableGroupPermissions = Boolean.parseBoolean((String)params.get("value"));
        }*/
	}

	public void propertyDeleted(String property, Map<String, Object> params) {
		// Hier werden Einstellungen geloescht
		/*if (property.equals("plugin.broadcast.groupMembersAllowed")) {
            this.groupMembersAllowed = true;
        }
        else if (property.equals("plugin.broadcast.disableGroupPermissions")) {
            this.disableGroupPermissions = false;
        }*/		
	}

	public void xmlPropertySet(String property, Map<String, Object> params) {
	}

	public void xmlPropertyDeleted(String property, Map<String, Object> params) {
	}

	public String getDescription() {
		return pluginManager.getDescription(this);
	}

	public String getName() {
		return pluginManager.getName(this);
	}

	public void initialize(JID arg0, ComponentManager arg1)
			throws ComponentException {
	}

	public void shutdown() {
	}

	public void start() {
	}
}