package com.dt.find_restaurant.global.filter;

import static com.dt.find_restaurant.global.util.CommUtils.LOGIN_URL;

import com.dt.find_restaurant.global.config.security.WhiteListProperties;
import com.dt.find_restaurant.global.util.JsonUtils;
import com.dt.find_restaurant.security.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final List<PathPattern> whitePatterns;

    public JwtFilter(JwtService jwtService, WhiteListProperties whiteListProperties) {
        this.jwtService = jwtService;
        PathPatternParser parser = new PathPatternParser();
        this.whitePatterns = whiteListProperties.getPaths().stream()
                .map(parser::parse) // "/oauth", "/public/**", "/swagger-ui/**" 등
                .toList();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);
        String uri = request.getRequestURI();
        LocalDateTime requestAt = LocalDateTime.now();

        try {

            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                log.info("\n ===CORS preflight request, passing through without authentication.===\n");
                filterChain.doFilter(request, response);
                return;
            }

            if (uri.equals(LOGIN_URL)) {
                log.info("\n ===Login URL 접근, 인증 필터 패스.===\n");
                filterChain.doFilter(req, res);
                return;
            }

            if (isWhiteList(request)) {
                log.info("\n ===화이트리스트 URL 접근, 인증 필터 패스.===\n");
                filterChain.doFilter(request, response);
                return;
            }

            log.info("\n===접근한 URL : {}===\n", uri);

            String validAccessToken = jwtService.validateAccessToken(req);
            if (validAccessToken == null) {
                log.warn("유효하지 않은 Access Token입니다. 리프레시 토큰으로 재발급 시도합니다.");
                validAccessToken = jwtService.reissueAccessByRefresh(req).getRefreshToken();
                if (validAccessToken == null) {
                    log.warn("리프레시 토큰으로도 유효한 Access Token을 발급받지 못했습니다.");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                    return;
                }
            }

            log.info("JWT valid success : {}", validAccessToken);
            Authentication authentication = jwtService.getAuthentication(validAccessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(req, res);

        } finally {
            logAccess(req, res, requestAt);
            res.copyBodyToResponse();
        }
    }


    private void logAccess(ContentCachingRequestWrapper req, ContentCachingResponseWrapper res,
                           LocalDateTime requestAt) {
        LocalDateTime responseAt = LocalDateTime.now();

        String requestBody = new String(req.getContentAsByteArray(), StandardCharsets.UTF_8);
        String responseBody = new String(res.getContentAsByteArray(), StandardCharsets.UTF_8);

        String headers = Collections.list(req.getHeaderNames()).stream()
                .collect(Collectors.toMap(h -> h, req::getHeader))
                .toString();

        AccessLogRequest accessLog = AccessLogRequest.of(
                req.getMethod(),
                req.getRequestURI(),
                req.getQueryString(),
                requestBody,
                responseBody,
                headers,
                req.getHeader("User-Agent"),
                AccessLogRequest.extractClientIp(req),
                res.getStatus(),
                Thread.currentThread().getName(),
                requestAt,
                responseAt
        );

        log.info("\nAccessLog : {}\n", JsonUtils.toJson(accessLog));
    }

    private boolean isWhiteList(HttpServletRequest request) {
        // CORS preflight는 패스
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 컨텍스트패스 영향 없는 servletPath 사용
        String path = request.getServletPath();

        for (PathPattern p : whitePatterns) {
            if (p.matches(PathContainer.parsePath(path))) {
                if (log.isDebugEnabled()) {
                    log.debug("화이트리스트 URL: {}", path);
                }
                return true;
            }
        }
        return false;
    }
}
