package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

public class HTTPErrorException extends Exception {

	private static final long serialVersionUID = 1087808240090239488L;
	private int httpStatus;
	private float version;
	private String path;
	
	public HTTPErrorException(int statusCode) {
		super("HTTP Status " + statusCode, null, true, false);
		httpStatus = statusCode;
	}
	
	public int getHttpStatusCode() {
		return httpStatus;
	}
	
	public float getVersion() {
		return version;
	}
	
	public String getPath() {
		return path;
	}

}
