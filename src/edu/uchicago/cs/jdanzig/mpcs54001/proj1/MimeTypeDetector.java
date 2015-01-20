package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

import java.io.File;
import java.io.IOException;
import javax.activation.MimetypesFileTypeMap;

public class MimeTypeDetector {

	private final static String MIME_TYPE_MAP_PATH = "/etc/mime.types";
	private static MimetypesFileTypeMap mimeMap;

	
	private static void setup() throws IOException {
		if(mimeMap != null) return;
		mimeMap = new MimetypesFileTypeMap(MIME_TYPE_MAP_PATH);
	}
	
	public static String getContentType(File f) throws IOException {
		setup();
		return mimeMap.getContentType(f);
	}
	
	public static String getContentType(String filename) throws IOException {
		setup();
		return mimeMap.getContentType(filename);
	}
}
