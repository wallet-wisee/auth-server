package com.walletwise.authservice.v1.api.validation.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class PasswordFieldValidatorTests {
    @Test
    @DisplayName("Should return error message if password field is not string")
    public void shouldReturnErrorMessageIfPasswordFieldIsNotString() {
        String fieldName = "password";
        String fieldValue = "123456";

        PasswordFieldValidator validator = new PasswordFieldValidator(fieldName, fieldValue);
        String result = validator.validate();
        assertThat(result).isEqualTo("The 'password' must be strong , try o add uppercase, lowercase, number and special characters!");
    }

    @Test
    @DisplayName("Should return null if password is strong")
    public void shouldReturnNullIfPasswordIsString() {
        String fieldName = "password";
        String fieldValue = "Abc123";

        PasswordFieldValidator validator = new PasswordFieldValidator(fieldName, fieldValue);
        String result = validator.validate();
        assertThat(result).isNull();
    }
}
