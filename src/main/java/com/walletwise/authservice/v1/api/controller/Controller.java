package com.walletwise.authservice.v1.api.controller;

import com.walletwise.authservice.v1.api.validation.ValidationComposite;
import com.walletwise.authservice.v1.api.validation.contract.IValidator;
import com.walletwise.authservice.v1.core.config.annotations.Generated;
import com.walletwise.authservice.v1.core.http.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Generated
public abstract class Controller<E> {
    public abstract ResponseEntity<Response> perform(E request);

    public abstract List<IValidator> buildValidators(E request);

    protected String validate(E request) {
        List<IValidator> validators = this.buildValidators(request);
        return new ValidationComposite(validators).validate();
    }
}
