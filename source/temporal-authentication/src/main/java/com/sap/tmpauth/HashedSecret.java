package com.sap.tmpauth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class HashedSecret {

	private final String identifier;
	private final String mySecret;
	private Instant createInstant = Instant.now();

	public HashedSecret(String identifier, String mySecret) {
		this.identifier = identifier;
		this.mySecret = mySecret;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getMySecret() {
		return mySecret;
	}

	/**
	 * Secret will expire if the it has not been successfully validated during
	 * specified ttl.
	 * 
	 * @param ttl
	 *            time secret is valid
	 * @param temporalUnit
	 *            see {@link java.time.temporal.TemporalUnit}
	 * @return true if expired else false
	 *
	 */
	public boolean hasExpired(long ttl, TemporalUnit temporalUnit) {
		Instant expired = createInstant.plus(ttl, temporalUnit);
		return !Instant.now().isBefore(expired);
	}
	
	public boolean setExpired(){
		createInstant = Instant.now().minus(SecretController.TTL, SecretController.UNIT);
		return hasExpired(SecretController.TTL, SecretController.UNIT);
	}

	/**
	 * Validates some secret against my secret to check if they are equal. If
	 * they are equal someSecret is considered valid.
	 * 
	 * @param someSecret
	 *            to compare to mySecret. If equal someSecret is valid
	 */
	public boolean isValid(Secret someSecret) {
		boolean isValid = false;
		// if true then reset the instant to now

		if (someSecret.getMySecret() != null && !someSecret.getMySecret().isEmpty()) {
			try {
				isValid = HashUtil.validatePassword(someSecret.getMySecret(), this.mySecret);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}

		if (isValid) {
			createInstant = Instant.now();
		}

		return isValid;
	}
}
