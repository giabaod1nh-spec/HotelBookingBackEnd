package org.baoxdev.hotelbooking_test.configuration;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityArchitectureLearn {
    //Tao 1 securityContext mới
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    //Tạo 1 Authentication object để truyền vào securityContext
    Authentication authentication = new TestingAuthenticationToken("username" ,"password" , "ROLE_USER");



}
