package com.study.security_eastzi.handler.exception;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class CustomValidationApiException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	//에러메시지를 담기 위한 map
	private Map<String, String> errorMap;
	
	private CustomValidationApiException() {
		this("error", new HashMap<String, String>());
	}
	
	public CustomValidationApiException(String message) {
		this(message, new HashMap<String, String>());
	}
	
	//메시지 + map
	public CustomValidationApiException(String message, Map<String, String> errorMap) {
		super(message);
		this.errorMap = errorMap;
	}
	
	
	
	

}
