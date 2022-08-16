package com.study.security_eastzi.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.study.security_eastzi.handler.exception.CustomValidationApiException;
import com.study.security_eastzi.web.dto.CMRespDto;

@RestController
@ControllerAdvice
public class RestControllerExceptionHandler {
	/*
	 * 요구사항에 의한 예외 처리 
	 * (ex. validation > 특정 값이 0~255범위가 아니면 유효하지 않은 값으로 판단하고 예외 처리)
	 */

	/*
	 * @ExceptionHandler라는 어노테이션을 쓰고
	 * 	인자로 캐치하고 싶은 예외클래스를 등록해주면 끝난다.
	 *
	 * authController에서 예외를 받아서 handler에서 넘겨줬다? -> 공백일 수 없습니다. 메시지
	 */

	@ExceptionHandler(CustomValidationApiException.class) //예외를 받는다..
	public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
		return ResponseEntity.badRequest().body(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()));
	}
	
}
