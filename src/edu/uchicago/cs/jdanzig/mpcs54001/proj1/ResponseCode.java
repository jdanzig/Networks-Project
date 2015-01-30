package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

public enum ResponseCode {
	OK(200, "OK"), 
	MOVED_PERMANENTLY(301, "MOVED PERMANENTLY"),
	FORBIDDEN(404, "FORBIDDEN"),
	INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR"),
	BAD_REQUEST(400, "BAD REQUEST"),
	MALFORMED_PATH(406, "MALFORMED PATH NAME");

	private int codeValue;
	private String codeString;
	
	private ResponseCode(int codeValue, String codeString) {
		this.codeValue = codeValue;
		this.codeString = codeString;
		
	}
}
