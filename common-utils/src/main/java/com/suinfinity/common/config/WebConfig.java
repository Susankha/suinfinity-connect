package com.suinfinity.common.config;

import com.suinfinity.common.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(
    name = "log.interceptor.enabled",
    havingValue = "true",
    matchIfMissing = false)
public class WebConfig implements WebMvcConfigurer {

  @Autowired private LogInterceptor logInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(logInterceptor);
  }
}
