package com.kunai.keyvault.crypto.voltage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kunai.keyvault.crypto.Encryptor;
import com.kunai.keyvault.crypto.EncryptorFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by acooley on 9/7/16.
 */
@JsonTypeName("voltage")
public class VoltageEncryptorFactory implements EncryptorFactory {
    @JsonProperty
    @NotNull
    String voltageIdentity;

    @JsonProperty
    @NotNull
    String voltageAuthInfo;

    @JsonProperty
    @NotNull
    String voltageWsdlUrl;

    @Override
    public Encryptor createEncryptor() {
        VoltageEncryptor encryptor = new VoltageEncryptor();

        VoltageEncryptor.IDENTITY = voltageIdentity;
        VoltageEncryptor.AUTHINFO = voltageAuthInfo;
        VoltageEncryptor.WSDL = voltageWsdlUrl;

        return encryptor;
    }
}
