package project1;

import java.io.*;
import java.util.regex.*;
import java.util.Hashtable;

public class Redirects {
	private static final String REDIRECT_DEF_FILE = "www/redirect.defs";
	private static final Pattern REDIRECT_MAPPING = Pattern.compile("^([^\\s]+)\\s(.*)$");
	private static Hashtable<String,String> mappings;

	private static void setup() throws HTTPErrorException {
		if(mappings != null) return;
		mappings = new Hashtable<String,String>();
		FileInputStream fstream;
		BufferedReader br;
		String line;
		Matcher m;
		try {
			fstream = new FileInputStream(REDIRECT_DEF_FILE);
			br = new BufferedReader(new InputStreamReader(fstream));
			while ((line = br.readLine()) != null) {
				addMapping(line);
			}
			br.close();
			fstream.close();
		} catch (Exception e) {
			throw new HTTPErrorException(500);
		}
	}
	
	private static boolean addMapping(String mapping) {
		Matcher m = REDIRECT_MAPPING.matcher(mapping);
		if(!m.matches()) return false;
		String from = m.group(1).trim();
		String to = m.group(2).trim();
		mappings.put(from, to);
		return true;
	}
	
	public static boolean isRedirect(String path) throws HTTPErrorException {
		setup();
		return mappings.containsKey(path);
	}
	
	public static String getRedirect(String path) throws HTTPErrorException {
		setup();
		return mappings.get(path);
	}
}
