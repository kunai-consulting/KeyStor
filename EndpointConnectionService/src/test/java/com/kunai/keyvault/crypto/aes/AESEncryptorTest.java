package com.kunai.keyvault.crypto.aes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;


public class AESEncryptorTest {

    static {
    }


    private AESEncryptor encryptor;

    @Before
    public void setUp() throws Exception {
        encryptor = new AESEncryptor();
        encryptor.init();
    }

    @After
    public void tearDown() throws Exception {
        encryptor.destroy();
    }

    /**
     * Test to make sure that the service can card_data encrypt
     * @throws Exception
     */
    @Test
    public void testCardDataEncrypt() throws Exception {
        String data = "4012888888881881";
        String encryptedData = encryptor.protectFormattedData(data, null);

        assertFalse(encryptedData.equals(data));
        assertThat(AES.unPackString(AES.decrypt(AES.decode(encryptedData), encryptor.encryptionKey)), is(data));
    }

    //TODO: Add tests for the rest of the APIs
}
