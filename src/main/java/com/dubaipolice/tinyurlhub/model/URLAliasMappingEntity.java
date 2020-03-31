package com.dubaipolice.tinyurlhub.model;

import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "URLAliasMapping")
public class URLAliasMappingEntity {

	@PrimaryKeyColumn(name = "realurl", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String realurl;

	@PrimaryKeyColumn(name = "alias", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String alias;

	private int expirytime;
	private int urlAccessLimit;

	private UUID created_date;

	public URLAliasMappingEntity() {
		super();
	}

	public URLAliasMappingEntity(String realurl, String alias, int expirytime, int urlAccessLimit, UUID created_date) {
		this.alias = alias;
		this.realurl = realurl;
		this.expirytime = expirytime;
		this.urlAccessLimit = urlAccessLimit;
		this.created_date = created_date;
		// TODO Auto-generated constructor stub
	}

	public String getRealurl() {
		return realurl;
	}

	public void setRealurl(String realurl) {
		this.realurl = realurl;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getExpirytime() {
		return expirytime;
	}

	public void setExpirytime(int expirytime) {
		this.expirytime = expirytime;
	}

	public int getUrlAccessLimit() {
		return urlAccessLimit;
	}

	public void setUrlAccessLimit(int urlAccessLimit) {
		this.urlAccessLimit = urlAccessLimit;
	}

	public UUID getCreated_date() {
		return created_date;
	}

	public void setCreated_date(UUID created_date) {
		this.created_date = created_date;
	}

}
