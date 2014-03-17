
package org.jivesoftware.openfire.plugin;

/**
 * @author Andreas Braun, Christian Tiefenau
 */
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.user.UserManager;
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

import alcomat.src.java.org.jivesoftware.openfire.plugin.alcomat.dao.*;
import alcomat.src.java.org.jivesoftware.openfire.plugin.mysql.MySQLConnector;

import java.io.File;
import java.util.List;
import java.util.Map;

public class AlcomatPlugin implements Plugin, Component, PropertyEventListener {
	private String serviceName;
	private ComponentManager componentManager;
	private PluginManager pluginManager;
	private UserManager userManager;
	private GroupManager groupManager;
	private static final Logger Log = LoggerFactory.getLogger(AlcomatPlugin.class);
	private MySQLConnector connector = null;
	private RoomDAO roomDAO;
	private UserDAO userDAO;

	public AlcomatPlugin(){
		serviceName = "alcomat";
	}

	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		pluginManager = manager;
		componentManager = ComponentManagerFactory.getComponentManager();
		userManager = UserManager.getInstance();
		groupManager = GroupManager.getInstance();
		
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
			roomDAO = new RoomDAO(connector);
			userDAO = new UserDAO(connector);
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
		String body = message.getBody();
		String command = body.substring(0,body.indexOf("::")).toUpperCase();
		body = body.substring(body.indexOf("::")+2);

		Log.debug(command+ " // " + body);

		if(connector!=null){
			if(command.equals("GETUSERROOMS")){
				List<String> result = userDAO.getUserRooms(Integer.parseInt(body));
				String send = "";
				for(String x : result){
					send += x+",";
				}
				replyMessage(message,send);
			} else if (command.equals("GETROOMUSERS")) {
				List<String> result = roomDAO.getRoomUsers(Integer.parseInt(body));
				String send = "";
				for(String x : result){
					send += x+",";
				}
				replyMessage(message,send);					
			} else if (command.equals("CREATEUSER")){
				if(StringUtils.countMatches(body, ",")==2){
					String username = body.substring(0,body.indexOf(","));
					String password = body.substring(body.indexOf(",")+1,body.lastIndexOf(","));
					String name = body.substring(body.lastIndexOf(",")+1);
					
					if(userDAO.registerUser(username, password, name)){
						userManager.createUser(username, password, username, "");
					}
					
					replyMessage(message,"USERCREATED");
				}
			} else if (command.equals("CREATEROOM")){
				String newRoom = roomDAO.createRoom(body, message.getFrom().getNode());
				if(newRoom!=""){
					Group group = groupManager.createGroup(newRoom);
					replyMessage(message,newRoom);
				}
			} else if (command.equals("ADDUSER")){
				
			} else if (command.equals("REMOVEUSER")){
				
			} else if (command.equals("UPDATEUSERNAME")){

			} else if (command.equals("UPDATEROOMNAME")){
				
			} else if (command.equals("UPDATEINTERVAL")){
				roomDAO.setInterval(Integer.parseInt(body.substring(0,body.indexOf(","))), Integer.parseInt(body.substring(body.lastIndexOf(",")+1)));
			} else if (command.equals("UPDATEACTIVE")){
				roomDAO.setActive(Integer.parseInt(body.substring(0,body.indexOf(","))), Integer.parseInt(body.substring(body.lastIndexOf(",")+1))>0?true:false);
			} 
		}
	}


	private void replyMessage(Message message, String send) {
		try {
			Message reply = new Message();
			reply.setID(message.getID());
			reply.setFrom(message.getTo());
			reply.setTo(message.getFrom());
			reply.setBody(send);
			componentManager.sendPacket(this, reply);
			Log.debug("Sent Roomlist "+send);
		} catch (Exception e){
			Log.error(e.toString());	
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