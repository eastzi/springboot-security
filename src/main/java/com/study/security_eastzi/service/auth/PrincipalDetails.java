package com.study.security_eastzi.service.auth;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.study.security_eastzi.domain.user.User;

import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User { 
	/*
	 * UserDetails
	 * 1. 사용자 정보를 담는 인터페이스 
	 * 2. db에서 사용자 정보를 가져오는 건 -> userDetailsService 
	 * 3. userDetails 의 타입을 상속받은 PrincipalDetails 를 authentication 객체 안에 넣음
	 */
	private static final long serialVersionUID = 1L;
	
	private User user;
	private Map<String, Object> attribute; 
	
	public PrincipalDetails(User user) {
		this.user = user;
	} 
	
	public PrincipalDetails(User user, Map<String, Object> attribute) {
		this.user = user;
		this.attribute = attribute;
	} 

	//계정이 가지고 있는 권한을(해당 user의 권한) List로 리턴하기 위한 설정 
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		
//		List<String> roleList = user.getUserRoles(); //split가 있으면 리스트를 받음
//		
//		for(String role : roleList) {
//			GrantedAuthority authority = new GrantedAuthority() {
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public String getAuthority() {
//					return role;
//				}
//			};
//			
//			grantedAuthorities.add(authority); //grantedAuthorities의 객체만 받을 수 있으므로 객체를 생성해야함.
//		}
		
//		GrantedAuthority authority1 = new GrantedAuthority() {
//			
//			@Override
//			public String getAuthority() {
//				return roleList.get(0);
//			}
//		};
//		
//		user.getUser_roles(); //리스트를 반환함. -> 컬렉션으로 옮겨져야 함
		
		/*
		 * 람다식으로 변형
		 * user.getUserRoles().forEach(role -> {
		 * 	grantedAuthorities.add(() -> role);
		 * });
		 */

		 user.getUserRoles().forEach(role -> {
			 grantedAuthorities.add(() -> role);
		 });
		 
		return grantedAuthorities;
	}
	
	//계정 비밀번호 리턴
	@Override
	public String getPassword() {
		return user.getUser_password();
	}

	//계정 이름 리턴(아이디 or 이메일)  
	@Override
	public String getUsername() {
		return user.getUser_id();
	}
	
	/*
	 * 계정 만료여부 확인
	 * 1. true - 만료안됨
	 * 2. false - 만료됨
	 */
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * 계정 잠김 여부
	 * 1. true - 계정 잠기지 않음
	 * 2. false - 계정 잠김 
	 */
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * 비밀번호 만료여부
	 * 1. true - 만료안됨
	 * 2. false - 만료됨
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	/*
	 * 사용자 활성화 여부(사용가능 여부)
	 * (사이트 내에서 1년동안 로그인을 안하면 휴먼계정으로 전환함)
	 * 1. true - 활성화
	 * 2. false - 비활성화
	 */
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attribute;
	}

	@Override
	public String getName() {
		return user.getUser_name();
	}
	
}
