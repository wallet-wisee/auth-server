package com.walletwise.authservice.v1.service.impl.authentication;

import com.walletwise.authservice.v1.api.dto.RecoveryPasswordRequest;
import com.walletwise.authservice.v1.core.util.FuncUtils;
import com.walletwise.authservice.v1.event.SendEmailRequest;
import com.walletwise.authservice.v1.model.entity.UserCredential;
import com.walletwise.authservice.v1.model.repository.IUserCredentialRepository;
import com.walletwise.authservice.v1.service.contract.authentication.IResetPasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class ResetPasswordServiceTests {
    private IResetPasswordService service;
    @MockBean
    private IUserCredentialRepository repository;
    @MockBean
    private FuncUtils funcUtils;
    @MockBean
    private KafkaTemplate<String, SendEmailRequest> kafkaTemplate;

    @BeforeEach
    public void setup() {
        this.service = new ResetPasswordService(repository, kafkaTemplate, funcUtils);
    }

    @Test
    @DisplayName("Should throws user not found exception if email is not registered")
    public void shouldThrowUserNotRegistered() {
        RecoveryPasswordRequest request = RecoveryPasswordRequest.builder().email("any_email").build();

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        Throwable exception = catchThrowable(() -> this.service.recover(request));

        verify(this.repository, atLeast(1)).findByEmail(request.getEmail());
        assertThat(exception).isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Can´t find an account with the email you passed, please verify the email and try again.");
    }

    @Test
    @DisplayName("Should return successful message")
    public void shouldReturnSuccessFullMessage() {
        RecoveryPasswordRequest request = RecoveryPasswordRequest.builder().email("any_email").build();
        Integer validationCode = 1234;

        SendEmailRequest sendEmailRequest = SendEmailRequest
                .builder()
                .receiver(request.getEmail())
                .subject("Recovery you you password")
                .body("Validation code: " + validationCode)
                .build();

        UserCredential SavedUserCredential = UserCredential
                .builder()
                .id("any_id")
                .name("any_name")
                .email("any_email")
                .isActive(true)
                .password("any_password")
                .validationCode(null)
                .build();

        UserCredential toSaveUserCredential = UserCredential
                .builder()
                .id("any_id")
                .name("any_name")
                .email("any_email")
                .isActive(false)
                .password("any_password")
                .validationCode(validationCode)
                .build();

        when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.of(toSaveUserCredential));
        when(this.funcUtils.generateValidationCode()).thenReturn(validationCode);
        when(this.repository.save(toSaveUserCredential)).thenReturn(toSaveUserCredential);

        String result = this.service.recover(request);

        verify(this.repository, atLeast(1)).findByEmail(request.getEmail());
        verify(this.funcUtils, atLeast(1)).generateValidationCode();
        verify(this.repository, atLeast(1)).save(toSaveUserCredential);
        verify(this.kafkaTemplate, atLeast(1)).send("notificationTopic", sendEmailRequest);

        assertThat(result).isEqualTo("Verify your email to recover your password.");
    }

}
