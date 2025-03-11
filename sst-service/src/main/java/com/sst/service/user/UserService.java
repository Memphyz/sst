package com.sst.service.user;

import static com.sst.enums.ValidationMessagesType.USER_NOT_FOUND;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sst.model.user.User;
import com.sst.repository.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	UserRepository repository;

	@Override
	public User loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("GET | Finding user by email: {}", email);
		User user= repository.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException(USER_NOT_FOUND.getMessage());
		}
		return user;
	}
	
	public void deleteByEmail(String email) {
		repository.deleteById(email);
	}
	
}