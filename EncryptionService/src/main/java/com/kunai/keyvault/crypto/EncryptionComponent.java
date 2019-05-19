package com.kunai.keyvault.crypto;

import com.kunai.keyvault.config.EncryptionConfiguration;
import com.kunai.keyvault.crypto.aes.AESEncryptor;
import com.kunai.keyvault.crypto.aws_kms.KMSEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by acooley on 9/7/16.
 */
@Component
@Scope("singleton")
public class EncryptionComponent {
    public Encryptor encryptor;

    @Autowired
    public EncryptionComponent(EncryptionConfiguration configuration) {
        if (configuration.getType().trim().toLowerCase().equals("kms")) {
            encryptor = new KMSEncryptor(configuration.getKey());
        }
        else {
            encryptor = new AESEncryptor(configuration.getKey().getBytes());
        }
    }
}
