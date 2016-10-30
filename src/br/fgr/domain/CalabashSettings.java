package br.fgr.domain;

import com.google.gson.annotations.SerializedName;

public class CalabashSettings {

	@SerializedName("keystore_location")
	private String location;
	@SerializedName("keystore_password")
	private String password;
	@SerializedName("keystore_alias")
	private String alias;

	public CalabashSettings(String location, String password, String alias) {
		this.location = location;
		this.password = password;
		this.alias = alias;
	}

	public String getLocation() {
		return location;
	}

	public String getPassword() {
		return password;
	}

	public String getAlias() {
		return alias;
	}

}
