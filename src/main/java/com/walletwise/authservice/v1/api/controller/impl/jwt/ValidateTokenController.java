package com.walletwise.authservice.v1.api.controller.impl.jwt;

import com.walletwise.authservice.v1.api.controller.Controller;
import com.walletwise.authservice.v1.api.controller.contract.jwt.IValidateTokenController;
import com.walletwise.authservice.v1.api.validation.ValidationBuilder;
import com.walletwise.authservice.v1.api.validation.contract.IValidator;
import com.walletwise.authservice.v1.core.http.Response;
import com.walletwise.authservice.v1.service.contract.jwt.IValidateTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token/validate")
public class ValidateTokenController extends Controller<String> implements IValidateTokenController {
    private final IValidateTokenService service;

    @Override
    public ResponseEntity<Response> perform(String request) {
        this.service.validate(request);
        Response response = Response
                .builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("Token is valid!")
                .build();
        return new ResponseEntity<Response>(response, response.getStatus());
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid!"),
            @ApiResponse(responseCode = "400", description = "Bad request happened"),
            @ApiResponse(responseCode = "403", description = "Forbidden request happened"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred"),
    })
    public ResponseEntity<Response> handle(@RequestParam("token") String token) {
        Response response = null;
        ResponseEntity<Response> responseEntity = null;

        var error = this.validate(token);
        if (error != null) {
            response = Response.builder().message(error).status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build();
            return new ResponseEntity<Response>(response, response.getStatus());
        }

        try {
            responseEntity = this.perform(token);
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException
                    || e instanceof UnsupportedJwtException
                    || e instanceof MalformedJwtException
                    || e instanceof SignatureException
                    || e instanceof IllegalArgumentException)
                response = Response.builder().message(e.getMessage()).status(HttpStatus.FORBIDDEN).statusCode(HttpStatus.FORBIDDEN.value()).build();
            else
                response = Response.builder().message(e.getMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
            responseEntity = new ResponseEntity<Response>(response, response.getStatus());
        }

        return responseEntity;
    }

    @Override
    public List<IValidator> buildValidators(String token) {
        List<IValidator> validators = new ArrayList<>();
        validators.add(ValidationBuilder.of("token", token).required().build().get(0));
        return validators;
    }
}
