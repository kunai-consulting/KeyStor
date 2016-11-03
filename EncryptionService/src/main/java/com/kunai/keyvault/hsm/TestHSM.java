package com.kunai.keyvault.hsm;

import com.kunai.keyvault.resources.params.DUKPTEncryption;

/**
 * This class implementes a test interface that mocs out the HSM
 * Created by acooley on 9/19/15.
 */
public class TestHSM implements HardwareSecurityModule{
    /**
     * return bytes that say 'TESTINGTESTING123'
     *
     * @return the IPEK
     */
    @Override
    public byte[] getIPEK() {
        return new String("TESTINGTESTING123").getBytes();
    }

    /**
     * Just return the cryptogram
     *
     * @param encryption The encryption produced by the DUKPT reader
     * @return The decrypted secret (e.g. card number)
     */
    @Override
    public byte[] decrypt(DUKPTEncryption encryption) {
        return encryption.getCryptogram().getBytes();
    }
}
