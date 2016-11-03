package com.kunai.keyvault.crypto.voltage;

import com.kunai.keyvault.crypto.Encryptor;
import com.kunai.keyvault.crypto.voltage.vibesimple.FaultResponse;
import com.kunai.keyvault.crypto.voltage.vibesimple.VibeSimple;
import com.kunai.keyvault.crypto.voltage.vibesimple.VibeSimple_Service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by acooley on 9/7/16.
 */
public class VoltageEncryptor implements Encryptor {
    public static String IDENTITY;
    public static String AUTHINFO;
    public static String WSDL;
    private static VibeSimple service;

    @Override
    public void init() {
        try {
            service = new VibeSimple_Service(new URL(WSDL)).getVibeSimpleSOAP();
        } catch (MalformedURLException e) {
            //TODO: determine proper error handling here
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * @param socialSecurityNumber
     * @return returns java.lang.String
     * @throws FaultResponse
     */
    @Override
    public String protectSocialSecurityNumber(String socialSecurityNumber) throws FaultResponse {
        return service.protectSocialSecurityNumber(socialSecurityNumber, IDENTITY, "", com.kunai.keyvault.crypto.voltage.vibesimple.AuthMethod.SHARED_SECRET, AUTHINFO);
    }

    /**
     * @param dataIn
     * @param format
     * @return returns java.lang.String
     * @throws FaultResponse
     */
    @Override
    public String protectFormattedData(String dataIn, String format) throws FaultResponse {
        return service.protectFormattedData(dataIn, format, IDENTITY, "", com.kunai.keyvault.crypto.voltage.vibesimple.AuthMethod.SHARED_SECRET, AUTHINFO);
    }

    /**
     * @param dataIn
     * @return returns byte[]
     * @throws FaultResponse
     */
    @Override
    public String protectGenericData(String dataIn) throws FaultResponse {
        return service.protectGenericStringData(dataIn, IDENTITY, false, com.kunai.keyvault.crypto.voltage.vibesimple.AuthMethod.SHARED_SECRET, AUTHINFO, "");
    }
}
