package com.kunai.keyvault;

import com.kunai.keyvault.config.EncryptionConfiguration;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *  Main class for performing the Integration Test in the healthcheck, encryption and decryption
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EncryptionServiceApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    EncryptionConfiguration encryptionConfiguration;

    /**
     * Test to make sure that the context loads correctly.  Note that if something was changed for the testing context
     * this test is checking to make sure the test context loaded correctly.  Production could be different.
     */
    @Test
    public void contextLoads() {
        assertThat("Encryption Key is in the configuration", encryptionConfiguration.getKey(), is("ABCDEF0123456789"));
        assertThat("Encryption Type is in the configuration", encryptionConfiguration.getType(), is("aes"));
    }

    /**
     * Test to make sure that the health check is working
     */
    @Test
    public void healthCheckEnabled() {
        String responseBody = this.restTemplate.getForEntity("http://localhost:" + port + "/api/actuator/health", String.class).getBody();

        Assert.assertThat(responseBody, CoreMatchers.is("{\"status\":\"UP\"}"));
    }
}
