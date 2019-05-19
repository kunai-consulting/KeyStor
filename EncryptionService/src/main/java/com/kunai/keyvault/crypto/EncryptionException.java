
package com.kunai.keyvault.crypto;

public class EncryptionException extends RuntimeException {
    public EncryptionException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}