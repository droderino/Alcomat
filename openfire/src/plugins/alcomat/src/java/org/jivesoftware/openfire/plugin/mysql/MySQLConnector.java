package alcomat.src.java.org.jivesoftware.openfire.plugin.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLConnector {
	private Connection connect = null;

	private static final Logger Log = LoggerFactory.getLogger(MySQLConnector.class);

	public MySQLConnector() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager
				.getConnection("jdbc:mysql://ratink.de:3307/alcomat?user=alcomat&password=alcomaticus");
	}

	public List<String> getUserRooms(int id){
		List<String> resultList = new ArrayList<String>();
		try {
			PreparedStatement ps = connect
					.prepareStatement("SELECT rooms.id, rooms.roomid FROM rooms INNER JOIN rooms_have_users ON rooms_have_users.room_id = rooms.id WHERE rooms_have_users.user_id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				resultList.add(rs.getString("roomid"));
			}

		} catch (SQLException e) {
		}
		return resultList;
	}
	
	public List<String> getRoomUsers(int id){
		List<String> resultList = new ArrayList<String>();
		try {
			PreparedStatement ps = connect
					.prepareStatement("SELECT users.id, users.name FROM users INNER JOIN rooms_have_users ON rooms_have_users.user_id = users.id WHERE rooms_have_users.room_id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				resultList.add(rs.getString("name"));
			}

		} catch (SQLException e) {
		}
		return resultList;
	}
}
