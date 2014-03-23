package alcomat.src.java.org.jivesoftware.openfire.plugin.alcomat.dao;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alcomat.src.java.org.jivesoftware.openfire.plugin.mysql.MySQLConnector;

public class RoomDAO {
	private MySQLConnector connector;
	private static final Logger Log = LoggerFactory.getLogger(MySQLConnector.class);
	private SecureRandom random = new SecureRandom();
	public RoomDAO(MySQLConnector connector){
		this.connector = connector;
	}
	
	public String createRoom(String name, int adminId) throws SQLException{
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("INSERT INTO rooms (roomid, name, created_at, admin_id, active, interval) VALUES (?,?,?,?,1,30)");
		String roomId = createRoomId();
		ps.setString(1, roomId);
		ps.setString(2, name);
		ps.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
		ps.setInt(4, adminId);
		ps.executeUpdate();	
		return roomId;
	}
	
	public String createRoom(String name, String username) throws SQLException{
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("SELECT users.id FROM users WHERE username = ?");
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return createRoom(name,rs.getInt(1));
	}
	
	public void updateRoomName(int roomId, String name) throws SQLException{
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("UPDATE rooms SET name = ? WHERE id = ?");
		ps.setInt(2, roomId);
		ps.setString(1, name);
		ps.executeUpdate();
	}
	
	private String createRoomId() {
		return new BigInteger(130, random).toString(20);
	}

	public boolean addUser(int roomId, int userId) throws SQLException{
		// TODO: Check if user already in room
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("INSERT INTO rooms_have_users (user_id, room_id, created_at) VALUES (?,?,?)");
		ps.setInt(1, userId);
		ps.setInt(2, roomId);
		ps.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
		ps.executeUpdate();
		return true;
	}
	
	public void removeUser(int roomId, int userId) throws SQLException{
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("DELETE FROM rooms_have_users WHERE user_id = ? AND room_id = ?");
		ps.setInt(1, userId);
		ps.setInt(2, roomId);
		ps.executeUpdate();
	}
	
	public String getAdmin(int roomId) throws SQLException{
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("SELECT users.id, users.username FROM rooms INNER JOIN users ON users.user_id = rooms.admin_id WHERE rooms.id = ?");
		ps.setInt(1, roomId);
		ResultSet rs = ps.executeQuery();
		return rs.getString("username");
	}
	
	public void addPunishmentType(int roomId, int punishmentTypeId) throws SQLException{
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("INSERT INTO rooms_have_punishmenttypes (punishmenttype_id, room_id, created_at) VALUES (?,?,?)");
		ps.setInt(1, punishmentTypeId);
		ps.setInt(2, roomId);
		ps.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
		ps.executeUpdate();
	}
	
	public void removePunishmentType(int roomId, int punishmentTypeId) throws SQLException{
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("DELETE FROM rooms_have_punishmenttypes WHERE punishmenttype_id = ? AND room_id = ?");
		ps.setInt(1, punishmentTypeId);
		ps.setInt(2, roomId);
		ps.executeUpdate();
	}

	public void setActive(int id, boolean active) throws SQLException{
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("UPDATE rooms SET active = ? WHERE id = ?");
		ps.setInt(2, id);
		ps.setInt(1, active?1:0);
		ps.executeUpdate();
	}
	
	public void setInterval(int id, int interval) throws SQLException{
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("UPDATE rooms SET interval = ? WHERE id = ?");
		ps.setInt(2, id);
		ps.setInt(1, interval);
		ps.executeUpdate();
	}

	public List<String> getRoomUsers(int id) throws SQLException{
		List<String> resultList = new ArrayList<String>();
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("SELECT users.id, users.name FROM users INNER JOIN rooms_have_users ON rooms_have_users.user_id = users.id WHERE rooms_have_users.room_id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			resultList.add(rs.getString("name"));
		}

		return resultList;
	}
}
