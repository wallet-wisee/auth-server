package com.walletwise.authservice.v1.service.impl.authentication;

import com.walletwise.authservice.v1.api.dto.RegisterUserRequest;
import com.walletwise.authservice.v1.api.exception.BusinessException;
import com.walletwise.authservice.v1.core.helper.security.PasswordEncoderImpl;
import com.walletwise.authservice.v1.core.util.FuncUtils;
import com.walletwise.authservice.v1.event.SendEmailRequest;
import com.walletwise.authservice.v1.model.entity.UserCredential;
import com.walletwise.authservice.v1.model.repository.IUserCredentialRepository;
import com.walletwise.authservice.v1.service.contract.authentication.IRegisterUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserService implements IRegisterUserService {
    private final IUserCredentialRepository repository;
    private final PasswordEncoderImpl passwordEncoder;
    private final KafkaTemplate<String, SendEmailRequest> kafkaTemplate;
    private final FuncUtils funcUtils;

    @Async
    @Override
    public String register(RegisterUserRequest request) {
        if (this.repository.findByEmail(request.getEmail()).isPresent())
            throw new BusinessException("The email is already registered");

        String passwordEncoded = this.passwordEncoder.encode(request.getPassword());
        request.setPassword(passwordEncoded);
        UserCredential userCredential = this.mapToUserCredential(request);
        Integer validationCode = this.funcUtils.generateValidationCode();
        userCredential.setValidationCode(validationCode);
        userCredential = this.repository.save(userCredential);
        SendEmailRequest sendEmailRequest = SendEmailRequest
                .builder()
                .receiver(userCredential.getEmail())
                .subject("Active your account using the given validation code.")
                .body("Validation code: " + validationCode)
                .build();
        kafkaTemplate.send("notificationTopic", sendEmailRequest);
        return "Please verify your email " + userCredential.getEmail() + " to activate your account!";
    }

    private UserCredential mapToUserCredential(RegisterUserRequest request) {
        return UserCredential
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
}
