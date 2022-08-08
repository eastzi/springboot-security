package com.study.security_eastzi.domain.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {
	public int save(User user) throws Exception; //회원가입 시
	public User findUserByUsername(String username) throws Exception; //로그인 시 
}
