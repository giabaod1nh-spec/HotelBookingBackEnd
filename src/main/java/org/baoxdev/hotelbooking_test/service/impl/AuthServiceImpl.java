package org.baoxdev.hotelbooking_test.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.dto.request.AuthRequest;
import org.baoxdev.hotelbooking_test.dto.request.IntroSpectRequest;
import org.baoxdev.hotelbooking_test.dto.request.LogOutRequest;
import org.baoxdev.hotelbooking_test.dto.request.RefreshTokenRequest;
import org.baoxdev.hotelbooking_test.dto.response.AuthResponse;
import org.baoxdev.hotelbooking_test.dto.response.IntroSpectResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.model.entity.RefreshToken;
import org.baoxdev.hotelbooking_test.model.entity.User;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.model.enums.TokenType;
import org.baoxdev.hotelbooking_test.repository.RefreshTokenRepository;
import org.baoxdev.hotelbooking_test.repository.UserRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
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
    RefreshTokenRepository refreshTokenRepository;
    HttpServletRequest httpServletRequest;
    RedisTokenService redisTokenService;
    AuthenticationManager authenticationManager;


    @Override
    public AuthResponse checkAuthenticationUser(AuthRequest request) {
    //    var password = userRepository.findUserByUserName(request.getUsername()).orElseThrow().getPassword();
        var user = userRepository.findUserByUserName(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    //    boolean authenticate = encoder.matches(request.getPassword(),password);

    //    if(!authenticate) throw new AppException(ErrorCode.AUTHENTICATED_FAILED);

    //THAY VÌ CUSTOM CHECK TA SẼ CHECK BẰNG CHUẨN CỦA SPRING SECURITY
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getUsername() , request.getPassword()));

    //UsernamePaswordAuthenticationToken đc pass vào trong AuthenticationManager
    //Authentication Manager configure use DaoAuthenticationProvider
    //DaoAuthenticationProvider xét UserDetails trong UserDetailService
    //Use PasswordEncoder để validate với password trong UserDetails

    //Lưu object Authentication trả về trong SecurityContextHolder

        SecurityContextHolder.getContext().setAuthentication(authentication);

    //Generate token
        String accessToken = generateToken(user , TokenType.ACCESS , new Date(Instant.now().plus(1 , ChronoUnit.HOURS).toEpochMilli()));

        Date sessionExpire = new Date(Instant.now().plus(7 , ChronoUnit.DAYS).toEpochMilli());

        String refreshToken = generateToken(user , TokenType.REFRESH ,new Date(Instant.now().plus(7 , ChronoUnit.DAYS  ).toEpochMilli()));

        RefreshToken refreshToken0 = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .sessionExpiryTime(sessionExpire)
                .build();

        refreshTokenRepository.save(refreshToken0);



        return AuthResponse.builder()
                .authenticated(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public IntroSpectResponse introspectToken(IntroSpectRequest request) {
        var token = request.getToken();
        boolean isValid = true;

        try{
            verifyToken(token);
        }catch (JOSEException |ParseException e){
            isValid = false;
            throw new AppException(ErrorCode.AUTHENTICATED_FAILED);
        }

        return IntroSpectResponse.builder()
                .isValid(isValid)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshTokenAfterTimeOut(RefreshTokenRequest request) throws ParseException, JOSEException {

        SignedJWT signedJWT = verifyToken(request.getRefreshToken());

        String store = signedJWT.getJWTClaimsSet().getStringClaim("tokenType");

        //Check xem dung loai token ko
        if(!"REFRESH".equals(store)) throw new AppException(ErrorCode.TOKEN_TYPE_INVALID);

        //Check Refresh token co ton tai trong DB ko
        RefreshToken oldToken = refreshTokenRepository.findByToken(request.getRefreshToken());
        //Phai nem ra exception cho nay neu ko tim thay refreshToken ??

        User user = userRepository.findUserByUserName(signedJWT.getJWTClaimsSet().getSubject())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

       String newAccessToken = generateToken(user , TokenType.ACCESS , new Date(Instant.now().plus(1 , ChronoUnit.HOURS).toEpochMilli()));

       String newRefreshToken = generateToken(user , TokenType.REFRESH, signedJWT.getJWTClaimsSet().getExpirationTime());

       RefreshToken newToken = RefreshToken
               .builder()
               .token(newRefreshToken)
               .sessionExpiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
               .user(user)
               .build();
       //Xoa refreshToken cu
       refreshTokenRepository.deleteRefreshTokenByToken(request.getRefreshToken());

       //Luu refreshToken moi vao
        refreshTokenRepository.save(newToken);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .authenticated(true)
                .build();
    }

    @Override
    @Transactional
    public void logOut(LogOutRequest request) throws ParseException, JOSEException {
        //Xoa refresh token
        refreshTokenRepository.deleteRefreshTokenByToken(request.getToken());

        //Blacklist Access Token
        String header = httpServletRequest.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")){
            String accessToken = header.substring(7);
            SignedJWT signedJWT = verifyToken(accessToken);
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();

            //String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

            long ttl = expiration.getTime() - System.currentTimeMillis();

            if (ttl > 0) {
                redisTokenService.blackListToken(accessToken, ttl);
                log.info("Success save token in Redis");
            }else{
                log.info("Access token timeout no need to save in blacklist");
            }
        }else{
            log.warn("No Bearer token found in Authorization header, skipping blacklists");
        }
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role ->{
                    stringJoiner.add(role.getRoleName());
                    if (!CollectionUtils.isEmpty(role.getPermissions()))
                        role.getPermissions().forEach(permission ->
                                stringJoiner.add(permission.getPermissionName()));
            });
        return stringJoiner.toString();
    }

    private String generateToken(User user , TokenType type , Date expiryTime){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("baoxdev.com")
                .subject(user.getUserName())
                .issueTime(new Date())
                //.expirationTime(new Date(Instant.now().plus(1 , ChronoUnit.HOURS).toEpochMilli()))
                .expirationTime(expiryTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("scope" , buildScope(user))
                .claim("tokenType" , type)
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



    protected SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        //Xac thuc token
        var verified = signedJWT.verify(jwsVerifier);

        //Check xem token da qua thoi gian expireTime chua
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        //Lay jwtId , key de check trong Redis
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

        if(!verified){
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
        if(expiryTime.before(new Date())){
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
        //Check xem token da bi logout chua(blacklist token)

        if(redisTokenService.isTokenBlackListed(jwtId)){
            throw new AppException(ErrorCode.TOKEN_REVOKED);
        }
        //Su dung jwtId , phan nay tich hop Redis de store blacklistToken
        return signedJWT;
    }

}
