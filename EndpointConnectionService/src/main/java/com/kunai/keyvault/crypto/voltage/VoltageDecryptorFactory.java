package com.kunai.keyvault.crypto.voltage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kunai.keyvault.crypto.Decryptor;
import com.kunai.keyvault.crypto.DecryptorFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by acooley on 9/7/16.
 */
@JsonTypeName("voltage")
public class VoltageDecryptorFactory implements DecryptorFactory {
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
    public Decryptor createDecryptor() {
        VoltageDecryptor decryptor = new VoltageDecryptor();

        VoltageDecryptor.IDENTITY = voltageIdentity;
        VoltageDecryptor.AUTHINFO = voltageAuthInfo;
        VoltageDecryptor.WSDL = voltageWsdlUrl;

        return decryptor;
    }
}
