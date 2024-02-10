package com.walletwise.authservice.v1.api.validation.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class EmailFieldValidatorTests {
    @Test
    @DisplayName("Should return error message if the emailfield is invalid")
    public void shouldReturnErrorMessageIfTheEmailFieldIsInvalid() {
        String fieldName = "email";
        String fieldValue = "foo.bar@gmailcom ";

        EmailFieldValidator validator = new EmailFieldValidator(fieldName, fieldValue);
        String result = validator.validate();
        assertThat(result).isEqualTo("The field 'email' is invalid!");
    }

    @Test
    @DisplayName("Should return null if the field is valid")
    public void shouldReturnNullIfTheFieldIsValid() {
        String fieldName = "email";
        String fieldValue = "gervasio@gmail.com ";

        EmailFieldValidator validator = new EmailFieldValidator(fieldName, fieldValue);
        String result = validator.validate();
        assertThat(result).isNull();
    }
}
