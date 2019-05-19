package com.kunai.keyvault.crypto.aws_kms;

public class KMSEncryptorTest {

    //TODO: Fix and re-enable with mocking
/*
    static {
    }


    private KMSEncryptor encryptor;

    @Before
    public void setUp() throws Exception {
        encryptor = new KMSEncryptor();
        encryptor.keyId = "arn:aws:kms:us-west-2:365647338134:key/1466ea1b-ed5f-433c-a4ea-fdad372df818";
        encryptor.init();
    }

    @After
    public void tearDown() throws Exception {
        encryptor.destroy();
    }

    */
/**
     * A utility function to decode and decrypt KMS encrypted data for testing
     * @param encryptedData The encoded, encrypted data to encrypt
     * @param encryptor An initialized KMS encryptor
     * @return The decrpted data as a byte array
     *//*

    public static byte[] kmsDecrypt(String encryptedData, KMSEncryptor encryptor) throws Exception {
        byte[] decodedData = AES.decode(encryptedData);
        byte[] encryptedKeyBytes = Arrays.copyOfRange(decodedData,0, 151);
        byte[] originalEncrypted = Arrays.copyOfRange(decodedData, 151, decodedData.length);

        // Decrypt the encrypted key to get the original plain text key...
        ByteBuffer ciphertextBlob = ByteBuffer.wrap(encryptedKeyBytes);

        DecryptRequest req = new DecryptRequest().withCiphertextBlob(ciphertextBlob);
        ByteBuffer plainText = encryptor.kms.decrypt(req).getPlaintext();

        return AES.decrypt(originalEncrypted, plainText.array());
    }

    */
/**
     * Test to make sure that the service can card_data encrypt
     * @throws Exception
     *//*

    @Test
    public void testCardDataEncrypt() throws Exception {
        String data = "4012888888881881";
        String encryptedData = encryptor.protectFormattedData(data, null);

        assertFalse(encryptedData.equals(data));

        assertThat(AES.unPackString(kmsDecrypt(encryptedData, encryptor)), is(data));
    }
*/

    //TODO: Add tests for the rest of the APIs
}
