package com.kunai.keyvault.crypto.aes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kunai.keyvault.crypto.Decryptor;
import com.kunai.keyvault.crypto.DecryptorFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by acooley on 9/7/16.
 */
@JsonTypeName("aes")
public class AESDecryptorFactory implements DecryptorFactory {
    @JsonProperty
    @NotNull
    String key;

    @Override
    public Decryptor createDecryptor() {
        AESDecryptor decryptor = new AESDecryptor();
        decryptor.encryptionKey = key.getBytes();
        return decryptor;
    }
}
