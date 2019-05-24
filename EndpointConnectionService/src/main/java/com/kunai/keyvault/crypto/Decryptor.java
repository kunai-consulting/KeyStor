package com.kunai.keyvault.crypto;

/**
 * Created by acooley on 8/27/16.
 */
public interface Decryptor {


    /**
     *
     * @param socialSecurityNumber an String representing a SSN
     * @return java.lang.String
     * @throws DecryptionException if getting the info fails.
     *
     */
    public String accessSocialSecurityNumber(String socialSecurityNumber) throws DecryptionException;

    /**
     *
     * @param format the format of the original string
     * @param dataIn the original String
     * @return java.lang.String
     * @throws DecryptionException
     */
    public String accessFormattedData(String dataIn, String format) throws DecryptionException;

    /**
     *
     * @param dataIn
     * @return byte[]
     * @throws DecryptionException
     */
    public String accessGenericData(String dataIn) throws DecryptionException;

}
