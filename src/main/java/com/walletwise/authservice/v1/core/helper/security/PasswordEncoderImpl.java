package com.walletwise.authservice.v1.core.helper.security;

import com.walletwise.authservice.v1.core.config.annotations.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Generated
@Component
@RequiredArgsConstructor
public class PasswordEncoderImpl {
    private final PasswordEncoder passwordEncoder;
    public String encode(String password) {
        return this.passwordEncoder.encode(password);
    }
}
