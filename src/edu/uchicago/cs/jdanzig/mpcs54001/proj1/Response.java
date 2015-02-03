package edu.uchicago.cs.jdanzig.mpcs54001.proj1;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.io.File;
import java.util.Date;
import java.io.*;


public class Response {
	private String path;
	private Request req;
	private int httpStatus;
	private Hashtable<String,String> headers;
	String redirectPath;
	public Response(Request req) {
		this.req = req;
		this.headers = new Hashtable<String,String>();
		headers.put("Connection", "Close");
		headers.put("Date", new Date().toString());
	}
	
	public Response() {
		
		this.headers = new Hashtable<String,String>();
		headers.put("Connection", "Close");
		headers.put("Date", new Date().toString());
	}
	
	private boolean handleRedirect(PrintWriter out) throws HTTPErrorException {
		this.path = this.req.getPath();
		if (Redirects.isRedirect(this.path)) {
			showError(out, new HTTPErrorException(301, Redirects.getRedirect(this.path), req.getHttpVersion()));
			return true;
		}
		return false;
	}
	
	private void addContentTypeHeader(File f) {
		try{
			headers.put("Content-Type", MimeTypeDetector.getContentType(f));	
		} catch (IOException x)	{ 
			headers.put("Content-Type", "application/unknown");
		}
	}
	
	private void writeHeaders(StringBuffer tempOut) {
		for(String headerName : this.headers.keySet()) {	
			tempOut.append(String.format("%s: %s\r\n", headerName, this.headers.get(headerName)));
		}	
		tempOut.append("\r\n");
	}
	
	private void writeContent(StringBuffer tempOut, File f) throws IOException {
		String lineOut;
		BufferedReader fileReader = new BufferedReader(new FileReader(f));
		if (!MimeTypeDetector.getContentType(f).contains("text")){
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			while ((lineOut = fileReader.readLine()) != null){
				outByteStream.write(lineOut.getBytes());
			}
			tempOut.append(outByteStream.toString() + "\r\n");
			System.out.print("this");
		}
		else
		{
		while ((lineOut = fileReader.readLine()) != null){
			tempOut.append(lineOut);
		}
		tempOut.append("\r\n");
		}
	}
	
	public void show(PrintWriter out) throws HTTPErrorException {
		
		StringBuffer tempOut = new StringBuffer ("");
		try {
    if(handleRedirect(out)) return;
		this.path = "www/" + this.path;
		File reqFile = new File(path);
		if (!reqFile.exists() || this.path == "www/redirect.defs") throw new HTTPErrorException(404, req.getHttpVersion());
		addContentTypeHeader(reqFile);
		tempOut.append(String.format("HTTP/%.1f 200 OK \r\n", req.getHttpVersion()));
		writeHeaders(tempOut);
			if (req.getRequestMethod() == "GET") writeContent(tempOut, reqFile);
		} catch(IOException e) {
			throw new HTTPErrorException(500);
		}	
		
		try {
			//filled StringBuffer, now try and print it to out
			out.println("");
			out.println(tempOut);
		}
		catch(Exception e) {
			showError(out, new HTTPErrorException(500));
		}	
	}
	
	public void showError(PrintWriter out, HTTPErrorException exp) {

	StringBuffer tempOut = new StringBuffer("");
	ResponseCode rc = new ResponseCode();
	tempOut.append(String.format("HTTP/%.1f \r\n", exp.getVersion()));
	tempOut.append(String.format("%d %s \r\n", exp.getHttpStatusCode(), rc.getCode(exp.getHttpStatusCode())));
	if (exp.getHttpStatusCode()==301){
		tempOut.append(String.format("Location: %s \r\n", exp.getPath()));
	}
	out.println("");
	out.println(tempOut);
    // http://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml#http-status-codes-1
	}
}
