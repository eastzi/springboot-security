package com.study.security_eastzi.service.auth;

import java.util.Collection;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.security_eastzi.domain.user.User;
import com.study.security_eastzi.domain.user.UserRepository;
import com.study.security_eastzi.web.dto.auth.SignupReqDto;

import lombok.RequiredArgsConstructor;

@Service //메모리에 등록하는 역할 
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	/*
	 * UserDetailsService
	 * 1. dao로써 db에서 유저 정보를 직접 가져오는 역할/ 가져와서 리턴함.
	 *  
	 * PrincipalDetailsService
	 * 1. Authentication 객체를 생성하기 위한 class
	 * 
	 */


	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*
		 * <loadUserByUsername>
		 * 1. 로그인 요청을 받으면 실행되는 메소드 
		 * 2. db에서 사용자 정보를 불러오는 메소드 
		 * 3. 사용자 이름을 기준으로 사용자를 찾습니다.
		 * 4. username이 있다면 유저 객체를 반환하고, 없으면 null을 반환하여 로그인을 하게 함
		 * 
		 * <UsernameNotFoundException>
		 * 1. 사용자를 찾을 수 없거나 사용자에게 권한이 없는 경우 
		 * UsernameNotFoundException을 발생시킵니다.
		 */
		
		
//		System.out.println("username : " + username);
		User userEntity = null;
		
		try {
			//username 으로 사용자 정보를 가져옴. 
			userEntity = userRepository.findUserByUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
			//username 의 사용자 정보가 없다면 NotFound 예외를 던짐. 
			throw new UsernameNotFoundException(username);
		}
		
		//userEntity가 null일때 NotFound 예외를 던짐. 
		if(userEntity == null) { 
			throw new UsernameNotFoundException(username + "사용자 이름은 사용할 수 없습니다.");
		}

//		if(!username.equals("test")) { //loadUserByUsername를 config에서 호출받아 username을 비교
//			throw new UsernameNotFoundException(username + "사용자 이름은 사용할 수 없습니다.");
//		}
		
		/*
		PrincipalDetails 에서 사용함.(여기서는 더이상 사용안함)
		UserDetails userDetails = new UserDetails() {
			
			@Override
			public boolean isEnabled() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean isCredentialsNonExpired() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean isAccountNonLocked() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean isAccountNonExpired() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public String getUsername() {
				return "test";
			}
			
			@Override
			public String getPassword() { //암호화가 되어 있어야함.
				return new BCryptPasswordEncoder().encode("1234"); 
				//비밀번호를 인코딩해주는 메서드와 사용자의 의해 제출된 비밀번호와 
				//저장소에 저장되어 있는 비밀번호의 일치 여부를 확인해주는 메서드를 제공합니다.
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}
		};
		*/
		
		return new PrincipalDetails(userEntity);
	}
	
	public boolean addUser(SignupReqDto signupReqDto) throws Exception {
		return userRepository.save(signupReqDto.toEntity()) > 0;
	}

}
