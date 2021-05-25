package com.cobong.yuja.config;

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
//	1. Youtube
//	2. Editor
//	3. Thumb
//	4. Winwin
//	5. Colabo
//	6. CustomService
//	7. Free
//	8. Report
//	9. Notice
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
				.antMatchers("/static/**")
					.permitAll()
				.antMatchers("/api/auth/**","/api/main/board") // 로그인 회원가입
					.permitAll() //  all methods all authorities
				.antMatchers(HttpMethod.GET,"/api/1/board/**","/api/2/board/**","/api/3/board/**","/api/4/board/**", "/api/5/board/**", "/api/7/board/**","/api/6/board/**")
					.permitAll()
					
				// 모든 회원	
				.antMatchers(HttpMethod.POST,"/api/2/board", "/api/3/board", "/api/4/board","/api/5/board","/api/6/board", "/api/7/board","/api/2/board/img/upload","/api/3/board/img/upload","/api/4/board/img/upload","/api/5/board/img/upload","/api/6/board/img/upload","/api/7/board/img/upload", 
						"api/3/thumbnail/upload", "/socket/room/**" , "/api/comment") 
					.hasAnyAuthority("ROLE_GENERAL", "ROLE_MANAGER","ROLE_ADMIN") // 모든 회원 POST
					
				.antMatchers(HttpMethod.PUT,"/api/2/board/**", "/api/3/board/**","/api/4/board/**", "/api/5/board/**","/api/6/board", "/api/7/board", "/api/user/**") 
					.hasAnyAuthority("ROLE_GENERAL", "ROLE_MANAGER","ROLE_ADMIN", "/api/comment/**")// 모든 회원 PUT
					
				.antMatchers(HttpMethod.DELETE, "/api/4/board/**", "/api/5/board/**", "/socket/room/**", "/api/comment/**", "/api/user/**") 
					.hasAnyAuthority("ROLE_GENERAL", "ROLE_MANAGER", "ROLE_ADMIN")// 모든 회원 DELETE
					
				.antMatchers(HttpMethod.GET, "/api/user/board/**","/api/user/likedBy/**","api/user/commentedBy/**", "/api/notice/private/**", "/rooms","/api/notiUnread/**", "/api/notiread/**", "/api/user/**") 
					.hasAnyAuthority("ROLE_GENERAL", "ROLE_MANAGER", "ROLE_ADMIN")// 모든 회원 GET
					
					
				// 유튜버
				.antMatchers(HttpMethod.POST,"/api/1/board", "api/1/thumbnail/upload","/api/1/board/img/upload")
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_MANAGER","ROLE_ADMIN") // 유튜버 POST
					
				.antMatchers(HttpMethod.PUT,"/api/1/board/**")
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_MANAGER","ROLE_ADMIN") // 유튜버 PUT
					
				.antMatchers(HttpMethod.DELETE,"/api/1/board/**")
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_MANAGER","ROLE_ADMIN") // 유튜버 DELETE
					
				// 편집자
				.antMatchers(HttpMethod.POST,"/api/2/board")
					.hasAnyAuthority("ROLE_EDITOR","ROLE_MANAGER","ROLE_ADMIN") // 편집자 POST
					
				.antMatchers(HttpMethod.PUT,"/api/2/board/**")
					.hasAnyAuthority("ROLE_EDITOR","ROLE_MANAGER","ROLE_ADMIN") // 편집자 PUT
					
				.antMatchers(HttpMethod.DELETE,"/api/2/board/**")
					.hasAnyAuthority("ROLE_EDITOR","ROLE_MANAGER","ROLE_ADMIN") // 편집자 DELETE
					
				// 썸네일러
				.antMatchers(HttpMethod.POST,"/api/3/board", "api/1/thumbnail/upload")
					.hasAnyAuthority("ROLE_THUMBNAILOR","ROLE_MANAGER","ROLE_ADMIN") // 썸네일러 POST
					
				.antMatchers(HttpMethod.PUT,"/api/3/board/**")
					.hasAnyAuthority("ROLE_THUMBNAILOR","ROLE_MANAGER","ROLE_ADMIN") // 썸네일러 PUT
					
				.antMatchers(HttpMethod.DELETE,"/api/3/board/**")
					.hasAnyAuthority("ROLE_THUMBNAILOR","ROLE_MANAGER","ROLE_ADMIN") // 썸네일러 DELETE
					
				// 유튜버 편집자 썸네일러
				.antMatchers(HttpMethod.POST,"/api/board/liked/**")
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_EDITOR","ROLE_THUMBNAILOR","ROLE_MANAGER","ROLE_ADMIN") // 유튜버 편집자 썸네일러 POST
					
				.antMatchers(HttpMethod.DELETE,"/api/board/liked/**")
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_EDITOR","ROLE_THUMBNAILOR","ROLE_MANAGER","ROLE_ADMIN") // 유튜버 편집자 썸네일러 DELETE
					
//				.antMatchers(HttpMethod.POST,"/chat/send")
//					.hasAnyAuthority("ROLE_THUMBNAILOR")

				// 매니저
				.antMatchers(HttpMethod.POST,"/api/8/board", "/api/9/board","/api/8/board/img/upload","/api/9/board/img/upload", "/api/admin/promote/**") 
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN") // 매니저만 POST
					
				.antMatchers(HttpMethod.PUT,"/api/8/board", "/api/9/board", "/api/banned/**")
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN") // 매니저만  PUT
					
				.antMatchers(HttpMethod.DELETE,"/api/8/board", "/api/9/board","/api/admin/promote/**")
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN") // 매니저만 DELETE 
					
				.antMatchers(HttpMethod.GET,"/api/8/board", "/api/9/board", "/api/user","/api/admin/promote/**")
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN") // 매니저만 GET
					
				.anyRequest()
//					.permitAll();// 임시
					.authenticated();
		
		http
		.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}