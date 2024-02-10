package com.walletwise.authservice.v1.service.contract.authentication;

import com.walletwise.authservice.v1.api.dto.CreateNewPasswordRequest;

public interface ICreateNewPasswordService {
    String create(CreateNewPasswordRequest request);
}
