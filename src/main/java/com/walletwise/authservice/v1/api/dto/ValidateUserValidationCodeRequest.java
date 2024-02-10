package com.walletwise.authservice.v1.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateUserValidationCodeRequest {
    private String email;
    private Integer validationCode;
}
