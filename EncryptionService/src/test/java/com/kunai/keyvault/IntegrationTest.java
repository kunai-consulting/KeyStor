package com.kunai.keyvault;

import com.kunai.keyvault.resources.params.DUKPTEncryption;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

/**
 *  Main class for performing the Integration Test in the healthcheck, encryption and decryption
 */

public class IntegrationTest {

    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-service-aes.yml");

    @ClassRule
    public static final DropwizardAppRule<EncryptionServiceConfiguration> RULE = new DropwizardAppRule<>(
            EncryptionServiceApplication.class, CONFIG_PATH);

    private Client client;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    /**
     * Test to make sure that the healthcheck is working
     * @throws Exception
     */
    @Test
    public void testHealthCheck() throws Exception {
        final Response response = client.target("http://localhost:8081/healthcheck")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    /**
     * Test to make sure that the service can test encrypt
     * @throws Exception
     */
    @Test
    public void testTestEncrypt() throws Exception {
        String data = "some sensitive data";
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/encrypt?type=test")
                .request()
                .post(Entity.text(data))
                .readEntity(String.class);
        assertTrue(responseBody.equals("encrypted {some sensitive data}"));
    }

    /**
     * Test to make sure that the service can card_data encrypt
     * @throws Exception
     */
    @Test
    public void testCardDataEncrypt() throws Exception {
        String data = "4012888888881881";
        Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/encrypt?type=card_data")
                .request()
                .post(Entity.text(data));

        assertThat(response.getStatus(), is(200));
    }

    /**
     * Test to make sure that the service can ssn encrypt
     * @throws Exception
     */
    @Test
    public void testSSNEncrypt() throws Exception {
        String data = "078-05-1120";
        Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/encrypt?type=ssn")
                .request()
                .post(Entity.text(data));

        assertThat(response.getStatus(), is(200));
    }

    /**
     * Test to make sure that the service can generic encrypt
     * @throws Exception
     */
    @Test
    public void testGenericEncrypt() throws Exception {
        String data = "some sensitive data";
        Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/encrypt?type=generic")
                .request()
                .post(Entity.text(data));

        assertThat(response.getStatus(), is(200));
    }

    /**
     * Test to make sure that the proxy can generic encrypt DUKPUT encrypted data
     * @throws Exception
     */
    @Test
    @Ignore("Future functionality.")
    public void testDUKPTEncrypt() throws Exception {
        DUKPTEncryption encryption = new DUKPTEncryption("test", "some sensitive data");
        Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/dukpttoencrypt?type=generic")
                .request()
                .post(Entity.json(encryption));

        assertThat(response.getStatus(), is(200));
    }

    /**
     * Test to make sure that the service can ssn encrypt DUKPUT encrypted data
     * @throws Exception
     */
    @Ignore("Future functionality.")
    @Test
    public void testDUKPTSSNEncrypt() throws Exception {
        String data = "078-05-1120";
        DUKPTEncryption encryption = new DUKPTEncryption("test", data);
        Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/dukpttoencrypt?type=ssn")
                .request()
                .post(Entity.json(encryption));

        assertThat(response.getStatus(), is(200));
    }

    /**
     * Test to make sure that the service can ssn encrypt DUKPUT encrypted data
     * @throws Exception
     */
    @Ignore("Future functionality.")
    @Test
    public void testDUKPTTokenize() throws Exception {
        String data = "4012888888881881";
        DUKPTEncryption encryption = new DUKPTEncryption("test", data);
        Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/dukpttoencrypt?type=card_data")
                .request()
                .post(Entity.json(encryption));

        assertThat(response.getStatus(), is(200));
    }
}
