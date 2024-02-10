package com.walletwise.authservice.v1.service.impl.user;

import com.walletwise.authservice.v1.model.entity.UserCredential;
import com.walletwise.authservice.v1.model.repository.IUserCredentialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class CustomUserDetailsServiceTests {
    @MockBean
    private IUserCredentialRepository repository;
    private UserDetailsService service;

    @BeforeEach
    public void setUp() {
        this.service = new CustomUserDetailsService(repository);
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException if user does not exist")
    public void shouldThrowUsernameNotFoundExceptionIfUserDoesNotExist() {
        String username = "any_username";
        Optional<UserCredential> userCredentialSaved = Optional.empty();
        when(this.repository.findByEmail(username)).thenReturn(userCredentialSaved);
        Throwable exception = catchThrowable(() -> this.service.loadUserByUsername(username));
        assertThat(exception)
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("Should return a valid user details on success")
    public void shouldReturnAValidaUserDetailsOnSuccess() {
        String username = "any_username";
        Optional<UserCredential> userCredentialSaved = Optional.of(UserCredential
                .builder()
                .id("any_id")
                .name("any_name")
                .email(username)
                .password("any_password")
                .build());

        when(this.repository.findByEmail(username)).thenReturn(userCredentialSaved);
        UserDetails userDetails = this.service.loadUserByUsername(username);
        assertThat(userDetails.getUsername()).isEqualTo(userCredentialSaved.get().getEmail());
        assertThat(userDetails.getPassword()).isEqualTo(userCredentialSaved.get().getPassword());

    }
}
