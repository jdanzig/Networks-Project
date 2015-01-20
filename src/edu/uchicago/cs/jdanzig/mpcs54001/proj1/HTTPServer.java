package edu.uchicago.cs.jdanzig.mpcs54001.proj1;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class HTTPServer
{
	public static void main(String[] args) {
		CommandLineOptions options = new CommandLineOptions(args);
		listen(options.port);
	}

	private static void listen(int port) {
		try {
			ServerSocket server = new ServerSocket(port);
			Socket client = server.accept();
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			Request req;
			try {
				req = new Request(in.readLine());
				while(req.readHeader(in.readLine())) { }
			} catch (HTTPErrorException exp) {
				System.err.printf("Respond with HTTP Error: %i\n", exp.getHttpStatusCode());
				//TODO: actually respond
			}
			//TODO: read values from req to determine how to proceed
			//TODO: create new Response class
		} catch (IOException x) {
			System.err.printf("Exception: %s", x);
		}
	}


}
