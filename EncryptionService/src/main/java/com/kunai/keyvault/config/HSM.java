package com.kunai.keyvault.config;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HSM {

    public AWSKMS build() {
        return AWSKMSClientBuilder.defaultClient();
    }

}