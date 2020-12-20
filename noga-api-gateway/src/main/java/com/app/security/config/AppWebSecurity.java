package com.app.security.config;

import com.app.security.tokens.AppTokensService;
import com.app.security.filter.JWTAuthenticationFilter;
import com.app.security.filter.JWTAuthorizationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
public class AppWebSecurity extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final AppTokensService appTokensService;

    public AppWebSecurity(
            @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
            AppTokensService appTokensService) {
        this.userDetailsService = userDetailsService;
        this.appTokensService = appTokensService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(authException.getMessage()));
            response.getWriter().flush();
            response.getWriter().close();
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, authException) -> {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(authException.getMessage()));
            response.getWriter().flush();
            response.getWriter().close();
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()

                .and()
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/users/**").permitAll()
                .antMatchers("/api/users/security/**").permitAll()
                .antMatchers("/api/users/service/people/**").permitAll()
                .antMatchers("/api/data/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/service/admin/**").hasRole("ADMIN")
                .antMatchers("/api/service/user/**").hasRole("USER")
                .anyRequest().authenticated()

                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), appTokensService))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), appTokensService));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}