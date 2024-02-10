package com.walletwise.authservice.v1.core.util;

import com.walletwise.authservice.v1.core.config.annotations.Generated;
import org.springframework.stereotype.Component;

@Generated
@Component
public class FuncUtils {
    public Integer generateValidationCode() {
        int min = 1000;
        int max = 9000;
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}
