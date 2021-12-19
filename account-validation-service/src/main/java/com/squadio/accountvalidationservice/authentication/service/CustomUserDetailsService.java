package com.squadio.accountvalidationservice.authentication.service;


import com.squadio.accountvalidationmodule.authentication.entity.AccountUser;
import com.squadio.accountvalidationmodule.authentication.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepositoryImpl repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountUser user = repository.findByUserName(username);
        //System.out.println("user: "+ user.getUserName());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
