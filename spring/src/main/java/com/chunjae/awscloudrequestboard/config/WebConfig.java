package com.chunjae.awscloudrequestboard.config;

import com.chunjae.awscloudrequestboard.interceptor.IpCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final IpCheckInterceptor ipCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 모든 요청에 대해 인터셉터 적용, 하지만 / (메인 페이지)는 제외
        registry.addInterceptor(ipCheckInterceptor)
                .addPathPatterns("/**")  // 모든 경로에 대해 인터셉터 적용
                .excludePathPatterns("/", "/index"
                , "/css/**", "/font/**", "/img/**", "/js/**");  // 메인 페이지는 제외
    }
}
