package com.it.reservation.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.it.reservation.dto.response.AuthenticationResDTO;
import com.it.reservation.dto.response.RefreshTokenDTO;
import com.it.reservation.dto.response.UserDetailResDTO;
import com.it.reservation.entities.AuthenticationEntities;
import com.it.reservation.repository.AuthenticationRepository;
import com.it.reservation.repository.UserDetailRepository;
import com.it.reservation.service.AuthenticationService;
import com.it.reservation.service.JwtService;
import com.it.reservation.service.RefreshTokenService;
import com.it.reservation.util.Md5Util;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
	
	@Autowired
	AuthenticationRepository authenticationRepository;
	
    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    private ModelMapper mapper;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    RefreshTokenService refreshTokenService;

	@Override
	public AuthenticationResDTO login(String userName, String password) throws Exception {
		Optional<AuthenticationEntities> userOpt = authenticationRepository.findByUserName(userName);
        if (userOpt.isPresent()) {
        	AuthenticationEntities user = userOpt.get();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, passwordEncoder.encode(Md5Util.checkMd5(password))));

            var jwt = jwtService.generateToken(user);

            UserDetailResDTO userDetail = null;
            if (ObjectUtils.isNotEmpty(user)) {
                userDetail = mapper.map(userDetailRepository.findByUserId(user.getId()), new TypeToken<UserDetailResDTO>() {
                }.getType());
            }

            RefreshTokenDTO refreshTokenDTO = refreshTokenService.createRefreshToken(userName);

            return AuthenticationResDTO.builder()
                    .id(user.getId())
                    .userName(user.getUsername())
                    .status(user.getStatus())
                    .role(user.getRole())
                    .createBy(user.getCreateBy())
                    .createDate(user.getCreateDate())
                    .updateBy(user.getUpdateBy())
                    .updateDate(user.getUpdateDate())
                    .accessToken(jwt)
                    .token(refreshTokenDTO.getToken())
                    .userDetail(userDetail)
                    .build();
        }

        return null;
	}


}
