package Main;

import java.sql.*;

public class RulesGenerator implements Runnable {
	private String input_data;
	private Connection db_connection; 
	private String result;
	private String username;
	RulesGenerator(String in, String user, Connection db){
		input_data = in;
		db_connection = db;
		username = user;
	}
	@Override
	public void run() {
		//do something with input_data
		try {
			Statement stmt = db_connection.createStatement();
			stmt.execute(String.format("INSERT INTO `rules`(`username`, `rule`) VALUES (\"%s\",\"%s\")", username, result));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
