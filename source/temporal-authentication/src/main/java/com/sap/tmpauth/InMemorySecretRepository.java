package com.sap.tmpauth;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemorySecretRepository implements SecretRepository {

	private final ConcurrentMap<String, HashedSecret> secrets = new ConcurrentHashMap<String, HashedSecret>();

	@Override
	public Iterable<HashedSecret> findAll() {
		return this.secrets.values();
	}

	@Override
	public HashedSecret save(HashedSecret secret) {
		return this.secrets.put(secret.getIdentifier(), secret);
	}

	@Override
	public HashedSecret findSecret(String identifier) {
		return this.secrets.get(identifier);
	}
	
	@Override
	public boolean clear(String identifier) {
		if(this.secrets.containsKey(identifier)){
			this.secrets.remove(identifier);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean expire(String identifier) {
		if(this.secrets.containsKey(identifier)){
			this.secrets.get(identifier).setExpired();
			return true;
		}
		return false;
	}

}
