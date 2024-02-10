package com.walletwise.authservice.v1.api.validation.validators;

public class MinStringSizeValidator extends _AbstractValidator {
    private String returnMessage;
    private Integer minSize;

    public MinStringSizeValidator(String fieldName, Object fieldValue, Integer minSize) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.minSize = minSize;
        this.returnMessage = "The field '" + this.fieldName + "' must have at least " + minSize + " of characters!";
    }

    @Override
    public String validate() {
        String value = (String) this.fieldValue;
        value = value.trim();
        if (value.length() < minSize)
            return this.returnMessage;
        return null;
    }
}