package com.kunai.keyvault.crypto.aws_kms;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyResult;
import com.kunai.keyvault.crypto.Encryptor;
import com.kunai.keyvault.crypto.aes.AES;
import com.kunai.keyvault.crypto.voltage.vibesimple.Fault;
import com.kunai.keyvault.crypto.voltage.vibesimple.FaultResponse;

import java.nio.ByteBuffer;

/**
 * Created by acooley on 8/31/16.
 */
public class KMSEncryptor implements Encryptor {

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
    public String protectSocialSecurityNumber(String socialSecurityNumber) throws FaultResponse {
        return protectGenericData(socialSecurityNumber);
    }

    /**
     * @param dataIn
     * @param format
     * @return returns java.lang.String
     * @throws FaultResponse
     */
    @Override
    public String protectFormattedData(String dataIn, String format) throws FaultResponse {
        return protectGenericData(dataIn);
    }

    /**
     * @param dataIn
     * @return returns byte[]
     * @throws FaultResponse
     */
    @Override
    public String protectGenericData(String dataIn) throws FaultResponse {
        //Get a new data key...
        GenerateDataKeyRequest dataKeyRequest = new GenerateDataKeyRequest();
        dataKeyRequest.setKeyId(keyId);
        dataKeyRequest.setKeySpec("AES_128");

        GenerateDataKeyResult dataKeyResult = kms.generateDataKey(dataKeyRequest);

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
            Fault fault = new Fault();
            fault.setErrorCode(500);
            throw new FaultResponse("Error doing AES encryption: " + e.getLocalizedMessage(), fault);
        }
    }

}
