package com.walletwise.authservice.v1.api.controller.impl.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletwise.authservice.v1.api.dto.ValidateUserValidationCodeRequest;
import com.walletwise.authservice.v1.api.exception.BusinessException;
import com.walletwise.authservice.v1.service.contract.authentication.IValidateUserValidationCodeService;
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
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ValidateUserValidationCodeController.class)
public class ValidateUserValidationCodeControllerTests {
    static final String URL = "/validateCode";
    @Autowired
    MockMvc mvc;
    @MockBean
    private IValidateUserValidationCodeService service;
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
        ValidateUserValidationCodeRequest requestParams = ValidateUserValidationCodeRequest.builder().build();

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
        ValidateUserValidationCodeRequest requestParams = ValidateUserValidationCodeRequest
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
    @DisplayName("should return  400 if validation code is null")
    public void shouldReturn400rIfValidationCodeIsNull() throws Exception {
        ValidateUserValidationCodeRequest requestParams = ValidateUserValidationCodeRequest
                .builder()
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
                .andExpect(jsonPath("message", Matchers.is("The field 'validation code' is required!")));
    }

    @Test
    @DisplayName("should return  400 if the email is not registered on the server")
    public void shouldReturn400rIfTheEmailIsNotRegisteredOnTheServer() throws Exception {
        ValidateUserValidationCodeRequest requestParams = ValidateUserValidationCodeRequest
                .builder()
                .email("gervasioarthur@gmail.com")
                .validationCode(1234)
                .build();

        String json = new ObjectMapper().writeValueAsString(requestParams);

        doThrow(new BusinessException("Invalid email address, please verify the email address and try again!")).when(service).validate(requestParams);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("message", Matchers.is("Invalid email address, please verify the email address and try again!")));
    }

    @Test
    @DisplayName("should return  400 if the email  is already activated")
    public void shouldReturn400rIfTheEmailIsAlreadyActivated() throws Exception {
        ValidateUserValidationCodeRequest requestParams = ValidateUserValidationCodeRequest
                .builder()
                .email("gervasioarthur@gmail.com")
                .validationCode(1234)
                .build();

        String json = new ObjectMapper().writeValueAsString(requestParams);

        doThrow(new BusinessException("The email you've entered is already registered,please try to login  with your credentials.")).when(service).validate(requestParams);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("message", Matchers.is("The email you've entered is already registered,please try to login  with your credentials.")));
    }

    @Test
    @DisplayName("should return  400 if validation code is invalid")
    public void shouldReturn400rIfValidationCodeIsInvalid() throws Exception {
        ValidateUserValidationCodeRequest requestParams = ValidateUserValidationCodeRequest
                .builder()
                .email("gervasioarthur@gmail.com")
                .validationCode(1234)
                .build();

        String json = new ObjectMapper().writeValueAsString(requestParams);

        doThrow(new BusinessException("The validation code you've entered is invalid. Please enter a valid code.")).when(service).validate(requestParams);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("message", Matchers.is("The validation code you've entered is invalid. Please enter a valid code.")));
    }

    @Test
    @DisplayName("should return  500 if a internal server error is returned")
    public void shouldReturn400rIfAInternalServerErrorReturned() throws Exception {
        ValidateUserValidationCodeRequest requestParams = ValidateUserValidationCodeRequest
                .builder()
                .email("gervasioarthur@gmail.com")
                .validationCode(1234)
                .build();

        String json = new ObjectMapper().writeValueAsString(requestParams);

        doThrow(HttpServerErrorException.InternalServerError.class).when(service).validate(requestParams);
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
    @DisplayName("should return  200 if account is successfully activated")
    public void shouldReturn4200SIfAccountIsSuccessfullyActivated() throws Exception {
        ValidateUserValidationCodeRequest requestParams = ValidateUserValidationCodeRequest
                .builder()
                .email("gervasioarthur@gmail.com")
                .validationCode(1234)
                .build();

        String json = new ObjectMapper().writeValueAsString(requestParams);
        given(service.validate(requestParams)).willReturn("Code validated successfully!");


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("status", Matchers.is("OK")))
                .andExpect(jsonPath("message", Matchers.is("Code validated successfully!")));
    }
}
