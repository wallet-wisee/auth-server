package com.walletwise.authservice.v1.service.impl.authentication;

import com.walletwise.authservice.v1.api.dto.LoginRequest;
import com.walletwise.authservice.v1.api.exception.InactiveUserException;
import com.walletwise.authservice.v1.api.exception.InvalidCredentialsException;
import com.walletwise.authservice.v1.core.helper.jwt.GenerateToken;
import com.walletwise.authservice.v1.model.entity.UserCredential;
import com.walletwise.authservice.v1.model.repository.IUserCredentialRepository;
import com.walletwise.authservice.v1.service.contract.authentication.ILoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService implements ILoginService {
    private final GenerateToken generateToken;
    private final IUserCredentialRepository repository;
    private final AuthenticationManager authenticationManager;

    @Override
    public String generate(LoginRequest request) {
        Optional<UserCredential> user = this.repository.findByEmail(request.getEmail());

        if (user.isEmpty())
            throw new UsernameNotFoundException("User net found, please verify email and try again.");

        if (!user.get().isActive())
            throw new InactiveUserException();

        Authentication authenticate = this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        if (!authenticate.isAuthenticated())
            throw new InvalidCredentialsException();
        return this.generateToken.generate(request.getEmail());
    }
}
