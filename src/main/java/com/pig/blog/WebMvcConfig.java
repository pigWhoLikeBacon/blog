package com.pig.blog;

import javax.annotation.Resource;

import com.pig.blog.annotation.Authentication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //directory for upload file.
    public final static String FILE_DIR = "/root/violet/";
    
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/logs/**").addResourceLocations(
                "file:///" + WebMvcConfig.FILE_DIR + "logs/");
        registry.addResourceHandler("/upload/**").addResourceLocations(
                "file:///" + WebMvcConfig.FILE_DIR + "upload/");
    }
    
    @Resource
	private Authentication authentication;
    
	public void addInterceptors(InterceptorRegistry registry) {
		// 自定义拦截器，添加拦截路径和排除拦截路径
		registry.addInterceptor(authentication)
			
			.addPathPatterns("/logs/**")
			.addPathPatterns("/back/**")
			.excludePathPatterns("/back/loginPage")
			.excludePathPatterns("/back/login")
			
			.addPathPatterns("/article/**")
			.excludePathPatterns("/article/mainPage")
			.excludePathPatterns("/article/searchPage")
			.excludePathPatterns("/article/groupByTagPage")
			.excludePathPatterns("/article/groupByTimePage")
			
			.addPathPatterns("/comment/**")
			.excludePathPatterns("/comment/save")
			.excludePathPatterns("/comment/page")
			
			.addPathPatterns("/tag")
			
			.addPathPatterns("/image/**");
	}
}
