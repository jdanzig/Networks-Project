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
	private Hashtable<String, String> headers;
	String redirectPath;

	public Response(Request req) {
		this.req = req;
		this.headers = new Hashtable<String, String>();
		headers.put("Connection", "Close");
		headers.put("Date", new Date().toString());
	}

	public Response() {
		this.headers = new Hashtable<String, String>();
		headers.put("Connection", "Close");
		headers.put("Date", new Date().toString());
	}

	private boolean handleRedirect(DataOutputStream out)
			throws HTTPErrorException {
		this.path = this.req.getPath();
		if (Redirects.isRedirect(this.path)) {
			showError(out, new HTTPErrorException(301));
			return true;
		}
		return false;
	}

	private void addContentTypeHeader(File f) {
		try {
			headers.put("Content-Type", MimeTypeDetector.getContentType(f));
		} catch (IOException x) {
			headers.put("Content-Type", "application/unknown");
		}
	}

	private void writeHeaders(StringBuffer tempOut) {
		for (String headerName : this.headers.keySet()) {
			tempOut.append(String.format("%s: %s\r\n", headerName, this.headers.get(headerName)));
		}
		tempOut.append("\r\n");
	}

	private void writeContent(StringBuffer tempOut, ByteArrayOutputStream dataOut, File f) throws IOException {
		String lineOut;
		Boolean done = true;
		BufferedReader fileReader = new BufferedReader(new FileReader(f));
		BufferedInputStream bufferedInput = new BufferedInputStream(new FileInputStream(f));
		byte[] bArray = new byte[2000];
		if (!MimeTypeDetector.getContentType(f).contains("text")) { //this is not a text file, read it to the data stream
			while (done) {
				int read = bufferedInput.read(bArray);
				if (read != -1) {
					dataOut.write(bArray, 0, read); //if not at end of file, keep grabbing bytes
				} else {
					done = false;
				}
			}
		} else {
			while ((lineOut = fileReader.readLine()) != null) { //if it's text/html just read it into the buffered string
				tempOut.append(lineOut);
			}
			tempOut.append("\r\n");
		}
	}

	public void show(DataOutputStream out) throws HTTPErrorException {
		StringBuffer tempOut = new StringBuffer("");
		ByteArrayOutputStream dataOut = new ByteArrayOutputStream(2000);

		try {
			if (handleRedirect(out))
				return;
			this.path = "www/" + this.path;
			File reqFile = new File(path);

			if (!reqFile.exists() || this.path == "www/redirect.defs")
				throw new HTTPErrorException(404);
			addContentTypeHeader(reqFile);
			tempOut.append(String.format("HTTP/1.1 200 OK \r\n")); //Just return highest compatible HTTP version
			writeHeaders(tempOut);
			if (req.getRequestMethod() == "GET") {
				writeContent(tempOut, dataOut, reqFile);
			}
		} catch (IOException e) {
			throw new HTTPErrorException(500);
		}

		try {
			out.writeBytes("\r");
			out.writeBytes(tempOut.toString());
			dataOut.writeTo(out);
		} catch (Exception e) {
			showError(out, new HTTPErrorException(500));
		}
	}

	public void showError(DataOutputStream out, HTTPErrorException exp) {
		StringBuffer tempOut = new StringBuffer("");
		ByteArrayOutputStream dataOut = new ByteArrayOutputStream(2000);
		ResponseCode rc = new ResponseCode();
		tempOut.append(String.format("HTTP/1.1 "));
		tempOut.append(String.format("%d %s \r\n", exp.getHttpStatusCode(), rc.getCode(exp.getHttpStatusCode())));
		try {
			if (exp.getHttpStatusCode() == 301) {
				tempOut.append(String.format("Location: %s \r\n", Redirects.getRedirect(this.path)));
			}
			out.writeBytes("\r");
			out.writeBytes(tempOut.toString());
			dataOut.writeTo(out);
		} catch (IOException x) {
			showError(out, new HTTPErrorException(500));
		}
		catch(HTTPErrorException y){
			showError(out, new HTTPErrorException(500));
		}
		}
}
