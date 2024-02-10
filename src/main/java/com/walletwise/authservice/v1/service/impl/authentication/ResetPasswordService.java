package com.walletwise.authservice.v1.service.impl.authentication;

import com.walletwise.authservice.v1.api.dto.RecoveryPasswordRequest;
import com.walletwise.authservice.v1.core.util.FuncUtils;
import com.walletwise.authservice.v1.event.SendEmailRequest;
import com.walletwise.authservice.v1.model.entity.UserCredential;
import com.walletwise.authservice.v1.model.repository.IUserCredentialRepository;
import com.walletwise.authservice.v1.service.contract.authentication.IResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResetPasswordService implements IResetPasswordService {
    private final IUserCredentialRepository repository;
    private final KafkaTemplate<String, SendEmailRequest> kafkaTemplate;
    private final FuncUtils funcUtils;

    @Async
    @Override
    public String recover(RecoveryPasswordRequest request) {
        Optional<UserCredential> userCredential = this.repository.findByEmail(request.getEmail());
        if (userCredential.isEmpty())
            throw new UsernameNotFoundException("Can´t find an account with the email you passed, please verify the email and try again.");

        Integer validationCode = this.funcUtils.generateValidationCode();
        SendEmailRequest sendEmailRequest = SendEmailRequest
                .builder()
                .receiver(request.getEmail())
                .subject("Recovery you you password")
                .body("Validation code: " + validationCode)
                .build();

        userCredential.get().setValidationCode(validationCode);
        userCredential.get().setActive(false);
        this.repository.save(userCredential.get());

        kafkaTemplate.send("notificationTopic", sendEmailRequest);
        return "Verify your email to recover your password.";
    }
}
