package com.kunai.keyvault.resources.params;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object represents DUKPT encrypted data.
 *
 * Created by acooley on 8/26/15.
 */
public class DUKPTEncryption {
    private String ksn;
    private String cryptogram;

    public DUKPTEncryption(String ksn, String cryptogram) {
        this.ksn = ksn;
        this.cryptogram = cryptogram;
    }

    public DUKPTEncryption() {
        this.ksn = ksn;
        this.cryptogram = cryptogram;
    }

    @JsonProperty
    public String getKsn() {
        return ksn;
    }

    @JsonProperty
    public void setKsn(String ksn) {
        this.ksn = ksn;
    }

    @JsonProperty
    public String getCryptogram() {
        return cryptogram;
    }

    @JsonProperty
    public void setCryptogram(String cryptogram) {
        this.cryptogram = cryptogram;
    }
}
