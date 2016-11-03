package com.kunai.keyvault.crypto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

/**
 * Created by acooley on 9/7/16.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface EncryptorFactory extends Discoverable {
    Encryptor createEncryptor();
}
