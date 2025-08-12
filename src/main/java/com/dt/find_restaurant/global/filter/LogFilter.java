package com.dt.find_restaurant.global.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("로그 필터 초기화 완료");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        log.info("log filter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();
        String requestMethod = httpRequest.getMethod();

        String uuid = UUID.randomUUID().toString();

        try {
            log.info("\n [REQUEST] \n \n  UUID : {} \n  URL : {} \n  Method : {}", uuid, requestURI, requestMethod);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("[RESPONSE] \n \n   RelatedUUID : {} \n  RequestURI : {}", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
