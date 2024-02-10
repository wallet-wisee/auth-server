package com.walletwise.authservice.v1.api.controller.contract.jwt;

import com.walletwise.authservice.v1.core.http.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Authentication")
public interface IValidateTokenController {
    @Operation(summary = "Validate token")
    ResponseEntity<Response> handle(@RequestParam("token") String token);
}
