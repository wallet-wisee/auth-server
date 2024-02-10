package com.walletwise.authservice.v1.api.validation.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class RequiredFieldValidatorTests {

    @Test
    @DisplayName("Should validate if the string is empty")
    public void shouldValidateIfTheStringIEmpty() {
        String fieldlName = "name";
        String fieldlValue = "";

        RequiredFieldValidator validator = new RequiredFieldValidator(fieldlName, fieldlValue);
        String result = validator.validate();
        assertThat(result).isEqualTo("The field 'name' is required!");
    }

    @Test
    @DisplayName("Should validate if integer is zero")
    public void shouldValidateIfTheIntegerIsZero() {
        String fieldlName = "name";
        Integer fieldlValue = 0;

        RequiredFieldValidator validator = new RequiredFieldValidator(fieldlName, fieldlValue);
        String result = validator.validate();
        assertThat(result).isEqualTo("The field 'name' is required!");
    }

    @Test
    @DisplayName("Should validate return null if integer is greater than 0")
    public void shouldReturnNullIfIntegerIsGreaterThanZero() {
        String fieldlName = "name";
        Integer fieldlValue = 1;

        RequiredFieldValidator validator = new RequiredFieldValidator(fieldlName, fieldlValue);
        String result = validator.validate();
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should validate if the Object is null")
    public void shouldValidateIfTheObjectIsNull() {
        String fieldlName = "name";
        Object fieldlValue = null;

        RequiredFieldValidator validator = new RequiredFieldValidator(fieldlName, fieldlValue);
        String result = validator.validate();
        assertThat(result).isEqualTo("The field 'name' is required!");
    }

    @Test
    @DisplayName("Should validate return null if the object is not null")
    public void shouldReturnNullIfObjetIsNotNull() {
        String fieldlName = "name";
        Object fieldlValue = new Object();

        RequiredFieldValidator validator = new RequiredFieldValidator(fieldlName, fieldlValue);
        String result = validator.validate();
        assertThat(result).isNull();
    }
}
