package com.walletwise.authservice.v1.api.controller.impl.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletwise.authservice.v1.service.contract.jwt.IValidateTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ValidateTokenController.class)
public class ValidateTokenControllerTests {
    static final String URL = "/token/validate";
    @Autowired
    MockMvc mvc;
    @MockBean
    private IValidateTokenService service;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }


    @Test
    @DisplayName("should return  400 if email is null")
    public void shouldReturn400rIfEmailIsNull() throws Exception {
        String token = "";
        String json = new ObjectMapper().writeValueAsString(token);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .param("token", token);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 403 if service throws ExpiredJwtException")
    public void shouldReturn543IfServiceThrowsExpiredJwtException() throws Exception {
        String token = UUID.randomUUID().toString();

        doThrow(ExpiredJwtException.class).when(service).validate(token);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .param("token", token);
        mvc
                .perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("should return 403 if service throws UnsupportedJwtException")
    public void shouldReturn543IfServiceThrowsUnsupportedJwtException() throws Exception {
        String token = UUID.randomUUID().toString();

        doThrow(UnsupportedJwtException.class).when(service).validate(token);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .param("token", token);
        mvc
                .perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("should return 403 if service throws MalformedJwtException")
    public void shouldReturn543IfServiceThrowsMalformedJwtException() throws Exception {
        String token = UUID.randomUUID().toString();

        doThrow(MalformedJwtException.class).when(service).validate(token);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .param("token", token);
        mvc
                .perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("should return 403 if service throws SignatureException")
    public void shouldReturn543IfServiceThrowsSignatureException() throws Exception {
        String token = UUID.randomUUID().toString();

        doThrow(SignatureException.class).when(service).validate(token);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .param("token", token);
        mvc
                .perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("should return 403 if service throws IllegalArgumentException")
    public void shouldReturn543IfServiceThrowsIllegalArgumentException() throws Exception {
        String token = UUID.randomUUID().toString();

        doThrow(IllegalArgumentException.class).when(service).validate(token);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .param("token", token);
        mvc
                .perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("should return 500 occurs an Internal server error")
    public void shouldReturn500IfOccursAnInternalServerError() throws Exception {
        String token = UUID.randomUUID().toString();

        doThrow(HttpServerErrorException.InternalServerError.class).when(service).validate(token);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .param("token", token);
        mvc
                .perform(request)
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("should return  200 on success")
    public void shouldReturn400rsNull() throws Exception {
        String token = UUID.randomUUID().toString();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .param("token", token);
        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("status", Matchers.is("OK")))
                .andExpect(jsonPath("message", Matchers.is("Token is valid!")));
        ;
    }
}
