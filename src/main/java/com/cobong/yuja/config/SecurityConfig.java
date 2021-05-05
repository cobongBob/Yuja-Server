package com.cobong.yuja.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import com.cobong.yuja.config.auth.PrincipalDetailsService;
import com.cobong.yuja.config.jwt.JwtAuthenticationEntryPoint;
import com.cobong.yuja.config.jwt.JwtAuthenticationFilter;

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
			.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // https://www.inflearn.com/questions/34886
				.and()
			.authorizeRequests()
//				.antMatchers()
//					.permitAll()
//				.antMatchers()
//					.hasAuthority("ROLE_GENERAL")
//				.antMatchers()
//					.hasAuthority("ROLE_YOUTUBER")
//				.antMatchers()
//					.hasAuthority("ROLE_EDITOR")
//				.antMatchers()
//					.hasAuthority("ROLE_THUMBNAILOR")
//				.antMatchers()
//					.hasAuthority("ROLE_ADMIN")
//				.antMatchers()
//					.hasAuthority("ROLE_MANAGER")
				.anyRequest()
					.permitAll(); // 임시
//					.authenticated();
		
		// 내 커스텀 필터를 추가하고 가장먼저 실행시킴
		http
			.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}
