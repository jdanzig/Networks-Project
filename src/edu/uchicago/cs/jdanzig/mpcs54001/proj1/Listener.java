package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

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
		ServerSocket server = null;
		SSLServerSocket sslServer = null;
		Socket client;
		String password = "codyjon";
		Boolean running;
		running = true;
		try{
			if (this.secure){ //use SSL
				KeyStore store = KeyStore.getInstance("JKS");
				store.load(new FileInputStream("server.jks"), password.toCharArray());
				KeyManagerFactory factory = KeyManagerFactory.getInstance("SunX509");
				factory.init(store, password.toCharArray());
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(factory.getKeyManagers(), null, null);
				SSLServerSocketFactory sslFactory = context.getServerSocketFactory();
				sslServer = (SSLServerSocket) sslFactory.createServerSocket(this.port);
				System.out.print("SECURE SOCKET ESTABLISHED\n");
				client= sslServer.accept();
			} else { //server is a regular old socket port, not using SSL
				server = new ServerSocket(this.port);
				System.out.print("STANDARD SOCKET ESTABLISHED\n");
				client = server.accept();
			}

			while(running){
				//if (this.secure) sslSession = ((SSLSocket)client).getSession();
				DataOutputStream out;
				BufferedReader in;
				Request req = null;
				Response resp;
				out = new DataOutputStream(client.getOutputStream());
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				try {
					req = new Request(in.readLine());
					while (req.readHeader(in.readLine())) {}
					resp = new Response(req);
					resp.show(out);
					if (!req.persist()){
						client.close();
						if (!this.secure){
						server.close();
						System.out.print("CLOSED STANDARD CONNECTION\n"); 
						}
						else{
							sslServer.close();
							System.out.print("CLOSED SECURE CONNECTION\n");
						}
						this.run();
					}
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
