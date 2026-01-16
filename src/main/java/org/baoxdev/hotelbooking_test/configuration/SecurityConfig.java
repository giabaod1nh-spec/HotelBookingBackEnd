package org.baoxdev.hotelbooking_test.configuration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final String[]  PUBLIC_ENDPOINT = { "/user/create" , "/user/getAll", "/auth/login" , "/auth/refresh"
     , "/auth/introspect"};
    private final CustomJwtDecoder jwtDecoder;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config){
        return config.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity){

        //Cho phép nhung endpoint đi qua SecurityFilterChain
        httpSecurity.authorizeHttpRequests(request
                -> request.requestMatchers( HttpMethod.POST ,PUBLIC_ENDPOINT)
                .permitAll()
                .requestMatchers(HttpMethod.GET , PUBLIC_ENDPOINT)
                .permitAll()
                .anyRequest().authenticated()
        );

        //Tắt config của csrf
        httpSecurity.csrf(httpSecurityCsrfConfigurer
                -> httpSecurityCsrfConfigurer.disable());
        //server ko can luu session cho user
        httpSecurity.sessionManagement(session
                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        //Xác thực bearer token trong header khi user request
        httpSecurity.oauth2ResourceServer(oauth2
                -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder)
                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );
        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // 1. Chặn refresh token
            String tokenType = jwt.getClaimAsString("tokenType");
            if (!"ACCESS".equals(tokenType)) {
                throw new AppException(ErrorCode.TOKEN_TYPE_INVALID);
            }

            List<GrantedAuthority> authorities = new ArrayList<>();

            // 2. Lấy scope
            String scope = jwt.getClaimAsString("scope");
            if (scope == null) return authorities;

            for (String s : scope.split(" ")) {
                // ROLE_ cho role
                authorities.add(new SimpleGrantedAuthority("ROLE_" + s));
                // permission giữ nguyên
                authorities.add(new SimpleGrantedAuthority(s));
            }
            return authorities;
        });

        return converter;
    }





}
