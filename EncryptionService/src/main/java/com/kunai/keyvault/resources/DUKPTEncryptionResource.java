package com.kunai.keyvault.resources;

import com.codahale.metrics.annotation.Timed;
import com.kunai.keyvault.crypto.Encryptor;
import com.kunai.keyvault.crypto.EncryptorFactory;
import com.kunai.keyvault.crypto.voltage.vibesimple.FaultResponse;
import com.kunai.keyvault.hsm.HardwareSecurityModule;
import com.kunai.keyvault.resources.params.DUKPTEncryption;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Proxy a request to a remote resource
 */
@Path("/dukpttoencrypt")
public class DUKPTEncryptionResource {
    public static Encryptor encryptor;

    public static final String FORMAT_FIRST_4_LAST_4_SST = "First 4 Last 4 SST";

    public static HardwareSecurityModule hsm;

    public DUKPTEncryptionResource(EncryptorFactory encryptorFactory) {
        this.encryptor = encryptorFactory.createEncryptor();
    }

    /**
     * POST DUKPT encrypted data to tokenize
     * @param encryption The DUKPT encrypted data to tokenize
     * @return Returns a tokenize version of the encrypted data
     */
    @POST
    @Timed(name = "post-dukpttoencrypt")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response proxyPost(@Valid DUKPTEncryption encryption, @DefaultValue("generic") @QueryParam("type") String type) throws IOException, FaultResponse {
        type = type.toLowerCase().trim();

        return Response.status(400).build();

    }
}
