package com.study.security_eastzi.domain.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
	private int user_code;
	private String user_name;
	private String user_email;
	private String user_id;
	private String oauth2_id;
	private String user_password;
	private String user_roles;         //ROLE_USER,ROLE_ADMIN,ROLE_MANAGER
	private String user_provider;
	private String user_profile_img;
	private String user_address;
	private String user_phone;
	private int user_gender;
	
	public List<String> getUserRoles() {
		if(user_roles == null || user_roles.isBlank()) {
			return new ArrayList<String>();
		}
		return Arrays.asList(user_roles.replaceAll(" ", "").split(",")); //role 3개를 배열로 만들고 리스트로 받음
		//split된 리스트를 서비스클래스로 보냄.
		//replaceAll -> 공백제거
	}
}
