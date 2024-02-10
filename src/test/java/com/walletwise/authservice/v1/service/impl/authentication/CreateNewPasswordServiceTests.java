package com.walletwise.authservice.v1.service.impl.authentication;

import com.walletwise.authservice.v1.api.dto.CreateNewPasswordRequest;
import com.walletwise.authservice.v1.api.exception.BusinessException;
import com.walletwise.authservice.v1.core.helper.security.PasswordEncoderImpl;
import com.walletwise.authservice.v1.model.entity.UserCredential;
import com.walletwise.authservice.v1.model.repository.IUserCredentialRepository;
import com.walletwise.authservice.v1.service.contract.authentication.ICreateNewPasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class CreateNewPasswordServiceTests {
    private ICreateNewPasswordService service;
    @MockBean
    private IUserCredentialRepository repository;
    @MockBean
    private PasswordEncoderImpl passwordEncoder;

    @BeforeEach
    public void setUp() {
        service = new CreateNewPasswordService(repository, passwordEncoder);
    }

    @Test
    @DisplayName("should throw Business exception if email is not registered")
    public void shouldThrowBusinessExceptionIfEmailNotRegistered() {
        CreateNewPasswordRequest request = CreateNewPasswordRequest
                .builder()
                .email("any_email")
                .build();
        Mockito.when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        Throwable exception = catchThrowable(() -> this.service.create(request));
        verify(this.repository, atLeast(1)).findByEmail(request.getEmail());
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Invalid email address, please verify the email address and try again!");

    }

    @Test
    @DisplayName("Should throw BusinessException if the account is already activated")
    public void shouldThrowBusinessExceptionIfTheAccountIsAlreadyActivated() {
        CreateNewPasswordRequest request = CreateNewPasswordRequest
                .builder()
                .email("any_email")
                .build();

        UserCredential savedUserCredential = UserCredential
                .builder()
                .id("any_id")
                .name("any_name")
                .password("any_password")
                .email("any_email")
                .isActive(true)
                .validationCode(null)
                .build();

        Mockito.when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.of(savedUserCredential));

        Throwable exception = catchThrowable(() -> this.service.create(request));
        verify(this.repository, atLeast(1)).findByEmail(request.getEmail());
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("The email you've entered is already registered,please try to login  with your credentials.");
    }

    @Test
    @DisplayName("Should throw business exception if the validation code  is invalid")
    public void shouldThrowBusinessExceptionIfValidationCodeIsInvalid() {
        CreateNewPasswordRequest request = CreateNewPasswordRequest
                .builder()
                .email("any_email")
                .validationCode(1234)
                .password("any_new_password")
                .build();

        UserCredential savedUserCredential = UserCredential
                .builder()
                .name("any_name")
                .isActive(false)
                .validationCode(5353)
                .build();
        when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.of(savedUserCredential));
        Throwable exception = catchThrowable(() -> this.service.create(request));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("The validation code you've entered is invalid. Please enter a valid code.");
        verify(this.repository, atLeast(1)).findByEmail(request.getEmail());
    }

    @Test
    @DisplayName("Should change a password when validation succeeds")
    public void shouldChangeAPasswordWhenValidationSuceeds() {
        CreateNewPasswordRequest request = CreateNewPasswordRequest
                .builder()
                .email("any_email")
                .validationCode(1234)
                .password("any_new_password")
                .build();

        String encodedPassword = UUID.randomUUID().toString();
        UserCredential toSaveUserCredential = UserCredential
                .builder()
                .name("any_name")
                .isActive(true)
                .validationCode(1234)
                .password(encodedPassword)
                .build();

        UserCredential savedUserCredential = UserCredential
                .builder()
                .id("any_id")
                .name("any_name")
                .email("any_email")
                .isActive(false)
                .validationCode(1234)
                .password("any_password")
                .build();


        when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.of(savedUserCredential));
        when(this.passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(this.repository.save(any())).thenReturn(savedUserCredential);


        String result = this.service.create(request);
        assertThat(result).isEqualTo("Password recovered successfully. Please log into your account!");
        verify(this.repository, atLeast(1)).findByEmail(any());
        verify(this.passwordEncoder, atLeast(1)).encode(any());
        verify(this.repository, atLeast(1)).save(any());
    }
}