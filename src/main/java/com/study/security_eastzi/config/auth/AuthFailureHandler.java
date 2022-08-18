package com.study.security_eastzi.config.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AuthFailureHandler implements AuthenticationFailureHandler{
	/*
	 * AuthenticationFailureHandler를 impl 하여 config에서 설정을 통해 핸들러로 등록 
	 */
	

	/*
	 * <onAuthenticationFailure>
	 * 1. 로그인 실패시 호출할 핸들러
	 * 2. 로그인 실패시 예외가 발생하는데 예외발생시 낚아채는 역할
	 * 	  (UsernameNotFoundException 같은 예외)
	 * 3. 매개변수로 request정보를 가지고, response 설정을 할 수 있고, 
	 * 	  exception으로 로그인 실패시 예외에 대한 정보를 가짐
	 */
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		System.out.println(exception.getMessage());
		
		//클라이언트로 전송되는 응답문자를 설정 
		response.setCharacterEncoding("utf-8");
		//클라이언트로 전송되는 응답 내용 유형 설정 -> text/html 응답을 문자로 설정 
		response.setContentType("text/html; charset=utf-8");
		//문자데이터를 반환할 수 있는 printWriter 개체를 반환함. 
		//회원가입이 안된 id,pw로 로그인시 록그인실패 alert을 띄움 
		response.getWriter().print("<html><head></head><body><script>alert(\"로그인실패\");history.back();</script></body></html>");
		
	}

}
