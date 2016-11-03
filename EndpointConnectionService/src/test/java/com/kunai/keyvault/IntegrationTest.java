package com.kunai.keyvault;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class IntegrationTest {

    static {
        disableSslVerification();
    }

    /**
     * Disable SSL Verification since the cert that we have installed on Voltage in the test environment is broken...
     */
    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-service-aes.yml");
    private static final String TEST_IMAGE_FILE_NAME = "test.jpg";
    private static final String TEST_IMAGE_PATH = ResourceHelpers.resourceFilePath(TEST_IMAGE_FILE_NAME);

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
     * Test to make sure that the proxy can proxy a redirect
     * @throws Exception
     */
    @Test
    public void testPassThrough() throws Exception {
        final String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://www.google.com")
                .get(String.class);
        assertTrue(responseBody.toLowerCase().contains("google"));
    }

    /**
     * Test to make sure that the proxy can proxy a redirect
     * @throws Exception
     */
    @Test
    public void testPassThroughRedirect() throws Exception {
        final Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://yahoo.com")
                .get();
        assertThat(response.getStatus(), is(301));
    }

    /**
     * Test to make sure that the proxy can proxy a redirect
     * @throws Exception
     */
    @Test
    public void testPassThroughPost() throws Exception {
        String data = "somedata";
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://httpbin.org/post")
                .post(Entity.text(data))
                .readEntity(String.class);
        assertTrue(responseBody.toLowerCase().contains(data));
    }

    /**
     * Test to make sure that the proxy can proxy a redirect
     * @throws Exception
     */
    @Test
    public void testPassThroughPostMultiPart() throws Exception {
        final FileDataBodyPart filePart = new FileDataBodyPart("test_file", new File(TEST_IMAGE_PATH));

        MultiPart multiPart = new FormDataMultiPart()
                .field("foo", "bar")
                .bodyPart(filePart);
        Client multipartClient = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class)
                .build();
        String responseBody = multipartClient.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://httpbin.org/post")
                .post(Entity.entity(multiPart, multiPart.getMediaType()))
                .readEntity(String.class);
        assertTrue(responseBody.toLowerCase().contains("test_file"));
    }

    /**
     * Test to make sure that the proxy can proxy a redirect
     * @throws Exception
     */
    @Test
    public void testPassThroughPut() throws Exception {
        String data = "somedata";
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://httpbin.org/put")
                .put(Entity.text(data))
                .readEntity(String.class);
        assertTrue(responseBody.toLowerCase().contains(data));
    }

    /**
     * Test to make sure that the proxy can encrypt a response
     * @throws Exception
     */
    @Test
    public void testSimpleEncrypt() throws Exception {
        String data = "<ID>some sensitive data</ID> and then some data and then <CARD>some card data</CARD> and some more data and then <CARD>some other card data</CARD> and then finally <ID>some other sensitive data</ID> and the something.";
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://httpbin.org/put")
                .header("encryption-regex0", "(?<=<ID>).*?(?=</ID>)")
                .header("encryption-type0", "test")
                .header("encryption-regex1", "(?<=<CARD>).*?(?=</CARD>)")
                .header("encryption-type1", "test")
                .put(Entity.text(data))
                .readEntity(String.class);
        assertTrue(responseBody.contains("<ID>encrypted {some sensitive data}</ID> and then some data and then <CARD>encrypted {some card data}</CARD> and some more data and then <CARD>encrypted {some other card data}</CARD> and then finally <ID>encrypted {some other sensitive data}</ID> and the something."));
    }

    /**
     * Test to make sure that the proxy can decrypt a request
     * @throws Exception
     */
    @Test
    public void testSimpleDecrypt() throws Exception {
        String data = "<ID>encrypted {some sensitive data}</ID> and then some data and then <CARD>encrypted {some card data}</CARD> and some more data and then <CARD>encrypted {some other card data}</CARD> and then finally <ID>encrypted {some other sensitive data}</ID> and the something.";
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://httpbin.org/put")
                .header("decryption-regex0", "(?<=<ID>).*?(?=</ID>)")
                .header("decryption-type0", "test")
                .header("decryption-regex1", "(?<=<CARD>).*?(?=</CARD>)")
                .header("decryption-type1", "test")
                .put(Entity.text(data))
                .readEntity(String.class);
        assertTrue(responseBody.contains("<ID>some sensitive data</ID> and then some data and then <CARD>some card data</CARD> and some more data and then <CARD>some other card data</CARD> and then finally <ID>some other sensitive data</ID> and the something."));

    }

    /**
     * Test to make sure that the proxy can proxy a delete
     * @throws Exception
     */
    @Test
    public void testPassThroughDelete() throws Exception {
        String data = "somedata";
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/proxy")
                .request()
                .header("proxy-url", "http://httpbin.org/delete")
                .header("some-header", data)
                .delete()
                .readEntity(String.class);
        assertTrue(responseBody.toLowerCase().contains(data));
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
