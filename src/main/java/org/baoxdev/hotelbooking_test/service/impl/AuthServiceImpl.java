package org.baoxdev.hotelbooking_test.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.dto.request.AuthRequest;
import org.baoxdev.hotelbooking_test.dto.response.AuthResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.model.entity.User;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.repository.UserRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IAuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements IAuthService {
    protected static String secretKey = "bc4ab14dbbb049a77290ca0196a37d597d399a4fd5d8ccf2b831191d1995e84e";
    UserRepository userRepository;
    PasswordEncoder encoder = new BCryptPasswordEncoder(10);
    @Override
    public AuthResponse checkAuthenticationUser(AuthRequest request) {
        var password = userRepository.findUserByUserName(request.getUsername()).orElseThrow().getPassword();
        var user = userRepository.findUserByUserName(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean authenticate = encoder.matches(request.getPassword(),password);
        if(!authenticate) throw new AppException(ErrorCode.AUTHENTICATED_FAILED);
        return AuthResponse.builder()
                .authenticated(authenticate)
                .token(generateToken(user))
                .build();
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role ->{
                    stringJoiner.add(role.getRoleName());
                    if (CollectionUtils.isEmpty(role.getPermissions()))
                        role.getPermissions().forEach(permission ->
                                stringJoiner.add(permission.getPermissionName()));
            });
        return stringJoiner.toString();
    }

    private String generateToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("baoxdev.com")
                .subject(user.getUserName())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1 , ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope" , buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader , payload);

        try {
            JWSSigner jwsSigner = new MACSigner(secretKey.getBytes());
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        }catch (JOSEException e){
              log.error("Cannot create token" , e);
              throw new AppException(ErrorCode.CREATETOKEN_FAILED);
        }
    }



}
