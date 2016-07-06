package com.sap.tmpauth;

public class Status {
	enum State {
		VALID, INVALID, EXPIRED, UNKNOWN
	}

	public final State state;

	private Status(State state) {
		this.state = state;
	}
	
	public static Status valid() {
		return new Status(State.VALID);
	}
	
	public static Status inValid() {
		return new Status(State.INVALID);
	}
	
	public static Status expired() {
		return new Status(State.EXPIRED);
	}

	public static Status unknown() {
		return new Status(State.UNKNOWN);
	}
}
