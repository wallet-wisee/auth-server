package com.walletwise.authservice.v1.api.controller.contract.authentication;

import com.walletwise.authservice.v1.api.dto.RecoveryPasswordRequest;
import com.walletwise.authservice.v1.core.http.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication")
public interface IResetPasswordController {
    @Operation(summary = "Recovery Password")
    ResponseEntity<Response> handle(@RequestBody RecoveryPasswordRequest request);
}
