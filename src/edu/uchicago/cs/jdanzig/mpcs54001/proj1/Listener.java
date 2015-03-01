package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.cert.*;
import javax.net.ssl.*;

import java.security.*;

public class Listener extends Thread {
	
	public int port;
	public boolean secure;
	public Listener(int port, boolean secure) {
		this.port = port;
		this.secure = secure;
	}
	
	public void run () {
		ServerSocket server;
		Socket client;
		SSLSession sslSession;
		String password = "codyjon";
		
		try{
			if (this.secure){ //use SSL
				KeyStore store = KeyStore.getInstance("JKS");
				store.load(new FileInputStream("./server.jks"), password.toCharArray());
				KeyManagerFactory factory = KeyManagerFactory.getInstance("SunX509");
				factory.init(store, password.toCharArray());
				SSLContext context = SSLContext.getInstance("SSL");
				context.init(factory.getKeyManagers(), null, null);
				SSLServerSocketFactory sslFactory = context.getServerSocketFactory();
				server = sslFactory.createServerSocket(this.port);
				
			} else { //server is a regular old socket port, not using SSL
				server = new ServerSocket(this.port);
			}
			client = server.accept(); 
			if (this.secure) sslSession = ((SSLSocket)client).getSession();
			
			DataOutputStream out;
			BufferedReader in;
			Request req = null;
			Response resp;
			while (true) {
				out = new DataOutputStream(client.getOutputStream());
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				try {
					req = new Request(in.readLine());
					while (req.readHeader(in.readLine())) {}
					resp = new Response(req);
					resp.show(out);
					if (!req.persist()) client.close();
				} catch (HTTPErrorException exp) {
					System.err.printf("Respond with HTTP Error: %d\n",
							exp.getHttpStatusCode());
					resp = (req == null) ? new Response() : new Response(req);
					resp.showError(out, exp);
					client.close();
				}
			}
		} catch (IOException x) {
			System.err.printf("1Exception: %s", x);
		}
		catch (UnrecoverableKeyException x) {
		System.err.printf("2Exception: %s", x);
		}
		catch (CertificateException x) {
			System.err.printf("3Exception: %s", x);
			}
		catch (KeyStoreException x) {
			System.err.printf("4Exception: %s", x);
		}
		catch (KeyManagementException x) {
			System.err.printf("5Exception: %s", x);
		}
		catch (NoSuchAlgorithmException x) {
			System.err.printf("6Exception: %s", x);
		}
	}
}
