package com.dubaipolice.tinyurlhub.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.dubaipolice.tinyurlhub.model.URLAliasMappingEntity;

public interface URLAliasRepository extends CassandraRepository<URLAliasMappingEntity, String> {
	List<URLAliasMappingEntity> findByAlias(String alias);

	List<URLAliasMappingEntity> findByRealurl(String realurl);

//	URLAliasMappingEntity update(URLAliasMappingEntity entity);
//	<T> T update(T entity);

	// @Query("update URLAliasMapping set no_of_hit =:counter where alias
	// =:alias and realurl =:realurl ")
	// int increamentURLCounter(@Param("counter") Integer
	// counter,@Param("alias") String alias,@Param("realurl") String realurl);
}
