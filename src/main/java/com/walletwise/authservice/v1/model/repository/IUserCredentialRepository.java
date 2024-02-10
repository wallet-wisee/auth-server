package com.walletwise.authservice.v1.model.repository;

import com.walletwise.authservice.v1.core.config.annotations.Generated;
import com.walletwise.authservice.v1.model.entity.UserCredential;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Generated
@Repository
public interface IUserCredentialRepository extends MongoRepository<UserCredential, String> {
    Optional<UserCredential> findByEmail(String email);

    Optional<UserCredential> findByEmailAndValidationCode(String email, Integer validationCode);

}
