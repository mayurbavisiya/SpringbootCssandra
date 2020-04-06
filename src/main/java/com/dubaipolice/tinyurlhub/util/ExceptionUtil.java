package com.dubaipolice.tinyurlhub.util;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dubaipolice.tinyurlhub.controller.URLAliasController;

public class ExceptionUtil {
	public static final int EXCEPTION_VALIDATION = 1;
	public static final int EXCEPTION_BUSINESS = 2;
	public static final int EXCEPTION_CUSTOM = 3;
	public static final int EXCEPTION_CUSTOM_CODE = 1000;
	public static final String PROPS_VALIDATIONS = "exception_validation";
	public static final String PROPS_BUSINESS = "exception_business";
	public static final String TINYURLERVICES_EXCEPTION = "TINYURLAPP";

	private static Logger loggerInfo = LoggerFactory.getLogger(URLAliasController.class);

	static ResourceBundle resource_validation = null;
	static ResourceBundle resource_business = null;

	public static void throwException(String message, int excType) throws ServiceException {

		try {

			throwExceptionFinal("", message, excType);

		} catch (ServiceException ex) {
			loggerInfo.info("ExceptionUtil-->throwException", ex);
			throw ex;
		} catch (ValidationException ex) {
			loggerInfo.info("ExceptionUtil-->throwException", ex);
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
		} else if (excType == EXCEPTION_BUSINESS) {
			throw new ServiceException(message);
		} else if (excType == EXCEPTION_CUSTOM) {
			throw new RuntimeException(message);
		}
	}

	public static void throwStringValueZeroBusException(Object referenceId, String excCode) throws Exception {

		if (referenceId == null || Constants.STR_ZERO.equalsIgnoreCase(referenceId.toString())) {
			throwExceptionFinal(null, excCode, ExceptionUtil.EXCEPTION_BUSINESS);
		}
	}

	public static void throwNullOrEmptyValidationException(String fieldName, Object value, boolean validateZero)
			throws Exception {
		boolean isValid = false;

		if (value != null && value.getClass().isArray()) {

			isValid = ValidationUtil.isArrayNotNullAndNotEmpty(value);

		} else {

			isValid = ValidationUtil.isStringValueNotNullAndNotEmpty(value);
		}

		if (validateZero && (value != null && !value.toString().isEmpty())) {

			isValid = !ValidationUtil.isStringValueZero(value.toString());
		}

		if (!validateZero && !fieldName.isEmpty()) {

			if (value != null) {

				isValid = ValidationUtil.validateNullString(value.toString());
			}
		}

		if (!isValid) {

			throwExceptionFinal(fieldName, ExceptionValidationsConstants.REQUIRED_FIELD_EMPTY,
					ExceptionUtil.EXCEPTION_VALIDATION);
		}

	}

	public static void throwSizeExceedsException(String fieldName, String field, int fieldSizeExpected)
			throws Exception {
		if (field.length() > fieldSizeExpected) {
			String errorMessage = getErrorMessageFromProps(ExceptionValidationsConstants.MAX_FIELD_LENGTH_CHECK,
					ExceptionUtil.EXCEPTION_VALIDATION);
			errorMessage = errorMessage.replace("{fieldName}", fieldName);
			errorMessage = errorMessage.replace("{size}", fieldSizeExpected + "");
			String message = ExceptionValidationsConstants.MAX_FIELD_LENGTH_CHECK;
			message = message += " - " + errorMessage;
			message += " - " + TINYURLERVICES_EXCEPTION;
			throw new ValidationException(message);
		}

	}

	public static void throwNoDataException() throws ServiceException {

		ExceptionUtil.throwException(ExceptionBusinessConstants.NO_DATA_FOUND, ExceptionUtil.EXCEPTION_BUSINESS);
	}

	public static String getErrorMessageFromProps(String errorCode, int excType) {
		String errorMessage = "";

		if (resource_validation == null) {

			resource_validation = ResourceBundle.getBundle(PROPS_VALIDATIONS);
		}

		if (resource_business == null) {

			resource_business = ResourceBundle.getBundle(PROPS_BUSINESS);
		}

		if (excType == EXCEPTION_VALIDATION) {

			errorMessage = resource_validation.getString(errorCode);

		} else if (excType == EXCEPTION_BUSINESS) {

			errorMessage = resource_business.getString(errorCode);
		}

		return errorMessage;
	}

	public static void logParamStackTrace(String endpointServiceName, Exception ex, String param) {

		if (ex instanceof ServiceException) {

			loggerInfo.info(param, ex);

		} else if (ex instanceof ValidationException) {

			loggerInfo.info(param, ex);

		} else {

			loggerInfo.error(param, ex);
		}
	}
}
