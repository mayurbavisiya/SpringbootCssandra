package com.dubaipolice.tinyurlhub.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dubaipolice.tinyurlhub.model.URLAliasMappingDTO;
import com.dubaipolice.tinyurlhub.model.URLAliasMappingEntity;
import com.dubaipolice.tinyurlhub.service.URLAliasService;
import com.dubaipolice.tinyurlhub.util.Constants;
import com.google.gson.Gson;

@RestController
@RequestMapping("/tinyurl")
public class URLAliasController {

	private static Logger logger = LoggerFactory.getLogger(URLAliasController.class);

	@Autowired
	URLAliasService service;

	@PostMapping("/generateAlias")
	public String generateAlias(@RequestBody URLAliasMappingDTO dto) throws Exception {
		Instant start = Instant.now();
		CompletableFuture<URLAliasMappingEntity> entity = service.createAlias(dto);

		JSONObject respObject = new JSONObject();
		Gson gson = new Gson();
		if (entity != null) {
			respObject.put(Constants.RESPONSE_CODE, Constants.SUCCESS_CODE);
			respObject.put(Constants.RESPONSE_DESC, Constants.SUCCESS_DESC);
			String response = gson.toJson(entity.get());

			respObject.put("result", response);
			Instant finish = Instant.now();
		    long timeElapsed = Duration.between(start, finish).toMillis();  //in millis
			logger.info("GenerateAlias Request Served within milisec : " + timeElapsed);
		} else {
			respObject.put(Constants.RESPONSE_CODE, Constants.FAILURE_CODE);
			respObject.put(Constants.RESPONSE_DESC, Constants.FAILURE_DESC);

		}

		return gson.toJson(respObject);
	}

	
//	@GetMapping("/getRealURL")
//	public void getRealURL(@RequestParam String alias, HttpServletResponse servletResponse) throws IOException {
//
//		CompletableFuture<String> entity;
//		try {
//			entity = service.getRealURL(alias);
//			entity.get();
//			servletResponse.sendError(200);
//
//		} catch (Exception e) {
//			if (e.getMessage().contains("Alias not found")) {
//				servletResponse.sendError(404);
//			} else {
//				servletResponse.sendError(400);
//			}
//			e.printStackTrace();
//		}
//
//	}

}
