package Main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.json.JSONObject;

public class BigDataSender{
	private Socket client;
	private String data;
	private String command;
	private BufferedReader input;
	private PrintWriter output;
	private int parts = 0;
	private ArrayList<String> dataParts;
	private Logger logger = Main.logger;
	ArrayList<String> dataSplitter(String d, int blockSize){
		parts = d.length() / blockSize + (((d.length() % blockSize) > 0)?1:0);
		ArrayList <String> result = new ArrayList<String>();
		for(int i = 0; i < parts; i++) {
			result.add(d.substring(i * blockSize, Math.min((i + 1) * blockSize, d.length())));
		}
		return result;
	}
	BigDataSender(Socket sock, BufferedReader in, PrintWriter out, String d, String c){
		client = sock;
		output = out;
		input = in;
		data = d;
		command = c;
		dataParts = dataSplitter(data, 4096);
	}
	public void send() {
		try {
			JSONObject header  = new JSONObject();
			header.put("Command", "splitted_data");
			header.put("SplittedCommand", command);
			header.put("Parts", parts);
			BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8)); 
			PrintWriter output = new PrintWriter(client.getOutputStream());
			output.println(header.toString());
			System.out.println(header);
			System.out.println(header.length());
			output.flush();
			outer:
			for(int i = 0; i < parts; i++) {
				JSONObject body = new JSONObject();
				body.put("Command", "splitted_data");
				body.put("SplittedCommand", "continue");
				System.out.println(i);
				body.put("Part", i);
				body.put("SplittedData", dataParts.get(i));
				System.out.println(body);
				output.println(body.toString());
				output.flush();
//				output.print(body.toString());
				CharBuffer charb = CharBuffer.allocate(10000);
				String temp;
				JSONObject response = null;
				long begin = ZonedDateTime.now().toInstant().toEpochMilli();
				System.out.println(input);
//				while(!input.ready());
//				input.read(charb);
//				while((temp = input.readLine()) == null);
//				temp = input.readLine();
//				temp = new String(charb.array());
//				System.out.println(23212134);
//				System.out.println(temp);
//				response = new JSONObject(temp);
//				if(ZonedDateTime.now().toInstant().toEpochMilli() - begin > 10000) {
//					System.out.println("Timeouted");
//					output.println("{\"Status\": \"timeouted\"}");
//	 				output.flush();
//					break outer;
//				}
//				
//				if(!response.getString("Status").equals("OK")) {
//					i--;
//					System.out.println("i decreased");
//				}
				
			}
			logger.info("Big data sent successfully!");
			//return true;
		}catch(Exception e){
			logger.warning("Exception while sending big data:\n" + e.toString());
			e.printStackTrace();
		}
	}
	
}