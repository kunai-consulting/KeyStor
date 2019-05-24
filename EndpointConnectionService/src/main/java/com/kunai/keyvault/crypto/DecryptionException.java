package com.kunai.keyvault.crypto;

public class DecryptionException extends RuntimeException {
    public DecryptionException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}