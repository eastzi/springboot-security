package com.study.security_eastzi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	//cors를 허용하는 설정 
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*"); //허용할 url
		config.addAllowedHeader("*"); //허용할 header
		config.addAllowedMethod("*"); //허용할 http method
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
