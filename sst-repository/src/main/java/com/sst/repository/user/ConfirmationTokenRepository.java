package com.sst.repository.user;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.sst.model.user.ConfirmationToken;

public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, String> {
	ConfirmationToken findByConfirmationToken(String confirmationToken);
	ConfirmationToken deleteByConfirmationToken(String confirmationToken);
	boolean existsByEmail(String email);
	void deleteByEmail(String email);
	ConfirmationToken findByEmail(String email);
	
	@Query("{ $expr: { $lt: [ { $add: [ '$createdDate', 10800000  ] }, new Date() ] } }")
	List<ConfirmationToken> findAllExpired();
}
