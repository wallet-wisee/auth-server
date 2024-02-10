package com.walletwise.authservice.v1.service.impl.authentication;

import com.walletwise.authservice.v1.api.dto.ValidateUserValidationCodeRequest;
import com.walletwise.authservice.v1.api.exception.BusinessException;
import com.walletwise.authservice.v1.model.entity.UserCredential;
import com.walletwise.authservice.v1.model.repository.IUserCredentialRepository;
import com.walletwise.authservice.v1.service.contract.authentication.IValidateUserValidationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidateUserValidationCodeService implements IValidateUserValidationCodeService {
    private final IUserCredentialRepository repository;

    @Override
    public String validate(ValidateUserValidationCodeRequest request) {
        if (this.repository.findByEmail(request.getEmail()).isEmpty())
            throw new BusinessException("Invalid email address, please verify the email address and try again!");

        if (this.repository.findByEmail(request.getEmail()).get().isActive())
            throw new BusinessException("The email you've entered is already registered,please try to login  with your credentials.");

        Optional<UserCredential> userCredential = this.repository
                .findByEmailAndValidationCode(request.getEmail(), request.getValidationCode());

        if (userCredential.isEmpty())
            throw new BusinessException("The validation code you've entered is invalid. Please enter a valid code.");

        UserCredential userCredentialsToSave = userCredential.get();
        userCredentialsToSave.setValidationCode(null);
        userCredentialsToSave.setActive(true);
        this.repository.save(userCredentialsToSave);
        return "Code validated successfully!";
    }
}
