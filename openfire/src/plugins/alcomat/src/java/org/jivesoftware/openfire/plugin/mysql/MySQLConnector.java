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
		setConnect(DriverManager
				.getConnection("jdbc:mysql://ratink.de:3307/alcomat?user=alcomat&password=alcomaticus"));
	}

	public Connection getConnect() {
		return connect;
	}

	private void setConnect(Connection connect) {
		this.connect = connect;
	}
}
