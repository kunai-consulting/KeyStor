package com.kunai.keyvault.controllers;

import com.kunai.keyvault.config.EncryptionConfiguration;
import com.kunai.keyvault.crypto.EncryptionComponent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConnectionControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String TEST_IMAGE_FILE_NAME = "test.jpg";



    @Autowired
    private EncryptionComponent encryptionComponent;
    @Autowired
    private EncryptionConfiguration encryptionConfiguration;

    private byte[] encryptionKey;

    @Before
    public void setUp(){
        encryptionKey = encryptionConfiguration.getKey().getBytes();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test to make sure that the proxy can proxy a redirect
     * @throws Exception
     */
    @Test
    public void testPassThroughRedirect() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("connection-url", "http://httpbin.org/absolute-redirect/2");
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.GET, request, String.class);
        assertTrue(responseBody.getStatusCode().is2xxSuccessful());
    }

    /**
     * Test to make sure that the proxy can proxy a delete
     * @throws Exception
     */
    @Test
    public void testPassThroughPost() throws Exception {
        String data = "somedata";

        HttpHeaders headers = new HttpHeaders();
        headers.add("connection-url", "http://httpbin.org/post");
        HttpEntity<String> request = new HttpEntity<>(data, headers);
        ResponseEntity<String> responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.POST, request, String.class);
        assertTrue(responseBody.getBody().toLowerCase().contains(data));
    }

    /**
     * Test to make sure that the proxy can pass though a multipart request
     * @throws Exception
     */
    @Test
    public void testPassThroughPostMultiPart() throws Exception {
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("fileField", new ClassPathResource(TEST_IMAGE_FILE_NAME));
        body.add("stringfield", TEST_IMAGE_FILE_NAME);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        headers.add("connection-url", "http://httpbin.org/post");
        HttpEntity request = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.POST, request, String.class);
        assertFalse(responseBody.getStatusCode().isError());
    }

    /**
     * Test to make sure that the proxy can proxy a delete
     * @throws Exception
     */
    @Test
    public void testPassThroughPut() throws Exception {
        String data = "somedata";

        HttpHeaders headers = new HttpHeaders();
        headers.add("connection-url", "http://httpbin.org/put");
        HttpEntity<String> request = new HttpEntity<>(data, headers);
        ResponseEntity<String> responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.PUT, request, String.class);
        assertTrue(responseBody.getBody().toLowerCase().contains(data));
    }

    /**
     * Test to make sure that the proxy can encrypt a response
     * @throws Exception
     */
    @Test
    public void testSimpleEncrypt() throws Exception {
        String data = "<ID>some sensitive data</ID> and then some data and then <CARD>some card data</CARD> and some more data and then <CARD>some other card data</CARD> and then finally <ID>some other sensitive data</ID> and the something.";
        HttpHeaders headers = new HttpHeaders();
        headers.add("connection-url", "http://httpbin.org/put");
        headers.add("encryption-regex0", "(?<=<ID>).*?(?=</ID>)");
        headers.add("encryption-type0", "test");
        headers.add("encryption-regex1", "(?<=<CARD>).*?(?=</CARD>)");
        headers.add("encryption-type1", "test");

        HttpEntity<String> request = new HttpEntity<>(data, headers);
        ResponseEntity<String> responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.PUT, request, String.class);

        assertTrue(responseBody.getBody().contains("<ID>encrypted {some sensitive data}</ID> and then some data and then <CARD>encrypted {some card data}</CARD> and some more data and then <CARD>encrypted {some other card data}</CARD> and then finally <ID>encrypted {some other sensitive data}</ID> and the something."));
    }

    /**
     * Test to make sure that the proxy can decrypt a request
     * @throws Exception
     */
    @Test
    public void testSimpleDecrypt() throws Exception {
        String data = "<ID>encrypted {some sensitive data}</ID> and then some data and then <CARD>encrypted {some card data}</CARD> and some more data and then <CARD>encrypted {some other card data}</CARD> and then finally <ID>encrypted {some other sensitive data}</ID> and the something.";

        HttpHeaders headers = new HttpHeaders();
        headers.add("connection-url", "http://httpbin.org/put");
        headers.add("decryption-regex0", "(?<=<ID>).*?(?=</ID>)");
        headers.add("decryption-type0", "test");
        headers.add("decryption-regex1", "(?<=<CARD>).*?(?=</CARD>)");
        headers.add("decryption-type1", "test");

        HttpEntity<String> request = new HttpEntity<>(data, headers);
        ResponseEntity<String> responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.PUT, request, String.class);

        assertTrue(responseBody.getBody().contains("<ID>some sensitive data</ID> and then some data and then <CARD>some card data</CARD> and some more data and then <CARD>some other card data</CARD> and then finally <ID>some other sensitive data</ID> and the something."));

    }

    /**
     * Test to make sure that the proxy can proxy a delete
     * @throws Exception
     */
    @Test
    public void testPassThroughDelete() throws Exception {
        String data = "somedata";

        HttpHeaders headers = new HttpHeaders();
        headers.add("connection-url", "http://httpbin.org/delete");
        HttpEntity<String> request = new HttpEntity<>(data, headers);
        ResponseEntity<String> responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.DELETE, request, String.class);
        assertTrue(responseBody.getBody().toLowerCase().contains(data));
    }

    /**
     * Test Credit Card Encryption and Decryption
     * @throws Exception
     */
    @Test
    public void testCreditCardEncryptDecrypt() throws Exception {
        String data = "<CARD>4012888888881881</CARD>";

        HttpHeaders headers = new HttpHeaders();
        headers.add("connection-url", "http://httpbin.org/put");
        headers.add("encryption-regex1", "(?<=<CARD>).*?(?=</CARD>)");
        headers.add("encryption-type1", "card_data");
        HttpEntity<String> request = new HttpEntity<>(data, headers);

        ResponseEntity<String> responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.PUT, request, String.class);
        assertTrue(!responseBody.getStatusCode().isError());
        assertTrue(!responseBody.getBody().contains("4012888888881881"));

        headers = new HttpHeaders();
        headers.add("connection-url", "http://httpbin.org/put");
        headers.add("decryption-regex1", "(?<=<CARD>).*?(?=</CARD>)");
        headers.add("decryption-type1", "card_data");
        request = new HttpEntity<>(responseBody.getBody(), headers);

        responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.PUT, request, String.class);
        assertTrue(responseBody.getBody().contains(data));
    }

    /**
     * Test Generic Encryption and Decryption
     * @throws Exception
     */
    @Test
    public void testGenericEncryptDecrypt() throws Exception {
        String data = "<CARD>somedata</CARD>";

        HttpHeaders headers = new HttpHeaders();
        headers.add("connection-url", "http://httpbin.org/put");
        headers.add("encryption-regex1", "(?<=<CARD>).*?(?=</CARD>)");
        headers.add("encryption-type1", "generic");
        HttpEntity<String> request = new HttpEntity<>(data, headers);

        ResponseEntity<String> responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.PUT, request, String.class);
        assertTrue(!responseBody.getStatusCode().isError());
        assertTrue(!responseBody.getBody().contains("somedata"));

        headers = new HttpHeaders();
        headers.add("connection-url", "http://httpbin.org/put");
        headers.add("decryption-regex1", "(?<=<CARD>).*?(?=</CARD>)");
        headers.add("decryption-type1", "generic");
        request = new HttpEntity<>(responseBody.getBody(), headers);

        responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.PUT, request, String.class);
        assertTrue(responseBody.getBody().contains(data));
    }

    @Test
    public void testPassThrough() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("connection-url", "http://www.google.com");
        HttpEntity<String> request = new HttpEntity<>(null, headers);


        String responseBody = restTemplate.exchange(new URI("http://localhost:" + port + "/api/proxy"), HttpMethod.GET, request, String.class).getBody();
        assertTrue(responseBody.toLowerCase().contains("google"));
    }
    /**
     * Test SSN Encryption and Decryption
     * @throws Exception
     */
    @Test
    public void testSSNEncryptDecrypt() throws Exception {
        String data = "<CARD>078-05-1120</CARD>";

        HttpHeaders headers = new HttpHeaders();
        headers.add("connection-url", "http://httpbin.org/put");
        headers.add("encryption-regex1", "(?<=<CARD>).*?(?=</CARD>)");
        headers.add("encryption-type1", "ssn");
        HttpEntity<String> request = new HttpEntity<>(data, headers);

        ResponseEntity<String> responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.PUT, request, String.class);
        assertTrue(!responseBody.getStatusCode().isError());
        assertTrue(!responseBody.getBody().contains("078-05-1120"));

        headers = new HttpHeaders();
        headers.add("connection-url", "http://httpbin.org/put");
        headers.add("decryption-regex1", "(?<=<CARD>).*?(?=</CARD>)");
        headers.add("decryption-type1", "ssn");
        request = new HttpEntity<>(responseBody.getBody(), headers);

        responseBody = restTemplate.exchange("http://localhost:" + port + "/api/proxy", HttpMethod.PUT, request, String.class);
        assertTrue(responseBody.getBody().contains(data));
    }
}
