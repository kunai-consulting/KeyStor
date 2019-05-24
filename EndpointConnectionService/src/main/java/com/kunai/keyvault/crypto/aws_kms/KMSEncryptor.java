package com.kunai.keyvault.crypto.aws_kms;

import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyResult;
import com.kunai.keyvault.config.HSM;
import com.kunai.keyvault.crypto.EncryptionException;
import com.kunai.keyvault.crypto.Encryptor;
import com.kunai.keyvault.crypto.aes.AES;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.ByteBuffer;

/**
 * Created by acooley on 8/31/16.
 */
public class KMSEncryptor implements Encryptor {

    public String keyId;

    @Autowired
    private HSM hsmClient;

    public KMSEncryptor(String keyId) {
        this.keyId = keyId;
    }

    /**
     * @param socialSecurityNumber a String that represents a Social Security Number
     * @return returns java.lang.String
     * @throws EncryptionException if the encoding can't be done.
     */
    @Override
    public String protectSocialSecurityNumber(String socialSecurityNumber) throws EncryptionException {
        return protectGenericData(socialSecurityNumber);
    }

    /**
     * @param dataIn a String representing a value that has to be encrypted.
     * @param format a String representing the desired format of the dataIn parameter.
     * @return returns java.lang.String
     * @throws EncryptionException if the encoding can't be done.
     */
    @Override
    public String protectFormattedData(String dataIn, String format) throws EncryptionException {
        return protectGenericData(dataIn);
    }

    /**
     * @param dataIn a String representing a value that has to be encrypted.
     * @return returns byte[]
     * @throws EncryptionException if the encoding can't be done.
     */
    @Override
    public String protectGenericData(String dataIn) throws EncryptionException {
        //Get a new data key...
        GenerateDataKeyRequest dataKeyRequest = new GenerateDataKeyRequest();
        dataKeyRequest.setKeyId(keyId);
        dataKeyRequest.setKeySpec("AES_128");

        GenerateDataKeyResult dataKeyResult = hsmClient.build().generateDataKey(dataKeyRequest);

        ByteBuffer plaintextKey = dataKeyResult.getPlaintext();
        ByteBuffer encryptedKey = dataKeyResult.getCiphertextBlob();

        try {
            byte[] originalEncrypted = AES.encrypt(AES.pack(dataIn), plaintextKey.array());
            //Joining the encryptedKey and the encrypted bytes
            byte[] encryptedKeyBytes = encryptedKey.array();
            byte[] destination = new byte[originalEncrypted.length + encryptedKeyBytes.length];
            System.arraycopy(encryptedKeyBytes, 0, destination, 0, encryptedKeyBytes.length);
            System.arraycopy(originalEncrypted, 0, destination, encryptedKeyBytes.length, originalEncrypted.length);

            return AES.encode(destination);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptionException("Error doing AES encryption: " + e.getLocalizedMessage(), e);
        }
    }

}
