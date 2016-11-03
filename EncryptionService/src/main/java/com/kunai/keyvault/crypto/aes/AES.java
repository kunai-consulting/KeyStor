package com.kunai.keyvault.crypto.aes;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * Ths class contains the methods for encrypting, decrypting, pack, unpack bytes and String
 */

public class AES {

    /**
     * This method performs the encryption of a given String received in bytes.
     * @param bytes This is the original String
     * @param encryptionKey This is a value taken from example.yml in Encryptor Settings
     * @return an array of bytes that is the encrypted String.
     * @throws Exception if there's no way to get the Cipher, the SecretKey or the instance of the secureRandom.
     */
    public static byte[] encrypt(byte[] bytes, byte[] encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding","SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
        SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");

        //Generate the IV Randomly...
        byte[] iv = new byte[cipher.getBlockSize()];
        randomSecureRandom.nextBytes(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] originalEncrypted = cipher.doFinal(bytes);

        //Joining the IV and the encrypted bytes
        byte[] destination = new byte[originalEncrypted.length + iv.length];
        System.arraycopy(iv, 0, destination, 0, iv.length);
        System.arraycopy(originalEncrypted, 0, destination, iv.length, originalEncrypted.length);

        return destination;

    }

    /**
     *
     * @param cipherText it receives the array of bytes of the encrypted String
     * @param encryptionKey This is a value taken from example.yml in Encryptor Settings
     * @return the original decrypted String in a byte array format.
     * @throws Exception if Cipher or Secret Key is not generated
     */
    public static byte[] decrypt(byte[] cipherText, byte[] encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding","SunJCE");

        //Extract the IV
        byte[] iv = Arrays.copyOfRange(cipherText,0, 16);
        byte[] originalEncrypted = Arrays.copyOfRange(cipherText, 16, cipherText.length);

        SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] original = cipher.doFinal(originalEncrypted);

        return original;
    }

    /**
     * It takes the original String in a byte array and pack the bytes depending of the ASCII characters.
     * @param bytes The original String received in a byte[]
     * @return byte[]
     */
    public static byte[] pack(byte[] bytes) {
        int length = bytes.length + 4;
        int packedLength = length + (((length % 16) != 0) ? (16 - (length % 16)) : 0);
        byte[] result = new byte[packedLength];

        // First four bytes are the original length...
        result[0] = (byte) ((bytes.length & 0xFF000000) >> 24);
        result[1] = (byte) ((bytes.length & 0x00FF0000) >> 16);
        result[2] = (byte) ((bytes.length & 0x0000FF00) >> 8);
        result[3] = (byte) ((bytes.length & 0x000000FF) >> 0);

        //The rest is the data...
        System.arraycopy(bytes, 0, result, 4, bytes.length);

        return result;
    }

    /**
     *
     * @param string The original clear text String
     * @return byte[] the execution of pack function
     * @throws UnsupportedEncodingException if the indicated coding type is not recognized.
     */
    public static byte[] pack(String string) throws UnsupportedEncodingException {
        return pack(string.getBytes("UTF-8"));
    }

    /**
     *
     * @param bytes a byte[] taken from the encrypted array of bytes
     * @return byte[]
     */
    public static byte[] unPackBytes(byte[] bytes) {
        int length = ((bytes[0] & 0xff) << 24) | ((bytes[1] & 0xff) << 16) |
                ((bytes[2] & 0xff) << 8)  | (bytes[3] & 0xff);
        return Arrays.copyOfRange(bytes, 4, length + 4);
    }

    /**
     *
     * @param bytes a byte[] taken from the encrypted array of bytes
     * @return String containing the original decrypted value
     * @throws UnsupportedEncodingException if the indicated coding type is not recognized.
     */
    public static String unPackString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(unPackBytes(bytes), "UTF-8");
    }

    /**
     * Encode a byte array in base 64 and return it as a string
     * @param bytesToEncode The byte array you want as a string
     * @return The byte array as a string
     */
    public static String encode(byte[] bytesToEncode) {
        return new String(Base64.getEncoder().encode(bytesToEncode));
    }

    /**
     * Decode a base 64 byte array and return a byte array
     * @param stringToDecode The string to decode
     * @return A byte array representing the encoded string
     */
    public static byte[] decode(String stringToDecode) {
        return Base64.getDecoder().decode(stringToDecode);
    }
}