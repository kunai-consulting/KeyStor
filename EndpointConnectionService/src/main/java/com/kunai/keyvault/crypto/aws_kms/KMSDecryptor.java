package com.kunai.keyvault.crypto.aws_kms;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.kunai.keyvault.config.HSM;
import com.kunai.keyvault.crypto.DecryptionException;
import com.kunai.keyvault.crypto.Decryptor;
import com.kunai.keyvault.crypto.aes.AES;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by acooley on 8/31/16.
 */
public class KMSDecryptor implements Decryptor {

    public String keyId;

    @Autowired
    private HSM hsmClient;

    public KMSDecryptor(String keyId) {
        this.keyId = keyId;
    }

    /**
     * Create an AWS KMS client.
     * @return
     */
    private AWSKMS getClient() {
        return AWSKMSClientBuilder.defaultClient();
    }

    /**
     * @param socialSecurityNumber
     * @return returns java.lang.String
     * @throws DecryptionException
     */
    @Override
    public String accessSocialSecurityNumber(String socialSecurityNumber) throws DecryptionException {
        return accessGenericData(socialSecurityNumber);
    }
    /**
     * @param dataIn
     * @param format
     * @return returns java.lang.String
     * @throws DecryptionException
     */
    @Override
    public String accessFormattedData(String dataIn, String format) throws DecryptionException {
        return accessGenericData(dataIn);
    }

    /**
     * @param dataIn
     * @return returns byte[]
     * @throws DecryptionException
     */
    @Override
    public String accessGenericData(String dataIn) throws DecryptionException {
        byte[] decodedData = AES.decode(dataIn);
        byte[] encryptedKeyBytes = Arrays.copyOfRange(decodedData,0, 151);
        byte[] originalEncrypted = Arrays.copyOfRange(decodedData, 151, decodedData.length);

        // Decrypt the encrypted key to get the original plain text key...
        ByteBuffer ciphertextBlob = ByteBuffer.wrap(encryptedKeyBytes);

        DecryptRequest req = new DecryptRequest().withCiphertextBlob(ciphertextBlob);
        ByteBuffer plainText = hsmClient.build().decrypt(req).getPlaintext();

        String ret;
        try {
            ret = new String(AES.unPackBytes(AES.decrypt(originalEncrypted, plainText.array())));
        } catch (Exception e) {
            e.printStackTrace();
            throw new DecryptionException("Error doing AES decryption: " + e.getLocalizedMessage(), e);
        }

        return ret;
    }
}
