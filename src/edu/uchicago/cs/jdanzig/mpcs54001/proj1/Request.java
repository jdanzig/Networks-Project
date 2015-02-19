package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

import java.util.regex.*;
import java.util.Hashtable;
import java.io.*;

public class Request {
	private RequestMethod requestMethod;
	private ConnectionMethod connectionMethod;
	private String path;
	private Hashtable<String, String> headers;
	private float httpVersion;
	private static Pattern requestLinePattern = Pattern
			.compile("^([A-Z]+)\\s([^\\s]+)\\sHTTP\\/([0-9]\\.[0-9])$");
	private static Pattern headerLinePattern = Pattern
			.compile("^([^\\s:]+):\\s*(.*)\\s*$");
	private static Pattern safePathPattern = Pattern
			.compile("^(\\/(?:[A-Za-z0-9]|(?<!\\.)\\.|(?<!\\/)\\/)*)$");

	public Request(String requestLine) throws HTTPErrorException {
		Matcher m = requestLinePattern.matcher(requestLine);
		
		if (!m.matches()) {
			System.err.printf("Could not parse request line: %s\n", requestLine);
			throw new HTTPErrorException(400);
		}
		try {
			requestMethod = RequestMethod.valueOf(m.group(1));
		} catch (IllegalArgumentException exp) {
			System.err.printf("Invalid request method: %s\n", m.group(1));
			throw new HTTPErrorException(403);
		}
		try {
			path = m.group(2);
			// If the path doesn't match this pattern, it could be a huge
			// security hole
			Matcher pathMatcher = safePathPattern.matcher(path);
			if (!pathMatcher.matches())
				throw new IllegalArgumentException(path);
		} catch (IllegalArgumentException exp) {
			System.err.printf("Malformed Pathname: %s\n", m.group(2));
			throw new HTTPErrorException(406);
		}
		try {
			httpVersion = Float.parseFloat(m.group(3));
		} catch (NumberFormatException exp) {
			System.err.printf("Invalid HTTP Version: %s\n", m.group(3));
			throw new HTTPErrorException(505);
		}
		headers = new Hashtable<String, String>();
	}

	public boolean readHeader(String headerLine) throws HTTPErrorException {
		if (headerLine.length() == 0) {
			return false;
		}
		Matcher m = headerLinePattern.matcher(headerLine);
		if (!m.matches()) {
			System.err.printf("Invalid Header Transmitted: %s\n", headerLine);
			throw new HTTPErrorException(400);
		}
		headers.put(m.group(1).trim().toLowerCase(), m.group(2).trim());
		return true;
	}

	public float getHttpVersion() {
		return httpVersion;
	}

	public String getRequestMethod() {
		return requestMethod.toString();
	}
	
	public boolean persist() {
		System.out.print(this.headers.get("connection"));
		if (headers.containsKey("connection") & headers.get("connection")=="Keep-Alive"){
			return true;
		}
		else{
			return false;
		}
		
	}

	public String getPath() {
		return path.toString();
	}

	public String getHeader(String headerName) {
		return headers.get(headerName.toLowerCase());
	}
}