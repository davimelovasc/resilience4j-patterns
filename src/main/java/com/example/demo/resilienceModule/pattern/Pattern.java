package com.example.demo.resilienceModule.pattern;

import com.example.demo.GlobalVariables;

import util.Options;

public interface Pattern {
	boolean request(GlobalVariables variables, Options options);
}
