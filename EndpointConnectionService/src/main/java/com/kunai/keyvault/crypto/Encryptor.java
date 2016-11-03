package com.kunai.keyvault.crypto;

import com.kunai.keyvault.crypto.voltage.vibesimple.FaultResponse;

/**
 * Created by acooley on 8/27/16.
 */
public interface Encryptor {

    public void init();
    public void destroy();


    /**
     *
     * @param socialSecurityNumber
     * @return
     *     returns java.lang.String
     * @throws FaultResponse
     */
    public String protectSocialSecurityNumber(String socialSecurityNumber) throws FaultResponse;

    /**
     *
     * @param format
     * @param dataIn
     * @return
     *     returns java.lang.String
     * @throws FaultResponse
     */
    public String protectFormattedData(String dataIn, String format) throws FaultResponse;

    /**
     *
     * @param dataIn
     * @return
     *     returns byte[]
     * @throws FaultResponse
     */
    public String protectGenericData(String dataIn) throws FaultResponse;

}
