package com.walletwise.authservice.v1.service.impl.authentication;

import com.walletwise.authservice.v1.api.dto.CreateNewPasswordRequest;
import com.walletwise.authservice.v1.api.exception.BusinessException;
import com.walletwise.authservice.v1.core.helper.security.PasswordEncoderImpl;
import com.walletwise.authservice.v1.model.entity.UserCredential;
import com.walletwise.authservice.v1.model.repository.IUserCredentialRepository;
import com.walletwise.authservice.v1.service.contract.authentication.ICreateNewPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateNewPasswordService implements ICreateNewPasswordService {
    private final IUserCredentialRepository repository;
    private final PasswordEncoderImpl passwordEncoder;

    @Override
    public String create(CreateNewPasswordRequest request) {
        Optional<UserCredential> userCredential = this.repository
                .findByEmail(request.getEmail());

        if (userCredential.isEmpty())
            throw new BusinessException("Invalid email address, please verify the email address and try again!");

        if (userCredential.get().isActive())
            throw new BusinessException("The email you've entered is already registered,please try to login  with your credentials.");

        if (!userCredential.get().getValidationCode().equals(request.getValidationCode()))
            throw new BusinessException("The validation code you've entered is invalid. Please enter a valid code.");

        String encodedPassword = this.passwordEncoder.encode(request.getPassword());
        userCredential.get().setActive(true);
        userCredential.get().setPassword(encodedPassword);
        userCredential.get().setValidationCode(null);
        this.repository.save(userCredential.get());
        return "Password recovered successfully. Please log into your account!";
    }
}
