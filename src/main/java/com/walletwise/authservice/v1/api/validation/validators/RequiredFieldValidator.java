package com.walletwise.authservice.v1.api.validation.validators;

public class RequiredFieldValidator extends _AbstractValidator {
    private String returnMessage;

    public RequiredFieldValidator(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.returnMessage = "The field '" + this.fieldName + "' is required!";
    }

    public String validate() {
        if (this.fieldValue instanceof String) {
            return ((String) this.fieldValue).trim().equals("") ? this.returnMessage : null;
        } else if (this.fieldValue instanceof Integer) {
            return this.fieldValue.equals(0) ? this.returnMessage : null;
        } else {
            return this.fieldValue == null ? this.returnMessage : null;
        }
    }
}