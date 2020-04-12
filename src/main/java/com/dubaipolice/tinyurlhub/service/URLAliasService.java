package com.dubaipolice.tinyurlhub.service;



import java.util.concurrent.CompletableFuture;

import com.dubaipolice.tinyurlhub.model.URLAliasMappingDTO;

public interface URLAliasService {

	public CompletableFuture<String> createAlias(URLAliasMappingDTO dto) throws Exception;
	public CompletableFuture<String> getRealURL(String alias)throws Exception;
}
