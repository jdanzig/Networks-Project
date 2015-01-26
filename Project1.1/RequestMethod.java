package project1;

public enum RequestMethod {
	HEAD("HEAD"), GET("GET");
	@SuppressWarnings("unused")
	private String value;
	
	private RequestMethod(String value) {
		this.value = value;
	}
}
