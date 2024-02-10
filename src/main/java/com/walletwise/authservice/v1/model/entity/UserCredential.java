package com.walletwise.authservice.v1.model.entity;

import com.walletwise.authservice.v1.core.config.annotations.Generated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Generated
@AllArgsConstructor
@Document(value = "user_credential")
public class UserCredential {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private boolean isActive = false;
    private Integer validationCode;
}
