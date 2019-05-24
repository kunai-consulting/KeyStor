package com.kunai.keyvault.crypto;

/**
 * Created by acooley on 8/27/16.
 */
public interface Encryptor {
    /**
     *
     * @param socialSecurityNumber a String representing a Social Security Number of a person.
     * @return
     *     returns java.lang.String
     * @throws EncryptionException if the socialSecurityNumber cannot be encoded.
     */
    public String protectSocialSecurityNumber(String socialSecurityNumber) throws EncryptionException;

    /**
     *
     * @param format A String representing the desired format of the dataIn parameter.
     * @param dataIn A String representing a value that has to be encrypted.
     * @return
     *     returns java.lang.String
     * @throws EncryptionException if the encoding fails.
     */
    public String protectFormattedData(String dataIn, String format) throws EncryptionException;

    /**
     *
     * @param dataIn a String representing a value that has to be encrypted
     * @return
     *     returns byte[]
     * @throws EncryptionException if the encoding fails.
     */
    public String protectGenericData(String dataIn) throws EncryptionException;

}
