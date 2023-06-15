package Main;

import java.io.IOException;
import java.util.logging.Logger;
import java.io.*;
public class MailSender {
	private static Logger logger = Main.logger;
	public static void sendMail(String data, String subject, String email) {
		try {
			System.out.println("Mail");
			Process sender = (new ProcessBuilder("python", "mail_sender.py")).start();
			BufferedReader input = new BufferedReader(new InputStreamReader(sender.getInputStream()));
			PrintWriter output = new PrintWriter(new OutputStreamWriter(sender.getOutputStream()));
			output.write(data + "\n");
			output.write(subject + "\n");
			output.write(email + "\n");
			output.flush();
			logger.info("Sent email letter to " + email);
			
		} catch (IOException e) {
			logger.warning("Exception while sending email: \n" + e.toString());
			e.printStackTrace();
		}
	}
}
