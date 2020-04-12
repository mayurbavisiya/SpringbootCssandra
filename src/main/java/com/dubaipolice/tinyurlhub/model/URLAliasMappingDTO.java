package com.dubaipolice.tinyurlhub.model;

public class URLAliasMappingDTO {
	private int expirytime;
	private String realurl;
	private int urlAccessLimit;
//	private String alias;

	public int getExpirytime() {
		return expirytime;
	}

	public void setExpirytime(int expirytime) {
		this.expirytime = expirytime;
	}

	public String getRealurl() {
		return realurl;
	}

	public void setRealurl(String realurl) {
		this.realurl = realurl;
	}

	public int getUrlAccessLimit() {
		return urlAccessLimit;
	}

	public void setUrlAccessLimit(int urlAccessLimit) {
		this.urlAccessLimit = urlAccessLimit;
	}
//
//	public String getAlias() {
//		return alias;
//	}
//
//	public void setAlias(String alias) {
//		this.alias = alias;
//	}

}
