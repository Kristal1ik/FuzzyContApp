package Main;
import java.time.ZonedDateTime;

import java.sql.*;
import java.security.spec.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.net.Socket;
import java.net.SocketException;
import java.net.InetSocketAddress;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.File;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.nio.file.Files;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.*;
import org.json.*;
public class Handler implements Runnable{
	private enum Actions{
		register,
		reset_password,
		reset_password_2,
		login;

	}
	private String DRIVER = "com.mysql.cj.jdbc.Driver";
	private String DATABASE = "jdbc:mysql://localhost:3306/fuzzy";
	private String USERNAME = "root";
	private String PASSWORD = "";
	private String MAX_POOL = "250";
	private Socket client;
	private boolean running = true;
	private boolean authorized = false;
	private PrivateKey privateKey = null;
	private RSAPublicKey publicKey = null;
	private RSAPublicKey clientKey = null;
	private SecretKey symmetricKey = null;
	private int exponent; 
	private int status = 1;
	private static Connection db_connection = null;
	private static Statement stmt = null;
	private ResultSet resultSet = null; 
	private Properties db_properties = null;
	private Actions last_action; 
	private BigInteger confirmationCode;
	private String userLogin = "";
	private String userPassword = "";
	private String userEmail = "";
	private Cipher decrypt;
	private Cipher encrypt;
	private Cipher encoder;
	private Cipher decoder;
	private ExecutorService rg;
	private String clientIP;
	private String sessionID;
	private String splittedCommand = "";
	private String splittedData = "";
	private int parts = 0;
	private int lastPart = 0;
	private int recievedParts = 0;
	private String firstJson = "";
	private int userID = -1;
	private Logger logger = Main.logger;
	Handler(Socket s, ExecutorService rg1){
		this.client = s;
		rg = rg1;
	}
	private static String sha256(String str) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encoded = digest.digest(str.getBytes(StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder(2* encoded.length);
		for(int i = 0; i < encoded.length;i++) {
			String hex = Integer.toHexString(0xff & encoded[i]);
			if(hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	private void setProperties() {
		if(db_properties == null) {
			db_properties = new Properties();
			db_properties.setProperty("user", USERNAME);
			db_properties.setProperty("password", PASSWORD);
			db_properties.setProperty("MaxPoolStatement", MAX_POOL);
		}
		 
	}
	private void connect() {
		try {
			setProperties();
			Class.forName(DRIVER);
			db_connection = DriverManager.getConnection(DATABASE, db_properties);
			stmt = db_connection.createStatement();
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	public double doMath(double vel, double pos, double w) {
		//do something with vel and pos
		return 0;
	}
	@Override
	public void run(){
		try {
			
		System.out.println(client.getReceiveBufferSize());
			client.setReceiveBufferSize(100000);
			client.setSendBufferSize(10000000);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// TODO Auto-generated method stub
		System.out.println("Client connected!!!");
		clientIP = ((InetSocketAddress) client.getRemoteSocketAddress()).getAddress().getHostAddress();
		logger.info("New connection from " + clientIP);
		connect();//while(true) {
		logger.info("Conneted to database");
			boolean OK = true;
			try(BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8)); PrintWriter output = new PrintWriter(client.getOutputStream())){
				while(running) {
				JSONObject response = new JSONObject();
				int terminate = 0;
				CharBuffer charb = CharBuffer.allocate(10000);
				long start = ZonedDateTime.now().toInstant().toEpochMilli();
				long end = 0;
				System.out.println("Client connected!!!");
				int jsonLength = 0;
				String request  = null;
				while((!input.ready()) || (request = input.readLine()) == null &&  ((end = (ZonedDateTime.now().toInstant().toEpochMilli() - start)) <= 300000)) terminate++;
				if(end >= 300000)
					throw new IOException();
				//if(Main.DEBUG) {
				//	logger.info("New data recieved: \n" + request);
				//}
				ArrayList<String> str = new ArrayList<String>();
				System.out.println(request);
				//while(input.ready()) {
				//	input.read(charb);
				//}
//				System.out.println((new String(charb.array())));
				FileOutputStream fos1 = new FileOutputStream("test.txt");
				PrintWriter pw = new PrintWriter(fos1);
				pw.println(request);
//				String temp = new String(charb.array());
//				String[] lines = temp.split("\n");
//				String temp = new String(data);
//				if(lines[0] == "close_session") {
//					break;
//				}
//				while(input.read(charb) == -1);
				JSONObject json;
				try {
					json = new JSONObject(request);
					System.out.println(json.toString());
					String command = json.getString("Command");
					System.out.println(command.equals("confirm_email"));
					switch(status) {
					case 1:
//						userLogin = new String(decrypt.doFinal(json.getString("Login").getBytes()));
//						userPassword = new String(decoder.doFinal(Base64.getDecoder().decode(json.getString("Password").getBytes())));
						if(command.equals("login")) {
							boolean userFound = false;
							userLogin = json.getString("Login");
							userPassword = sha256(json.getString("Password"));
							resultSet = stmt.executeQuery("select * from users where username = '" + userLogin + "'");
							while(resultSet.next()) {
								userFound = true;
								if(resultSet.getString(4).equals(userPassword)) {
									userID = resultSet.getInt(1);
									userEmail = resultSet.getString(3);
									last_action = Actions.login;
									sessionID = sha256(Long.toString(ZonedDateTime.now().toInstant().toEpochMilli()) + clientIP + (new BigInteger(128, new Random()).toString()));
									stmt.executeUpdate(String.format("INSERT INTO `sessions`(`session_id`, `username`, `ip`, `expiration_date`) VALUES ('%s','%s', '%s', '%s')", sessionID, userLogin, clientIP, "" ));
									output.println(String.format("{\"Status\": 'OK', \"SessionID\": \"%s\", \"Email\": \"%s\"}", sessionID, userEmail));
									status++;
									logger.info("Logged in from: " + clientIP + " as " + userLogin);
									break;
								}
								else {
									output.println("{\"Status\": 'Wrong_password'}");
									logger.info("Wrong password from: " + clientIP);
								}
							}
							if(!userFound) {
								output.println("{\"Status\": 'User_not_found'}");
							}
						}
						else if(command.equals("register")){
							userLogin = json.getString("Login");
							userPassword = sha256(json.getString("Password"));
							System.out.println("select count(*) from users where `username` = \"" + userLogin + "\"");
							resultSet = stmt.executeQuery("select count(*) from users where `username` = \"" + userLogin + "\"");
							resultSet.next();
							if(resultSet.getInt(1) > 0) {
								output.println("{\"Status\": 'User_exists'}");
							}
							else {
//								output.print("{\"Status\": 'Confirmation_code_sent'}");
								confirmationCode = new BigInteger(16, new Random());
								userEmail = json.getString("Email");
								last_action = Actions.register;
								MailSender.sendMail(confirmationCode.toString(), "Registration", userEmail);
								System.out.println(confirmationCode.toString());
								logger.info("New user registration started from: " + clientIP);
//								stmt.executeUpdate(String.format("INSERT INTO `users`(`username`, `email`, `password`) VALUES (\"%s\",\"%s\",\"%s\")", userLogin, userEmail, userPassword));
								output.println("{\"Status\": \"OK\"}");
							}
						}
						else if(command.equals("reset_password")) {
							userLogin = json.getString("Login");
//							userPassword = sha256(json.getString("Password"));
							resultSet = stmt.executeQuery("select count(*) from users where `username` = \"" + userLogin + "\"");
							resultSet.next();
							if(resultSet.getInt(1) == 0) {
								output.println("{\"Ststus\": 'User_not_found'}");
							}
							else {
								resultSet = stmt.executeQuery("select * from users where `username` = \"" + userLogin + "\"");
								resultSet.next();
								output.println("{\"Status\": 'OK'}");
								userID = resultSet.getInt(1);
								confirmationCode = new BigInteger(16, new Random());
								userEmail = resultSet.getString(3);
								MailSender.sendMail(confirmationCode.toString(),"Reset password", userEmail);
								last_action = Actions.reset_password;
							}
							
						}
						else if(command.equals("confirm_email")) {
							if(confirmationCode.equals(json.getBigInteger("ConfirmationCode"))) {
								switch(last_action) {
									case register:
										System.out.println(String.format("INSERT INTO `users`(`username`, `email`, `password`) VALUES (\"%s\",\"%s\",\"%s\")", userLogin, userEmail, userPassword));
										stmt.executeUpdate(String.format("INSERT INTO `users`(`username`, `email`, `password`) VALUES (\"%s\",\"%s\",\"%s\")", userLogin, userEmail, userPassword));
										sessionID = sha256(Long.toString(ZonedDateTime.now().toInstant().toEpochMilli()) + clientIP + (new BigInteger(128, new Random()).toString()));
										stmt.executeUpdate(String.format("INSERT INTO `sessions`(`session_id`, `username`, `ip`, `expiration_date`) VALUES ('%s','%s','%s','%s')", sessionID, userLogin, clientIP, "" ));
										resultSet = stmt.executeQuery(String.format("SELECT * FROM `users` WHERE `username`='%s'", userLogin));
										resultSet.next();
										userID = resultSet.getInt(1);
										output.println(String.format("{\"Status\": 'OK', \"SessionID\": \"%s\"}", sessionID));
										logger.info("New user created from: " + clientIP);
										break;
									case reset_password:
										userPassword= sha256(json.getString("Password"));
										resultSet = stmt.executeQuery(String.format("SELECT `id` FROM users WHERE `username`=\"%s\"", userLogin));
										resultSet.next();
										int id = resultSet.getInt(1);
										userID = id;
										System.out.println(String.format(String.format("UPDATE `users` SET `password`=\"%s\" WHERE `id`=%s", userPassword, id)));
										stmt.executeUpdate(String.format(String.format("UPDATE `users` SET `password`=\"%s\" WHERE `id`=%s", userPassword, id)));
										output.println("{\"Status\": \"OK\"}");
										break;
								}
							}
							else {
//								output.println("HTTP/1.1 403");
//								output.println("Context-Type: text/html; charset=utf-8");
//								output.println();
								output.println("{\"Status\": \"Confirmation code incorrect\"}");
							}
						}
						else if(json.has("SessionID")){
							resultSet = stmt.executeQuery(String.format("SELECT * FROM `sessions` WHERE `session_id` = '%s'", json.getString("SessionID")));
							resultSet.next();
							if(resultSet == null) {
								OK = false;
								break;
							}
							else {
								userLogin = resultSet.getString(3);
								userID = resultSet.getInt(1);
								status++;
								System.out.println("Session restored");
							}
						}
						else  {
							output.println("HTTP/1.1 403");
							output.println("Context-Type: text/html; charset=utf-8");
							output.println();
							output.println("<h>403</h>");
						}
					case 2:
						for(int loop = 0; loop < 1; loop++) {
						switch(command) {
							case "splitted_data":
								System.out.println(parts);
								if(json.getString("SplittedCommand").equals("continue")) {
									splittedData += json.getString("SplittedData");
									if(json.getInt("Part") != lastPart + 1) {
										output.println(String.format("{\"Status\": \"missing_part\", \"MissingPart\": %s}", lastPart + 1));
										break;
									}
									lastPart = json.getInt("Part");
									recievedParts += 1;
									output.println(String.format("{\"Status\": \"OK\"}"));
									if(recievedParts != parts - 1) {
										
										break;
									}
										
										command = splittedCommand;
										recievedParts = 0;
										System.out.println(splittedData.length());
										parts = 0;
										loop--;
										break;
								}
								else {
									splittedCommand = json.getString("SplittedCommand");
									parts = json.getInt("Parts");
									recievedParts = -1;
									lastPart = -1;
									firstJson = json.toString();
									output.println(String.format("{\"Status\": \"OK\"}"));
									break;
								}
							
							case "send_image":
								byte[] image;
								System.out.println();
								if(splittedData == null) {
									image = Base64.getDecoder().decode(json.getString("Image").getBytes());
								}
								else {
									image = Base64.getDecoder().decode(splittedData.getBytes());
									splittedData = "";
								}
								String imageName = ZonedDateTime.now().toInstant().toEpochMilli() + ".jpg";
								FileOutputStream imageWriter = new FileOutputStream(imageName);
								imageWriter.write(image);
								imageWriter.flush();
								imageWriter.close();
								stmt.executeUpdate(String.format("INSERT INTO `images`(`username`, `type`, `image`) VALUES (\"%s\",\"%s\",\"%s\")", userLogin, "", imageName));
								output.println(String.format("{\"Status\": \"OK\"}"));
								break;
							case "get_image":
								resultSet  = stmt.executeQuery(String.format("SELECT * FROM `images` WHERE `image`= '%s'", json.getString("Image")));
								File imageFile = new File("123.jpg");
								byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
								String imageEncoded = Base64.getEncoder().encodeToString(imageBytes);
								BigDataSender imageSender = new BigDataSender(client, input, output, imageEncoded, "get_image");
								imageSender.send();
//								output.print(String.format("{\"Status\": \"OK\", \"Image\": \"%s\"}", imageEncoded));
								break;
							case "upload_profile_image":
								byte profile_image[];
								profile_image = Base64.getDecoder().decode(splittedData);
								splittedData = "";
								parts = 0;
								String profile_image_name = userLogin + Long.toString(ZonedDateTime.now().toInstant().toEpochMilli()) + ".jpg";
								FileOutputStream profileImageWriter = new FileOutputStream(profile_image_name);
								profileImageWriter.write(profile_image);
								profileImageWriter.flush();
								stmt.executeUpdate(String.format("UPDATE `users` SET `profilePhoto`= '%s' WHERE `id`= %s", profile_image_name, userID));
								break;
							case "get_profile_image":
								resultSet = stmt.executeQuery(String.format("SELECT `profilePhoto` FROM `users` WHERE `id` = %s ", userID));
								resultSet.next();
								BigDataSender sender = new BigDataSender(client, input, output, Base64.getEncoder().encodeToString(Files.readAllBytes((new File(resultSet.getString(1)).toPath()))) ,"get_profile_photo");
								sender.send();
								break;
							case "do_math":
								System.out.println(doMath(json.getDouble("Velocity"), json.getDouble("Position"), json.getDouble("W")));
								break;
							case "display_rules":
								resultSet = stmt.executeQuery("select * from 'users' where 1");
								ResultSetMetaData rsmt = resultSet.getMetaData(); 
								while(resultSet.next()) {
									for(int i = 0; i < rsmt.getColumnCount(); i++) {
										output.println(resultSet.getString(i) + " ");
									}
									output.println();
								}
								break;
							case "get_base":
									int start_idx = json.getInt("StartIdx");
									int end_idx = json.getInt("EndIdx");
									int rules_count = 0;
									System.out.println( Base64.getEncoder().encodeToString(Files.readAllBytes((new File("rule1_2.jpg")).toPath())).length());
									resultSet = stmt.executeQuery(String.format("SELECT COUNT(*) FROM rules WHERE ((type= \"public\" OR username = \"%s\") AND status > 0) LIMIT %s, %s", userLogin, start_idx, end_idx - start_idx ));
									resultSet.next();
									rules_count = resultSet.getInt(1);
									resultSet = stmt.executeQuery(String.format("SELECT * FROM rules WHERE ((type= \"public\" OR username = \"%s\") AND status > 0 ) LIMIT %s, %s", userLogin, start_idx, end_idx - start_idx ));
									JSONObject rules_info = new JSONObject();
									rules_info.put("Command", "send_rule");
									rules_info.put("RulesCount", rules_count);
									rules_info.put("Status", "OK");
									output.println(rules_info);
									output.flush();
									while(resultSet.next()) {
										JSONObject rule_info = new JSONObject();
										rule_info.put("Username", resultSet.getString(2));
										rule_info.put("Base", resultSet.getString(4));
										rule_info.put("Description", resultSet.getString(9));
										rule_info.put("RuleID", resultSet.getInt(1));
										rule_info.put("isSaved", isSaved(userID, resultSet.getInt(1)));
										output.println(rule_info);
										output.flush();
										String image_r = Base64.getEncoder().encodeToString(Files.readAllBytes((new File(resultSet.getString(5))).toPath()));
										BigDataSender img_sender = new BigDataSender(client, input, output, image_r, "send_image_xxx");
										img_sender.send();
										image_r = Base64.getEncoder().encodeToString(Files.readAllBytes((new File(resultSet.getString(6))).toPath()));
										img_sender = new BigDataSender(client, input, output, image_r, "send_image_vvv");
										img_sender.send();
										image_r = Base64.getEncoder().encodeToString(Files.readAllBytes((new File(resultSet.getString(7))).toPath()));
										img_sender = new BigDataSender(client, input, output, image_r, "send_image_www");
										img_sender.send();
									}
//									output.println("{\"Status\": \"OK\"}");
//									output.flush();
									logger.info("Rules base sent to: " + clientIP + "(logged in as " + userLogin + ")");
									break;
							case "send_question":

//							userPassword = sha256(json.getString("Password"));
							MailSender.sendMail(json.getString("Question") + "; Reply to: " + json.getString("Email"), "Fuzzy question", "korobovtsevaolga@yandex.ru");
							break;
							case "get_saved_rules":
								start_idx = json.getInt("StartIdx");
								end_idx = json.getInt("EndIdx");
								rules_count  = 0;
//								resultSet = stmt.executeQuery(String.format("SELECT COUNT(*) FROM saved_rules WHERE `id` >= %s and `id` <= %s AND (`userID` = \"%s\")", start_idx, end_idx, userID));
								resultSet = stmt.executeQuery(String.format("SELECT COUNT(*) FROM saved_rules WHERE (`userID` = \"%s\") LIMIT %s, %s", userID, start_idx, end_idx - start_idx));
								resultSet.next();
								rules_count  = resultSet.getInt(1);
								rules_info = new JSONObject();
								rules_info.put("Status", "OK");
								rules_info.put("Command", "send_saved_rules");
								rules_info.put("RulesCount", rules_count);
								output.println(rules_info);
								output.flush();
								Statement stmt1 = db_connection.createStatement();
//								resultSet = stmt.executeQuery(String.format("SELECT * FROM saved_rules WHERE `id` >= %s and `id` <= %s AND (`userID` = \"%s\")", start_idx, end_idx, userID));
								resultSet = stmt.executeQuery(String.format("SELECT * FROM saved_rules WHERE (`userID` = \"%s\") LIMIT %s, %s",  userID, start_idx, end_idx - start_idx));
								while(resultSet.next()) {
									ResultSet tempResultSet;
									tempResultSet = stmt1.executeQuery(String.format("SELECT `userID`, `rule`, `xxx`, `vvv`, `www`, `description` FROM rules WHERE `id`=%s", resultSet.getInt(3)));
									tempResultSet.next();
									JSONObject rule_info = new JSONObject();
									rule_info.put("Username", getUsername(tempResultSet.getInt(1)));
									rule_info.put("Base", tempResultSet.getString(2));
									rule_info.put("Decscription", tempResultSet.getString(6));
									rule_info.put("RuleID", tempResultSet.getInt(1));
									rule_info.put("isSaved", true);
									output.println(rule_info);
									output.flush();
									String image_r = Base64.getEncoder().encodeToString(Files.readAllBytes((new File(tempResultSet.getString(3))).toPath()));
									BigDataSender img_sender = new BigDataSender(client, input, output, image_r, "send_image_xxx");
									img_sender.send();
									image_r = Base64.getEncoder().encodeToString(Files.readAllBytes((new File(tempResultSet.getString(4))).toPath()));
									img_sender = new BigDataSender(client, input, output, image_r, "send_image_vvv");
									img_sender.send();
									image_r = Base64.getEncoder().encodeToString(Files.readAllBytes((new File(tempResultSet.getString(5))).toPath()));
									img_sender = new BigDataSender(client, input, output, image_r, "send_image_www");
									img_sender.send();
								}
								//output.println("{\"Status\":\"OK\"}");
								break;
							case "get_my_rules":
									start_idx = json.getInt("StartIdx");
									end_idx = json.getInt("EndIdx");
									rules_count = 0;
									System.out.println( Base64.getEncoder().encodeToString(Files.readAllBytes((new File("rule1_2.jpg")).toPath())).length());
									resultSet = stmt.executeQuery(String.format("SELECT COUNT(*) FROM rules WHERE (username = \"%s\" AND status > 0) LIMIT %s, %s", userLogin, start_idx, end_idx - start_idx ));
									resultSet.next();
									rules_count = resultSet.getInt(1);
									resultSet = stmt.executeQuery(String.format("SELECT * FROM rules WHERE (username = \"%s\" AND status > 0 ) LIMIT %s, %s", userLogin, start_idx, end_idx - start_idx ));
									rules_info = new JSONObject();
									rules_info.put("Command", "send_rule");
									rules_info.put("RulesCount", rules_count);
									rules_info.put("Status", "OK");
									output.println(rules_info);
									output.flush();
									while(resultSet.next()) {
										JSONObject rule_info = new JSONObject();
										rule_info.put("Username", resultSet.getString(2));
										rule_info.put("Base", resultSet.getString(4));
										rule_info.put("Description", resultSet.getString(9));
										rule_info.put("RuleID", resultSet.getInt(1));
										rule_info.put("isSaved", isSaved(userID, resultSet.getInt(1)));
										output.println(rule_info);
										output.flush();
										String image_r = Base64.getEncoder().encodeToString(Files.readAllBytes((new File(resultSet.getString(5))).toPath()));
										BigDataSender img_sender = new BigDataSender(client, input, output, image_r, "send_image_xxx");
										img_sender.send();
										image_r = Base64.getEncoder().encodeToString(Files.readAllBytes((new File(resultSet.getString(6))).toPath()));
										img_sender = new BigDataSender(client, input, output, image_r, "send_image_vvv");
										img_sender.send();
										image_r = Base64.getEncoder().encodeToString(Files.readAllBytes((new File(resultSet.getString(7))).toPath()));
										img_sender = new BigDataSender(client, input, output, image_r, "send_image_www");
										img_sender.send();
									}
//									output.println("{\"Status\": \"OK\"}");
//									output.flush();
									logger.info("Rules base sent to: " + clientIP + "(logged in as " + userLogin + ")");
									break;
							case "save_rule":
								int rule_id = json.getInt("RuleID");
								if(!isSaved(userID, rule_id))
								stmt.executeUpdate(String.format("INSERT INTO saved_rules (`userID`, `ruleID`) VALUES(%s, %s) ", userID, rule_id));
								//output.println("{\"Status\":\"OK\"}");
								break;
							case "delete_saved_rule":
								rule_id = json.getInt("RuleID");
								stmt.executeUpdate(String.format("DELETE FROM saved_rules WHERE `ruleID`=%s AND `userID`=%s", rule_id, userID));
								output.println("{\"Status\":\"OK\"}");
								break;
							case "get_rules_count":
								resultSet = stmt.executeQuery(String.format("SELECT COUNT(*) FROM rules WHERE ((type= \"public\" OR username = \"%s\") AND status > 0)", userLogin));
								resultSet.next();
								json.put("Status", "OK");
								json.put("RulesCount", resultSet.getInt(1));
								output.println(json);
								break;
							case "get_my_rules_count":
								resultSet = stmt.executeQuery(String.format("SELECT COUNT(*) FROM rules WHERE ((username = \"%s\") AND status > 0)", userLogin));
								resultSet.next();
								json.put("Status", "OK");
								json.put("RulesCount", resultSet.getInt(1));
								output.println(json);
								break;
							case "get_saved_rules_count":
								resultSet = stmt.executeQuery(String.format("SELECT COUNT(*) FROM saved_rules WHERE (userID = \"%s\")", userID));
								resultSet.next();
								json.put("Status", "OK");
								json.put("RulesCount", resultSet.getInt(1));
								output.println(json);
								break;
							
							case "upload_base":
//								client.setSoTimeout(10000);
//								RulesBase base  = new RulesBase();
//								base.recieve(userLogin, userID, input, output, stmt);
//								client.setSoTimeout(300000);
								String images_json = new String(Base64.getDecoder().decode(splittedData.getBytes()));
								System.out.println(images_json);
								JSONObject images = new JSONObject(images_json);
								byte[] image_xxx = Base64.getDecoder().decode(images.getString("xxx"));
								byte[] image_vvv = Base64.getDecoder().decode(images.getString("vvv"));
								byte[] image_www = Base64.getDecoder().decode(images.getString("www"));
								String title = images.getString("Title");
								String description = images.getString("Description");
								String rule = images.getString("Rule");
								String type = images.getString("Type");
								String xxx_name = "xxx" + userLogin + ZonedDateTime.now().toInstant().toEpochMilli() + ".jpg";
								String vvv_name = "vvv" + userLogin + ZonedDateTime.now().toInstant().toEpochMilli() + ".jpg";
								String www_name = "www" + userLogin + ZonedDateTime.now().toInstant().toEpochMilli() + ".jpg";
								FileOutputStream image_writer = new FileOutputStream(xxx_name);
								image_writer.write(image_xxx);
								image_writer.flush();
								image_writer = new FileOutputStream(vvv_name);
								image_writer.write(image_vvv);
								image_writer.flush();
								image_writer = new FileOutputStream(www_name);
								image_writer.write(image_www);
								image_writer.flush();
								stmt.executeUpdate(String.format("INSERT INTO `rules`(`username`, `type`, `rule`, `xxx`, `vvv`, `www`, `userID`, `description`) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s')", userLogin, type, rule, xxx_name, vvv_name, www_name, userID, description));
								 
								break;
							case "get_profile_info":
								resultSet = stmt.executeQuery(String.format("SELECT COUNT(*) FROM rules WHERE `userID`=\"%s\"", userID));
								resultSet.next();
								int myRules = resultSet.getInt(1);
								resultSet = stmt.executeQuery(String.format("SELECT COUNT(*) FROM saved_rules WHERE `userID`=\"%s\"", userID));
								resultSet.next();
								int savedRules = resultSet.getInt(1);
								response = new JSONObject();
								response.put("Status", "OK");
								response.put("myRules", myRules);
								response.put("savedRules", savedRules);
								output.println(response);
								break;
							case "select_rule":
								resultSet = stmt.executeQuery(String.format("SELECT * FROM `rules` WHERE `id` = %s", json.getInt("RuleID")));
								output.println(resultSet.getString(3));
								break;
							case "optimize":
							resultSet = stmt.executeQuery(String.format("SELECT COUNT(*) from rules WHERE title='%s' ", json.getString("Title")));
							resultSet.next();
							if(resultSet.getInt(1) > 0)
								output.print("{\"Status\": \"Rule_exists\"}");
							else{
								rg.execute(new Optimization(userLogin, userID, json.getFloat("x_start"), json.getFloat("x_finish"),json.getInt("iterations"), json.getFloat("step"), json.getString("Title"), json.getString("type"), json.getString("Type"), db_connection));
								output.print("{\"Status\": \"OK\"}");
							}
								break;
							case "get_optimizing_rules":
								resultSet = stmt.executeQuery(String.format("SELECT * FROM `rules` WHERE `userID`='%s' AND (`status`=0 OR `status` = 1)", userID));
								String rules = "";
								while(resultSet.next()) {
									JSONObject t = new JSONObject();
									t.put("Title", resultSet.getString(jsonLength));
								}
								break;
							case "get_username":
								String username = "";
								response.put("Status", "OK");
								response.put("Username", getUsername(json.getInt("UserID")));
								output.println(response);
								break;
							case "logout":
								status = 1;
								userLogin = "";
								userPassword = "";
								userEmail = "";
								output.println("{\"Status\":\"OK\"}");
								break;
							case "close_session":
								running = false;
								break;
						}
					}
					break;
					}
				}
				catch(JSONException | SQLException | NoSuchAlgorithmException e){
					OK = false;
					logger.warning("Someting went wrong!\n" + e.toString());
					e.printStackTrace();
				}
				if(!OK) {
					output.println("HTTP/1.1 400");
					output.println("Context-Type: text/html; charset=utf-8");
					output.println();
					output.println("<h1>400</h1>");
				}
				output.flush();
				}
			} 
			catch(IOException e){
				logger.info("Timeouted or session terminated with " + clientIP + "\n" + e.toString());
				e.printStackTrace();
			}
		//}
		try {
			client.close();
			logger.info("Closed connection with: " + clientIP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	boolean isSaved(int userID, int ruleID) throws SQLException {
		Statement tempStatement  = db_connection.createStatement();
		ResultSet userResultSet;
		try {
		userResultSet = tempStatement.executeQuery(String.format("SELECT COUNT(*) FROM saved_rules WHERE `userID`=%s AND `ruleID`=%s", userID, ruleID));
		userResultSet.next();
		return userResultSet.getInt(1) > 0?true:false;
		}
		catch(Exception e) {
			return false;
		}
	}
	static String getUsername(int id) throws SQLException, IOException {
		Statement tempStatement  = db_connection.createStatement();
		ResultSet userResultSet;
		userResultSet = tempStatement.executeQuery(String.format("SELECT `username` FROM `users` WHERE `id`=%s", id));
		userResultSet.next();
		if(userResultSet == null)
			throw new IOException("User does not exist");
		return userResultSet.getString(1);
	}
}