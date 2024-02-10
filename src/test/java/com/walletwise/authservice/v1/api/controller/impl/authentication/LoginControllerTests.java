package com.walletwise.authservice.v1.api.controller.impl.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletwise.authservice.v1.api.dto.LoginRequest;
import com.walletwise.authservice.v1.api.exception.InactiveUserException;
import com.walletwise.authservice.v1.api.exception.InvalidCredentialsException;
import com.walletwise.authservice.v1.service.contract.authentication.ILoginService;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LoginController.class)
public class LoginControllerTests {
    static final String URL = "/login";
    @Autowired
    MockMvc mvc;
    @MockBean
    private ILoginService service;
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
        LoginRequest requestParams = LoginRequest
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
    @DisplayName("should return  400 if Password is null")
    public void shouldReturn400rIfPasswordIsNull() throws Exception {
        LoginRequest requestParams = LoginRequest.
                builder()
                .email("gervasioarthur@gmail.com")
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
                .andExpect(jsonPath("message", Matchers.is("The field 'password' is required!")));
    }

    @Test
    @DisplayName("should return  403 if service throws InvalidCredentialsException")
    public void shouldReturn403IfServiceThrowsInvalidCredentialsException() throws Exception {
        LoginRequest requestParams = LoginRequest.
                builder()
                .email("gervasioarthur@gmail.com")
                .password("password")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);
        Mockito.doThrow(InvalidCredentialsException.class).when(service).generate(requestParams);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("status", Matchers.is("FORBIDDEN")));
        verify(this.service, atLeast(1)).generate(requestParams);

    }

    @Test
    @DisplayName("should return  403 if service throws UsernameNotFoundException")
    public void shouldReturn403IfServiceThrowsUsernameNotFoundException() throws Exception {
        LoginRequest requestParams = LoginRequest.
                builder()
                .email("gervasioarthur@gmail.com")
                .password("password")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);
        Mockito.doThrow(UsernameNotFoundException.class).when(service).generate(requestParams);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("status", Matchers.is("FORBIDDEN")));
        verify(this.service, atLeast(1)).generate(requestParams);

    }

    @Test
    @DisplayName("should return  403 if service throws BadCredentialsException")
    public void shouldReturn403IfServiceThrowsBadCredentialsException() throws Exception {
        LoginRequest requestParams = LoginRequest.
                builder()
                .email("gervasioarthur@gmail.com")
                .password("password")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);
        Mockito.doThrow(BadCredentialsException.class).when(service).generate(requestParams);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("status", Matchers.is("FORBIDDEN")));
        verify(this.service, atLeast(1)).generate(requestParams);

    }

    @Test
    @DisplayName("should return  403 if service throws InactiveUserException")
    public void shouldReturn403IfServiceThrowsInactiveUserException() throws Exception {
        LoginRequest requestParams = LoginRequest.
                builder()
                .email("gervasioarthur@gmail.com")
                .password("password")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);
        Mockito.doThrow(InactiveUserException.class).when(service).generate(requestParams);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("status", Matchers.is("FORBIDDEN")));
        verify(this.service, atLeast(1)).generate(requestParams);

    }

    @Test
    @DisplayName("should return  500 if an internal server error occurs")
    public void shouldReturn500IfAnInternalServerErrorOccurs() throws Exception {
        LoginRequest requestParams = LoginRequest.
                builder()
                .email("gervasioarthur@gmail.com")
                .password("password")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);
        Mockito.doThrow(HttpServerErrorException.InternalServerError.class).when(service).generate(requestParams);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("status", Matchers.is("INTERNAL_SERVER_ERROR")));
        verify(this.service, atLeast(1)).generate(requestParams);

    }

    @Test
    @DisplayName("should return  200 on success")
    public void shouldReturn200OnSuccess() throws Exception {
        LoginRequest requestParams = LoginRequest
                .builder()
                .email("gervasioarthur@gmail.com")
                .password("password")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isOk());
        verify(this.service, atLeast(1)).generate(requestParams);

    }
}
