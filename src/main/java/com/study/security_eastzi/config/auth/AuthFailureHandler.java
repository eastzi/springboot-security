package com.study.security_eastzi.config.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AuthFailureHandler implements AuthenticationFailureHandler{

	/*
	 * 로그인 실패시 호출할 핸들러
	 * 로그인 실패시 예외가 발생하는데 예외발생시 낚아채는 역할
	 */
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		System.out.println(exception.getMessage());
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().print("<html><head></head><body><script>alert(\"로그인실패\");history.back();</script></body></html>");
		
	}

}
