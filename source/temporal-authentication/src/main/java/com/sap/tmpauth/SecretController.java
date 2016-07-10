package com.sap.tmpauth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecretController {
	
	private final SecretRepository secretRepository;
	
	public static final long TTL = 3600;
	public static final ChronoUnit UNIT = ChronoUnit.SECONDS;
	
	@Autowired
	public SecretController(SecretRepository secretRepository) {
		this.secretRepository = secretRepository;
	}

	@RequestMapping(value = "/secret/validate", method = RequestMethod.POST)
    public Status validate(@RequestBody Secret secret) {
		Status state = Status.unknown();
		HashedSecret hashedSecret = secretRepository.findSecret(secret.getIdentifier());
		boolean revalidate = secret.getMySecret() == null? false:true;
		
		if(hashedSecret == null) {
			state = Status.unknown();
		}else if(!revalidate && hashedSecret.hasExpired(TTL, UNIT)) {
			state = Status.expired();
		}else if(!hashedSecret.isValid(secret)) {
			state = Status.inValid();
		}else {
			state = Status.valid();
		}
		
		return state;
	}
	
	@RequestMapping(value = "/secret/clear", method = RequestMethod.POST)
    public Status clear(@RequestBody Secret secret) {
		Status state = Status.unknown();
		boolean result = secretRepository.clear(secret.getIdentifier());
		
		if(!result) {
			state = Status.inValid();
		}
		
		return state;
	}
	
	@RequestMapping(value = "/secret/expire", method = RequestMethod.POST)
    public Status expire(@RequestBody Secret secret) {
		Status state = Status.expired();
		boolean result = secretRepository.expire(secret.getIdentifier());
		
		if(!result) {
			state = Status.inValid();
		}
		
		return state;
	}
	
	@RequestMapping(value = "/secret/", method = RequestMethod.POST)
	public Status save(@RequestBody Secret secret) {
		Status state = Status.unknown();
		
		HashedSecret hashedSecret;
		try {
			hashedSecret = new HashedSecret(secret.getIdentifier(), HashUtil.createHash(secret.getMySecret()));
			secretRepository.save(hashedSecret);
			state = Status.valid();
		} catch (NoSuchAlgorithmException e) {
			state = Status.inValid();
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			state = Status.inValid();
			e.printStackTrace();
		}
		return state;
	}
}
