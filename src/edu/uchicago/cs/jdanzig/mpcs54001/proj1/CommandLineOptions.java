package edu.uchicago.cs.jdanzig.mpcs54001.proj1;
import org.apache.commons.cli.*;

public class CommandLineOptions {
	
	public int port;
	
	private static CommandLineParser commandLineParser = new GnuParser();
	private static HelpFormatter helpFormatter = new HelpFormatter();
	private static Options options;
	
	private static Options getOptions() {
		if(options == null) {
			options = new Options();
			Option portOption = new Option("p", "port", true, "port to listen on");
			portOption.setRequired(true);
			options.addOption(portOption);
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
			port = Integer.parseInt(line.getOptionValue("port"));
		} catch (NumberFormatException exp) {
			System.err.printf("Invalid port: %s", line.getOptionValue("port"));
		}
	}
	
	public static void help() {
		helpFormatter.printHelp("java -jar project1.jar [OPTIONS]", getOptions());
	}
}
