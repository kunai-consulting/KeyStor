package com.kunai.keyvault.resources;

import com.codahale.metrics.annotation.Timed;
import com.kunai.keyvault.crypto.*;
import com.kunai.keyvault.crypto.voltage.vibesimple.FaultResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Proxy a request to a remote resource
 */
@Path("/proxy")
public class ConnectionResource {
    public static final String PROXY_HEADER = "proxy-url";
    public static final String ENCRYPTION_REGEX_HEADER = "encryption-regex";
    public static final String ENCRYPTION_TYPE_HEADER = "encryption-type";
    public static final String DECRYPTION_REGEX_HEADER = "decryption-regex";
    public static final String DECRYPTION_TYPE_HEADER = "decryption-type";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionResource.class);
    public static final int GET_REQUEST_TYPE = 0;
    public static final int POST_REQUEST_TYPE = 1;
    public static final int PUT_REQUEST_TYPE = 2;
    public static final int DELETE_REQUEST_TYPE = 3;
    public static final String[] REQUEST_TYPE_MAP = {"GET", "POST", "PUT", "DELETE"};

    public static Encryptor encryptor;
    public static Decryptor decryptor;
    public HttpClient client;

    public ConnectionResource(EncryptorFactory encryptorFactory, DecryptorFactory decryptorFactory, HttpClient client) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        encryptor = encryptorFactory.createEncryptor();
        encryptor.init();
        decryptor = decryptorFactory.createDecryptor();
        decryptor.init();
        this.client = client;
    }

    /**
     * Proxy a GET request to a remote end-point using the URL in the header PROXY_HEADER
     * @param httpRequest The request to proxy with the header value set
     * @return Returns the response from the end-point or 400 (malformed request) if the header value was not set.
     */
    @GET
    @Timed(name = "get-proxy")
    public Response proxyGet(@Context HttpServletRequest httpRequest) {
        return proxy(GET_REQUEST_TYPE, httpRequest, null);
    }

    /**
     * Proxy a POST request to a remote end-point using the URL in the header PROXY_HEADER
     * @param httpRequest The request to proxy with the header value set
     * @return Returns the response from the end-point or 400 (malformed request) if the header value was not set.
     */
    @POST
    @Timed(name = "post-proxy")
    public Response proxyPost(@Context HttpServletRequest httpRequest, InputStream postData) {
        return proxy(POST_REQUEST_TYPE, httpRequest, postData);
    }

    /**
     * Proxy a PUT request to a remote end-point using the URL in the header PROXY_HEADER
     * @param httpRequest The request to proxy with the header value set
     * @return Returns the response from the end-point or 400 (malformed request) if the header value was not set.
     */
    @PUT
    @Timed(name = "put-proxy")
    public Response proxyPut(@Context HttpServletRequest httpRequest, InputStream putData) {
        return proxy(PUT_REQUEST_TYPE, httpRequest, putData);
    }

    /**
     * Proxy a DELETE request to a remote end-point using the URL in the header PROXY_HEADER
     * @param httpRequest The request to proxy with the header value set
     * @return Returns the response from the end-point or 400 (malformed request) if the header value was not set.
     */
    @DELETE
    @Timed(name = "delete-proxy")
    public Response proxyDelete(@Context HttpServletRequest httpRequest) {
        return proxy(DELETE_REQUEST_TYPE, httpRequest, null);
    }

    /**
     * Proxy a request to a remote end-point using the URL in the header PROXY_HEADER
     * @param httpRequest The request to proxy with the header value set
     * @return Returns the response from the end-point or 400 (malformed request) if the header value was not set.
     */
    private Response proxy (int type, HttpServletRequest httpRequest, InputStream data) {
        HashMap<Integer, CryptoOp> encryptions = new HashMap<Integer, CryptoOp>();
        HashMap<Integer, CryptoOp> decryptions = new HashMap<Integer, CryptoOp>();
        Map<String, String> headers = new HashMap<String, String>();

        //extract the proxy url from the header
        String proxyUrl = httpRequest.getHeader(PROXY_HEADER);

        // Return malformed request if the header wasn't set
        if (proxyUrl == null) {
            LOGGER.warn("[" + httpRequest.getRequestedSessionId() + "] " + REQUEST_TYPE_MAP[type] +
                    "request did not contain " + PROXY_HEADER);
            throw new WebApplicationException(400);
        }
        else {
            LOGGER.debug("[" + httpRequest.getRequestedSessionId() + "] " + REQUEST_TYPE_MAP[type] +
                    " proxied to: " + proxyUrl);
        }

        Enumeration headerValues = httpRequest.getHeaderNames();
        List<String> headerValuesList = Collections.list(headerValues);

        //extract the crypto operations
        for (String headerName : headerValuesList) {
            if (headerName.trim().startsWith(ENCRYPTION_REGEX_HEADER)) {
                int index = Integer.parseInt(headerName.substring(ENCRYPTION_REGEX_HEADER.length()));
                encryptions.put(index, new CryptoOp(httpRequest.getHeader(headerName)));
            }

            if (headerName.trim().startsWith(DECRYPTION_REGEX_HEADER)) {
                int index = Integer.parseInt(headerName.substring(DECRYPTION_REGEX_HEADER.length()));
                decryptions.put(index, new CryptoOp(httpRequest.getHeader(headerName)));
            }
        }

        //extract the type operations
        for (String headerName : headerValuesList) {
            if (headerName.trim().startsWith(ENCRYPTION_TYPE_HEADER)) {
                int index = -1;
                try {
                    index = Integer.parseInt(headerName.substring(ENCRYPTION_TYPE_HEADER.length()));
                }
                catch (NumberFormatException e) {
                    LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] " + headerName + "should end with an index so that the regex can be matched to the type");
                    throw new WebApplicationException(400);
                }

                CryptoOp encryption = encryptions.get(index);
                if (encryption != null) {
                    try {
                        encryption.setType(httpRequest.getHeader(headerName));
                    }
                    catch (NumberFormatException e) {
                        LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] " + headerName + " header value should be an integer but was " + httpRequest.getHeader(headerName));
                        throw new WebApplicationException(400);
                    }
                }
                else {
                    LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] " + "Header " + ENCRYPTION_REGEX_HEADER + index + " not found but '" + headerName + "' was found");
                    throw new WebApplicationException(400);
                }

            }

            if (headerName.trim().startsWith(DECRYPTION_TYPE_HEADER)) {
                int index = -1;
                try {
                    index = Integer.parseInt(headerName.substring(DECRYPTION_TYPE_HEADER.length()));
                }
                catch (NumberFormatException e) {
                    LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] " + headerName + "should end with an index so that the regex can be matched to the type");
                    throw new WebApplicationException(400);
                }

                CryptoOp decryption = decryptions.get(index);
                if (decryption != null) {
                    try {
                        decryption.setType(httpRequest.getHeader(headerName));
                    }
                    catch (NumberFormatException e) {
                        LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] " + headerName + " header value should be an integer but was " + httpRequest.getHeader(headerName));
                        throw new WebApplicationException(400);
                    }
                }
                else {
                    LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] " + "Header " + DECRYPTION_REGEX_HEADER + index + " not found but '" + headerName + "' was found");
                    throw new WebApplicationException(400);
                }

            }

        }

        // make sure all the types where set
        Iterator it = encryptions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, CryptoOp> pair = (Map.Entry)it.next();
            if (pair.getValue().getType().equals("unset")) {
                LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] " + " regex " + pair.getValue().getRegex() + " found but there was no matching " + ENCRYPTION_TYPE_HEADER + " header" );
                throw new WebApplicationException(400);
            }
        }

        // make sure all the types where set
        it = decryptions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, CryptoOp> pair = (Map.Entry) it.next();
            if (pair.getValue().getType().equals("unset")) {
                LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] " + " regex " + pair.getValue().getRegex() + " found but there was no matching " + DECRYPTION_TYPE_HEADER + " header");
                throw new WebApplicationException(400);
            }
        }

        //copy everything but the proxy-url header, the encryption/decryption parameters, and the content length headers
        //since the length will change, and the host since that will be modified.
        for (String headerName : headerValuesList) {
            if (!headerName.contentEquals(PROXY_HEADER) &&
                    !headerName.trim().startsWith(ENCRYPTION_REGEX_HEADER) &&
                    !headerName.trim().startsWith(ENCRYPTION_TYPE_HEADER) &&
                    !headerName.trim().startsWith(DECRYPTION_REGEX_HEADER) &&
                    !headerName.trim().startsWith(DECRYPTION_TYPE_HEADER) &&
                    !headerName.trim().equalsIgnoreCase("Content-Length") &&
                    !headerName.trim().equalsIgnoreCase("Host")) {
                headers.put(headerName, httpRequest.getHeader(headerName));
            }
        }

        byte[] requestData = null;
        if (data != null) {
            //perform decryptions on the request body...
            try {
                // Large or small we have to pull the entire thing in here to do this.  That's going to be a limitation of the service.
                // If that limitation is too much then we are either going to have to shift to file based operation or use an offset.
                // For now I don't think it's a limitation.
                requestData = org.apache.commons.io.IOUtils.toByteArray(data);
            } catch (IOException e) {
                LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] unable to read body data into a byte array: " + e.getMessage());
                throw new WebApplicationException(400);
            }
            // Decrypt
            it = decryptions.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, CryptoOp> pair = (Map.Entry) it.next();
                try {
                    requestData = pair.getValue().decrypt(new String(requestData)).getBytes();
                } catch (DecryptionException e) {
                    LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] unable to decrypt " + e.getMessage());
                    throw new WebApplicationException(400);
                } catch (FaultResponse faultResponse) {
                    LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] error accessing Voltage or other encryption/decryption server" + faultResponse.getMessage());
                    throw new WebApplicationException(400);
                } catch (MalformedURLException e) {
                    LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] error incorrect URL for Voltage WSDL or other encryption/decryption server");
                    throw new WebApplicationException(500);
                }

                it.remove();
            }
        }

        //make the proxied request

        HttpResponse proxyResponse = null;
        switch (type) {
            case GET_REQUEST_TYPE:
                HttpGet get = new HttpGet(proxyUrl);
                setHeaders(get, headers);
                try {
                    proxyResponse = client.execute(get);
                } catch (IOException e) {
                    LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] IOException proxing a GET to " +
                    proxyUrl + ": " + e.getMessage());
                    throw new WebApplicationException(500);
                }
                break;
            case POST_REQUEST_TYPE:
                if (requestData != null) {
                    HttpPost post = new HttpPost(proxyUrl);
                    setHeaders(post, headers);
                    post.setEntity(new ByteArrayEntity(requestData));
                    try {
                        proxyResponse = client.execute(post);
                    } catch (IOException e) {
                        LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] IOException proxing a POST to " +
                                proxyUrl + ": " + e.getMessage());
                        throw new WebApplicationException(500);
                    }
                }
                break;
            case PUT_REQUEST_TYPE:
                if (requestData != null) {
                    HttpPut put = new HttpPut(proxyUrl);
                    setHeaders(put, headers);
                    put.setEntity(new ByteArrayEntity(requestData));
                    try {
                        proxyResponse = client.execute(put);
                    } catch (IOException e) {
                        LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] IOException proxing a PUT to " +
                                proxyUrl + ": " + e.getMessage());
                        throw new WebApplicationException(500);
                    }
                }
                break;
            case DELETE_REQUEST_TYPE:
                HttpDelete delete = new HttpDelete(proxyUrl);
                setHeaders(delete, headers);
                try {
                    proxyResponse = client.execute(delete);
                } catch (IOException e) {
                    LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] IOException proxing a DELETE to " +
                            proxyUrl + ": " + e.getMessage());
                    throw new WebApplicationException(500);
                }
                break;
        }


        //return the response
        Response response = null;
        if (proxyResponse != null){
            if (proxyResponse.getStatusLine().getStatusCode() >= 400) {
                LOGGER.warn("[" + httpRequest.getRequestedSessionId() + "] " + REQUEST_TYPE_MAP[type] +
                        " proxied to: " + proxyUrl + "returned response status:" + proxyResponse.getStatusLine().getStatusCode());
            } else {
                LOGGER.debug("[" + httpRequest.getRequestedSessionId() + "] Response status:" + proxyResponse.getStatusLine().getStatusCode());

                // perform encryption on the response body
                byte[] responseData;
                try {
                    responseData = IOUtils.toByteArray(proxyResponse.getEntity().getContent());
                } catch (IOException e) {
                    LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] IOException while reading the entity response for the proxied request to " +
                            proxyUrl + ": " + e.getMessage());
                    throw new WebApplicationException(500);
                }
                if (responseData != null) {
                    it = encryptions.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Integer, CryptoOp> pair = (Map.Entry) it.next();
                        try {
                            responseData = pair.getValue().encrypt(new String(responseData)).getBytes();
                        } catch (EncryptionException e) {
                            LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] unable to encrypt " + e.getMessage());
                            throw new WebApplicationException(400);
                        } catch (FaultResponse faultResponse) {
                            LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] error accessing Voltage or other encryption/decryption server " + faultResponse.getMessage());
                            throw new WebApplicationException(400);
                        } catch (MalformedURLException e) {
                            LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] error incorrect URL for Voltage WSDL or other encryption/decryption server");
                            throw new WebApplicationException(500);
                        } catch (IOException e) {
                            LOGGER.error("[" + httpRequest.getRequestedSessionId() + "] IO error during encrypt");
                            throw new WebApplicationException(500);
                        }


                        it.remove();
                    }

                }
                else {
                    LOGGER.debug("[" + httpRequest.getRequestedSessionId() + "] " + REQUEST_TYPE_MAP[type] +
                            " proxied to: " + proxyUrl + "had null entity data");
                }
                //Build the response...
                Response.ResponseBuilder responseBuilder = Response.status(proxyResponse.getStatusLine().getStatusCode()).entity(responseData);
                Header[] proxyResponseHeaders = proxyResponse.getAllHeaders();
                for (int i = 0; i < proxyResponseHeaders.length; i++){
                    responseBuilder.header(proxyResponseHeaders[i].getName(), proxyResponseHeaders[i].getValue());
                }
                response = responseBuilder.build();
            }
        }
        else {
            LOGGER.warn("[" + httpRequest.getRequestedSessionId() + "] " + REQUEST_TYPE_MAP[type] +
                    " proxied to: " + proxyUrl + " proxy response is null");
        }

        return response;

    }

    /**
     * Set the headers in a request
     * @param request The request to set the headers in
     * @param headers The headers to set
     */
    private static void setHeaders(HttpRequestBase request, Map<String, String> headers) {
        Iterator<Map.Entry<String, String>> i = headers.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, String> mapEntry = i.next();
            request.setHeader(mapEntry.getKey(), mapEntry.getValue());
        }
    }
}
