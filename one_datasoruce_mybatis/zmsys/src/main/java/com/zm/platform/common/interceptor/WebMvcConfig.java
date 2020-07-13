package com.zm.platform.common.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zm.platform.common.interceptor.LoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${upload.resourceHandler}")
    private String resourceHandler;//请求 url 中的资源映射，如 /uploadFiles/**
    @Value("${upload.location}")
    private String location;//上传文件保存的本地目录，如 E:/uploadFiles/

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	//addPathPatterns("/**")需要拦截的路径,不需要加上下文
        //excludePathPatterns("/**")无需拦截的路径,不需要加上下文
    	registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(resourceHandler);
    }
    //静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //就是说 url 中出现 resourceHandler 匹配时，则映射到 location 中去,location 相当于虚拟路径
        //映射本地文件时，开头必须是 file:/// 开头，表示协议
        registry.addResourceHandler(resourceHandler).addResourceLocations("file:///" + location);
    }
}