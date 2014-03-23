package alcomat.src.java.org.jivesoftware.openfire.plugin.alcomat.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alcomat.src.java.org.jivesoftware.openfire.plugin.mysql.MySQLConnector;

public class UserDAO {
	private MySQLConnector connector;
	private static final Logger Log = LoggerFactory.getLogger(MySQLConnector.class);

	public UserDAO(MySQLConnector connector){
		this.connector = connector;
	}

	public boolean registerUser(String username, String password, String name) throws SQLException{
		// TODO: Check if username exists already and return false
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("INSERT INTO users (name,username,password) VALUES (?,?,?)");
		ps.setString(1, name);
		ps.setString(2, username);
		ps.setString(3, password);
		ps.executeUpdate();
		return true;
	}

	public void deleteUser(String username){
		// Maybe noch nicht ;)
	}

	public void updateUsername(int userId, String name) throws SQLException{
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("UPDATE users SET name = ? WHERE id = ? ");
		ps.setString(1, name);
		ps.setInt(2, userId);
		ps.executeUpdate();
	}

	public List<String> getUserRooms(int id) throws SQLException{
		List<String> resultList = new ArrayList<String>();
		PreparedStatement ps = connector.getConnect()
				.prepareStatement("SELECT rooms.id, rooms.roomid FROM rooms INNER JOIN rooms_have_users ON rooms_have_users.room_id = rooms.id WHERE rooms_have_users.user_id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			resultList.add(rs.getString("roomid"));
		}

		return resultList;
	}
}
