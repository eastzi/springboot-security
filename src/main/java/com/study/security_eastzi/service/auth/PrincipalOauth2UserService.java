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
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		String provider = null;
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		ClientRegistration clientRegistration = userRequest.getClientRegistration();
		Map<String, Object> attributes = oAuth2User.getAttributes();
		
		//토큰정보를 받아봄
		log.error(">>>>> ClientRegistration: {}", clientRegistration);
		//google에서 사용자 정보 받아보기 
		log.error(">>>>> oAuth2User: {}", attributes);
		
		//naver or google을 들고옴 -> naver or google 구분을 clientName으로 함. 
		provider = clientRegistration.getClientName();
		
		User user = getOAuth2User(provider, attributes);
		
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
			id = (String) response.get("sub");
//			oauth2_id = provider + "_" + response.get("id");
		}else {
			throw new OAuth2AuthenticationException("provider Error!");
		}
		
		oauth2_id = provider + "_" + id;
		
		//db에 위에서 생성된 id가 있는지 체크 
		//예외발생 =  db에 문제가 있음
		try {
			user = userRepository.findOAuth2UserByUsername(oauth2_id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuth2AuthenticationException("DATABASE Error!");
		}
		
		//user로 찾았는지 체크 
		if(user == null) { //null = 회원가입이 필요한 상태 
			//user 생성
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
				userRepository.save(user);
				//save 후 user 다시 들고오기 
				user = userRepository.findOAuth2UserByUsername(oauth2_id);
			} catch (Exception e) {
				e.printStackTrace();
				throw new OAuth2AuthenticationException("DATABASE Error!");
			} //user를 저장함. 그전에 user를 만들어야 함.
		}
		
		return user;
	}
}
