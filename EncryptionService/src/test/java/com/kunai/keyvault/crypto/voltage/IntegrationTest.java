package com.kunai.keyvault.crypto.voltage;

import com.kunai.keyvault.EncryptionServiceApplication;
import com.kunai.keyvault.EncryptionServiceConfiguration;
import com.kunai.keyvault.crypto.voltage.vibesimple.AuthMethod;
import com.kunai.keyvault.crypto.voltage.vibesimple.VibeSimple;
import com.kunai.keyvault.crypto.voltage.vibesimple.VibeSimple_Service;
import com.kunai.keyvault.resources.params.DUKPTEncryption;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;

import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static org.junit.Assert.assertTrue;


@Ignore("Enable if you have voltage installed.")
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
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private static final String FORMAT_FIRST_4_LAST_4_SST = "First 4 Last 4 SST";
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-service-voltage.yml");

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

        VibeSimple service = new VibeSimple_Service().getVibeSimpleSOAP();
        assertTrue(data.equals(service.accessFormattedData(responseBody, FORMAT_FIRST_4_LAST_4_SST, VoltageEncryptor.IDENTITY, "", AuthMethod.SHARED_SECRET, VoltageEncryptor.AUTHINFO)));
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

        VibeSimple service = new VibeSimple_Service(new URL(VoltageEncryptor.WSDL)).getVibeSimpleSOAP();
        assertTrue(data.equals(service.accessSocialSecurityNumber(responseBody, VoltageEncryptor.IDENTITY, "", false, AuthMethod.SHARED_SECRET, VoltageEncryptor.AUTHINFO)));
    }

    /**
     * Test to make sure that the service can generic encrypt
     * @throws Exception
     */
    @Test
    public void testGenericEncrypt() throws Exception {
        String data = "some sensitive data";
        byte[] responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/encrypt?type=generic")
                .request()
                .post(Entity.text(data))
                .readEntity(byte[].class);

        VibeSimple service = new VibeSimple_Service(new URL(VoltageEncryptor.WSDL)).getVibeSimpleSOAP();
        String decryptedData = new String(service.accessGenericData(responseBody, VoltageEncryptor.IDENTITY, "", false, AuthMethod.SHARED_SECRET, VoltageEncryptor.AUTHINFO));
        assertTrue(decryptedData.contentEquals(data));
    }

    /**
     * Test to make sure that the proxy can generic encrypt DUKPUT encrypted data
     * @throws Exception
     */
    @Test
    public void testDUKPTEncrypt() throws Exception {
        DUKPTEncryption encryption = new DUKPTEncryption("test", "some sensitive data");
        byte[] responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/dukpttoencrypt")
                .request()
                .post(Entity.json(encryption))
                .readEntity(byte[].class);

        VibeSimple service = new VibeSimple_Service(new URL(VoltageEncryptor.WSDL)).getVibeSimpleSOAP();
        String decryptedData = new String(service.accessGenericData(responseBody, VoltageEncryptor.IDENTITY, "", false, AuthMethod.SHARED_SECRET, VoltageEncryptor.AUTHINFO));
        assertTrue(decryptedData.contentEquals("some sensitive data"));
    }

    /**
     * Test to make sure that the service can ssn encrypt DUKPUT encrypted data
     * @throws Exception
     */
    @Test
    public void testDUKPTSSNEncrypt() throws Exception {
        String data = "078-05-1120";
        DUKPTEncryption encryption = new DUKPTEncryption("test", data);
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/dukpttossnencrypt")
                .request()
                .post(Entity.json(encryption))
                .readEntity(String.class);

        VibeSimple service = new VibeSimple_Service(new URL(VoltageEncryptor.WSDL)).getVibeSimpleSOAP();
        assertTrue(data.equals(service.accessSocialSecurityNumber(responseBody, VoltageEncryptor.IDENTITY, "", false, AuthMethod.SHARED_SECRET, VoltageEncryptor.AUTHINFO)));
    }

    /**
     * Test to make sure that the service can ssn encrypt DUKPUT encrypted data
     * @throws Exception
     */
    @Test
    public void testDUKPTTokenize() throws Exception {
        String data = "4012888888881881";
        DUKPTEncryption encryption = new DUKPTEncryption("test", data);
        String responseBody = client.target("http://localhost:" + RULE.getLocalPort() + "/dukpttotoken")
                .request()
                .post(Entity.json(encryption))
                .readEntity(String.class);

        VibeSimple service = new VibeSimple_Service(new URL(VoltageEncryptor.WSDL)).getVibeSimpleSOAP();
        assertTrue(data.equals(service.accessFormattedData(responseBody, FORMAT_FIRST_4_LAST_4_SST, VoltageEncryptor.IDENTITY, "", AuthMethod.SHARED_SECRET, VoltageEncryptor.AUTHINFO)));
    }
}
