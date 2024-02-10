package com.walletwise.authservice.v1.api.controller.impl.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletwise.authservice.v1.api.dto.RegisterUserRequest;
import com.walletwise.authservice.v1.api.exception.BusinessException;
import com.walletwise.authservice.v1.model.entity.UserCredential;
import com.walletwise.authservice.v1.service.contract.authentication.IRegisterUserService;
import jakarta.ws.rs.core.MediaType;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RegisterUserController.class)
public class RegisterUserControllerTests {
    static final String URL = "/register";
    @Autowired
    MockMvc mvc;
    @MockBean
    private IRegisterUserService service;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    @DisplayName("should return  400 if name is null")
    public void shouldReturn400rIfNameIsNull() throws Exception {
        RegisterUserRequest requestParams = RegisterUserRequest.builder().build();
        String json = new ObjectMapper().writeValueAsString(requestParams);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("message", Matchers.is("The field 'name' is required!")));
    }

    @Test
    @DisplayName("should return  400 if name has no minimum length")
    public void shouldReturn400rIfNameHasNoMinimumLength() throws Exception {
        RegisterUserRequest requestParams = RegisterUserRequest
                .builder()
                .name("any")
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
                .andExpect(jsonPath("message", Matchers.is("The field 'name' must have at least 4 of characters!")));
    }

    @Test
    @DisplayName("should return  400 if email is null")
    public void shouldReturn400rIfEmailIsNull() throws Exception {
        RegisterUserRequest requestParams = RegisterUserRequest
                .builder()
                .name("Gervasio")
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
                .andExpect(jsonPath("message", Matchers.is("The field 'email' is required!")));
    }

    @Test
    @DisplayName("should return  400 if email is not valid")
    public void shouldReturn400rIfEmailIsNotValid() throws Exception {
        RegisterUserRequest requestParams = RegisterUserRequest
                .builder()
                .name("Gervasio")
                .email("@gmail.com ")
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
    @DisplayName("should return  400 if password is null")
    public void shouldReturn400rIfPasswordIsNull() throws Exception {
        RegisterUserRequest requestParams = RegisterUserRequest
                .builder()
                .name("Gervasio")
                .email("gervasio@gmail.com")
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
    @DisplayName("should return  400 if password has no minimum length")
    public void shouldReturn400rIfPasswordHasNoMinimumLength() throws Exception {
        RegisterUserRequest requestParams = RegisterUserRequest
                .builder()
                .name("Gervasio")
                .email("gervasio@gmail.com")
                .password("1234")
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
                .andExpect(jsonPath("message", Matchers.is("The field 'password' must have at least 6 of characters!")));
    }

    @Test
    @DisplayName("should return  400 if password is not strong")
    public void shouldReturn400rIfPasswordIsNotString() throws Exception {
        RegisterUserRequest requestParams = RegisterUserRequest
                .builder()
                .name("Gervasio")
                .email("gervasio@gmail.com")
                .password("123456")
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
                .andExpect(jsonPath("message", Matchers.is("The 'password' must be strong , try o add uppercase, lowercase, number and special characters!")));
    }

    @Test
    @DisplayName("should return  400 the email is already taken")
    public void shouldReturn400rIfTheEmailIsAlreadyTaken() throws Exception {
        RegisterUserRequest requestParams = RegisterUserRequest
                .builder()
                .name("Gervasio")
                .email("gervasio@gmail.com")
                .password("Abc123")
                .build();

        UserCredential userCredential = UserCredential
                .builder()
                .id("any_id")
                .name("Gervasio")
                .email("gervasio@gmail.com")
                .password("Abc123")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);

        doThrow(new BusinessException("The email is already registered")).when(service).register(requestParams);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("message", Matchers.is("The email is already registered")));
        verify(this.service, atLeast(1)).register(requestParams);

    }

    @Test
    @DisplayName("should return  500 if an internal server error occurs")
    public void shouldReturn500IfAnInternalServerErrorOccurs() throws Exception {
        RegisterUserRequest requestParams = RegisterUserRequest
                .builder()
                .name("Gervasio")
                .email("gervasio@gmail.com")
                .password("Abc123")
                .build();

        UserCredential userCredential = UserCredential
                .builder()
                .id("any_id")
                .name("Gervasio")
                .email("gervasio@gmail.com")
                .password("Abc123")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);

        doThrow(HttpServerErrorException.InternalServerError.class).when(service).register(requestParams);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("status", Matchers.is("INTERNAL_SERVER_ERROR")));
    }

    @Test
    @DisplayName("should return  201 on successful registration")
    public void shouldReturn201OnSuccessfulRegistration() throws Exception {
        RegisterUserRequest requestParams = RegisterUserRequest
                .builder()
                .name("Gervasio")
                .email("gervasio@gmail.com")
                .password("Abc123")
                .build();

        String json = new ObjectMapper().writeValueAsString(requestParams);
        given(service.register(requestParams)).willReturn("Hello " + requestParams.getName() + "! Welcome to Wallet wise world!");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status", Matchers.is("CREATED")))
                .andExpect(jsonPath("message", Matchers.is("Hello " + requestParams.getName() + "! Welcome to Wallet wise world!")));

        verify(this.service, atLeast(1)).register(requestParams);

    }
}
