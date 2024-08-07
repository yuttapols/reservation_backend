package com.it.reservation.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

	UserDetailsService userDetailsService() throws Exception;
}
