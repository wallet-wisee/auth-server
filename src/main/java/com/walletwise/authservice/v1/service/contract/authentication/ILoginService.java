package com.walletwise.authservice.v1.service.contract.authentication;

import com.walletwise.authservice.v1.api.dto.LoginRequest;

public interface ILoginService {
    String generate(LoginRequest request);
}
