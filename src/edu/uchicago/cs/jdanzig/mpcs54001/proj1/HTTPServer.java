package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer{
	public static void main(String[] args) {
		CommandLineOptions options = new CommandLineOptions(args);
		Listener HTTPListener = new Listener(options.port,false);
		Listener HTTPSListener = new Listener(options.sslPort,true);
		HTTPListener.start();	
		HTTPSListener.start();
	}
}
