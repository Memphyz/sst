package com.sst.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "https://www.purgomalum.com/service", name = "containsprofanity")
public interface PurgoMalumClientHttp {
	
	@GetMapping(value = "/containsprofanity")
	String containsProfanity(@RequestParam String text);
	
	public default boolean hasDirtyWord(String text) {
		return Boolean.parseBoolean(containsProfanity(text));
	}
	
}
