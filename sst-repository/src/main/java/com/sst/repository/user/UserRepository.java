package com.sst.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sst.model.user.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	public User findByEmail(String email);
	
	public boolean existsByEmail(String email);
}
