package com.walletwise.authservice.v1.api.validation.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordFieldValidator extends _AbstractValidator {
    private String returnMessage;

    public PasswordFieldValidator(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.returnMessage = "The '" + this.fieldName + "' must be strong , try o add uppercase, lowercase, number and special characters!";
    }

    public String validate() {
        String value = (String) this.fieldValue;
        value = value.trim();

        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches())
            return this.returnMessage;
        return null;
    }
}