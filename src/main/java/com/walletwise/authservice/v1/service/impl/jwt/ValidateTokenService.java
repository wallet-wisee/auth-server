package com.walletwise.authservice.v1.service.impl.jwt;

import com.walletwise.authservice.v1.core.helper.jwt.ValidateToken;
import com.walletwise.authservice.v1.service.contract.jwt.IValidateTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateTokenService implements IValidateTokenService {
    private final ValidateToken validateToken;

    @Override
    public void validate(String token) {
        this.validateToken.validate(token);
    }
}
