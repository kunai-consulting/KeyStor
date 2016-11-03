package com.kunai.keyvault.crypto.aws_kms;

import com.kunai.keyvault.EncryptionServiceApplication;
import com.kunai.keyvault.EncryptionServiceConfiguration;
import com.kunai.keyvault.crypto.aes.AES;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import static com.kunai.keyvault.crypto.aws_kms.KMSEncryptorTest.kmsDecrypt;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntegrationTest {

    private static final String FORMAT_FIRST_4_LAST_4_SST = "First 4 Last 4 SST";
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-service-kms.yml");

    @ClassRule
    public static final DropwizardAppRule<EncryptionServiceConfiguration> RULE = new DropwizardAppRule<>(
            EncryptionServiceApplication.class, CONFIG_PATH);

    private Client client;
    private KMSEncryptor encryptor;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
        encryptor = (KMSEncryptor) RULE.getConfiguration().encryptor.createEncryptor();
        encryptor.init();
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

        assertThat(AES.unPackString(kmsDecrypt(responseBody, encryptor)), is(data));
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

        assertThat(AES.unPackString(kmsDecrypt(responseBody, encryptor)), is(data));
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

        assertThat(AES.unPackString(kmsDecrypt(responseBody, encryptor)), is(data));
    }
}
