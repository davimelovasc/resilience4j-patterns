package com.example.demo.resilienceModule.pattern;

import com.example.demo.cliente.Util;

public class PatternFactory {
	
	public Pattern getPattern(String patternName) {
		if(patternName.equalsIgnoreCase(Util.NORMAL_PATTERN_KEY)) {
			return new Normal();
		} else if(patternName.equalsIgnoreCase(Util.CIRCUIT_BREAKER_PATTERN_KEY)) {
			return new CircuitBreakerPattern();
		} else if(patternName.equalsIgnoreCase(Util.RETRY_PATTERN_KEY)) {
			return new RetryPattern();
		}
		return null;
	}

}
