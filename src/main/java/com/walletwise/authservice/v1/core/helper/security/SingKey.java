package com.walletwise.authservice.v1.core.helper.security;

import com.walletwise.authservice.v1.core.config.annotations.Generated;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

@Generated
public class SingKey {
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public static Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
