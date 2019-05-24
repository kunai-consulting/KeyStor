package com.kunai.keyvault.crypto.aes;

import com.kunai.keyvault.crypto.DecryptionException;
import com.kunai.keyvault.crypto.Decryptor;

/**
 * Created by acooley on 8/31/16.
 */
public class AESDecryptor implements Decryptor {

    //TODO: Implement key rotation
    private byte[] encryptionKey = new String("0123456789abcdef").getBytes();

    public AESDecryptor(byte[] key) {
        encryptionKey = key;
    }

    /**
     * @param socialSecurityNumber a String representing the SSN of a person.
     * @return java.lang.String
     * @throws DecryptionException if getting tha data fails.
     */
    @Override
    public String accessSocialSecurityNumber(String socialSecurityNumber) throws DecryptionException {
        return accessGenericData(socialSecurityNumber);
    }
    /**
     * @param dataIn String input data.
     * @param format String format
     * @return returns java.lang.String
     * @throws DecryptionException if getting tha data fails.
     */
    @Override
    public String accessFormattedData(String dataIn, String format) throws DecryptionException {
        return accessGenericData(dataIn);
    }

    /**
     * @param dataIn String input data.
     * @return returns byte[]
     * @throws DecryptionException if getting tha data fails.
     */
    @Override
    public String accessGenericData(String dataIn) throws DecryptionException {
        String ret = null;
        try {
            ret = new String(AES.unPackBytes(AES.decrypt(AES.decode(dataIn), encryptionKey)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecryptionException("Error doing AES decryption: " + e.getLocalizedMessage(), e);
        }

        return ret;
    }
}
