package com.it.reservation.service;

import com.it.reservation.dto.response.AuthenticationResDTO;

public interface AuthenticationService {

	public AuthenticationResDTO login(String userName, String password) throws Exception;
}
