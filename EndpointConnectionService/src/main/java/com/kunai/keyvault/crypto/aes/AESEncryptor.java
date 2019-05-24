package com.kunai.keyvault.crypto.aes;

import com.kunai.keyvault.crypto.EncryptionException;
import com.kunai.keyvault.crypto.Encryptor;

/**
 * Created by acooley on 8/31/16.
 */
public class AESEncryptor implements Encryptor {

    private byte[] encryptionKey = new String("0123456789abcdef").getBytes();

    public AESEncryptor(byte[] encryptionKey) {
        this.encryptionKey = encryptionKey;
    }


    /**
     * @param socialSecurityNumber a String representing a Social Security Number of a person.
     * @return returns java.lang.String
     * @throws EncryptionException If the encryption can't be done
     */
    @Override
    public String protectSocialSecurityNumber(String socialSecurityNumber) throws EncryptionException {
        return protectGenericData(socialSecurityNumber);
    }

    /**
     * @param dataIn a String representing a value that has to be encrypted.
     * @param format a String representing a desired format for the dataIn String.
     * @return returns java.lang.String
     * @throws EncryptionException If the encryption can't be done
     */
    @Override
    public String protectFormattedData(String dataIn, String format) throws EncryptionException {
        return protectGenericData(dataIn);
    }

    /**
     * @param dataIn a String representing a value that has to be encrypted.
     * @return returns byte[]
     * @throws EncryptionException If the encryption can't be done
     */
    @Override
    public String protectGenericData(String dataIn) throws EncryptionException {
        String ret = null;
        try {
            ret = AES.encode(AES.encrypt(AES.pack(dataIn), encryptionKey));
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptionException("Error doing AES encryption: " + e.getLocalizedMessage(), e);
        }

        return ret;
    }
}
