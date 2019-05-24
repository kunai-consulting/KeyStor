package com.kunai.keyvault.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This method gets the values of hsmType and hsmUrl from the example.yml file
 */
@Configuration
@ConfigurationProperties(prefix="encryptor")
@Data
public class EncryptionConfiguration {
    private String type;
    private String key;
}
