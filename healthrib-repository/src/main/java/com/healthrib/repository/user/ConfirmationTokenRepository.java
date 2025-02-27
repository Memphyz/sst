package com.healthrib.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.healthrib.model.user.ConfirmationToken;

public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, Long> {
	ConfirmationToken findByConfirmationToken(String confirmationToken);
	ConfirmationToken deleteByConfirmationToken(String confirmationToken);
}
