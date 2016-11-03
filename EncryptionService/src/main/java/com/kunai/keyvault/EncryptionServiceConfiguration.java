package com.kunai.keyvault;

import com.kunai.keyvault.crypto.EncryptorFactory;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;


/**
 * This method gets the values of hsmType and hsmUrl from the example.yml file
 */
public class EncryptionServiceConfiguration extends Configuration {
    //  Future use
    public String hsmType;
    public String hsmUrl;

    @NotNull
    public EncryptorFactory encryptor;

}
