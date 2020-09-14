package com.example.demo.cliente;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.GlobalVariables;
import com.example.demo.resilienceModule.pattern.CircuitBreakerPattern;
import com.example.demo.resilienceModule.pattern.Normal;
import com.example.demo.resilienceModule.pattern.Pattern;
import com.example.demo.resilienceModule.pattern.PatternFactory;
import com.example.demo.resilienceModule.pattern.RetryPattern;

import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;

@Controller
public class ClienteController {
	
	@Autowired
	private CircuitBreakerPattern cb;
	
	@Autowired
	private Normal normal;
	
	@Autowired
	private RetryPattern retry;
	
	PatternFactory patternFactory = new PatternFactory();
	
	@PostMapping("/")
	public ResponseEntity<?> normalRequests(
			@RequestParam(name = "req")  Integer qtdReqSuccess, 
			@RequestParam(name = "maxReq") Integer maxRequest,
			@RequestParam(name = "mode") String resiliencePattern) {
		long time = System.currentTimeMillis();
		int reqSucessApp = 0, reqFailApp = 0;
		
		//GlobalVariables.resetVariables();
		GlobalVariables variables = new GlobalVariables();
		Pattern pattern = this.getPattern(resiliencePattern);
		
		Map<String, String> response = new HashMap<>();
		
		for (int i = 0; i < maxRequest && variables.successRequests < qtdReqSuccess ; i++) {
			if(pattern.request(variables)) {
				reqSucessApp++;
			} else {
				reqFailApp++;
			}
		}
		time = System.currentTimeMillis() - time;
		
		response.put("Resilience Pattern: ", pattern.getClass().getSimpleName());
		response.put("Time (ms): ", String.valueOf(time));
		response.put("Total req bem sucedidas (app cliente)", String.valueOf(reqSucessApp));
		response.put("Total req mal sucedidas (app cliente)", String.valueOf(reqFailApp));
		response.put("Total req bem sucedidas ao sv (mod. resiliencia)", String.valueOf(variables.successRequests));
		response.put("Total req mal sucedidas ao sv (mod. resiliencia)", String.valueOf(variables.requestsToServer - variables.successRequests));
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/config")
	public ResponseEntity<?> config() {
		RetryConfigCustomizer.of("", builder -> builder.maxAttempts(2));
		return null;
	}
	
	private Pattern getPattern(String patternKey) {
		if(patternKey.equalsIgnoreCase(Util.NORMAL_PATTERN_KEY)) {
			return this.normal;
		} else if(patternKey.equalsIgnoreCase(Util.CIRCUIT_BREAKER_PATTERN_KEY)) {
			return this.cb;
		} else if(patternKey.equalsIgnoreCase(Util.RETRY_PATTERN_KEY)) {
			return this.retry;
		}
		return normal;
	}


}
