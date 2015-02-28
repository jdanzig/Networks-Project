package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;

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
		try{
			if (this.secure){ //use SSL
				SSLServerSocketFactory sslFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
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
			System.err.printf("Exception: %s", x);
		}
	}
}
