package com.study.security_eastzi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.study.security_eastzi.config.auth.AuthFailureHandler;


@EnableWebSecurity //기존의 WebSecurityConfigurerAdapter를 비활성시키고 현재 시큐리티 설정에 따른다는 의미(필수설정)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter { //adapter -> 스프링 시큐리티의 웹 보안 기능 초기화 및 설정
	/*
	 * <SecurityConfig>
	 * 1. Security Dependency를 추가한 이후 기본적인 security를 설정및 구현하는 클래스
	 * 2. HttpSecurity 라는 세부적인 보안기능을 설정할수 있는 API를 제공하는 클래스를 생성한다.
	 */
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		1. csrf -> 사이트 간 요청 위조.(데이터를 가로채서 변조) 
		2. token -> 인증에 관련된 것(검증요소, 표준화된 것)
		3. jwt -> 웹토큰 
		*/
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/", "/index", "/mypage/**") //우리가 지정한 요청 
			.authenticated()			//인증을 거쳐야 하는 요청 -> /와 index 이외의 다른 모든 요청은 모두 권한을 부여함.(인증필요x)
			.anyRequest()
			.permitAll() //모든 권한을 줌. 인증을 거칠 필요 없음.
			.and()
			.formLogin() //로그인 방법 3가지 - Http / form / jwt(토큰이용) 로그인 중 한가지 
			.loginPage("/auth/signin") //로그인시 로그인페이지에 접근하는 get요청 주소 
			.loginProcessingUrl("/auth/signin") //로그인 요청(= post요청(정보 보호를 위해 get이 아님)) 
			.failureHandler(new AuthFailureHandler())
			.defaultSuccessUrl("/"); //로그인 성공시 보낼 주소 / antMatchers 주소로 보냄. / = /, /index = /index, /mypage = /mypage (이전페이지로 보냄)
	
	}
}
