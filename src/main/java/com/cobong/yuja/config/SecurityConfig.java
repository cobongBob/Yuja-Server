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
				.addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy", "frame-ancestors self https://www.withyuja.com"))
				.and()
			.authorizeRequests()
				.antMatchers("/static/**","/imgs/**","/files/**")
					.permitAll()
				.antMatchers("/api/auth/**","/api/main/board") // 로그인 회원가입 메인보드
					.permitAll() //  all methods all authorities

				// 비로그인 용
				.antMatchers(HttpMethod.GET,"/api/1L/board/**","/api/2L/board/**","/api/3L/board/**","/api/4L/board/**", "/api/5L/board/**", "/api/7L/board/**","/api/6L/board/**","/api/9L/board/**")
					.permitAll()
				.antMatchers("/api/notiUnread/**","/api/notiread/**","/api/findnoti/**","/api/deletenoti/**")
					.permitAll()
					
				// 모든 회원	
				.antMatchers(HttpMethod.POST,"/api/2L/board", "/api/3L/board", "/api/4L/board","/api/5L/board","/api/6L/board", "/api/7L/board","/api/2L/board/img/upload","/api/3L/board/img/upload","/api/4L/board/img/upload","/api/5L/board/img/upload","/api/6L/board/img/upload","/api/7L/board/img/upload", 
						"api/3/thumbnail/upload", "/socket/room/**" , "/api/comment","/api/8/board") 
					.hasAnyAuthority("ROLE_GENERAL", "ROLE_MANAGER","ROLE_ADMIN") // 모든 회원 POST
					
				.antMatchers(HttpMethod.PUT,"/api/4L/board/**", "/api/5L/board/**","/api/6L/board", "/api/7L/board", "/api/user/**", "/api/comment/**") 
					.hasAnyAuthority("ROLE_GENERAL", "ROLE_MANAGER","ROLE_ADMIN")// 모든 회원 PUT
					
				.antMatchers(HttpMethod.DELETE, "/api/4L/board/**", "/api/5L/board/**", "/socket/room/**", "/api/comment/**", "/api/user/**") 
					.hasAnyAuthority("ROLE_GENERAL", "ROLE_MANAGER", "ROLE_ADMIN")// 모든 회원 DELETE
					
				.antMatchers(HttpMethod.GET, "/api/user/board/**","/api/user/likedBy/**","api/user/commentedBy/**", "/api/notice/private/**", "/rooms", "/api/user/**") 
					.hasAnyAuthority("ROLE_GENERAL", "ROLE_MANAGER", "ROLE_ADMIN")// 모든 회원 GET
					
					
				// 유튜버
				.antMatchers(HttpMethod.POST,"/api/1L/board", "api/1L/thumbnail/upload","/api/1L/board/img/upload")
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_MANAGER","ROLE_ADMIN") // 유튜버 POST
					
				.antMatchers(HttpMethod.PUT,"/api/1L/board/**")
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_MANAGER","ROLE_ADMIN") // 유튜버 PUT
					
				.antMatchers(HttpMethod.DELETE,"/api/1L/board/**")
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_MANAGER","ROLE_ADMIN") // 유튜버 DELETE
					
				// 편집자
					
				.antMatchers(HttpMethod.PUT,"/api/2L/board/**")
					.hasAnyAuthority("ROLE_EDITOR","ROLE_MANAGER","ROLE_ADMIN") // 편집자 PUT
					
				.antMatchers(HttpMethod.DELETE,"/api/2L/board/**")
					.hasAnyAuthority("ROLE_EDITOR","ROLE_MANAGER","ROLE_ADMIN") // 편집자 DELETE
					
				// 썸네일러
					
				.antMatchers(HttpMethod.PUT,"/api/3L/board/**")
					.hasAnyAuthority("ROLE_THUMBNAILER","ROLE_MANAGER","ROLE_ADMIN") // 썸네일러 PUT
					
				.antMatchers(HttpMethod.DELETE,"/api/3L/board/**")
					.hasAnyAuthority("ROLE_THUMBNAILER","ROLE_MANAGER","ROLE_ADMIN") // 썸네일러 DELETE
					
				// 유튜버 편집자 썸네일러 찜하기
				/* 프론트에서 유튜브, 편집자, 썸네일러 게시판 글들에 한해 권한이 없을시 블락 중. 여기서 잡으면 자유게시판에도 좋아요를 할수 없어지니 없에는게 좋을 듯.
				.antMatchers(HttpMethod.POST,"/api/board/liked/**")
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_EDITOR","ROLE_THUMBNAILER","ROLE_MANAGER","ROLE_ADMIN") // 유튜버 편집자 썸네일러 POST
					
				.antMatchers(HttpMethod.DELETE,"/api/board/liked/**")
					.hasAnyAuthority("ROLE_YOUTUBER","ROLE_EDITOR","ROLE_THUMBNAILER","ROLE_MANAGER","ROLE_ADMIN") // 유튜버 편집자 썸네일러 DELETE
				 * */
				
				// 매니저
				.antMatchers(HttpMethod.POST, "/api/9L/board","/api/8L/board/img/upload","/api/9L/board/img/upload", "/api/admin/promote/**") 
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN") // 매니저만 POST
					
				.antMatchers(HttpMethod.PUT, "/api/9L/board", "/api/banned/**")
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN") // 매니저만  PUT
					
				.antMatchers(HttpMethod.DELETE,"/api/8L/board", "/api/9L/board","/api/admin/promote/**")
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN") // 매니저만 DELETE 
					
				.antMatchers(HttpMethod.GET,"/api/8L/board", "/api/user","/api/admin/promote/**")
					.hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN") // 매니저만 GET
				
				.anyRequest()
					.permitAll();
//					.authenticated()
				
				
		
		http
		.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}
