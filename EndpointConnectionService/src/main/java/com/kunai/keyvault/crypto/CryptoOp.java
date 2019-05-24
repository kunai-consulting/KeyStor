package com.kunai.keyvault.crypto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements an abstraction layer for Crypto operations used by the proxy service
 *
 * Created by acooley on 5/23/15.
 */
public class CryptoOp {
    private static final String FORMAT_FIRST_4_LAST_4_SST = "First 4 Last 4 SST";

    private static final String ENCRYPT_PREFIX = "encrypted {";
    private static final String ENCRYPT_POSTFIX = "}";

    private static final String TEST_ENCRYPT = "test";
    private static final String GENERIC_ENCRYPT = "generic";
    private static final String SSN_ENCRYPT = "ssn";
    private static final String PCI_CARD_MASKED_TOKENIZE = "card_data";

    private Pattern pattern;
    private String type = "unset";

    private EncryptionComponent encryptionComponent;

    /**
     * Create an encryption operation
     * @param regex Regular expression used to find the data
     * @param type Type of encryption.  Must be one of the public static ints associated with the class (e.g. TEST_ENCRYPT, PCI_CARD_MASKED_TOKENIZE)
     */
    public CryptoOp(String regex, String type, EncryptionComponent encryptionComponent) {
        this.encryptionComponent = encryptionComponent;
        this.pattern = Pattern.compile(regex);
        this.type = type;
    }

    /**
     * Create an encryption operation and set the type later
     * @param regex Regular expression used to find the data
     */
    public CryptoOp(String regex, EncryptionComponent encryptionComponent) {
        this.encryptionComponent = encryptionComponent;
        this.pattern = Pattern.compile(regex);
    }

    public String getRegex() {
        return pattern.toString();
    }

    /**
     * Get the type
     * @return The type of the crypto operation
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type
     * @param type The type of the crypto operation
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Run the operation to find strings matching the regular expression and encrypt
     * @param data The data containing strings that might need encryption
     * @return The data with the strings encrypted
     */
    public String encrypt(String data) throws EncryptionException {
        Matcher matcher = pattern.matcher(data);
        StringBuffer encryptedData = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(encryptedData, encryptOp(data.substring(matcher.start(), matcher.end()), type));
        }
        matcher.appendTail(encryptedData);

        return encryptedData.toString();
    }

    /**
     * Run the operation to find strings matching the regular expression and decrypt
     * @param data The data containing strings that might need decryption
     * @return The data with the strings decrypted
     * @throws DecryptionException
     */
    public String decrypt(String data) throws DecryptionException {
        Matcher matcher = pattern.matcher(data);
        StringBuffer decryptedData = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(decryptedData, decryptOp(data.substring(matcher.start(), matcher.end()), type));
        }
        matcher.appendTail(decryptedData);

        return decryptedData.toString();
    }

    /**
     * Encrypt using a particular type
     * @param unencrypted The unencrypted string to encrypt
     * @param type The type of encryption to perform
     * @return the encrypted string
     * @throws EncryptionException
     */
    private String encryptOp(String unencrypted, String type) throws EncryptionException {

        switch (type) {
            case TEST_ENCRYPT:
                return ENCRYPT_PREFIX + unencrypted + ENCRYPT_POSTFIX;

            case GENERIC_ENCRYPT:
                return encryptionComponent.encryptor.protectGenericData(unencrypted);

            case SSN_ENCRYPT:
                return encryptionComponent.encryptor.protectSocialSecurityNumber(unencrypted);

            case PCI_CARD_MASKED_TOKENIZE:
                return encryptionComponent.encryptor.protectFormattedData(unencrypted, FORMAT_FIRST_4_LAST_4_SST);

            default:
                throw new EncryptionException("Unknown encryption type during encryption",
                        new RuntimeException("Unknown encryption type during encryption"));
        }
    }

    /**
     * Decrypt using a particular type
     * @param encrypted The encrypted string to decrypt
     * @param type The type of decryption to perform
     * @return the decrypted string
     * @throws DecryptionException
     */
    private String decryptOp(String encrypted, String type) throws DecryptionException {

        switch (type) {
            case TEST_ENCRYPT:
                return encrypted.substring(ENCRYPT_PREFIX.length(), encrypted.length() - ENCRYPT_POSTFIX.length());
            case GENERIC_ENCRYPT:
                return encryptionComponent.decryptor.accessGenericData(encrypted);

            case SSN_ENCRYPT:
                return encryptionComponent.decryptor.accessSocialSecurityNumber(encrypted);

            case PCI_CARD_MASKED_TOKENIZE:
                return encryptionComponent.decryptor.accessFormattedData(encrypted, FORMAT_FIRST_4_LAST_4_SST);

            default:
                throw new DecryptionException("Unknown encryption type during decryption",
                        new RuntimeException("Unknown encryption type during decryption"));
        }
    }
}