package Main;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.util.logging.*;
import java.net.ServerSocket; 

public class Main {
	public static Logger logger;
	static FileHandler fh;
	public static boolean DEBUG = true; 
	public static void main(String[] args) {
		try {
			logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
			fh = new FileHandler(ZonedDateTime.now().toInstant().toString().replaceAll(":", ".") + ".log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			System.out.println(System.getProperty("user.dir"));
			ExecutorService ex = Executors.newFixedThreadPool(10);
			ExecutorService rg = Executors.newFixedThreadPool(5);
//			SSLContext sslContext = SSLContext.getInstance("TLS");
//			char[] pass = "Password".toCharArray();
//			KeyStore ks = KeyStore.getInstance("JKS");
//			FileInputStream fis = new FileInputStream("ssl.keystore");
//			ks.load(fis, pass);
//			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
//			kmf.init(ks, pass);
//			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
//			tmf.init(ks);
//			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
//			SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
			logger.info("Server started!");
			while(true) {
				try(ServerSocket server = ServerSocketFactory.getDefault().createServerSocket(22345)){
					Socket socket = server.accept();
					socket.setSoTimeout(300000);
					ex.execute(new Handler(socket, rg));
					
				}
				catch(IOException e){
					logger.warning(e.toString());
				}
			}
		}catch(Exception e) {
			logger.severe(e.toString());
			e.printStackTrace();
		}
	}
}
