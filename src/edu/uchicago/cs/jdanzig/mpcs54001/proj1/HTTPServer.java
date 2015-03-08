package edu.uchicago.cs.jdanzig.mpcs54001.proj1;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class HTTPServer{
	public static void main(String[] args) {
		CommandLineOptions options = new CommandLineOptions(args);
		if (options.port >= 1024) {
			Listener HTTPListener = new Listener(options.port,false);
			HTTPListener.start();
		}
		if (options.sslPort >= 1024 ) {
			Listener HTTPSListener = new Listener(options.sslPort,true);
			HTTPSListener.start();
		}
	}
}