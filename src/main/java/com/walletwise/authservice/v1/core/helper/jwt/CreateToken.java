package com.walletwise.authservice.v1.core.helper.jwt;

import com.walletwise.authservice.v1.core.config.annotations.Generated;
import com.walletwise.authservice.v1.core.helper.security.SingKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Generated
@Component
public class CreateToken {
    public String create(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(SingKey.getSignKey(), SignatureAlgorithm.HS256).compact();
    }
}
