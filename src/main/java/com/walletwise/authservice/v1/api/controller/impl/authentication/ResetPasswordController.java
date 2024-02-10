package com.walletwise.authservice.v1.api.controller.impl.authentication;

import com.walletwise.authservice.v1.api.controller.Controller;
import com.walletwise.authservice.v1.api.dto.RecoveryPasswordRequest;
import com.walletwise.authservice.v1.api.validation.ValidationBuilder;
import com.walletwise.authservice.v1.api.validation.contract.IValidator;
import com.walletwise.authservice.v1.core.http.Response;
import com.walletwise.authservice.v1.service.contract.authentication.IResetPasswordService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recoveryPassword")
public class ResetPasswordController extends Controller<RecoveryPasswordRequest> implements com.walletwise.authservice.v1.api.controller.contract.authentication.IResetPasswordController {
    private final IResetPasswordService service;

    @Override
    public ResponseEntity<Response> perform(RecoveryPasswordRequest request) {
        String result = this.service.recover(request);
        Response response = Response
                .builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message(result)
                .build();
        return new ResponseEntity<Response>(response, response.getStatus());
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return successful message"),
            @ApiResponse(responseCode = "400", description = "Bad request happened"),
            @ApiResponse(responseCode = "403", description = "Forbidden request happened"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred"),
    })
    public ResponseEntity<Response> handle(RecoveryPasswordRequest request) {
        Response response = null;
        ResponseEntity<Response> responseEntity = null;

        var error = this.validate(request);
        if (error != null) {
            response = Response.builder().message(error).status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build();
            return new ResponseEntity<Response>(response, response.getStatus());
        }

        try {
            responseEntity = this.perform(request);
        } catch (Exception e) {
            if (e instanceof UsernameNotFoundException)
                response = Response.builder().message(e.getMessage()).status(HttpStatus.FORBIDDEN).statusCode(HttpStatus.FORBIDDEN.value()).build();
            else
                response = Response.builder().message(e.getMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
            responseEntity = new ResponseEntity<Response>(response, response.getStatus());
        }
        return responseEntity;
    }

    @Override
    public List<IValidator> buildValidators(RecoveryPasswordRequest request) {
        List<IValidator> validators = new ArrayList<IValidator>();
        validators.add(ValidationBuilder.of("email", request.getEmail()).required().build().get(0));
        validators.add(ValidationBuilder.of("email", request.getEmail()).email().build().get(0));
        return validators;
    }
}
