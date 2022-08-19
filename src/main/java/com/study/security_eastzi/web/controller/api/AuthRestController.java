package com.study.security_eastzi.web.controller.api;

import java.util.HashMap;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.security_eastzi.handler.aop.annotation.Log;
import com.study.security_eastzi.handler.aop.annotation.Timer;
import com.study.security_eastzi.handler.aop.annotation.ValidCheck2;
import com.study.security_eastzi.handler.exception.CustomValidationApiException;
import com.study.security_eastzi.service.auth.AuthService;
import com.study.security_eastzi.service.auth.PrincipalDetails;
import com.study.security_eastzi.service.auth.PrincipalDetailsService;
import com.study.security_eastzi.web.dto.CMRespDto;
import com.study.security_eastzi.web.dto.auth.SignupReqDto;
import com.study.security_eastzi.web.dto.auth.UsernameCheckReqDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {
	
	/*
	 * <DI>
	 * 1. AuthRestController가 AuthService, PrincipalDetailsService에 의존성이 있다.
	 * 2. 생성자를 통한 의존관계 주입 
	 */
	
	private final AuthService authService;
	private final PrincipalDetailsService principalDetailsService;
	
	@Log
	@Timer //직접 생성한 어노테이션 
	@ValidCheck2
	@GetMapping("/signup/validation/username")
	public ResponseEntity<?> checkUsername(@Valid UsernameCheckReqDto usernameCheckReqDto, BindingResult bindingResult) {
		
//		if(bindingResult.hasErrors()) {
//			Map<String, String> errorMessage = new HashMap<String, String>();
//			
//			bindingResult.getFieldErrors().forEach(error -> {
////				System.out.println("오류발생 필드명: " + error.getField());
////				System.out.println("오류발생 상세메세지: " + error.getDefaultMessage());
//				errorMessage.put(error.getField(), error.getDefaultMessage());
//			});
//			//return ResponseEntity.badRequest().body(new CMRespDto<>(-1, "유효성 검사 실패", errorMessage));
//			//예외발생
//			throw new CustomValidationApiException("유효성 검사 실패", errorMessage);
//		}
		
		boolean status = false;
		
		try {
			status = authService.checkUsername(usernameCheckReqDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "서버 오류", status));
		}
		
		return ResponseEntity.ok(new CMRespDto<>(1, "회원가입 가능여부", status));
	}
	
	@ValidCheck2
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody @Valid SignupReqDto signupReqDto, BindingResult bindingResult) {
		boolean status = false;
		
//		if(bindingResult.hasErrors()) {
//			Map<String, String> errorMessage = new HashMap<String, String>();
//			
//			bindingResult.getFieldErrors().forEach(error -> {
//				errorMessage.put(error.getField(), error.getDefaultMessage());
//			});
//			
//			throw new CustomValidationApiException("유효성 검사 실패", errorMessage);
//		
//		}
		
		try {
			status = principalDetailsService.addUser(signupReqDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "회원가입 실패", status));
		}
		
		return ResponseEntity.ok(new CMRespDto<>(1, "회원가입 성공", status));

	}
	
	@GetMapping("/principal")
	public ResponseEntity<?> getPrincipal(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		if(principalDetails == null) {
			return ResponseEntity.badRequest().body(new CMRespDto<>(-1, "principal is null", null));
		}
		return ResponseEntity.ok().body(new CMRespDto<>(1, "principal success", principalDetails.getUser()));
	}
}
