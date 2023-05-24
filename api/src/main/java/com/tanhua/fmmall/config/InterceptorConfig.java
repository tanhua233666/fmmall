package com.tanhua.fmmall.config;

import com.tanhua.fmmall.intercepor.CheckTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private CheckTokenInterceptor checkTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkTokenInterceptor)
                .addPathPatterns("/shopcart/**")  //拦截资源，支持list
                .addPathPatterns("/useraddr/**")
                .addPathPatterns("/orders/**")
                .addPathPatterns("/user/check")
                .excludePathPatterns("/webSocket/**"); //放行资源
    }
}
