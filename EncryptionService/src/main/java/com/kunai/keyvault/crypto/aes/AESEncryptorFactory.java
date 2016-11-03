package com.kunai.keyvault.crypto.aes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kunai.keyvault.crypto.Encryptor;
import com.kunai.keyvault.crypto.EncryptorFactory;

import javax.validation.constraints.NotNull;


/**
 * Created by acooley on 9/7/16.
 */
@JsonTypeName("aes")
public class AESEncryptorFactory implements EncryptorFactory {
    @JsonProperty
    @NotNull
    String key;

    /**
     *
     * @return an object of type AESEncryptor
     */
    @Override
    public Encryptor createEncryptor() {
        AESEncryptor encryptor = new AESEncryptor();
        encryptor.encryptionKey = key.getBytes();
        return encryptor;
    }
}
