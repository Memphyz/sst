package com.sst.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sst.model.user.ConfirmationToken;

public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, String> {
	ConfirmationToken findByConfirmationToken(String confirmationToken);
	ConfirmationToken deleteByConfirmationToken(String confirmationToken);
}
