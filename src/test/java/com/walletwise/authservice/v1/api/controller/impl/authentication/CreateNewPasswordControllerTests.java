package com.walletwise.authservice.v1.api.controller.impl.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletwise.authservice.v1.api.dto.CreateNewPasswordRequest;
import com.walletwise.authservice.v1.api.exception.BusinessException;
import com.walletwise.authservice.v1.api.exception.InvalidCredentialsException;
import com.walletwise.authservice.v1.service.contract.authentication.ICreateNewPasswordService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CreateNewPasswordController.class)
public class CreateNewPasswordControllerTests {
    static final String URL = "/newPassword";
    @Autowired
    MockMvc mvc;
    @MockBean
    private ICreateNewPasswordService service;
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
        CreateNewPasswordRequest requestParams = CreateNewPasswordRequest.builder().build();
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
    @DisplayName("Should return 400 if email is invalid")
    public void shouldReturn400IfEmailIsInvalid() throws Exception {
        CreateNewPasswordRequest requestParams = CreateNewPasswordRequest
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
    @DisplayName("Should return 400 if validation code is null")
    public void shouldReturn400IfValidationCodeIsNull() throws Exception {
        CreateNewPasswordRequest requestParams = CreateNewPasswordRequest
                .builder()
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
                .andExpect(jsonPath("message", Matchers.is("The field 'validation code' is required!")));
    }

    @Test
    @DisplayName("Should return 400 if password is null")
    public void shouldReturn400IfPasswordIsNull() throws Exception {
        CreateNewPasswordRequest requestParams = CreateNewPasswordRequest
                .builder()
                .email("gervasio@gmail.com")
                .validationCode(1234)
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
    @DisplayName("Should return 400 if password has invalid size")
    public void shouldReturn400IfPasswordHasInvalidSize() throws Exception {
        CreateNewPasswordRequest requestParams = CreateNewPasswordRequest
                .builder()
                .email("gervasio@gmail.com")
                .validationCode(1234)
                .password("12345")
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
    @DisplayName("Should return 400 if password is not strong enough")
    public void shouldReturn400IfPasswordIsNotStrongEnough() throws Exception {
        CreateNewPasswordRequest requestParams = CreateNewPasswordRequest
                .builder()
                .email("gervasio@gmail.com")
                .validationCode(1234)
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
    @DisplayName("Should return Business exception i service returns business exception")
    public void shouldReturnBusinessExceptionIfServiceReturnsBusinessException() throws Exception {
        CreateNewPasswordRequest requestParams = CreateNewPasswordRequest
                .builder()
                .email("gervasio@gmail.com")
                .validationCode(1234)
                .password("Abc123")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);
        Mockito.doThrow(BusinessException.class).when(service).create(requestParams);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", Matchers.is("BAD_REQUEST")));
    }

    @Test
    @DisplayName("Should return internal server error if service throws internal server` error")
    public void shouldReturnInternalServiceErrorIfServiceThrows() throws Exception {
        CreateNewPasswordRequest requestParams = CreateNewPasswordRequest
                .builder()
                .email("gervasio@gmail.com")
                .validationCode(1234)
                .password("Abc123")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);
        Mockito.doThrow(InvalidCredentialsException.class).when(service).create(requestParams);

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
    @DisplayName("Should return sucessfull message")
    public void shouldReturnSucessfullMessage() throws Exception {
        CreateNewPasswordRequest requestParams = CreateNewPasswordRequest
                .builder()
                .email("gervasio@gmail.com")
                .validationCode(1234)
                .password("Abc123")
                .build();
        String json = new ObjectMapper().writeValueAsString(requestParams);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("status", Matchers.is("OK")));
    }
}
