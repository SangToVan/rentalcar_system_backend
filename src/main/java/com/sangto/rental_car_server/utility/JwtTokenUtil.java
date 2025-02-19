package com.sangto.rental_car_server.utility;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.exception.AppException;
import com.sangto.rental_car_server.repository.InvalidatedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtil {

    InvalidatedTokenRepository invalidatedTokenRepository;

    private final String BEARER_TOKEN = "Bearer ";

    @NonFinal
    @Value("${app.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${app.jwt.expire-seconds}")
    private Long JWT_EXPIRE_SECONDS;

    @Value("${app.jwt.refresh-seconds}")
    private Long JWT_REFRESH_SECONDS;

    @Value("${app.jwt.issuer}")
    private String ISSUER;

    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .claim("id", user.getId().toString())
                .subject(user.getEmail())
                .issuer(ISSUER)
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(JWT_EXPIRE_SECONDS, ChronoUnit.SECONDS)
                                .toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token: ", e);
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh)
            throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expireTime =
                (isRefresh)
                        ? new Date(
                        signedJWT
                                .getJWTClaimsSet()
                                .getIssueTime()
                                .toInstant()
                                .plus(JWT_REFRESH_SECONDS, ChronoUnit.SECONDS)
                                .toEpochMilli())
                        : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expireTime.after(new Date()))) {
            throw new AppException("UNAUTHENTICATED");
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException("UNAUTHENTICATED");
        }

        return signedJWT;
    }

    public Boolean validateToken(String token) {
        try {
            // Gọi verifyToken với isRefresh = false (token thông thường)
            verifyToken(token, false);
            return true;
        } catch (JOSEException | ParseException | AppException e) {
            log.error("Token validation failed: ", e);
            return false;
        }
    }

    public String getEmailFromToken(String token) throws ParseException {
        // Nếu token có tiền tố "Bearer ", loại bỏ tiền tố đó
        if (token.startsWith(BEARER_TOKEN)) {
            token = token.substring(BEARER_TOKEN.length());
        }
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getSubject();
    }

    public String getAccountId(String bearerToken) throws ParseException {
        // Nếu token có tiền tố "Bearer ", loại bỏ tiền tố đó
        if (bearerToken.startsWith(BEARER_TOKEN)) {
            bearerToken = bearerToken.substring(BEARER_TOKEN.length());
        }
        SignedJWT signedJWT = SignedJWT.parse(bearerToken);
        // Lấy claim "id" dưới dạng String
        return signedJWT.getJWTClaimsSet().getStringClaim("id");
    }
}
