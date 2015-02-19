package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener implements Runnable {
	
	public static void run (int port, boolean secure) {
		boolean running;
		running = true;
		try {
			ServerSocket server = new ServerSocket(port);
			while (running) {
				Socket client = server.accept();
				DataOutputStream out = new DataOutputStream(
						client.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(
						client.getInputStream()));
				Request req = null;
				Response resp;
				try {
					req = new Request(in.readLine());
					while (req.readHeader(in.readLine())) {
					}
					resp = new Response(req);
					resp.show(out);
					if (!req.persist()){
					client.close();
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
			System.err.printf("Exception: %s", x);
		}
	}

}
