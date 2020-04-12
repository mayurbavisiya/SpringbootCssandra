package com.dubaipolice.tinyurlhub.util;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.dubaipolice.tinyurlhub.model.URLAliasMappingDTO;

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
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString().trim();

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

	

	public static String getFullURL(HttpServletRequest request) {
		final StringBuffer requestURL = request.getRequestURL();
		final String queryString = request.getQueryString();

		final String result = queryString == null ? requestURL.toString()
				: requestURL.append('?').append(queryString).toString();

		return result;
	}

	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String getClientOS(HttpServletRequest request) {
		final String browserDetails = request.getHeader("User-Agent");

		// =================OS=======================
		final String lowerCaseBrowser = browserDetails.toLowerCase();
		if (lowerCaseBrowser.contains("windows")) {
			return "Windows";
		} else if (lowerCaseBrowser.contains("mac")) {
			return "Mac";
		} else if (lowerCaseBrowser.contains("x11")) {
			return "Unix";
		} else if (lowerCaseBrowser.contains("android")) {
			return "Android";
		} else if (lowerCaseBrowser.contains("iphone")) {
			return "IPhone";
		} else {
			return "UnKnown, More-Info: " + browserDetails;
		}
	}

	public static String getClientBrowser(HttpServletRequest request) {
		final String browserDetails = request.getHeader("User-Agent");
		final String user = browserDetails.toLowerCase();

		String browser = "";

		// ===============Browser===========================
		if (user.contains("msie")) {
			String substring = browserDetails.substring(browserDetails.indexOf("MSIE")).split(";")[0];
			browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
		} else if (user.contains("safari") && user.contains("version")) {
			browser = (browserDetails.substring(browserDetails.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
					+ (browserDetails.substring(browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
		} else if (user.contains("opr") || user.contains("opera")) {
			if (user.contains("opera"))
				browser = (browserDetails.substring(browserDetails.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
						+ (browserDetails.substring(browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
			else if (user.contains("opr"))
				browser = ((browserDetails.substring(browserDetails.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
						.replace("OPR", "Opera");
		} else if (user.contains("chrome")) {
			browser = (browserDetails.substring(browserDetails.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
		} else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)
				|| (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1)
				|| (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
			// browser=(userAgent.substring(userAgent.indexOf("MSIE")).split("
			// ")[0]).replace("/", "-");
			browser = "Netscape-?";

		} else if (user.contains("firefox")) {
			browser = (browserDetails.substring(browserDetails.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
		} else if (user.contains("rv")) {
			browser = "IE";
		} else {
			browser = "UnKnown, More-Info: " + browserDetails;
		}

		return browser;
	}

	public static String getUserAgent(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}

	public static String getReferer(HttpServletRequest request) {
		final String referer = request.getHeader("referer");
		return referer;
	}

	public static void validateRequest(URLAliasMappingDTO dto) throws Exception {

		ExceptionUtil.throwNullOrEmptyValidationException("realurl", dto.getRealurl(), true);
		// ExceptionUtil.throwNullOrEmptyValidationException("expirytime",
		// dto.getExpirytime(), true);
		if ((!dto.getRealurl().startsWith("http://") && !dto.getRealurl().startsWith("https://"))) {
			ExceptionUtil.throwException(ExceptionValidationsConstants.INALID_URL, ExceptionUtil.EXCEPTION_VALIDATION);
		}
	}

	public static void main(String[] args) throws Exception {
//		UUID uuid = UUID.fromString("c4c77300-731e-11ea-98e2-23145a36e1bd");
//		System.out.println(isLinkExpired(uuid, 5));
		
		String str = "/tinyurlhub/abctest/pOb5tb7P";
		int i = str.indexOf("/", str.indexOf("/") + 1) + 1;
		
		System.out.println(str.substring(i));
		
		URLAliasMappingDTO dto = new URLAliasMappingDTO();
		dto.setRealurl("https://www.linkedin.com/feed/");
		Utils.validateRequest(dto);
		
	}
}
