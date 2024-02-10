package com.walletwise.authservice.v1.service.impl.user;

import com.walletwise.authservice.v1.model.entity.CustomUserDetails;
import com.walletwise.authservice.v1.model.entity.UserCredential;
import com.walletwise.authservice.v1.model.repository.IUserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final IUserCredentialRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> userCredential = this.repository.findByEmail(username);
        return userCredential
                .map(CustomUserDetails::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException(" User not found with email" + username));
    }
}
