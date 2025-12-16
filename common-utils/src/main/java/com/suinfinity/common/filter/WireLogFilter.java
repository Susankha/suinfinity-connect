package com.suinfinity.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@ConditionalOnProperty(name = "log.wire.enabled", havingValue = "true", matchIfMissing = false)
public class WireLogFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
    log.info("Wire log enabled ");
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    log.info("Request URI : {}", request.getRequestURI());
    log.info("Request Method : {}", request.getMethod());
    Enumeration<String> requestHeaders = request.getHeaderNames();
    while (requestHeaders.hasMoreElements()) {
      String headerName = requestHeaders.nextElement();
      log.info("Request Header >>: {}: {}", headerName, request.getHeader(headerName));
    }
    filterChain.doFilter(servletRequest, servletResponse);
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    log.info("Response Status :{}", response.getStatus());
    for (String headerName : response.getHeaderNames()) {
      log.info("Response Header <<: {}: {}", headerName, response.getHeaders(headerName));
    }
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
