package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

import java.io.PrintWriter;
import java.util.Hashtable;

public class Response {

	private Request req;
	private int httpStatus;
	
	private Hashtable<String,String> headers;
	
	public Response(Request req) {
		this.req = req;
		this.headers = new Hashtable<String,String>();
	}
	
	public Response() {
		this.headers = new Hashtable<String,String>();
	}
	
	public void show(PrintWriter out) throws HTTPErrorException {
		// TODO: Get path from req. Determine if file exists. Throw error if not.
		// TODO: Populate headers. Print out headers. Return at this point if req.responseType == ResponseType.HEAD
		// TODO: Otherwise, print out contents of file, then return
	}
	public void showError(PrintWriter out, HTTPErrorException exp) {
		// TODO: Reply with error response corresponding to the httpStatus in exp
		// TODO: Populate headers. Print out headers. Return at this point if req.responseType == ResponseType.HEAD
		// TODO: Otherwise, print out contents of error page, then return
	}
}
