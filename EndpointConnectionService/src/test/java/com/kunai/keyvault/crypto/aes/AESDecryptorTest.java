package com.kunai.keyvault.crypto.aes;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;


public class AESDecryptorTest {
    private static String testKey = "11CDEF0123456789";

    private AESDecryptor decryptor;

    @Before
    public void setUp() throws Exception {
        decryptor = new AESDecryptor(testKey.getBytes());
    }

    /**
     * Test to make sure that the service can card_data encrypt
     * @throws Exception
     */
    @Test
    public void testCardDataDecrypt() throws Exception {
        String data = "4012888888881881";
        String encryptedData = AES.encode(AES.encrypt(AES.pack(data),
                testKey.getBytes()));


        assertFalse(encryptedData.equals(data));
        assertThat(decryptor.accessFormattedData(encryptedData, null), is(data));
    }

    //TODO: Add tests for the rest of the APIs
}
