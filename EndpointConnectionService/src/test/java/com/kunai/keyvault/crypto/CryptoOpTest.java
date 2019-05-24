package com.kunai.keyvault.crypto;

import com.kunai.keyvault.config.EncryptionConfiguration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CryptoOpTest {
    private static final String TEST_ENCRYPT = "test";
    EncryptionComponent encryptionComponent;

    @Before
    public void init() {
        EncryptionConfiguration configuration = new EncryptionConfiguration();
        configuration.setType("aes");
        configuration.setKey("ABCDEF0123456780");
        encryptionComponent = new EncryptionComponent(configuration);
    }
    /**
     * Test to make sure that encryption/decryption produces an equivelent string
     * @throws Exception
     */
    @Test
    public void testSimpleEncryptAndDecrypt() throws Exception {
        String testString = "<ID>some sensitive data</ID> then some stuff then <ID>some sensitive data</ID>";
        CryptoOp idOp = new CryptoOp("(?<=<ID>).*?(?=</ID>)", TEST_ENCRYPT, encryptionComponent);
        assertTrue(idOp.decrypt(idOp.encrypt(testString)).contentEquals(testString));
    }

    /**
     * Test to make sure that you can encrypt multiple things with one encryption operation
     * @throws Exception
     */
    @Test
    public void testMultipleEncrypt() throws Exception {
        String testString1 = "<ID>some sensitive data</ID> then some stuff then <ID>some other sensitive data</ID>";
        String testString2 = "<ID>some sensitive data</ID>";
        String testString3 = " then some stuff then <ID>some other sensitive data</ID>";

        CryptoOp idOp = new CryptoOp("(?<=<ID>).*?(?=</ID>)", TEST_ENCRYPT, encryptionComponent);
        assertTrue(idOp.encrypt(testString1).contentEquals(idOp.encrypt(testString2) + idOp.encrypt(testString3)));
    }

    /**
     * Test to make sure that you can encrypt
     * @throws Exception
     */
    @Test
    public void testEncrypt() throws Exception {
        String testString1 = "<ID>some sensitive data</ID> then some stuff then <ID>some other sensitive data</ID>";

        CryptoOp idOp = new CryptoOp("(?<=<ID>).*?(?=</ID>)", TEST_ENCRYPT, encryptionComponent);
        assertTrue(idOp.encrypt(testString1).contentEquals("<ID>encrypted {some sensitive data}</ID> then some stuff then <ID>encrypted {some other sensitive data}</ID>"));
    }

    /**
     * Test to make sure that you can Decrypt
     * @throws Exception
     */
    @Test
    public void testDecrypt() throws Exception {
        String testString1 = "<ID>encrypted {some sensitive data}</ID> and then some data and then <CARD>encrypted {some card data}</CARD> and some more data and then <CARD>encrypted {some other card data}</CARD> and then finally <ID>encrypted {some other sensitive data}</ID> and the something.";

        CryptoOp idOp = new CryptoOp("(?<=<ID>).*?(?=</ID>)", TEST_ENCRYPT, encryptionComponent);
        assertTrue(idOp.decrypt(testString1).contentEquals("<ID>some sensitive data</ID> and then some data and then <CARD>encrypted {some card data}</CARD> and some more data and then <CARD>encrypted {some other card data}</CARD> and then finally <ID>some other sensitive data</ID> and the something."));
    }

    /**
     * Test to make sure that if we don't support the encryption operation type we throw an exception
     * @throws Exception
     */
    @Test(expected = EncryptionException.class)
    public void testEncryptionException() throws Exception {
        String testString = "<ID>some sensitive data</ID> then some stuff then <ID>some sensitive data</ID>";
        CryptoOp idOP = new CryptoOp("(?<=<ID>).*?(?=</ID>)", "unset", encryptionComponent);
        idOP.encrypt(testString);
    }

    /**
     * Test to make sure that if we don't support the decryption operation type we throw an exception
     * @throws Exception
     */
    @Test(expected = DecryptionException.class)
    public void testDecryptionException() throws Exception {
        String testString = "<ID>encrypted {some sensitive data}</ID> then some stuff then <ID>encrypted {some other sensitive data}</ID>";
        CryptoOp idOP = new CryptoOp("(?<=<ID>).*?(?=</ID>)", "unset", encryptionComponent);
        idOP.decrypt(testString);
    }
}
