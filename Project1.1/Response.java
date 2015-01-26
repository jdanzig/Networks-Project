package project1;
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
	
	public Response(Request req) {
		this.req = req;
		this.headers = new Hashtable<String,String>();
	}
	
	public Response() {
		this.headers = new Hashtable<String,String>();
	}
	
	public void show(PrintWriter out) throws HTTPErrorException{
		out.println("connected");
		path = this.req.getPath();
		path = "www/" + path;
		File reqFile = new File (path);
		String lineOut;
		if (reqFile.exists()){
			this.httpStatus = 200;
		}
		else
		{
			this.httpStatus = 404;
		}
		Date date = new Date();
		out.println("HTTP/1.1 " + this.httpStatus + " OK");
		out.println("connection: close");
		out.println("Date: " + date.toString());	
		try{
		out.println("Content-Type: " + MimeTypeDetector.getContentType(reqFile));	
	}
		catch (IOException x)
	{
	System.out.printf("Hit exception: %s", x);	
	}
	
		out.println("Server: Jon and Cody's server \r\n");
		if (req.getRequestMethod()=="GET"){
			try{
			BufferedReader fileReader = new BufferedReader (new FileReader (reqFile));
			
			while ((lineOut=fileReader.readLine())!=null){
			out.println(lineOut);
			}
			}
			catch(IOException e){
			}
		}
		// TODO: Populate headers. Print out headers. Return at this point if req.responseType == ResponseType.HEAD
		// TODO: Otherwise, print out contents of file, then return
	}
	public void showError(PrintWriter out, HTTPErrorException exp) {
		// TODO: Reply with error response corresponding to the httpStatus in exp
		// TODO: Populate headers. Print out headers. Return at this point if req.responseType == ResponseType.HEAD
		// TODO: Otherwise, print out contents of error page, then return
	}
}
