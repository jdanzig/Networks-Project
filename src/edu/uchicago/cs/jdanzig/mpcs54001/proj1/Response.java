package edu.uchicago.cs.jdanzig.mpcs54001.proj1;
import java.io.PrintWriter;
import java.util.Hashtable;
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
	}
	
	public Response() {
		this.headers = new Hashtable<String,String>();
	}
	
	public void show(PrintWriter out) throws HTTPErrorException{
		path = this.req.getPath();
		Boolean redirects= Redirects.isRedirect(path);
		if (redirects){
			redirectPath = Redirects.getRedirect(path);
		}
		
		path = "www/" + path;
		File reqFile = new File (path);
		String lineOut;
		if (reqFile.exists() && path!= "www/redirect.defs"){
			this.httpStatus = 200;
			headers.put("HTTP/1.1", "200");
			try{
			headers.put("Content-Type", MimeTypeDetector.getContentType(reqFile));	
			}
				catch (IOException x)
			{ }
		}
		else
		{
			if (!redirects){
				this.httpStatus = 404;
				headers.put("HTTP/1.1", "404");
			}
			else
			{
				this.httpStatus = 301;
				headers.put("HTTP/1.1", "301");
				headers.put("Location", redirectPath);
			}
		}
	
    //print the key-value pairs of headers
    out.print("\r");
	if (headers.containsKey("HTTP/1.1")) { 
		out.println("HTTP/1.1: " + headers.get("HTTP/1.1"));
		}
	if (headers.containsKey("Location")) { 
		out.println("Location: " + headers.get("Location"));
		}
	if (headers.containsKey("Content-Type")) { 
		out.println("Content-Type: " + headers.get("Content-Type")); 
		}
	out.println("connection: close");
	Date date = new Date();
	out.println("Date: " + date.toString());
			if (req.getRequestMethod()=="GET"){
				try{
					out.print("\r\n");
					BufferedReader fileReader = new BufferedReader (new FileReader (reqFile));
					while ((lineOut=fileReader.readLine())!=null){
						out.println(lineOut);
					}
				}
				catch(IOException e){
				}
			}	
	}
	public void showError(PrintWriter out, HTTPErrorException exp) {
		out.println("HTTP/1.1: " + exp.getHttpStatusCode());
		// Not sure if we need to return content explaining the errors, I'll look at some sample requests
	}
}
