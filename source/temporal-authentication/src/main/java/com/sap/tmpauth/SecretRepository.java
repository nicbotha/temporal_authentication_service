package com.sap.tmpauth;

public interface SecretRepository {

	Iterable<HashedSecret> findAll();
	HashedSecret save(HashedSecret secret);
	HashedSecret findSecret(String identifier);
	boolean clear(String identifier);
	boolean expire(String identifier);
}
