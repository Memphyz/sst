package com.sst.resource.loader;

import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.InputStream;

/*
 * 
 * 
 * */
public class ResourceLoader {

	public static String loadTemplate(String fileName) {
		ClassLoader classLoader = currentThread().getContextClassLoader();
		try {
			InputStream inputStream = classLoader.getResourceAsStream("template/" + fileName);
            return new String(inputStream.readAllBytes(), UTF_8);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load template: " + fileName, e);
		}
	}
}
