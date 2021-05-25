package com.cobong.yuja.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import com.cobong.yuja.config.auth.PrincipalDetailsService;
import com.cobong.yuja.config.jwt.JwtAuthenticationEntryPoint;
import com.cobong.yuja.config.jwt.JwtAuthenticationFilter;
import com.cobong.yuja.exception.Forbidden403Exception;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		securedEnabled = true, // @secured 어노테이션으로 컨트롤러/서비스 메소드를 보호함
		jsr250Enabled = true, // @RolesAlowed (권한 부여 결정)
		prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	PrincipalDetailsService principalDetailsService;
	
	@Autowired
	JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	Forbidden403Exception unauthorizedException;
		
	// 유효성, 토큰관련 세부사항 로드 
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	// 사용자 인증을 위한 Spring Security 를 생성하는데 사용
	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder
		.userDetailsService(principalDetailsService)
		.passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//AuthenticationFilter에서 생성한 UsernamePasswordToken을 AuthenticationManager에게 전달
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	// antMatchers 설정 진행해야댐
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors()
				.and()
			.csrf()
				.disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // https://www.inflearn.com/questions/34886
				.and()
			.exceptionHandling()
				.accessDeniedHandler(unauthorizedException)
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and()
			.headers().frameOptions().disable()
				.addHeaderWriter(new StaticHeadersWriter("X-FRAME-OPTIONS", "ALLOW-FROM " + "http://localhost:3000"))
				.and()
			.authorizeRequests()
			
//				.antMatchers(HttpMethod.GET,"/api/9/board/**","/api/8/board/**")
//					.hasAnyAuthority("ROLE_MANAGER", "ROLE_MANAGER") // 관리자
//					
				.antMatchers(HttpMethod.GET,"/api/2/board/**", "/api/auth/**","/api/main/board")
					.permitAll() // 비회원 이상
					
				.antMatchers(HttpMethod.POST,"/api/2/board", "/api/3/board", "/api/4/board","/api/5/board","/api/7/board") 
					.hasAnyAuthority("ROLE_GENERAL", "ROLE_YOUTUBER","ROLE_EDITOR","ROLE_THUMBNAILOR","ROLE_MANAGER","ROLE_ADMIN" ) // GENERAL 이상
					
				.antMatchers(HttpMethod.PUT, "/api/**/board/**") 
					.hasAnyAuthority("ROLE_GENERAL", "ROLE_YOUTUBER","ROLE_EDITOR","ROLE_THUMBNAILOR","ROLE_MANAGER","ROLE_ADMIN" ) // GENERAL 이상
					
				.antMatchers(HttpMethod.DELETE,"/api/2/board/**") 
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_EDITOR","ROLE_THUMBNAILOR","ROLE_MANAGER","ROLE_ADMIN" ) // GENERAL 이상
					
				.antMatchers(HttpMethod.POST,"/api/1/board")
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_MANAGER","ROLE_ADMIN") // YOUTUBER 이상
					
				.antMatchers(HttpMethod.PUT)
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_MANAGER","ROLE_ADMIN") // YOUTUBER 이상
					
				.antMatchers(HttpMethod.DELETE)
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_MANAGER","ROLE_ADMIN","ROLE_GENERAL") // YOUTUBER 이상
					
				.antMatchers(HttpMethod.DELETE)
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN", "ROLE_GENERAL") 
					
				.antMatchers(HttpMethod.DELETE) // 유저 삭제, 신고리스트 삭제 
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN", "ROLE_GENERAL") // 매니저
					
				.antMatchers(HttpMethod.POST,"/api/8/board","/api/9/board") 
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN") // manager admin 
				.antMatchers(HttpMethod.GET,"/**")
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN") // manager admin 
				.antMatchers(HttpMethod.PUT)
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN") // manager admin 
				.antMatchers() 
					.hasAuthority("ROLE_THUMBNAILOR") // 썸넬러
//				.antMatchers()
//					.hasAuthority("ROLE_ADMIN")
				.anyRequest()
					.permitAll();// 임시
					//.authenticated();
		
		http
		.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}
