package com.sst.scheduled;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sst.model.user.ConfirmationToken;
import com.sst.model.user.RecoveryPassword;
import com.sst.service.authorization.AuthorizationService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenExpirationSchedule {
	
	@Autowired
	private AuthorizationService service;
	
	public static final long TREE_HOURS =  3 * 60 * 60 * 1000;
	
	@Scheduled(fixedDelay = TREE_HOURS)
	public void deleteExpiredTokens() {
		List<RecoveryPassword> passwords = service.findAllRecoveryPasswordExpired();
		log.info("TokenExpirationSchedule | deleteExpiredTokens | Searching for expired tokens");
		if(!passwords.isEmpty()) {
			service.deleteManyRecoverys(passwords);
			log.info("TokenExpirationSchedule | deleteExpiredTokens | Deleting {} expired passwords recoverys", passwords.size());
		}
		List<ConfirmationToken> tokens = service.findAllConfirmationTokenExpired();
		if(!tokens.isEmpty()) {
			service.deleteManyConfirmationTokens(tokens);
			log.info("TokenExpirationSchedule | deleteExpiredTokens | Deleting {} expired confirmation tokens recoverys", tokens.size());
		}
		if(passwords.isEmpty() && tokens.isEmpty()) {
			log.info("TokenExpirationSchedule | deleteExpiredTokens | No tokens expired found");
		}
	}

}
