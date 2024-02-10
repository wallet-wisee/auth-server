package com.walletwise.authservice.v1.service.contract.authentication;

import com.walletwise.authservice.v1.api.dto.RegisterUserRequest;

public interface IRegisterUserService {
    String register(RegisterUserRequest request);
}
