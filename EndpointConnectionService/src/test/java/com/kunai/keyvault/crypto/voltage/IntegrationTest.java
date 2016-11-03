package com.kunai.keyvault.crypto.voltage;

import com.kunai.keyvault.ConnectionServiceApplication;
import com.kunai.keyvault.ConnectionServiceConfiguration;
import com.kunai.keyvault.crypto.voltage.vibesimple.AuthMethod;
import com.kunai.keyvault.crypto.voltage.vibesimple.VibeSimple;
import com.kunai.keyvault.crypto.voltage.vibesimple.VibeSimple_Service;
import com.kunai.keyvault.resources.ConnectionResource;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;

import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static org.assertj.core.api.Assertions.assertThat;

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
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-service-voltage.yml");
    private static final String TEST_IMAGE_FILE_NAME = "test.jpg";
    private static final String TEST_IMAGE_PATH = ResourceHelpers.resourceFilePath(TEST_IMAGE_FILE_NAME);

    private static String WSDL;
    private static String IDENTITY;
    private static String AUTHINFO;
    public static String FORMAT_FIRST_4_LAST_4_SST = "First 4 Last 4 SST";


    @ClassRule
    public static final DropwizardAppRule<ConnectionServiceConfiguration> RULE = new DropwizardAppRule<>(
            ConnectionServiceApplication.class, CONFIG_PATH);

    private Client client;

    /**
     * Taking the parameters from the VolageEncryptor
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
        WSDL = ((VoltageEncryptor) ConnectionResource.encryptor).WSDL;
        IDENTITY = ((VoltageEncryptor) ConnectionResource.encryptor).IDENTITY;
        AUTHINFO = ((VoltageEncryptor) ConnectionResource.encryptor).AUTHINFO;
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }


    /**
     * Test to make sure that we can connect to the Voltage server
     * @throws Exception
     */
    @Test
    public void testVoltageServerConnection() throws Exception {
        VibeSimple service = new VibeSimple_Service(new URL(((VoltageEncryptor) ConnectionResource.encryptor).WSDL)).getVibeSimpleSOAP();
        assertThat(service != null);
    }

    /**
     * Test Credit Card Number Tokenization
     * @throws Exception
     */
    @Test
    public void testProtectCreditCardNumber() throws Exception {
        String cardNumber = "4012888888881881";
        VibeSimple service = new VibeSimple_Service(new URL(WSDL)).getVibeSimpleSOAP();
        String tokenizedCardNumber = service.protectFormattedData(cardNumber, FORMAT_FIRST_4_LAST_4_SST, ((VoltageEncryptor) ConnectionResource.encryptor).IDENTITY, "", AuthMethod.SHARED_SECRET, ((VoltageEncryptor) ConnectionResource.encryptor).AUTHINFO);
        assertThat(tokenizedCardNumber.startsWith("4012"));
        assertThat(tokenizedCardNumber.endsWith("1881"));
        assertThat(!tokenizedCardNumber.contentEquals("4012888888881881"));
    }

    /**
     * Test Credit Card Number De-tokenization
     * @throws Exception
     */
    @Test
    public void testAccessCreditCardNumber() throws Exception {
        String cardNumber = "4012888888881881";
        VibeSimple service = new VibeSimple_Service(new URL(WSDL)).getVibeSimpleSOAP();
        String tokenizedCardNumber = service.protectFormattedData(cardNumber, FORMAT_FIRST_4_LAST_4_SST, IDENTITY, "", AuthMethod.SHARED_SECRET, AUTHINFO);
        String deTokenizedCardNumber = service.accessFormattedData(tokenizedCardNumber, FORMAT_FIRST_4_LAST_4_SST, IDENTITY, "", AuthMethod.SHARED_SECRET, AUTHINFO);
        assertThat(cardNumber.contentEquals(deTokenizedCardNumber));
    }

    /**
     * Test Genertic Data Encryption
     * @throws Exception
     */
    @Test
    public void testProtectGenericData() throws Exception {
        String data = "some generic sensitive data";
        VibeSimple service = new VibeSimple_Service(new URL(WSDL)).getVibeSimpleSOAP();
        String protectedData = service.protectGenericStringData(data, IDENTITY, false, AuthMethod.SHARED_SECRET, AUTHINFO, "");

        assertThat(!protectedData.contentEquals("some generic sensitive data"));
    }

    /**
     * Test Generic Data Decryption
     * @throws Exception
     */
    @Test
    public void testAccessGenericData() throws Exception {
        String data = "some generic sensitive data";
        VibeSimple service = new VibeSimple_Service(new URL(WSDL)).getVibeSimpleSOAP();
        String protectedData = service.protectGenericStringData(data, IDENTITY, false, AuthMethod.SHARED_SECRET, AUTHINFO, "");
        String decryptedData = service.accessGenericStringData(protectedData, IDENTITY, "", false, AuthMethod.SHARED_SECRET, AUTHINFO);
        assertThat(decryptedData.contentEquals(data));
    }

    /**
     * Test SSN Encryption
     * @throws Exception
     */
    @Test
    public void testProtectSocialSecurityNumber() throws Exception {
        String ssn = "078-05-1120";
        VibeSimple service = new VibeSimple_Service(new URL(WSDL)).getVibeSimpleSOAP();
        String protectedData = service.protectSocialSecurityNumber(ssn, IDENTITY, "", AuthMethod.SHARED_SECRET, AUTHINFO);

        assertThat(!protectedData.contentEquals("078-05-1120"));
    }

    /**
     * Test SSN Decryption
     * @throws Exception
     */
    @Test
    public void testAccessSocialSecurityNumber() throws Exception {
        String ssn = "078-05-1120";
        VibeSimple service = new VibeSimple_Service(new URL(WSDL)).getVibeSimpleSOAP();
        String protectedData = service.protectSocialSecurityNumber(ssn, IDENTITY, "", AuthMethod.SHARED_SECRET, AUTHINFO);
        String decryptedData = service.accessSocialSecurityNumber(protectedData, IDENTITY, "", false, AuthMethod.SHARED_SECRET, AUTHINFO);
        assertThat(decryptedData.contentEquals(ssn));
    }
}
