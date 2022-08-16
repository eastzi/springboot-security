package com.study.security_eastzi.handler.aop.annotation;

import static java.lang.annotation.ElementType.METHOD;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME) // 실행 중에 
@Target({ TYPE, METHOD }) //class, method 위에 달 수 있다
public @interface Timer {
	
}
