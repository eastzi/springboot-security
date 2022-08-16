package com.study.security_eastzi.handler.aop;

import java.lang.reflect.Parameter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class TimerAop {
	
	//syso 대신에 logger 사용
	//logger로 사용하기 위한 생성단계
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	//* ->returnType, execution -> findMethod
	@Pointcut("execution(* com.study.security_eastzi.web.controller..*.*(..))") //하위의 모든 메소드, 모든 매개변수
	private void pointCut() {}
	
	//pointcut => 위치, 어떤 메소드의 앞에서 실행할 것인지 / 오버로드 일때 매개변수에 따라서도 가능 
	@Pointcut("@annotation(com.study.security_eastzi.handler.aop.annotation.Timer)")
	private void enableTimer() {}
	
	//Throwable -> exception 상위 object
	//around 뒤에는 무조건 throw추가
	@Around("pointCut() && enableTimer()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		//메소드 결과
		Object result = joinPoint.proceed();
 		
		stopWatch.stop();
		
		//joinPoint-> 메소드
		//메소드 실행 시간: {클래스명}, ({메소드명}) = {메소드결과} 
		LOGGER.info(">>>>> {}({}) 메소드 실행 시간: {}ms", 
				joinPoint.getSignature().getDeclaringTypeName(), 
				joinPoint.getSignature().getName(),
				stopWatch.getTotalTimeSeconds());
		
		return result;
	}
	
	
}
