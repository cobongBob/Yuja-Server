package com.cobong.yuja.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// files경로로 들어온 애들을 정해놓은 폴더의 파일로 내보내주게 한다.
		//addResourceLocations에 url 경로를 설정하고, addResourceLocations에 매핑될 리소스 위치를 설정

		registry.addResourceHandler("/files/**").addResourceLocations("file:files/")
			.setCachePeriod(100)
			.resourceChain(true)
			.addResolver(new PathResourceResolver());
	}
}
