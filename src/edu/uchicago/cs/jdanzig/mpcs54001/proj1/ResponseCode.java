package edu.uchicago.cs.jdanzig.mpcs54001.proj1;
import java.util.HashMap; 

public class ResponseCode {
	
	HashMap<Integer, String> lookup = new HashMap<Integer, String>();{
	lookup.put(200, "OK");
	lookup.put(301, "MOVED PERMANENTLY");
	lookup.put(404, "FORBIDDEN");
	lookup.put(403, "FORBIDDEN ");
	lookup.put(500, "INTERNAL SERVER ERROR");
	lookup.put(501, "INVALID REQUEST");
	lookup.put(400, "BAD REQUEST");
	lookup.put(406, "MALFORMED PATH NAME");}

	public String getCode(int codeNum){
		return lookup.get(codeNum);
		
	}
	
	
}
