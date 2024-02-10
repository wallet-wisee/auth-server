package com.walletwise.authservice.v1.api.validation.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class MinStringSizeValidatorTests {
    @Test
    @DisplayName("Sould have the min size of the string")
    public void testMinStringSize() {
        String fieldName = "name";
        String fieldValue = "val";
        Integer minSize = 4;

        MinStringSizeValidator validator = new MinStringSizeValidator(fieldName, fieldValue, minSize);
        String result = validator.validate();
        assertThat(result).isEqualTo("The field 'name' must have at least " + minSize + " of characters!");

    }
}
