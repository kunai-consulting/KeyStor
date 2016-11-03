package com.kunai.keyvault.crypto.aws_kms;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;


public class KMSDecryptorTest {

    static {
    }


    private KMSDecryptor decryptor;
    private KMSEncryptor encryptor;

    @Before
    public void setUp() throws Exception {
        decryptor = new KMSDecryptor();
        decryptor.keyId = "arn:aws:kms:us-west-2:365647338134:key/1466ea1b-ed5f-433c-a4ea-fdad372df818";
        decryptor.init();

        encryptor = new KMSEncryptor();
        encryptor.keyId = "arn:aws:kms:us-west-2:365647338134:key/1466ea1b-ed5f-433c-a4ea-fdad372df818";
        encryptor.init();
    }

    @After
    public void tearDown() throws Exception {
        decryptor.destroy();
    }

    /**
     * Test to make sure that the service can card_data encrypt
     * @throws Exception
     */
    @Test
    public void testCardDataDecrypt() throws Exception {
        String data = "4012888888881881";
        String encryptedData = encryptor.protectFormattedData(data, null);


        assertFalse(encryptedData.equals(data));
        assertThat(decryptor.accessFormattedData(encryptedData, null), is(data));
    }
}
