package com.walletwise.authservice.v1.core.helper.jwt;

import com.walletwise.authservice.v1.core.config.annotations.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Generated
@Component
@RequiredArgsConstructor
public class GenerateToken {
    private final CreateToken createToken;

    public String generate(String email) {
        Map<String, Object> claims = new HashMap<>();
        return this.createToken.create(claims, email);
    }
}
