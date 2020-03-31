package com.dubaipolice.tinyurlhub.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.dubaipolice.tinyurlhub.model.URLAliasMappingEntity;

public interface URLAliasRepository extends CassandraRepository<URLAliasMappingEntity, String> {
	List<URLAliasMappingEntity> findByAlias(String alias);
}
