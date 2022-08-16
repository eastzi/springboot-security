package com.study.security_eastzi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.study.security_eastzi.config.auth.AuthFailureHandler;
import com.study.security_eastzi.service.auth.PrincipalOauth2UserService;

import lombok.RequiredArgsConstructor;


@EnableWebSecurity //기존의 WebSecurityConfigurerAdapter를 비활성시키고 현재 시큐리티 설정에 따른다는 의미(필수설정)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter { //adapter -> 스프링 시큐리티의 웹 보안 기능 초기화 및 설정
	/*
	 * <SecurityConfig>
	 * 1. Security Dependency를 추가한 이후 기본적인 security를 설정및 구현하는 클래스(서버설정 클래스) 
	 * 2. HttpSecurity 라는 세부적인 보안기능을 설정할수 있는 API를 제공하는 클래스를 생성한다.
	 * 3. api = 인증, 인가 api
	 */

	private final PrincipalOauth2UserService principalOauth2UserService;
	
	/*
	 * <BCryptPasswordEncoder>
	 * 비밀번호가 노출되어 인코딩 되는 방식을 사용자 정의함. 
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/*
	 * <HttpSecurity http>
	 * 1. 사용자의 세부적인 보안 기능 설정
	 * 2. HttpSecurity 클래스에는 인증 및 인가와 관련된 다양한 API를 제공한다.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception { //WebSecurityConfigurerAdapter 에서 override
		/*
		 * 1. csrf -> 사이트 간 요청 위조.(id, pw를 입력 후 로그인 요청시(로그인 외 요청도 해당) 요청 데이터를 가로채서 변조 및 위조) 
		 * 2. token -> 인증에 관련된 것(검증요소, 표준화된 것)
		 * 3. jwt -> 웹토큰 
		 */
		http.csrf().disable();
		http.authorizeRequests() //요청이 들어왔을때 인증을 거쳐라.
			// 인가 API - URL 방식 
			.antMatchers("/api/v1/grant/test/user/**")
			.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			
			.antMatchers("/api/v1/grant/test/manager/**")
			.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			
			.antMatchers("/api/v1/grant/test/admin/**")
			.access("hasRole('ROLE_ADMIN')")
			
			.antMatchers("/", "/index", "/mypage/**") //우리가 지정한 요청(단순히 저 3개의 요청일 때 - 고정 주소 아님.) 
			.authenticated()			//인증을 거쳐야 하는 요청(인증을 거쳐라) -> /와 index 이외의 다른 모든 요청은 모두 권한을 부여함.(인증필요x)
										// 인증이 필요하다 = 로그인이 필요하다 (로그인 페이지로 보냄)
			.anyRequest() //모든 권한을 줌. 인증을 거칠 필요 없음.
			.permitAll() //모든 권한을 줌. 인증을 거칠 필요 없음.
			
			.and()
			
			.formLogin() //로그인 방법 3가지 - httpBasic / formLogin / jwt(토큰이용) 로그인 중 한가지 
			.loginPage("/auth/signin") //로그인시 로그인페이지에 접근하는 get요청 주소(사용자 정의 로그인 페이지 주소 - 기본제공x)
			.loginProcessingUrl("/auth/signin") //로그인 요청(= post요청(정보 보호를 위해 get이 아님)) / PrincipalDetailsService 호출함.
			.failureHandler(new AuthFailureHandler()) //로그인 실패 시 호출할 핸들러 -> 핸들러는 로그인 이후 작업을 처리함.
			
			.and()
			
			.oauth2Login()
			.userInfoEndpoint()
			.userService(principalOauth2UserService) //PrincipalOauth2UserService 객체가 옴
			
			.and()
			
			.defaultSuccessUrl("/") //로그인 성공시 보낼 주소 / antMatchers 주소로 보냄. / = /, /index = /index, /mypage = /mypage (이전페이지로 보냄)
			;
	}
}
