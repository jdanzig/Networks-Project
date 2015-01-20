package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

public class HTTPErrorException extends Exception {

	private static final long serialVersionUID = 1087808240090239488L;
	private int httpStatus;
	
	public HTTPErrorException(int statusCode) {
		super("HTTP Status " + statusCode, null, true, false);
		httpStatus = statusCode;
	}
	
	public int getHttpStatusCode() {
		return httpStatus;
	}

}
