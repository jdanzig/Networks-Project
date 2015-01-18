// Partially adapted from Java Tutorials Code Sample – EchoServer.java

package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

import java.net.*;
import java.io.*;
import org.apache.commons.cli.*;

public class SimpleEchoServer {

	public static void main(String[] args) {
		CommandLineParser parser = new GnuParser();

		Options options = new Options();
		Option portOption = new Option( "p", "port", true, "port to listen on" );
		portOption.setRequired(true);
		options.addOption(portOption);

		int port = 0;
		try {
			// parse the command line arguments
			CommandLine line = parser.parse( options, args );
			port = Integer.parseInt(line.getOptionValue( "port" ));
		}
		catch( ParseException exp ) {
			// oops, something went wrong
			System.err.println( "Parsing failed. Reason: " + exp.getMessage() );
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "java edu.uchicago.cs.jdanzig.mpcs54001.proj0.SimpleEchoServer", options );
			System.exit(1);
		}
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			Socket clientSocket = serverSocket.accept();     
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);                   
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) out.println(inputLine);
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
			System.out.println(e.getMessage());
		}

		System.exit(0);
	}


}
