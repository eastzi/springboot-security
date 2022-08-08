package com.study.security_eastzi.service.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.study.security_eastzi.domain.user.User;

public class PrincipalDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private User user;
	
	public PrincipalDetails(User user) {
		this.user = user;
	} 

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		user.getUser_roles();
		return null;
	}

	@Override
	public String getPassword() {
		return user.getUser_password();
	}

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
	 * 사용자 활성화 여부
	 * 1. true - 활성화
	 * 2. false - 비활성화
	 */

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
