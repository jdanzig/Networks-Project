package edu.uchicago.cs.jdanzig.mpcs54001.proj1;
import org.apache.commons.cli.*;
public class CommandLineOptions {
	public int port;
	public int sslPort;
	private static CommandLineParser commandLineParser = new GnuParser();
	private static HelpFormatter helpFormatter = new HelpFormatter();
	private static Options options;
	private static Options getOptions() {
		if(options == null) {
			options = new Options();
			Option httpPortOption = new Option("p", "serverPort", true, "HTTP port to listen on");
			//httpPortOption.setRequired(true);
			options.addOption(httpPortOption);
			Option httpsPortOption = new Option("sp", "sslServerPort", true, "HTTPS port to listen on");
			//httpsPortOption.setRequired(true);
			options.addOption(httpsPortOption);
		}
		return options;
	}
	public CommandLineOptions(String[] args) {
		CommandLine line = null;
		try {
			line = commandLineParser.parse(getOptions(), args);
		} catch(ParseException exp) {
			help();
		}
		try {
			if (line.getOptionValue("serverPort") != null)
				port = Integer.parseInt(line.getOptionValue("serverPort"));
		} catch (NumberFormatException exp) {
			System.err.printf("Invalid port: %s", line.getOptionValue("serverPort"));
		}
		try {
			if (line.getOptionValue("sslServerPort") != null)
				sslPort = Integer.parseInt(line.getOptionValue("sslServerPort"));
		} catch (NumberFormatException exp) {
			System.err.printf("Invalid SSL port: %s", line.getOptionValue("sslServerPort"));
		}
		if (port == 0 && sslPort == 0) {
			System.err.printf("You must specify at least one port to listen on.");
			help();
		}
	}
	public static void help() {
		helpFormatter.printHelp("java -jar project1.jar [OPTIONS]", getOptions());
	}
}