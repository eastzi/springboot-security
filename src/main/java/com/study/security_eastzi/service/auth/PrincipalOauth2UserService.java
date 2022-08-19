package com.study.security_eastzi.service.auth;

import java.util.Map;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.study.security_eastzi.domain.user.User;
import com.study.security_eastzi.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j //final LOGGER를 자동생성함.
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	
	//id생성 쪽에서 db에 접근하기
	private final UserRepository userRepository;
	
	/*
	 * <loadUser 메소드의 목적>
	 * 1. 구글, 네이버 로그인 이후 가져온 사용자 정보(email, name 등)들을 기반으로 가입 및 정보수정, 세션저장 등의 기능 지원 
	 * 	  -> OAuth2User의 정보를 우리 서버 dataBase에 등록시키는 것
	 * 2. 정보가 없다 -> 우리 사이트에 회원가입 시키겠다. 
	 */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		String provider = null;
		
		/*
		 * super.loadUser(userRequest) -> userInfoEndpoint() 결과 즉, OAuth2User 정보를 가진 객체를 리턴함. 
		 */
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		/*
		 * ClientRegistration
		 * 1. Provider 정보를 가짐(아이디, 시크릿, 네임 등) 
		 */

		ClientRegistration clientRegistration = userRequest.getClientRegistration();
		
		/*
		 * attributes
		 * 1. 실제 프로필 정보를 Map 형태로 가짐. 
		 * 2. getAttributes를 호출하면 Map으로 리턴
		 */
		Map<String, Object> attributes = oAuth2User.getAttributes();
		
		//토큰정보를 받아봄
		log.error(">>>>> ClientRegistration: {}", clientRegistration);
		//google에서 사용자 정보 받아보기 
		log.error(">>>>> oAuth2User: {}", attributes);
		
		//naver or google을 들고옴 -> naver or google 구분을 clientName으로 함. 
		provider = clientRegistration.getClientName();

		/*
		 * user 객체
		 * 1. 로그인 되어질 user 객체 
		 * 2. 위의 과정들은 user 객체를 가져오기 위한 준비작업 
		 */
		User user = getOAuth2User(provider, attributes);
		
		/*
		 * 반환타입이 OAuth2User인데 PrincipalDetails인 이유
		 * 1. impl을 받고 있음. 
		 * 2. 우리가 필요한 것은 user 정보들. 따라서 밑에 getOAuth2User 메소드 생성.
		 */
		
		return new PrincipalDetails(user, attributes);
	
		
//		if(provider.equalsIgnoreCase("google")) {
//			
//		}else if(provider.equalsIgnoreCase("naver")) {
//			
//		}else {
//			throw new OAuth2AuthenticationException("provider Error!");
//		}

	}
	
	private User getOAuth2User(String provider, Map<String, Object> attributes) throws OAuth2AuthenticationException {
		User user = null;
		String oauth2_id = null;
		String id = null;
		Map<String, Object> response = null;

		//id 생성 
		if(provider.equalsIgnoreCase("google")) {
			response = attributes;
			id = (String) response.get("sub");
//			oauth2_id = provider + "_" + attributes.get("sub");
		}else if(provider.equalsIgnoreCase("naver")) {
			response = (Map<String, Object>) attributes.get("response");
			id = (String) response.get("id");
//			oauth2_id = provider + "_" + response.get("id");
		}else { //provider가 google이나 naver가 아니면 예외를 던짐.
			throw new OAuth2AuthenticationException("provider Error!");
		}
		
		//db에 oauth2_id 생성
		oauth2_id = provider + "_" + id;
		
		//db에 위에서 생성된 id가 있는지 체크 
		//예외발생 =  db에 문제가 있음
		try {
			//user 정보가 있다면 if문 건너띄고 user로 리턴 
			user = userRepository.findOAuth2UserByUsername(oauth2_id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2AuthenticationException("DATABASE Error!");
		}
		
		//user 정보가 없는 경우 
		if(user == null) { //null = 회원가입이 필요한 상태 
			//user 객체 생성 - builder 패턴
			user = User.builder()
					.user_name((String) response.get("name"))
					.user_email((String) response.get("email"))
					.user_id(oauth2_id)
					.oauth2_id(oauth2_id)
					.user_password(new BCryptPasswordEncoder().encode(id))
					.user_roles("ROLE_USER")
					.user_provider(provider)
					.build();
			try {
				//insert
				userRepository.save(user);
				//save 후 user 다시 들고오기 -> 생성 후 검증이 완료된 유효한 데이터를 들고오는 것 
				user = userRepository.findOAuth2UserByUsername(oauth2_id);
			} catch (Exception e) {
				e.printStackTrace();
				throw new OAuth2AuthenticationException("DATABASE Error!");
			} //user를 저장함. 그전에 user를 만들어야 함.
		}
		
		return user;
	}
}
