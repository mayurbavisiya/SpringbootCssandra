package com.dubaipolice.tinyurlhub.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class GenerateAlias {

	public static String generateUniqueAlias() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 8;
		SecureRandom secRandom = new SecureRandom();

		return secRandom.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		// System.out.println(generatedString);
	}
}
