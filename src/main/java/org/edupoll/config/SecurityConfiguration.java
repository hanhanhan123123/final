package org.edupoll.config;

import org.edupoll.config.support.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final JWTAuthenticationFilter jwtAuthenticationFilter;
	
	
	@Bean
	SecurityFilterChain finalAppSecurityChain(HttpSecurity http) throws Exception {
		//("/api/v1/user/private/**") 이쪽경로만 시큐리티 적용. 나머지는 허용
		http.csrf(t -> t.disable());
		http.sessionManagement(t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		//경로에 있는 것은 보호하고 다른건 허용하겠다. 경로에 따라 인증이 필요한가 아닌가 설정하는 곳
		http.authorizeHttpRequests(t -> t //
				.requestMatchers("/api/v1/user/private/**").authenticated() //인증
				.requestMatchers(HttpMethod.POST,"/api/vi/feed/storage").authenticated()//
				//post 요청일때는 인증이 필요하다는 뜻 메소드를 지정하지 않으면 모든 메소드가 막힌다.
				.anyRequest().permitAll()); //anyRequest는 else같은 느낌. 어떤 패턴은 인증을 받아야되고 어떤 패턴은 인증을 받아야되고...
		// ↑보호해야될 리소스 설정하는 곳. 나머지는 디폴트값이라고 생각해도 됨
		
		
		//위, 아래 이 두개정도 설정하면 됨
		http.anonymous(t -> t.disable());
		http.logout(t -> t.disable());
		http.addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class);
		//마지막 최종 필터
		http.cors(); //cors 활성화 . 시큐리티에서 cors를 막아서 활성화해준다
		return http.build();
	}
}
