package com.kunai.keyvault.crypto;

import com.kunai.keyvault.crypto.voltage.vibesimple.FaultResponse;

/**
 * Created by acooley on 8/27/16.
 */
public interface Decryptor {

    public void init();
    public void destroy();


    /**
     *
     * @param socialSecurityNumber an String representing a SSN
     * @return java.lang.String
     * @throws FaultResponse if getting the info fails.
     *
     */
    public String accessSocialSecurityNumber(String socialSecurityNumber) throws FaultResponse;

    /**
     *
     * @param format the format of the original string
     * @param dataIn the original String
     * @return java.lang.String
     * @throws FaultResponse
     */
    public String accessFormattedData(String dataIn, String format) throws FaultResponse;

    /**
     *
     * @param dataIn
     * @return byte[]
     * @throws FaultResponse
     */
    public String accessGenericData(String dataIn) throws FaultResponse;

}
