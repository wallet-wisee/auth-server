package com.walletwise.authservice.v1.api.controller.impl.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletwise.authservice.v1.api.dto.LoginRequest;
import com.walletwise.authservice.v1.api.dto.RecoveryPasswordRequest;
import com.walletwise.authservice.v1.service.contract.authentication.IResetPasswordService;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ResetPasswordController.class)
public class RecoveryPasswordControllerTests {
    static final String URL = "/recoveryPassword";
    @Autowired
    MockMvc mvc;
    @MockBean
    private IResetPasswordService service;
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
        LoginRequest requestParams = LoginRequest.builder().build();
        String json = new ObjectMapper().writeValueAsString(requestParams);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("message", Matchers.is("The field 'email' is required!")));
    }

    @Test
    @DisplayName("should return  400 if email is invalid")
    public void shouldReturn400rIfEmailIsInvalid() throws Exception {
        RecoveryPasswordRequest requestParams = RecoveryPasswordRequest
                .builder()
                .email("any_email")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("message", Matchers.is("The field 'email' is invalid!")));

    }

    @Test
    @DisplayName("should return  403 if email is not associated to any account")
    public void shouldReturn400rIfEmailNotAssociatedToAnyAccount() throws Exception {
        RecoveryPasswordRequest requestParams = RecoveryPasswordRequest
                .builder()
                .email("gervasioartur@outlook.com.br")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);

        doThrow(new UsernameNotFoundException("Can´t find an account with the email you passed, please verify the email and try again.")).when(service).recover(requestParams);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("status", Matchers.is("FORBIDDEN")))
                .andExpect(jsonPath("message", Matchers.is("Can´t find an account with the email you passed, please verify the email and try again.")));

        verify(this.service, atLeast(1)).recover(requestParams);

    }

    @Test
    @DisplayName("should return  500 if an internal server error occurs")
    public void shouldReturn500IfAnInternalServerErrorOccurs() throws Exception {
        RecoveryPasswordRequest requestParams = RecoveryPasswordRequest
                .builder()
                .email("gervasioartur@outlook.com.br")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);

        doThrow(HttpServerErrorException.InternalServerError.class).when(service).recover(requestParams);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("status", Matchers.is("INTERNAL_SERVER_ERROR")));
        verify(this.service, atLeast(1)).recover(requestParams);
    }

    @Test
    @DisplayName("should return  201 on successful registration")
    public void shouldReturn201OnSuccessfulRegistration() throws Exception {
        RecoveryPasswordRequest requestParams = RecoveryPasswordRequest
                .builder()
                .email("gervasioartur@outlook.com.br")
                .build();

        String json = new ObjectMapper().writeValueAsString(requestParams);
        given(service.recover(requestParams)).willReturn("Can´t find an account with the email you passed, please verify the email and try again.");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("status", Matchers.is("OK")))
                .andExpect(jsonPath("message", Matchers.is("Can´t find an account with the email you passed, please verify the email and try again.")));

        verify(this.service, atLeast(1)).recover(requestParams);

    }
}
