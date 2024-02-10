package com.walletwise.authservice.v1.api.validation.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailFieldValidator extends _AbstractValidator {

    public EmailFieldValidator(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    public String validate() {
        String value = (String) this.fieldValue;
        value = value.trim();

        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches())
            return "The field 'email' is invalid!";
        return null;
    }
}
