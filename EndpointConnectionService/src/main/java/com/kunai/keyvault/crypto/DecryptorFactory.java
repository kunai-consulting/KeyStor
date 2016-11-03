package com.kunai.keyvault.crypto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

/**
 * DecryptorFactory: Interface that uses JSON Serialization and Deserialization
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface DecryptorFactory extends Discoverable {
    Decryptor createDecryptor();
}
