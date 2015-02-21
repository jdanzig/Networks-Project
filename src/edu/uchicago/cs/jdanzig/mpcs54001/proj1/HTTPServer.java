package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer{
	public static void main(String[] args) {
		CommandLineOptions options = new CommandLineOptions(args);
		Listener HTTPListener = new Listener();
		Listener HTTPSListener = new Listener();
		HTTPListener.run(options.port, false);	
		//HTTPSListener.run(options.sslPort, true);
	}
}
