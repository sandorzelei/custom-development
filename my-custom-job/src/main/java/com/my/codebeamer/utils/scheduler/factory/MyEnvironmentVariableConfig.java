package com.my.codebeamer.utils.scheduler.factory;

public class MyEnvironmentVariableConfig {

	private String environmentVariableName;

	private String defaultValue;

	public MyEnvironmentVariableConfig(String environmentVariableName, String defaultValue) {
		super();
		this.environmentVariableName = environmentVariableName;
		this.defaultValue = defaultValue;
	}

	public String getEnvironmentVariableName() {
		return environmentVariableName;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

}