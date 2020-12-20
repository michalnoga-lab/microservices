package com.app.security.filter;

import com.app.exception.AppUsersServiceException;
import com.app.security.dto.AuthenticationDto;
import com.app.security.tokens.AppTokensService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AppTokensService appTokensService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AppTokensService appTokensService) {
        this.authenticationManager = authenticationManager;
        this.appTokensService = appTokensService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenticationDto authenticationDto = new ObjectMapper().readValue(request.getInputStream(), AuthenticationDto.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationDto.getUsername(),
                    authenticationDto.getPassword(),
                    Collections.emptyList()
            ));
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppUsersServiceException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain, Authentication authResult) throws IOException {
        var tokens = appTokensService.generateTokens(authResult);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokens));
        response.getWriter().flush();
        response.getWriter().close();
    }
}