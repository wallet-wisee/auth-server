package com.walletwise.authservice.v1.service.contract.authentication;

import com.walletwise.authservice.v1.api.dto.RecoveryPasswordRequest;

public interface IResetPasswordService {
    String recover(RecoveryPasswordRequest request);
}
