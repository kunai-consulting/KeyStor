package com.kunai.keyvault.crypto.aes;

import com.kunai.keyvault.crypto.Encryptor;
import com.kunai.keyvault.crypto.voltage.vibesimple.Fault;
import com.kunai.keyvault.crypto.voltage.vibesimple.FaultResponse;

/**
 * Created by acooley on 8/31/16.
 */
public class AESEncryptor implements Encryptor {

    public byte[] encryptionKey = new String("0123456789abcdef").getBytes();

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }


    /**
     * @param socialSecurityNumber a String representing a Social Security Number of a person.
     * @return returns java.lang.String
     * @throws FaultResponse if the Encoding fails.
     */
    @Override
    public String protectSocialSecurityNumber(String socialSecurityNumber) throws FaultResponse {
        return protectGenericData(socialSecurityNumber);
    }

    /**
     * @param dataIn a String representing a value that has to be encrypted.
     * @param format a String representing a desired format for the dataIn String.
     * @return returns java.lang.String
     * @throws FaultResponse if the encoding fails.
     */
    @Override
    public String protectFormattedData(String dataIn, String format) throws FaultResponse {
        return protectGenericData(dataIn);
    }

    /**
     * @param dataIn a String representing a value that has to be encrypted.
     * @return returns byte[]
     * @throws FaultResponse if the encoding fails.
     */
    @Override
    public String protectGenericData(String dataIn) throws FaultResponse {
        String ret = null;
        try {
            ret = AES.encode(AES.encrypt(AES.pack(dataIn), encryptionKey));
        } catch (Exception e) {
            e.printStackTrace();
            Fault fault = new Fault();
            fault.setErrorCode(500);
            throw new FaultResponse("Error doing AES encryption: " + e.getLocalizedMessage(), fault);
        }

        return ret;
    }
}
