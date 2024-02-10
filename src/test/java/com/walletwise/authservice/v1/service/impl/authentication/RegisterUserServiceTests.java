package com.walletwise.authservice.v1.service.impl.authentication;

import com.walletwise.authservice.v1.api.dto.RegisterUserRequest;
import com.walletwise.authservice.v1.api.exception.BusinessException;
import com.walletwise.authservice.v1.core.helper.security.PasswordEncoderImpl;
import com.walletwise.authservice.v1.core.util.FuncUtils;
import com.walletwise.authservice.v1.event.SendEmailRequest;
import com.walletwise.authservice.v1.model.entity.UserCredential;
import com.walletwise.authservice.v1.model.repository.IUserCredentialRepository;
import com.walletwise.authservice.v1.service.contract.authentication.IRegisterUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class RegisterUserServiceTests {
    private IRegisterUserService service;
    @MockBean
    private PasswordEncoderImpl passwordEncoder;
    @MockBean
    private IUserCredentialRepository repository;
    @MockBean
    private KafkaTemplate<String, SendEmailRequest> kafkaTemplate;
    @MockBean
    private FuncUtils funcUtils;

    @BeforeEach
    public void setUp() {
        service = new RegisterUserService(repository, passwordEncoder, kafkaTemplate, funcUtils);
    }

    @Test
    @DisplayName("should throw businessException if user already exists")
    public void shouldReturnBusinessExceptionIfUserAlreadyExists() {
        RegisterUserRequest request = RegisterUserRequest
                .builder()
                .name("any_name")
                .email("any_email")
                .password("any_password")
                .build();


        UserCredential existingUserCredential = UserCredential
                .builder()
                .id("any_id")
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUserCredential));
        Throwable exception = catchThrowable(() -> this.service.register(request));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("The email is already registered");
        verify(this.repository, never()).save(any());
    }


    @Test
    @DisplayName("should return success message when user is registered")
    public void shouldReturnSuccessMessageWhenUserIsRegistered() {
        RegisterUserRequest request = RegisterUserRequest
                .builder()
                .name("any_name")
                .email("any_email")
                .password("any_password")
                .build();

        String passwordEncoded = UUID.randomUUID().toString();
        UserCredential toSaveUserCredential = UserCredential
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoded)
                .validationCode(1234)
                .build();

        UserCredential savedUserCredential = UserCredential
                .builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoded)
                .validationCode(1234)
                .build();

        when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(null));
        when(this.passwordEncoder.encode("any_password")).thenReturn(passwordEncoded);
        when(this.funcUtils.generateValidationCode()).thenReturn(1234);
        when(this.repository.save(any())).thenReturn(savedUserCredential);

        String result = this.service.register(request);
        assertThat(result).isEqualTo("Please verify your email " + request.getEmail() + " to activate your account!");
        assertThat(savedUserCredential.isActive()).isEqualTo(false);
        verify(this.repository, atLeast(1)).save(toSaveUserCredential);
        verify(this.passwordEncoder, atLeast(1)).encode("any_password");
    }
}
