package Main;

import java.io.IOException;
import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.io.*;
import java.time.ZonedDateTime;



public class Optimization implements Runnable{
	private static Logger logger = Main.logger;
	private String login = "";
	private Connection connection = null;
	private int userID = -1;
	private float x_start = 0;
	private float x_finish = 0;
	private int iterations = 0;
	private float step = 0;
	private String title = "";
	private String type = "";
	private String tp = "";
	Optimization(String s, int i, float x_s, float x_f, int it, float st, String n, String t, String typ,  Connection c){
		login = s;
		userID = i;
		x_start = x_s;
		x_finish = x_f;
		iterations = it; 
		step = st;
		connection = c;
		title = n;
		type  = t;
		tp = typ;
	}
	public String optimize() {
		String base = "";
		try {
			System.out.println("Optimization");
			Process sender;
			if(tp.equals("basin"))
				sender = (new ProcessBuilder("python", "optimization.py")).start();
			else
				sender = (new ProcessBuilder("python", "GA_Fuzzy.py")).start();
			BufferedReader input = new BufferedReader(new InputStreamReader(sender.getInputStream()));
			PrintWriter output = new PrintWriter(new OutputStreamWriter(sender.getOutputStream()));
			BufferedReader error =  new BufferedReader(new InputStreamReader(sender.getErrorStream()));
			String xxxname = "xxx" + login + Long.toString(ZonedDateTime.now().toInstant().toEpochMilli()) + ".jpg";
			String vvvname = "vvv" + login + Long.toString(ZonedDateTime.now().toInstant().toEpochMilli()) + ".jpg";
			String wwwname = "www" + login + Long.toString(ZonedDateTime.now().toInstant().toEpochMilli()) + ".jpg";
			output.println(x_start);
			output.println(x_finish);
			output.println(iterations);
			if(tp.equals("basin"))
				output.println(step);
			else
				output.println((int)step);
			output.println(xxxname);
			output.println(vvvname);
			output.println(wwwname);
			output.flush();
			String temp = null;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(String.format("INSERT INTO `rules`(`username`, `rule`, `userID`, `status`, `title`) VALUES ('%s','%s','%s','%s', '%s')", login, base, Integer.toString(userID), "0", title));
//			while(true) {
			System.out.print("Test");
			CharBuffer charb = CharBuffer.allocate(10000);
			int read = -1;
//			int read  = input.read(charb);
			while((read = input.read(charb)) == -1);
			System.out.println(read);
			logger.info(temp);
			base = new String(charb.array()).substring(0, read - 1);
			logger.info("Optimization finished, result: " + base);
//			}
//			stmt.executeUpdate(String.format("UPDATE rules SET status=2, rule=\"%s\", type=\"%s\" WHERE userID='%s' AND title=\"%s\"", base, type, Integer.toString(userID), title));
			
//			sender = (new ProcessBuilder("python", "cycle.py")).start();
//			input = new BufferedReader(new InputStreamReader(sender.getInputStream()));
//			output = new PrintWriter(new OutputStreamWriter(sender.getOutputStream()));
//			error =  new BufferedReader(new InputStreamReader(sender.getErrorStream()));
			
			stmt.executeUpdate(String.format("UPDATE rules SET status=2, rule=\"%s\", type=\"%s\", xxx=\"%s\", vvv=\"%s\", www=\"%s\" WHERE userID='%s' AND title=\"%s\"", base, type, xxxname, vvvname, wwwname, Integer.toString(userID), title));
//			temp = null;
//			while((temp = input.readLine()) != null) {
//				System.out.println(temp);
//			}
			
		} catch (IOException | SQLException e) {
			logger.warning("Exception while sending optimizing: \n" + e.toString());
			e.printStackTrace();
		}
		return base;
	}
	@Override
	public void run() {
		System.out.println(optimize());
		
	}
}
