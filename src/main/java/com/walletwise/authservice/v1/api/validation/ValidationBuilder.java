package com.walletwise.authservice.v1.api.validation;

import com.walletwise.authservice.v1.api.validation.contract.IValidator;
import com.walletwise.authservice.v1.api.validation.validators.EmailFieldValidator;
import com.walletwise.authservice.v1.api.validation.validators.MinStringSizeValidator;
import com.walletwise.authservice.v1.api.validation.validators.PasswordFieldValidator;
import com.walletwise.authservice.v1.api.validation.validators.RequiredFieldValidator;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ValidationBuilder {
    private String fieldName;
    private Object fieldValue;
    private List<IValidator> validators = new ArrayList<>();


    private ValidationBuilder(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public static ValidationBuilder of(String fieldName, Object FieldValue) {
        return new ValidationBuilder(fieldName, FieldValue);
    }

    public ValidationBuilder required() {
        this.validators.add(new RequiredFieldValidator(this.fieldName, this.fieldValue));
        return this;
    }

    public ValidationBuilder minStringSize(Integer minSize) {
        this.validators.add(new MinStringSizeValidator(this.fieldName, this.fieldValue, minSize));
        return this;
    }

    public ValidationBuilder email() {
        this.validators.add(new EmailFieldValidator(this.fieldName, this.fieldValue));
        return this;
    }

    public ValidationBuilder password() {
        this.validators.add(new PasswordFieldValidator(this.fieldName, this.fieldValue));
        return this;
    }

    public List<IValidator> build() {
        return this.validators;
    }
}
