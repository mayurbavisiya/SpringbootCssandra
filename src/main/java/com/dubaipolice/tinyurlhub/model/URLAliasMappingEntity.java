package com.dubaipolice.tinyurlhub.model;

import java.util.UUID;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Table(value = "URLAliasMapping")
public class URLAliasMappingEntity {

	@JsonView
	@PrimaryKeyColumn(name = "realurl", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String realurl;

	@JsonView
	@PrimaryKeyColumn(name = "alias", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String alias;

	@JsonView
	@Column(value = "expirytime")
	private int expirytime;

	@JsonView
	@Column(value = "urlAccessLimit")
	private int urlAccessLimit;

	@JsonIgnore
	@Column(value = "created_date")
	private UUID created_date;

	@JsonView
	@Column(value = "no_of_hit")
	private int noOfHit;

	@Transient
	private String date;

	public URLAliasMappingEntity() {
		super();
	}

	public URLAliasMappingEntity(String realurl, String alias, int expirytime, int urlAccessLimit, UUID created_date,
			int noOfhit) {
		this.alias = alias;
		this.realurl = realurl;
		this.expirytime = expirytime;
		this.urlAccessLimit = urlAccessLimit;
		this.created_date = created_date;
		this.noOfHit = noOfhit;
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

	public int getNoOfHit() {
		return noOfHit;
	}

	public void setNoOfHit(int noOfHit) {
		this.noOfHit = noOfHit;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
