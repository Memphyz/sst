package com.sst.utils;

import static java.lang.Character.isUpperCase;

public class StringUtils {
	
	public static String toSkakeCase(String camelCase) {
		StringBuilder result = new StringBuilder();
		int index = 0;
	    for (char c : camelCase.toCharArray()) {
	        if (isUpperCase(c) && index > 0) {
	            result.append("_").append(c);
	        } else {
	            result.append(c);
	        }
	        ++index;
	    }
	    return result.toString();
	}
}
