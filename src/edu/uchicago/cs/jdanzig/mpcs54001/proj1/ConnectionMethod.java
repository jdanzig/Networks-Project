package edu.uchicago.cs.jdanzig.mpcs54001.proj1;

public enum ConnectionMethod {
	Close("Close"), KEEP_ALIVE("KEEP-ALIVE");
	@SuppressWarnings("unused")
	private String value;
	
	private ConnectionMethod(String value) {
		this.value = value;
	}
}
