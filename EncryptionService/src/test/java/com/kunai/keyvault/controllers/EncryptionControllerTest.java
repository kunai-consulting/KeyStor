package com.kunai.keyvault.controllers;

import com.kunai.keyvault.config.EncryptionConfiguration;
import com.kunai.keyvault.crypto.EncryptionComponent;
import com.kunai.keyvault.crypto.aes.AES;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EncryptionControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

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
     * Test to make sure that the service can card_data encrypt
     * @throws Exception
     */
    @Test
    public void testCardDataEncrypt() throws Exception {
        String data = "4012888888881881";
        String responseBody = this.restTemplate.postForEntity("http://localhost:" + port + "/api/encrypt?type=card_data", data, String.class).getBody();

        assertThat(AES.unPackString(AES.decrypt(AES.decode(responseBody), encryptionKey)), is(data));
    }

    /**
     * Test to make sure that the service can ssn encrypt
     * @throws Exception
     */
    @Test
    public void testSSNEncrypt() throws Exception {
        String data = "078-05-1120";
        String responseBody = this.restTemplate.postForEntity("http://localhost:" + port + "/api/encrypt?type=ssn", data, String.class).getBody();

        assertThat(AES.unPackString(AES.decrypt(AES.decode(responseBody), encryptionKey)), is(data));
    }

    /**
     * Test to make sure that the service can generic encrypt
     * @throws Exception
     */
    @Test
    public void testGenericEncrypt() throws Exception {
        String data = "some sensitive data";
        String responseBody = this.restTemplate.postForEntity("http://localhost:" + port + "/api/encrypt?type=generic", data, String.class).getBody();

        assertThat(AES.unPackString(AES.decrypt(AES.decode(responseBody), encryptionKey)), is(data));
    }

    /**
     * Test to make sure that the service can generic encrypt
     * @throws Exception
     */
    @Test
    public void testDefaultEncrypt() throws Exception {
        String data = "some sensitive data";
        String responseBody = this.restTemplate.postForEntity("http://localhost:" + port + "/api/encrypt", data, String.class).getBody();

        assertThat(AES.unPackString(AES.decrypt(AES.decode(responseBody), encryptionKey)), is(data));
    }

}
