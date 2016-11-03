package com.kunai.keyvault.crypto.aws_kms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kunai.keyvault.crypto.Encryptor;
import com.kunai.keyvault.crypto.EncryptorFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by acooley on 9/7/16.
 */
@JsonTypeName("kms")
public class KMSEncryptorFactory implements EncryptorFactory {
    @JsonProperty
    @NotNull
    String keyId;

    @Override
    public Encryptor createEncryptor() {
        KMSEncryptor encryptor = new KMSEncryptor();
        encryptor.keyId = keyId;
        return encryptor;
    }
}
