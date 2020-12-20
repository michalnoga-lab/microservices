package com.app.security.filter;

import com.app.security.tokens.AppTokensService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final AppTokensService appTokensService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, AppTokensService appTokensService) {
        super(authenticationManager);
        this.appTokensService = appTokensService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (accessToken != null) {
            UsernamePasswordAuthenticationToken auth = appTokensService.parseAccessToken(accessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}