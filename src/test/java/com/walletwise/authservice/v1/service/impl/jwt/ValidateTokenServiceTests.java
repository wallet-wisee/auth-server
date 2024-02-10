package com.walletwise.authservice.v1.service.impl.jwt;

import com.walletwise.authservice.v1.core.helper.jwt.ValidateToken;
import com.walletwise.authservice.v1.service.contract.jwt.IValidateTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class ValidateTokenServiceTests {
    @MockBean
    private ValidateToken validateToken;
    private IValidateTokenService service;

    @BeforeEach
    public void setUp() {
        service = new ValidateTokenService(validateToken);
    }

    @Test
    @DisplayName("Should validate the token")
    public void shouldValidateToken() {
        String token = UUID.randomUUID().toString();
        this.service.validate(token);
        Mockito.verify(validateToken, Mockito.atLeast(1)).validate(token);
    }

}
