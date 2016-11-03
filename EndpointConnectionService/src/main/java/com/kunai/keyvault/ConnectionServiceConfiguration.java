package com.kunai.keyvault;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kunai.keyvault.crypto.DecryptorFactory;
import com.kunai.keyvault.crypto.EncryptorFactory;
import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Configuration for the EndpointConnectionService
 */
public class ConnectionServiceConfiguration extends Configuration {
    @NotNull
    public EncryptorFactory encryptor;

    @NotNull
    public DecryptorFactory decryptor;

    @Valid
    @NotNull
    private HttpClientConfiguration httpClient = new HttpClientConfiguration();

    @JsonProperty("httpClient")
    public HttpClientConfiguration getHttpClientConfiguration() {
        return httpClient;
    }

    @JsonProperty("httpClient")
    public void setHttpClientConfiguration(HttpClientConfiguration httpClient) {
        this.httpClient = httpClient;
    }

}
