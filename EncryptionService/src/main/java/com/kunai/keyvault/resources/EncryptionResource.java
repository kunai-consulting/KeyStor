package com.kunai.keyvault.resources;

import com.codahale.metrics.annotation.Timed;
import com.kunai.keyvault.crypto.Encryptor;
import com.kunai.keyvault.crypto.EncryptorFactory;
import com.kunai.keyvault.crypto.voltage.vibesimple.FaultResponse;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

/**
 * Proxy a request to a remote resource
 */
@Path("/encrypt")
public class EncryptionResource {
    public static Encryptor encryptor;
    public static final String FORMAT_FIRST_4_LAST_4_SST = "First 4 Last 4 SST";

    public EncryptionResource(EncryptorFactory encryptorFactory) {
        this.encryptor = encryptorFactory.createEncryptor();
        this.encryptor.init();
    }

    /**
     * Proxy a POST an encryption
     * @param type The type of encryption. Must be one of 'generic', 'test', 'card_data', 'ssn'.  When this parameter is set to 'test' data will simply be wrapped in 'encrypted {}'
     * @param postData The data to encrypt
     * @return Returns the response from the end-point or 400 (malformed request) if the header value was not set.
     */
    @POST
    @Timed(name = "post-encrypt")
    public Response proxyPost(@DefaultValue("generic") @QueryParam("type") String type,
                              InputStream postData) throws IOException, FaultResponse {
        type = type.toLowerCase().trim();

        if (type.equals("generic")) {
            String data = IOUtils.toString(postData);
            String protectedData = encryptor.protectGenericData(data);

            return Response.status(200).entity(protectedData).build();
        }
        if (type.equals("test")) {
            String data = IOUtils.toString(postData);
            return Response.status(200).entity("encrypted {" + data + "}").build();
        }
        if (type.equals("card_data")) {
            String cardNumber = IOUtils.toString(postData);
            String tokenizedCardNumber = encryptor.protectFormattedData(cardNumber, FORMAT_FIRST_4_LAST_4_SST);

            return Response.status(200).entity(tokenizedCardNumber).build();
        }
        if (type.equals("ssn")) {
            String ssn = IOUtils.toString(postData);
            String protectedData = encryptor.protectSocialSecurityNumber(ssn);

            return Response.status(200).entity(protectedData).build();
        }


        return Response.status(400).build();
    }
}
