package com.kunai.keyvault.crypto.aws_kms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kunai.keyvault.crypto.Decryptor;
import com.kunai.keyvault.crypto.DecryptorFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by acooley on 9/7/16.
 */
@JsonTypeName("kms")
public class KMSDecryptorFactory implements DecryptorFactory {
    @JsonProperty
    @NotNull
    String keyId;

    @Override
    public Decryptor createDecryptor() {
        KMSDecryptor decryptor = new KMSDecryptor();
        decryptor.keyId = keyId;
        return decryptor;
    }
}
