package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User userByEmail = userService.findUserByEmail(s);
        if (userByEmail != null) {
            return userByEmail;
        } else {
            throw new UsernameNotFoundException("User with email: " + s + " has not found!");
        }
    }
}
