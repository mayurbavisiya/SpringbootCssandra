package com.dubaipolice.tinyurlhub.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dubaipolice.tinyurlhub.controller.URLAliasController;
import com.google.common.base.Splitter;
import com.google.gson.Gson;

public class ExceptionUtil {
	public static final int EXCEPTION_VALIDATION = 1;
	public static final int EXCEPTION_BUSINESS = 2;
	public static final int EXCEPTION_CUSTOM = 3;
	public static final int EXCEPTION_CUSTOM_CODE = 1000;
	public static final String PROPS_VALIDATIONS = "exception_validation";
	public static final String TINYURLERVICES_EXCEPTION = "TINYURLAPP";

	private static Logger loggerInfo = LoggerFactory.getLogger(URLAliasController.class);

	static ResourceBundle resource_validation = null;
	static ResourceBundle resource_business = null;

	public static void throwException(String message, int excType) throws ValidationException {

		try {

			throwExceptionFinal("", message, excType);

		} catch (ValidationException ex) {

			throw ex;
		} catch (RuntimeException ex) {
			loggerInfo.error("ExceptionUtil-->throwException", ex);
			throw ex;
		}
	}

	public static void throwExceptionFinal(String fieldName, String message, int excType) {

		if (excType == EXCEPTION_CUSTOM) {
			message = EXCEPTION_CUSTOM_CODE + " - " + message;
		} else {
			String errorMessage = getErrorMessageFromProps(message, excType);
			if (fieldName != null && !fieldName.isEmpty()) {
				errorMessage = errorMessage.replace("{fieldName}", fieldName);
			}
			fieldName = null;
			message += " - " + errorMessage;
		}

		message += " - " + TINYURLERVICES_EXCEPTION;
		if (excType == EXCEPTION_VALIDATION) {
			throw new ValidationException(message);
		} else if (excType == EXCEPTION_CUSTOM) {
			throw new RuntimeException(message);
		}
	}

	public static void throwNullOrEmptyValidationException(String fieldName, Object value, boolean validateZero)
			throws Exception {
		boolean isValid = false;

		if (value != null && value.getClass().isArray()) {

			isValid = isArrayNotNullAndNotEmpty(value);

		} else {

			isValid = isStringValueNotNullAndNotEmpty(value);
		}

		if (validateZero && (value != null && !value.toString().isEmpty())) {

			isValid = !isStringValueZero(value.toString());
		}

		if (!validateZero && !fieldName.isEmpty()) {

			if (value != null) {

				isValid = validateNullString(value.toString());
			}
		}

		if (!isValid) {

			throwExceptionFinal(fieldName, ExceptionValidationsConstants.REQUIRED_FIELD_EMPTY,
					ExceptionUtil.EXCEPTION_VALIDATION);
		}

	}

	public static String getErrorMessageFromProps(String errorCode, int excType) {
		String errorMessage = "";

		if (resource_validation == null) {

			resource_validation = ResourceBundle.getBundle(PROPS_VALIDATIONS);
		}

		if (excType == EXCEPTION_VALIDATION) {

			errorMessage = resource_validation.getString(errorCode);

		} else if (excType == EXCEPTION_BUSINESS) {

			errorMessage = resource_business.getString(errorCode);
		}

		return errorMessage;
	}

	public static void logParamStackTrace(String endpointServiceName, Exception ex, String param) {

		if (ex instanceof ValidationException) {

			loggerInfo.info(""); // param, ex);

		} else {

			loggerInfo.error(param, ex);
		}
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

	public static boolean validateNullString(String fieldName) {
		boolean isValid = true;

		if (fieldName.trim().equalsIgnoreCase("null")) {
			isValid = false;
			return isValid;
		}

		return isValid;
	}

	public static String handleException(Throwable ex) {

		String errorCode = Constants.STR_ZERO;
		String errorMsg = Constants.EXC_SERVER_ERROR;
		JSONObject responseObject = new JSONObject();
		Gson gson = new Gson();
		System.out.println(ex.getCause().getClass().getSimpleName());
		if (ex.getCause().getClass().getSimpleName().equals("ValidationException")) {
			String errorDtls[] = getErrorCodeAndDesc(ex.getMessage());
			List<String> elementsInString = Splitter.on(":").splitToList(errorDtls[0].trim());
			errorCode = elementsInString.get(1);
			errorMsg = errorDtls[1].trim();

		} else if (ex.getCause().getClass().getSimpleName().equals("Exception")) {
			String errorDtls[] = getErrorCodeAndDesc(ex.getMessage());

			errorCode = errorDtls[1].trim();
			errorMsg = errorDtls[1].trim();
		}else{			
			loggerInfo.error("Exception Occured: " , ex);
		}

		responseObject.put(Constants.RESPONSE_DESC, errorMsg);

		responseObject.put(Constants.RESPONSE_CODE, errorCode);
		return gson.toJson(responseObject);
	}

	private static String[] getErrorCodeAndDesc(String message) {
		String errorDtls[] = new String[2];

		errorDtls = message.split("-");

		return errorDtls;
	}
}
