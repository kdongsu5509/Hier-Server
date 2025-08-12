package com.dt.find_restaurant.global.filter;

import static com.dt.find_restaurant.global.util.CommUtils.LOGIN_URL;

import com.dt.find_restaurant.global.util.JsonUtils;
import com.dt.find_restaurant.security.jwt.component.JwtProperties;
import com.dt.find_restaurant.security.jwt.domain.JwtResult;
import com.dt.find_restaurant.security.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    {
        setFilterProcessesUrl(LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                obtainUsername(request), obtainPassword(request), null
        );

        log.info("Attempting to authenticate: {},{}", obtainUsername(request), obtainPassword(request));

        return authenticationManager.authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UserDetails user = (UserDetails) authResult.getPrincipal();
        JwtResult.Issue resDto = jwtService.issueJwtAuth(user.getUsername(),
                user.getAuthorities().iterator().next().getAuthority());

        log.info("login success : {}", JsonUtils.toJson(resDto));
        response.setHeader(jwtProperties.getAccessHeaderName(), "Bearer " + resDto.getAccessToken());
        response.addCookie(resDto.getCookie());
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = response.getWriter();
        String errorMsg = "Authentication failed: " + failed.getMessage();
        out.print("{\"error\": \"" + errorMsg + "\"}");

        out.flush();
    }
}
