package com.walletwise.authservice.v1.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewPasswordRequest {
    private String email;
    private Integer validationCode;
    private String password;
    private String confirmationPassword;
}
