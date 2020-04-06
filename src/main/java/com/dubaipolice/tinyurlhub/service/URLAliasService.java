package com.dubaipolice.tinyurlhub.service;



import java.util.concurrent.CompletableFuture;

import com.dubaipolice.tinyurlhub.model.URLAliasMappingDTO;
import com.dubaipolice.tinyurlhub.model.URLAliasMappingEntity;

public interface URLAliasService {

	public CompletableFuture<URLAliasMappingEntity> createAlias(URLAliasMappingDTO dto) throws Exception;
	public CompletableFuture<String> getRealURL(String alias)throws Exception;
}
