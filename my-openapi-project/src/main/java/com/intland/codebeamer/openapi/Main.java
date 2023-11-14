package com.intland.codebeamer.openapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intland.swagger.client.ApiClient;
import com.intland.swagger.client.ApiException;
import com.intland.swagger.client.api.ProjectApi;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws ApiException {
		ApiClient apiClient =  new CodebeamerApiClientFactory().getApiClient("http://localhost:8080", "youruser", "yourpassword");		
		ProjectApi projectApi = new ProjectApi(apiClient);
		
		logger.info("Projects:");
		projectApi.getProjects().stream().forEach(p -> logger.info("ID: {} - Name: {}", p.getId(), p.getName()));	
	}

}