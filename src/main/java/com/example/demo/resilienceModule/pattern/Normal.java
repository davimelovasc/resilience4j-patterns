package com.example.demo.resilienceModule.pattern;

import org.springframework.stereotype.Service;

import com.example.demo.GlobalVariables;
import com.example.demo.cliente.Connector;

import util.Options;

@Service
public class Normal implements Pattern {
	
	private Connector connector = new Connector();


	@Override
	public boolean request(GlobalVariables variables, Options options) {
		try {
			variables.requestsToServer++;
			connector.makeRequest();
			variables.successRequests++;
		}catch(Exception ex) {
			variables.failRequests++;
			return false;
		}
		return true;
	}

}
