package com.dubaipolice.tinyurlhub.repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.dubaipolice.tinyurlhub.model.Test;

public interface TestRepository extends CassandraRepository<Test, UUID> {
  
}
