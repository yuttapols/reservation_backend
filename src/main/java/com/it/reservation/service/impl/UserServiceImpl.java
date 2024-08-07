package com.it.reservation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.it.reservation.repository.AuthenticationRepository;
import com.it.reservation.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	AuthenticationRepository authenticationRepository;
	
	@Override
	public UserDetailsService userDetailsService() throws Exception {
		return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {

                return authenticationRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
	}

}
