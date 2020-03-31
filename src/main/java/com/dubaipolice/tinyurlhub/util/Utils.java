package com.dubaipolice.tinyurlhub.util;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.datastax.driver.core.utils.UUIDs;

@Service
public class Utils {

	static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;

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

	public static String getDateFromUUID(UUID uuidDate, String format) {

		long time = (uuidDate.timestamp() - NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) / 10000;
		Date date = new Date(time);
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	public static boolean isLinkExpired(UUID uuidDate, int expiryDays) {

		long time = (uuidDate.timestamp() - NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) / 10000;
		Date linkedCreationDate = new Date(time);
		LocalDateTime localDateTime = linkedCreationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		localDateTime = localDateTime.plusYears(0).plusMonths(0).plusDays(expiryDays);
		linkedCreationDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		Date sysDate = new Date();
		if (linkedCreationDate.compareTo(sysDate) > 0) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		UUID uuid = UUID.fromString("c4c77300-731e-11ea-98e2-23145a36e1bd");
		System.out.println(isLinkExpired(uuid, 5));
	}
}
