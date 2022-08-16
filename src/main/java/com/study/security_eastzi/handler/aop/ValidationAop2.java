package com.study.security_eastzi.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.study.security_eastzi.handler.exception.CustomValidationApiException;

@Aspect
@Component
public class ValidationAop2 {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Pointcut("@annotation(com.study.security_eastzi.handler.aop.annotation.ValidCheck2)")
	private void enableValid() {}
	
	@Before("enableValid()")
	public void ValidBefore(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		LOGGER.info(">>>>> 유효성 검사 중...");
		
		for(Object arg : args) {
			//instance of 와 동일한 방법 -> 이 방식으로 사용할 것 
			if(arg.getClass() == BeanPropertyBindingResult.class) {
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<String, String>();
					
					bindingResult.getFieldErrors().forEach(error -> {
						errorMap.put(error.getField(), error.getDefaultMessage());
					});
					
					throw new CustomValidationApiException("유효성 검사 실패", errorMap);
				}
			}
		}
	}
	
	@AfterReturning(value = "enableValid()", returning = "returnObj")
	public void afterReturn(JoinPoint joinPoint, Object returnObj) {
		LOGGER.info("유효성 검사 완료 : {}", returnObj);
	}
}
