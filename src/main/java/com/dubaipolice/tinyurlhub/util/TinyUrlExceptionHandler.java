package com.dubaipolice.tinyurlhub.util;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dubaipolice.tinyurlhub.controller.URLAliasController;
import com.google.gson.Gson;

@ControllerAdvice
public class TinyUrlExceptionHandler extends ResponseEntityExceptionHandler {

	private static Logger loggerInfo = LoggerFactory.getLogger(URLAliasController.class);

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		loggerInfo.info("<< handleExceptionInternal >>");

		printErrorLogsToLogger(ex, request);

		String errorMsg = getApplicationErrorMessage(ex);

		ResponseEntity<Object> responseEntity = super.handleExceptionInternal(ex, errorMsg, headers, getHttpStatus(ex),
				request);

		return responseEntity;
	}

	@ExceptionHandler(value = { ServiceException.class, ValidationException.class })
	protected ResponseEntity<String> handleValidationAndBusinessExceptions(Exception ex, WebRequest request) {

		loggerInfo.info("<< handleValidationAndBusinessExceptions >>");

		printErrorLogsToLogger(ex, request);

		String errorMsg = getApplicationErrorMessage(ex);

		return new ResponseEntity<String>(errorMsg, getHttpStatus(ex));
	}

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<String> handleAllExceptions(Exception ex, WebRequest request) {

		loggerInfo.info("<< handleAllExceptions >>");

		printErrorLogsToLogger(ex, request);

		String errorMsg = getApplicationErrorMessage(ex);

		return new ResponseEntity<String>(errorMsg, getHttpStatus(ex));
	}

	private String getApplicationErrorMessage(Exception ex) {
		JSONObject responseObject = new JSONObject();
		Gson gson = new Gson();

		String errorCode = Constants.STR_ZERO;
		String errorMsg = ex.getMessage(); // Constants.EXC_SERVER_ERROR ;
											// //ex.getMessage();
		System.out.println(ex.getMessage());
		if (ex instanceof ValidationException || ex instanceof ServiceException) {

			String errorDtls[] = getErrorCodeAndDesc(ex.getMessage());

			errorCode = errorDtls[0].trim();
			errorMsg = errorDtls[1].trim();

		} else if (ex instanceof MissingServletRequestParameterException) {
			errorMsg = ex.getMessage();

			errorMsg = errorMsg.replace("\'", "");
		} else if (ex instanceof SQLException) {

			if (((SQLException) ex).getErrorCode() == Constants.EXC_NO_DATA_FOUND) {

				errorCode = ExceptionBusinessConstants.NO_DATA_FOUND;
				errorMsg = ExceptionUtil.getErrorMessageFromProps(ExceptionBusinessConstants.NO_DATA_FOUND,
						ExceptionUtil.EXCEPTION_BUSINESS);
			}

		}

		try {
			responseObject.put(Constants.RESPONSE_DESC, errorMsg);

			responseObject.put(Constants.RESPONSE_CODE, errorCode);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return gson.toJson(responseObject);

	}

	private HttpStatus getHttpStatus(Exception ex) {

		if (ex instanceof ValidationException || ex instanceof ServiceException) {

			return HttpStatus.OK;

		} else {

			return HttpStatus.OK;
		}
	}

	private String[] getErrorCodeAndDesc(String message) {
		String errorDtls[] = new String[2];

		errorDtls = message.split("-");

		return errorDtls;
	}

	private String printRequestParams(Map<String, String[]> requestMap) {

		StringBuffer sb = new StringBuffer();
		sb.append("<<< REQUEST - START >>> ");

		if (requestMap != null) {

			Set<Entry<String, String[]>> set = requestMap.entrySet();

			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				Entry<String, String[]> entry = (Entry<String, String[]>) iterator.next();

				sb.append(entry.getKey());
				sb.append(" : ");
				sb.append(entry.getValue()[0]);
				sb.append(", ");
			}
		}

		sb.append("<<< REQUEST - END >>>");

		return sb.toString();
	}

	private void printErrorLogsToLogger(Exception ex, WebRequest request) {

		if (ex instanceof ValidationException || ex instanceof ServiceException) {

			loggerInfo.info(printRequestParams(request.getParameterMap()), ex);

		} else {

			logger.error(printRequestParams(request.getParameterMap()), ex);
		}
	}

}
