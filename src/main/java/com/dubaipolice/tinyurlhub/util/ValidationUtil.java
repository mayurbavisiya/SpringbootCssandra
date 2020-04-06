package com.dubaipolice.tinyurlhub.util;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String PHONE_UAE_PATTERN = "^(?:971)[0-9]{8,9}$";

	public static final String PHONE_OMAN_PATTERN = "^(?:968)[0-9]{8,11}$";

	public static final String PHONE_SAUDI_PATTERN = "^(?:966)[0-9]{8,11}$";

	public static final String PHONE_QATAR_PATTERN = "^(?:974)[0-9]{8,11}$";

	public static final String PHONE_KUWAIT_PATTERN = "^(?:965)[0-9]{8,11}$";

	public static final String PHONE_BAHRAIN_PATTERN = "^(?:973)[0-9]{8,11}$";

	public static final String EMIRATES_ID_PATTERN = "[0-9]{15,15}";

	public static final String[] ATTACHMENT_IMAGE_TYPES = { "JPG", "JPEG", "PNG", "GIF", "TIF", "TIFF" };

	public static final String[] ATTACHMENT_AUDIO_TYPES = { "MP3" };

	public static final String[] ATTACHMENT_VIDEO_TYPES = { "MP4", "MOV" };

	public static final String[] ATTACHMENT_DOCS_TYPES = { "DOC", "DOCX", "PDF", "XLS", "XLSX" };

	public static final String PHONE_UAE_MOBILE_PATTERN = "^05[\\s]{0,1}[0-9][\\s]{0,1}[0-9]{3}[\\s][0-9]{4}$"; // ^05[\\s]{0,1}[0-9]{3}[\\s][0-9]{4}$

	public static boolean validateEmiratesId(String emiratesId) {
		boolean isValid = false;

		if (emiratesId != null && emiratesId.matches(EMIRATES_ID_PATTERN)) {

			isValid = true;
		}

		return isValid;
	}

	public static boolean validateMobilePhoneNo(String strVal) {
		boolean isValid = false;

		if (strVal != null && !strVal.isEmpty()) {
			if (strVal.matches(PHONE_UAE_MOBILE_PATTERN)) {
				isValid = true;
			}
		}
		return isValid;
	}

	public static boolean validateEmail(String email) {
		boolean isValid = false;

		if (email != null && !email.isEmpty()) {

			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(email);
			isValid = matcher.matches();
		}

		return isValid;
	}

	public static boolean isStringValueZero(String strValue) {

		return Constants.STR_ZERO.equalsIgnoreCase(strValue);

	}

	public static boolean isArrayNotNullAndNotEmpty(Object strVal) {

		if (strVal != null && (Array.getLength(strVal) > 0 && !Constants.STR_QUESTION_MARK.equals(strVal.toString()))) {

			return true;

		} else {

			return false;

		}
	}

	public static boolean isStringValueNotNullAndNotEmpty(Object strVal) {

		if (strVal != null
				&& (!strVal.toString().isEmpty() && !Constants.STR_QUESTION_MARK.equals(strVal.toString()))) {

			return true;

		} else {

			return false;

		}
	}

	/**
	 * Method to validate and throw validation exception
	 * 
	 * @param required
	 * @param minLength
	 * @param maxLength
	 * @param valStr
	 * @param exceptionCode
	 * @return
	 * @throws Exception
	 */
	public static String validateStringValue(boolean required, int minLength, int maxLength, String valStr,
			String exceptionCode) throws Exception {
		// valStr = Utilities.getDefaultIfNull(valStr,null);
		if (required && valStr == null) {
			ExceptionUtil.throwException(exceptionCode, ExceptionUtil.EXCEPTION_VALIDATION);
		}
		if (valStr != null && (valStr.length() < minLength || valStr.length() > maxLength)) {
			ExceptionUtil.throwException(exceptionCode, ExceptionUtil.EXCEPTION_VALIDATION);
		}
		return valStr;
	}

	public static boolean isNumberOnly(String strVal) {
		boolean isValid = false;

		if (strVal != null && !strVal.isEmpty()) {

			String regex = "\\d+";

			if (strVal.matches(regex)) {
				isValid = true;
			}
		}

		return isValid;
	}

	public static boolean validatePhoneNo(String strVal) {
		boolean isValid = false;

		if (strVal != null && !strVal.isEmpty()) {

			if (isNumberOnly(strVal)) {

				if (strVal.matches(PHONE_UAE_PATTERN) || strVal.matches(PHONE_OMAN_PATTERN)
						|| strVal.matches(PHONE_SAUDI_PATTERN) || strVal.matches(PHONE_QATAR_PATTERN)
						|| strVal.matches(PHONE_KUWAIT_PATTERN) || strVal.matches(PHONE_BAHRAIN_PATTERN)) {

					isValid = true;
				}
			}
		}

		return isValid;
	}

	public static boolean validateAttachmentType(String attachmentName) {
		boolean isValid = false;

		if (attachmentName != null && !attachmentName.isEmpty()) {

			if (attachmentName.contains(".")) {

				String type = attachmentName.substring(attachmentName.lastIndexOf(".") + 1).toUpperCase();

				if (Arrays.asList((ATTACHMENT_IMAGE_TYPES)).contains(type)
						|| Arrays.asList((ATTACHMENT_DOCS_TYPES)).contains(type)
						|| Arrays.asList((ATTACHMENT_AUDIO_TYPES)).contains(type)
						|| Arrays.asList((ATTACHMENT_VIDEO_TYPES)).contains(type)) {

					isValid = true;
				}
			}
		}

		return isValid;
	}

	public static boolean validateDateFormat(String strDate, String pattern) {
		boolean isValid = true;

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		try {

			isValid = sdf.format(sdf.parse(strDate)).equals(strDate);

		} catch (ParseException e) {
			isValid = false;
		}

		return isValid;
	}

	public static boolean validateDateBeforeToday(String dateStr) throws ParseException {
		boolean isValid = false;

		if (dateStr != null) {

			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_ddMMyyyy);

			Date given = sdf.parse(dateStr);

			Date date = new Date();

			date = sdf.parse(sdf.format(date));

			if (given.before(date)) {
				isValid = true;
			}
		}

		return isValid;
	}

	public static boolean validateTimeFormat_24HRS(String time) {
		boolean isValid = false;

		String TIME_24HRS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

		if (time != null && !time.isEmpty()) {

			if (time.matches(TIME_24HRS_PATTERN)) {

				isValid = true;
			}

		}

		return isValid;
	}

	public static boolean validateNullString(String fieldName) {
		boolean isValid = true;

		if (fieldName.trim().equalsIgnoreCase("null")) {
			isValid = false;
			return isValid;
		}

		return isValid;
	}

	public static boolean validateDateAfterToday(String dateStr) throws ParseException {
		boolean isValid = false;

		if (dateStr != null) {

			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_ddMMyyyy);

			Date given = sdf.parse(dateStr);

			Date date = new Date();

			date = sdf.parse(sdf.format(date));

			if (given.after(date)) {
				isValid = true;
			}
		}

		return isValid;
	}

}
