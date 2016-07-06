package com.sap.tmpauth;

public class Secret {
	private final String identifier;
	private final String mySecret;

	public Secret(String mySecret, String identifier) {
		this.mySecret = mySecret;
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getMySecret() {
		return mySecret;
	}

}
