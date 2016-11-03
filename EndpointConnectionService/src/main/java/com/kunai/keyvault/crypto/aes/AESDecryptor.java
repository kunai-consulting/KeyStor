package com.kunai.keyvault.crypto.aes;

import com.kunai.keyvault.crypto.Decryptor;
import com.kunai.keyvault.crypto.voltage.vibesimple.Fault;
import com.kunai.keyvault.crypto.voltage.vibesimple.FaultResponse;

/**
 * Created by acooley on 8/31/16.
 */
public class AESDecryptor implements Decryptor {

    //TODO: Implement key rotation
    public byte[] encryptionKey = new String("0123456789abcdef").getBytes();

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }


    /**
     * @param socialSecurityNumber a String representing the SSN of a person.
     * @return java.lang.String
     * @throws FaultResponse if getting tha data fails.
     */
    @Override
    public String accessSocialSecurityNumber(String socialSecurityNumber) throws FaultResponse {
        return accessGenericData(socialSecurityNumber);
    }
    /**
     * @param dataIn String input data.
     * @param format String format
     * @return returns java.lang.String
     * @throws FaultResponse if getting tha data fails.
     */
    @Override
    public String accessFormattedData(String dataIn, String format) throws FaultResponse {
        return accessGenericData(dataIn);
    }

    /**
     * @param dataIn String input data.
     * @return returns byte[]
     * @throws FaultResponse if getting tha data fails.
     */
    @Override
    public String accessGenericData(String dataIn) throws FaultResponse {
        String ret = null;
        try {
            ret = new String(AES.unPackBytes(AES.decrypt(AES.decode(dataIn), encryptionKey)));
        } catch (Exception e) {
            e.printStackTrace();
            Fault fault = new Fault();
            fault.setErrorCode(500);
            throw new FaultResponse("Error doing AES decryption: " + e.getLocalizedMessage(), fault);
        }

        return ret;
    }
}
