package edu.uchicago.cs.jdanzig.mpcs54001.proj1;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Date;
import java.io.*;
import java.nio.ByteBuffer;

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
	
	private boolean handleRedirect(DataOutputStream out) throws HTTPErrorException {
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
	
	private void writeContent(StringBuffer tempOut, ByteArrayOutputStream tempOut2, File f) throws IOException {
		String lineOut;
		Boolean done =true;
		BufferedReader fileReader = new BufferedReader(new FileReader(f));
		BufferedInputStream bufferedInput = new BufferedInputStream(new FileInputStream(f));
		byte[] bArray = new byte[2000];
		if (!MimeTypeDetector.getContentType(f).contains("text")){
			while (done){
		    int read = bufferedInput.read(bArray);
		    if (read != -1){
		    	tempOut2.write(bArray, 0 , read);
		    }
		    else
		    {
		    done = false;	
		    }
		    }
		}
		else
		{
		while ((lineOut = fileReader.readLine()) != null){
			tempOut.append(lineOut);
		}
		tempOut.append("\r\n");
		}
	}
	
	public void show(DataOutputStream out) throws HTTPErrorException {
		StringBuffer tempOut = new StringBuffer ("");
		ByteArrayOutputStream tempOut2 = new ByteArrayOutputStream(2000);
		
		try {
    if(handleRedirect(out)) return;
		this.path = "www/" + this.path;
		File reqFile = new File(path);

		if (!reqFile.exists() || this.path == "www/redirect.defs") throw new HTTPErrorException(404, req.getHttpVersion());
		addContentTypeHeader(reqFile);
		tempOut.append(String.format("HTTP/%.1f 200 OK \r\n", req.getHttpVersion()));
		writeHeaders(tempOut);
			if (req.getRequestMethod() == "GET") {
				writeContent(tempOut,tempOut2, reqFile);
			}
		} catch(IOException e) {
			throw new HTTPErrorException(500);
		}	
		
		try {
			out.writeBytes(" ");
			out.writeBytes(tempOut.toString());
			tempOut2.writeTo(out);
		}
		catch(Exception e) {
			showError(out, new HTTPErrorException(500));
		}	
	}

	public void showError(DataOutputStream out, HTTPErrorException exp) {
	StringBuffer tempOut = new StringBuffer("");
	ByteArrayOutputStream tempOut2 = new ByteArrayOutputStream(2000);
	ResponseCode rc = new ResponseCode();
	tempOut.append(String.format("HTTP/%.1f \r\n", exp.getVersion()));
	tempOut.append(String.format("%d %s \r\n", exp.getHttpStatusCode(), rc.getCode(exp.getHttpStatusCode())));
	if (exp.getHttpStatusCode()==301){
		tempOut.append(String.format("Location: %s \r\n", exp.getPath()));
	}
	try{
		out.writeBytes(" ");
		out.writeBytes(tempOut.toString());
		tempOut2.writeTo(out);
	}
	catch (IOException x)	{ 
	}
	}
}
