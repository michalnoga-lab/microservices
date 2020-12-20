package com.app.security.service;

import com.app.proxy.FindUserProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final FindUserProxy findUserProxy;

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = findUserProxy.findByName(username);
        return new User(
                        user.getUsername(),
                        user.getPassword(),
                        true, true, true, true,
                        List.of(new SimpleGrantedAuthority(user.getRole().toString()))
        );
    }
}