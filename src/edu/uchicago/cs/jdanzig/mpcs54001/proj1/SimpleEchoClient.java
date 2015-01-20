// Partially adapted from Java Tutorials Code Sample – EchoClient.java

package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class SimpleEchoClient {

	public static void main(String[] args) {
		CommandLineParser parser = new GnuParser();

		Options options = new Options();
		Option ipOption = new Option( "i", "serverIP", true, "ip to listen on" );
		ipOption.setRequired(true);
		options.addOption(ipOption);
		Option portOption = new Option( "p", "serverPort", true, "port to listen on" );
		portOption.setRequired(true);
		options.addOption(portOption);

		String ip = "";
		int port = 0;
		try {
			// parse the command line arguments
			CommandLine line = parser.parse( options, args );
			ip += line.getOptionValue("serverIP");
			port = Integer.parseInt(line.getOptionValue( "serverPort" ));
		}
		catch( ParseException exp ) {
			// oops, something went wrong
			System.err.println( "Parsing failed. Reason: " + exp.getMessage() );
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "java edu.uchicago.cs.jdanzig.mpcs54001.proj0.SimpleEchoClient", options );
			System.exit(1);
		}
		try {
			Socket echoSocket = new Socket(ip, port);
			PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
				
			String userInput;
			while ((userInput = stdIn.readLine()) != null) {
				out.println(userInput);
				System.out.println("echo: " + in.readLine());
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + ip);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + ip);
			System.exit(1);
		}


		System.exit(0);
	}

}
