package com.dubaipolice.tinyurlhub.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dubaipolice.tinyurlhub.model.URLAliasMappingDTO;
import com.dubaipolice.tinyurlhub.service.URLAliasService;
import com.dubaipolice.tinyurlhub.util.Constants;
import com.dubaipolice.tinyurlhub.util.ExceptionUtil;
import com.google.gson.Gson;

@RestController
@RequestMapping("/")
public class URLAliasController {

	private static Logger logger = LoggerFactory.getLogger(URLAliasController.class);

	@Autowired
	URLAliasService service;

	@PostMapping(path="/generateAlias",produces = "application/json")
	public String generateAlias(@RequestBody URLAliasMappingDTO dto) throws Exception {
		Instant start = Instant.now();
		CompletableFuture<String> shorturl = service.createAlias(dto).exceptionally(exception -> ExceptionUtil.handleException(exception) );
		JSONObject respObject = new JSONObject();
		String res= shorturl.get();
		
		Gson gson = new Gson();
		if (shorturl != null && res.contains("http") ) {
			respObject.put(Constants.RESPONSE_CODE, Constants.SUCCESS_CODE);
			respObject.put(Constants.RESPONSE_DESC, Constants.SUCCESS_DESC);
			respObject.put("shortUrl", shorturl.get());
			Instant finish = Instant.now();
		    long timeElapsed = Duration.between(start, finish).toMillis();  //in millis
			logger.info("GenerateAlias Request Served within milisec : " + timeElapsed);
		} else 
			return shorturl.get().replace("\\", "");
					
		
		return gson.toJson(respObject); 

	}

		
}
