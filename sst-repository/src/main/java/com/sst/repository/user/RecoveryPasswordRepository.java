package com.sst.repository.user;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.sst.model.user.RecoveryPassword;

public interface RecoveryPasswordRepository extends MongoRepository<RecoveryPassword, String> {
	RecoveryPassword findByToken(String token);
	RecoveryPassword deleteByToken(String token);
	boolean existsByEmail(String email);
	void deleteByEmail(String email);
	RecoveryPassword findByEmail(String email);
	
	 @Query("{$expr: { $lt: [{ $add: ['$createdDate', 10800000 ] },new Date()]}}")
	List<RecoveryPassword> findAllExpired();
}
