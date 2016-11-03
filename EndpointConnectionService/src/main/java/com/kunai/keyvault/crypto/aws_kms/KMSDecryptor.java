package com.kunai.keyvault.crypto.aws_kms;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.kunai.keyvault.crypto.Decryptor;
import com.kunai.keyvault.crypto.aes.AES;
import com.kunai.keyvault.crypto.voltage.vibesimple.Fault;
import com.kunai.keyvault.crypto.voltage.vibesimple.FaultResponse;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by acooley on 8/31/16.
 */
public class KMSDecryptor implements Decryptor {

    public String keyId;
    protected AWSKMS kms;


    @Override
    public void init() {
        kms = getClient();
    }

    @Override
    public void destroy() {

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
     * @throws FaultResponse
     */
    @Override
    public String accessSocialSecurityNumber(String socialSecurityNumber) throws FaultResponse {
        return accessGenericData(socialSecurityNumber);
    }
    /**
     * @param dataIn
     * @param format
     * @return returns java.lang.String
     * @throws FaultResponse
     */
    @Override
    public String accessFormattedData(String dataIn, String format) throws FaultResponse {
        return accessGenericData(dataIn);
    }

    /**
     * @param dataIn
     * @return returns byte[]
     * @throws FaultResponse
     */
    @Override
    public String accessGenericData(String dataIn) throws FaultResponse {
        byte[] decodedData = AES.decode(dataIn);
        byte[] encryptedKeyBytes = Arrays.copyOfRange(decodedData,0, 151);
        byte[] originalEncrypted = Arrays.copyOfRange(decodedData, 151, decodedData.length);

        // Decrypt the encrypted key to get the original plain text key...
        ByteBuffer ciphertextBlob = ByteBuffer.wrap(encryptedKeyBytes);

        DecryptRequest req = new DecryptRequest().withCiphertextBlob(ciphertextBlob);
        ByteBuffer plainText = kms.decrypt(req).getPlaintext();

        String ret;
        try {
            ret = new String(AES.unPackBytes(AES.decrypt(originalEncrypted, plainText.array())));
        } catch (Exception e) {
            e.printStackTrace();
            Fault fault = new Fault();
            fault.setErrorCode(500);
            throw new FaultResponse("Error doing AES decryption: " + e.getLocalizedMessage(), fault);
        }

        return ret;
    }
}
