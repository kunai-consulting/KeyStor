package com.kunai.keyvault.crypto.aes;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class AESTest {
    static String paddedtext = "test text 123\0\0\0"; /*Note null padding*/
    static byte[] key = new String("0123456789abcdef").getBytes();
    static String unpaddedtext = "test text 123";

    /**
     * Test encrypt/decrypt
     * @throws Exception
     */
    @Test
    public void testEncryptDecrypt() throws Exception {
        byte[] encryptedData = AES.encrypt(paddedtext.getBytes("UTF-8"), key);

        assertFalse(new String(encryptedData, "UTF-8").contains(paddedtext));
        assertThat(new String(AES.decrypt(encryptedData, key)), is(paddedtext));
    }

    /**
     * Test string packing
     * @throws Exception
     */
    @Test
    public void testPackString() throws Exception {
        byte[] bytes = AES.pack(unpaddedtext);
        assertTrue("Original String Length was missing", bytes[3] == 13);
        assertThat("Original String was missing", new String(Arrays.copyOfRange(bytes, 4, 17), "UTF-8"), is(unpaddedtext));
        assertThat("Array was correctly padded", bytes.length, is(32));
    }

    /**
     * Test byte packing
     * @throws Exception
     */
    @Test
    public void testPackBytes() throws Exception {
        byte[] bytes = AES.pack(unpaddedtext.getBytes("UTF-8"));
        assertTrue("Original String Length was missing", bytes[3] == 13);
        assertThat("Original String was missing", new String(Arrays.copyOfRange(bytes, 4, 17), "UTF-8"), is(unpaddedtext));
        assertThat("Array was correctly padded", bytes.length, is(32));
    }

    /**
     * Test unpacking
     * @throws Exception
     */
    @Test
    public void testUnpackBytes() throws Exception {
        assertThat(new String(AES.unPackBytes(AES.pack(unpaddedtext)), "UTF-8"), is(unpaddedtext));
    }

    /**
     * Test unpacking
     * @throws Exception
     */
    @Test
    public void testUnpackString() throws Exception {
        assertThat(AES.unPackString(AES.pack(unpaddedtext)), is(unpaddedtext));
    }

    /**
     * Test encrypt/decrypt with packing/unpacking
     * @throws Exception
     */
    @Test
    public void testEncryptDecryptWithPackUnpack() throws Exception {
        assertThat((AES.unPackString(AES.decrypt(AES.encrypt(AES.pack(unpaddedtext),
                key), key))), is(unpaddedtext));
    }
}
