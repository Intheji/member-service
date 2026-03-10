package com.memberservice.common.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class ApiLoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);

        log.info("[API - LOG] 요청 시작 method={}, url={}", request.getMethod(), request.getRequestURI());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Object value = request.getAttribute(START_TIME);
        long startTime = value instanceof Long ? (Long) value : System.currentTimeMillis();
        long durationMs = System.currentTimeMillis() - startTime;

        if (ex == null) {
            log.info("[API - LOG] 요청 완료 method={}, uri={}, stataus={}, durationMs={}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), durationMs);
        } else {
            log.error("[API - LOG] 요청 실패 method={}, uri={}, status={}, durationMs={}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), durationMs, ex);
        }
    }
}
