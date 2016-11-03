package com.kunai.keyvault.hsm;

import com.kunai.keyvault.resources.params.DUKPTEncryption;

/**
 * Interface for an HSM supporting DUKPT encryption.
 *
 * Created by acooley on 9/19/15.
 */
public interface HardwareSecurityModule {

    /**
     * Get an initial PIN Encryption Key
     * @return the IPEK
     */
    public byte[] getIPEK();

    /**
     * Decrypt a secret encrypted by a DUKPT reader
     * @param encryption The encryption produced by the DUKPT reader
     * @return The decrypted secret (e.g. card number)
     */
    public byte[] decrypt(DUKPTEncryption encryption);
}
