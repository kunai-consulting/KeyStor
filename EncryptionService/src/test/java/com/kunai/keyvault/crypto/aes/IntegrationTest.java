package com.kunai.keyvault.crypto.aes;

import com.kunai.keyvault.EncryptionServiceApplication;
import com.kunai.keyvault.EncryptionServiceConfiguration;
import com.kunai.keyvault.resources.params.DUKPTEncryption;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntegrationTest {

    private static final String FORMAT_FIRST_4_LAST_4_SST = "First 4 Last 4 SST";
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-service-aes.yml");

    @ClassRule
    public static final DropwizardAppRule<EncryptionServiceConfiguration> RULE = new DropwizardAppRule<>(
            EncryptionServiceApplication.class, CONFIG_PATH);

    private Client client;
    private AESEncryptor encryptor;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
        encryptor = (AESEncryptor) RULE.getConfiguration().encryptor.createEncryptor();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    /**
     * Test to make sure that the service can card_data encrypt
     * @throws Exception
     */
    @Test
    public void testCardDataEncrypt() throws Exception {
        String data = "4012888888881881";
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/encrypt?type=card_data")
                .request()
                .post(Entity.text(data))
                .readEntity(String.class);

        assertThat(AES.unPackString(AES.decrypt(AES.decode(responseBody), encryptor.encryptionKey)), is(data));
    }

    /**
     * Test to make sure that the service can ssn encrypt
     * @throws Exception
     */
    @Test
    public void testSSNEncrypt() throws Exception {
        String data = "078-05-1120";
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/encrypt?type=ssn")
                .request()
                .post(Entity.text(data))
                .readEntity(String.class);

        assertThat(AES.unPackString(AES.decrypt(AES.decode(responseBody), encryptor.encryptionKey)), is(data));
    }

    /**
     * Test to make sure that the service can generic encrypt
     * @throws Exception
     */
    @Test
    public void testGenericEncrypt() throws Exception {
        String data = "some sensitive data";
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/encrypt?type=generic")
                .request()
                .post(Entity.text(data))
                .readEntity(String.class);

        assertThat(AES.unPackString(AES.decrypt(AES.decode(responseBody), encryptor.encryptionKey)), is(data));
    }

    /**
     * Test to make sure that the proxy can generic encrypt DUKPUT encrypted data
     * @throws Exception
     */
    @Test
    @Ignore("Future functionality.")
    public void testDUKPTEncrypt() throws Exception {
        DUKPTEncryption encryption = new DUKPTEncryption("test", "some sensitive data");
        byte[] responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/dukpttoencrypt")
                .request()
                .post(Entity.json(encryption))
                .readEntity(byte[].class);

        assertThat(AES.unPackString(AES.decrypt(responseBody, encryptor.encryptionKey)), is("some sensitive data"));
    }

    /**
     * Test to make sure that the service can ssn encrypt DUKPUT encrypted data
     * @throws Exception
     */
    @Test
    @Ignore("Future functionality.")
    public void testDUKPTSSNEncrypt() throws Exception {
        String data = "078-05-1120";
        DUKPTEncryption encryption = new DUKPTEncryption("test", data);
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/dukpttoencrypt?type=ssn")
                .request()
                .post(Entity.json(encryption))
                .readEntity(String.class);

        assertThat(AES.unPackString(AES.decrypt(AES.decode(responseBody), encryptor.encryptionKey)), is(data));
    }

    /**
     * Test to make sure that the service can ssn encrypt DUKPUT encrypted data
     * @throws Exception
     */
    @Test
    @Ignore("Future functionality.")
    public void testDUKPTTokenize() throws Exception {
        String data = "4012888888881881";
        DUKPTEncryption encryption = new DUKPTEncryption("test", data);
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/dukpttoencrypt?type=card_data")
                .request()
                .post(Entity.json(encryption))
                .readEntity(String.class);

        assertThat(AES.unPackString(AES.decrypt(AES.decode(responseBody), encryptor.encryptionKey)), is(data));
    }
}
