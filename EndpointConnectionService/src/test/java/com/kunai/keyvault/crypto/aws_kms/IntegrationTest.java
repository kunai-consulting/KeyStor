package com.kunai.keyvault.crypto.aws_kms;

import com.kunai.keyvault.ConnectionServiceApplication;
import com.kunai.keyvault.ConnectionServiceConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import static org.junit.Assert.assertTrue;

public class IntegrationTest {

    private static final String FORMAT_FIRST_4_LAST_4_SST = "First 4 Last 4 SST";
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-service-kms.yml");

    @ClassRule
    public static final DropwizardAppRule<ConnectionServiceConfiguration> RULE = new DropwizardAppRule<>(
            ConnectionServiceApplication.class, CONFIG_PATH);

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
     * Test Credit Card Encryption and Decryption
     * @throws Exception
     */
    @Test
    public void testCreditCardEncryptDecrypt() throws Exception {
        String data = "<CARD>4012888888881881</CARD>";
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://httpbin.org/put")
                .header("encryption-regex1", "(?<=<CARD>).*?(?=</CARD>)")
                .header("encryption-type1", "card_data")
                .put(Entity.text(data))
                .readEntity(String.class);
        assertTrue(!responseBody.contains("4012888888881881"));

        responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://httpbin.org/put")
                .header("decryption-regex1", "(?<=<CARD>).*?(?=</CARD>)")
                .header("decryption-type1", "card_data")
                .put(Entity.text(responseBody))
                .readEntity(String.class);
        assertTrue(responseBody.contains(data));

    }

    /**
     * Test Generic Encryption and Decryption
     * @throws Exception
     */
    @Test
    public void testGenericEncryptDecrypt() throws Exception {
        String data = "<CARD>somedata</CARD>";
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://httpbin.org/put")
                .header("encryption-regex1", "(?<=<CARD>).*?(?=</CARD>)")
                .header("encryption-type1", "generic")
                .put(Entity.text(data))
                .readEntity(String.class);
        assertTrue(!responseBody.contains("somedata"));

        responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://httpbin.org/put")
                .header("decryption-regex1", "(?<=<CARD>).*?(?=</CARD>)")
                .header("decryption-type1", "generic")
                .put(Entity.text(responseBody))
                .readEntity(String.class);
        assertTrue(responseBody.contains(data));

    }

    /**
     * Test SSN Encryption and Decryption
     * @throws Exception
     */
    @Test
    public void testSSNEncryptDecrypt() throws Exception {
        String data = "<CARD>078-05-1120</CARD>";
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://httpbin.org/put")
                .header("encryption-regex1", "(?<=<CARD>).*?(?=</CARD>)")
                .header("encryption-type1", "ssn")
                .put(Entity.text(data))
                .readEntity(String.class);
        assertTrue(!responseBody.contains("somedata"));

        responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://httpbin.org/put")
                .header("decryption-regex1", "(?<=<CARD>).*?(?=</CARD>)")
                .header("decryption-type1", "ssn")
                .put(Entity.text(responseBody))
                .readEntity(String.class);
        assertTrue(responseBody.contains(data));

    }
}
