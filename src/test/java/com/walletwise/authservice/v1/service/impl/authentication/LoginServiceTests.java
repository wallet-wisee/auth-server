package com.walletwise.authservice.v1.service.impl.authentication;

import com.walletwise.authservice.v1.api.dto.LoginRequest;
import com.walletwise.authservice.v1.api.exception.InactiveUserException;
import com.walletwise.authservice.v1.api.exception.InvalidCredentialsException;
import com.walletwise.authservice.v1.core.helper.jwt.GenerateToken;
import com.walletwise.authservice.v1.model.entity.UserCredential;
import com.walletwise.authservice.v1.model.repository.IUserCredentialRepository;
import com.walletwise.authservice.v1.service.contract.authentication.ILoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class
LoginServiceTests {
    private ILoginService service;
    @MockBean
    private GenerateToken generateToken;
    @MockBean
    private IUserCredentialRepository repository;
    @MockBean
    private AuthenticationManager authenticationManager;


    @BeforeEach
    public void setUp() {
        service = new LoginService(generateToken, repository, authenticationManager);
    }

    @Test
    @DisplayName("Should throw InactiveUserException if user is not present")
    public void shouldThrowInactiveUserExceptionIUserNotPresent() {
        LoginRequest request = LoginRequest
                .builder()
                .email("gervasio@gmail.com")
                .password("ggt5555")
                .build();

        when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        Throwable exception = catchThrowable(() -> this.service.generate(request));
        verify(this.authenticationManager, never()).authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        verify(this.generateToken, never()).generate(request.getEmail());
        assertThat(exception).isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw InactiveUserException if user is inactive")
    public void shouldThrowInactiveUserException() {
        LoginRequest request = LoginRequest
                .builder()
                .email("gervasio@gmail.com")
                .password("ggt5555")
                .build();

        UserCredential userCredential = UserCredential
                .builder()
                .email("gervasio@gmail.com")
                .password(UUID.randomUUID().toString())
                .isActive(false)
                .build();

        when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.of(userCredential));
        Throwable exception = catchThrowable(() -> this.service.generate(request));
        verify(this.authenticationManager, never()).authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        verify(this.generateToken, never()).generate(request.getEmail());
        assertThat(exception).isInstanceOf(InactiveUserException.class);
    }

    @Test
    @DisplayName("Should  throw invalid credentials exception")
    public void shouldThrowInvalidCredentialsException() {
        LoginRequest request = LoginRequest
                .builder()
                .email("gervasio@gmail.com")
                .password("ggt5555")
                .build();
        String tokenResult = UUID.randomUUID().toString();
        Authentication authentication = new Authentication() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
        };

        UserCredential userCredential = UserCredential
                .builder()
                .email("gervasio@gmail.com")
                .password(UUID.randomUUID().toString())
                .isActive(true)
                .build();

        when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.of(userCredential));
        when(this.generateToken.generate(request.getEmail())).thenReturn(tokenResult);
        when(this.authenticationManager.authenticate(any())).thenReturn(authentication);

        Throwable exception = catchThrowable(() -> this.service.generate(request));
        verify(this.repository, atLeast(1)).findByEmail(request.getEmail());
        verify(this.generateToken, never()).generate(request.getEmail());
        assertThat(exception)
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    @DisplayName("Should  return generated token")
    public void shouldReturnGeneratedToken() {
        LoginRequest request = LoginRequest
                .builder()
                .email("gervasio@gmail.com")
                .password("ggt5555")
                .build();
        String tokenResult = UUID.randomUUID().toString();
        Authentication authentication = new Authentication() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
        };

        UserCredential userCredential = UserCredential
                .builder()
                .email("gervasio@gmail.com")
                .password(UUID.randomUUID().toString())
                .isActive(true)
                .build();

        when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.of(userCredential));
        when(this.generateToken.generate(request.getEmail())).thenReturn(tokenResult);
        when(this.authenticationManager.authenticate(any())).thenReturn(authentication);
        String result = this.service.generate(request);

        verify(this.repository, atLeast(1)).findByEmail(request.getEmail());
        verify(this.authenticationManager, atLeast(1)).authenticate(any());
        verify(this.generateToken, atLeast(1)).generate(request.getEmail());
        assertThat(result).isEqualTo(tokenResult);
    }
}
