package com.walletwise.authservice.v1.service.contract.authentication;

import com.walletwise.authservice.v1.api.dto.ValidateUserValidationCodeRequest;

public interface IValidateUserValidationCodeService {
    String validate(ValidateUserValidationCodeRequest request);
}
