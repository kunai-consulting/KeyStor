package com.kunai.keyvault.resources;

import com.codahale.metrics.annotation.Timed;
import com.kunai.keyvault.crypto.voltage.vibesimple.AuthMethod;
import com.kunai.keyvault.crypto.voltage.vibesimple.FaultResponse;
import com.kunai.keyvault.crypto.voltage.vibesimple.VibeSimple;
import com.kunai.keyvault.crypto.voltage.vibesimple.VibeSimple_Service;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Proxy a request to a remote resource
 */
@Path("/pietotoken")
public class PIETokenizationResource {
    public static final String FORMAT_FIRST_4_LAST_4_SST = "First 4 Last 4 SST";
    public static final String FORMAT_PIE = "Fix this: what's the format for PIE";
    public static String IDENTITY;
    public static String AUTHINFO;
    public static String WSDL;

    public PIETokenizationResource (String identity, String authinfo, String wsdl) {
        IDENTITY = identity;
        AUTHINFO = authinfo;
        WSDL = wsdl;
    }

    /**
     * Proxy a POST an encryption
     * @param cyrptogram The PIE encrypted data to tokenize
     * @return Returns a tokenize version of the encrypted data
     */
    @POST
    @Timed(name = "post-pietotoken")
    public Response proxyPost(InputStream cyrptogram) throws IOException, FaultResponse {
        VibeSimple service = new VibeSimple_Service(new URL(WSDL)).getVibeSimpleSOAP();
        byte[] data = IOUtils.toByteArray(cyrptogram);

        //TODO: Broken. Need to determine formatting for PIE and fix this...
        String deTokenizedCardNumber = service.accessFormattedData(new String(data), FORMAT_PIE, IDENTITY, "", AuthMethod.SHARED_SECRET, AUTHINFO);
        String tokenizedCardNumber = service.protectFormattedData(deTokenizedCardNumber, FORMAT_FIRST_4_LAST_4_SST, IDENTITY, "", AuthMethod.SHARED_SECRET, AUTHINFO);

        return Response.status(200).entity(tokenizedCardNumber).build();

    }
}
