package com.memberservice.common.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// api 요청/응답 로그 남기기 위한 인터셉터
@Slf4j
@Component
public class ApiLoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    // 컨트롤러 실행 전에 호출
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);

        log.info("[API - LOG] 요청 시작 method={}, url={}", request.getMethod(), request.getRequestURI());

        return true;
    }

    // 요청 처리가 끝난 뒤에 호출
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Object value = request.getAttribute(START_TIME);
        long startTime = value instanceof Long ? (Long) value : System.currentTimeMillis();
        long durationMs = System.currentTimeMillis() - startTime;

        if (ex == null) {
            log.info("[API - LOG] 요청 완료 method={}, uri={}, status={}, durationMs={}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), durationMs);
        } else {
            log.error("[API - LOG] 요청 실패 method={}, uri={}, status={}, durationMs={}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), durationMs, ex);
        }
    }
}
