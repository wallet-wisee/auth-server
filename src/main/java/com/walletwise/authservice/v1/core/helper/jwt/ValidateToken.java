package com.walletwise.authservice.v1.core.helper.jwt;

import com.walletwise.authservice.v1.core.config.annotations.Generated;
import com.walletwise.authservice.v1.core.helper.security.SingKey;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Generated
@Component
public class ValidateToken {

    public void validate(final String token) {
        Jwts.parserBuilder().setSigningKey(SingKey.getSignKey()).build().parseClaimsJws(token);
    }
}
