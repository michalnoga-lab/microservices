package com.app.security.tokens;

import com.app.exception.AppUsersServiceException;
import com.app.proxy.FindUserProxy;
import com.app.security.dto.RefreshTokenDto;
import com.app.security.dto.TokensDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppTokensService {

    @Value("${tokens.access-token.expiration-time-ms}")
    private Long accessTokenExpirationTimeMs;

    @Value("${tokens.refresh-token.expiration-time-ms}")
    private Long refreshTokenExpirationTimeMs;

    @Value("${tokens.refresh-token.property}")
    private String refreshTokenProperty;

    @Value("${tokens.prefix}")
    private String tokenPrefix;

    private final FindUserProxy findUserProxy;
    private final SecretKey secretKey;

    public TokensDto generateTokens(Authentication authentication) {
        if (authentication == null) {
            throw new AppUsersServiceException("authentication object is null");
        }

        var user = findUserProxy
                .findByName(authentication.getName());
        var userId = String.valueOf(user.getId());

        var currentDate = new Date();

        var accessTokenExpirationDateMs = System.currentTimeMillis() + accessTokenExpirationTimeMs;
        var accessTokenExpirationDate = new Date(accessTokenExpirationDateMs);

        var refreshTokenExpirationDateMs = System.currentTimeMillis() + refreshTokenExpirationTimeMs;
        var refreshTokenExpirationDate = new Date(refreshTokenExpirationDateMs);

        var accessToken = Jwts
                .builder()
                .setSubject(userId)
                .setIssuedAt(currentDate)
                .setExpiration(accessTokenExpirationDate)
                .signWith(secretKey)
                .compact();

        var refreshToken = Jwts
                .builder()
                .setSubject(userId)
                .setIssuedAt(currentDate)
                .setExpiration(refreshTokenExpirationDate)
                .claim(refreshTokenProperty, accessTokenExpirationDateMs)
                .signWith(secretKey)
                .compact();

        return TokensDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UsernamePasswordAuthenticationToken parseAccessToken(String token) {
        if (token == null) {
            throw new AppUsersServiceException("token is null");
        }

        if (!token.startsWith(tokenPrefix)) {
            throw new AppUsersServiceException("token is not correct");
        }

        var accessToken = token.replace(tokenPrefix, "");
        if (!isTokenNonExpired(accessToken)) {
            throw new AppUsersServiceException("token has been expired");
        }

        var userId = id(accessToken);
        var user = findUserProxy
                .findById(userId);
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority(user.getRole().toString())));
    }


    public TokensDto refreshTokens(RefreshTokenDto refreshTokenDto) {
        if (refreshTokenDto == null) {
            throw new AppUsersServiceException("refresh token dto is null");
        }

        var refreshToken = refreshTokenDto.getToken();
        if (!isTokenNonExpired(refreshToken)) {
            throw new AppUsersServiceException("refresh token has been expired");
        }

        if (claims(refreshToken).get(refreshTokenProperty, Long.class) < System.currentTimeMillis()) {
            throw new AppUsersServiceException("cannot refresh token");
        }

        var userId = id(refreshToken);

        var currentDate = new Date();

        var accessTokenExpirationDateMs = System.currentTimeMillis() + accessTokenExpirationTimeMs;
        var accessTokenExpirationDate = new Date(accessTokenExpirationDateMs);

        var refreshTokenExpirationDate = expiration(refreshToken);

        var accessToken = Jwts
                .builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(currentDate)
                .setExpiration(accessTokenExpirationDate)
                .signWith(secretKey)
                .compact();

        var newRefreshToken = Jwts
                .builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(currentDate)
                .setExpiration(refreshTokenExpirationDate)
                .claim(refreshTokenProperty, accessTokenExpirationDateMs)
                .signWith(secretKey)
                .compact();

        return TokensDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();

    }

    private Claims claims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Long id(String token) {
        return Long.valueOf(claims(token).getSubject());
    }

    private Date expiration(String token) {
        return claims(token).getExpiration();
    }

    private boolean isTokenNonExpired(String token) {
        return expiration(token).after(new Date());
    }
}