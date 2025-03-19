package com.sst.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sst.model.user.RecoveryPassword;

public interface RecoveryPasswordRepository extends MongoRepository<RecoveryPassword, String> {
	RecoveryPassword findByToken(String token);
	RecoveryPassword deleteByToken(String token);
	boolean existsByEmail(String email);
	void deleteByEmail(String email);
	RecoveryPassword findByEmail(String email);
}
