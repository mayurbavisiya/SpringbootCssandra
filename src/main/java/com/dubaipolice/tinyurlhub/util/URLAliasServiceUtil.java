package com.dubaipolice.tinyurlhub.util;

import org.apache.commons.validator.routines.UrlValidator;

import com.dubaipolice.tinyurlhub.model.URLAliasMappingDTO;
public class URLAliasServiceUtil {

	public static void validateRequest(URLAliasMappingDTO dto) throws Exception{
		
		ExceptionUtil.throwNullOrEmptyValidationException("realurl", dto.getRealurl(), false);
		ExceptionUtil.throwNullOrEmptyValidationException("expirytime", dto.getExpirytime(), false);
//		ExceptionUtil.throwNullOrEmptyValidationException("alias", dto.getAlias(), false);
		UrlValidator validator = new UrlValidator();
//		if(!validator.isValid(dto.getRealurl())){
//			ExceptionUtil.throwException(ExceptionValidationsConstants.INALID_URL, ExceptionUtil.EXCEPTION_VALIDATION);
//		}
	}
}
