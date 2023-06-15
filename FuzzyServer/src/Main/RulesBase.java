package Main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Logger;
import org.json.JSONObject;

public class RulesBase{
		private String xxxName = "";
		private String vvvName = "";
		private String wwwName = "";
		private static Logger logger = Main.logger;
		private void reset() {
			xxxName = "";
			vvvName = "";
			wwwName = "";
		}
		public void recieve(String username, int userID, BufferedReader input, PrintWriter output, Statement stmt) {
			try {
				JSONObject response = new JSONObject();
				JSONObject baseInfo = new JSONObject(input.readLine());
				for(int i = 0; i < 3; i++) {
					JSONObject imageInfo = new JSONObject(input.readLine());
					String splittedImage = "";
					int parts = imageInfo.getInt("Parts");
					int lastPart = 0;
					for(int j = 1; j < parts; j++) {
						JSONObject temp = new JSONObject(input.readLine());
						if(temp.getInt("Part") != lastPart + 1) {
							j--;
							output.println("{\"Status\": \"missing_part\", \"Part\": " + Integer.toString(lastPart + 1) + "}");
							output.flush();
							continue;
						}
						splittedImage += temp.getString("SplittedData");
						output.println("{\"Status\": \"OK\"}");
						output.flush();
						
					}
					String imageName = Long.toString(ZonedDateTime.now().toInstant().toEpochMilli()) + ".jpg";
					FileOutputStream imageWriter = new FileOutputStream(imageName);
					imageWriter.write(Base64.getDecoder().decode(splittedImage));
					imageWriter.flush();
					switch(i) {
					case 0:
						xxxName = imageName;
						break;
					case 1:
						vvvName = imageName;
						break;
					case 2:
						wwwName = imageName;
						break;
					}
				}
			
				stmt.executeUpdate(String.format("INSERT INTO `rules`(`username`, `type`, `rule`, `xxx`, `vvv`, `www`, `userID`, `description`) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %s, '%s')", username, baseInfo.getString("Type"), baseInfo.getString("Base"), xxxName, vvvName, wwwName, userID, baseInfo.getString("Description")));
			}
			catch(Exception e){
				logger.warning("Something went wrong:\n" + e.toString());
			}
			
		reset();
		logger.info("Got base of rules!");
	}
}