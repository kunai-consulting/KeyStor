package com.kunai.keyvault.controllers;

import com.amazonaws.util.IOUtils;
import com.kunai.keyvault.crypto.CryptoOp;
import com.kunai.keyvault.crypto.DecryptionException;
import com.kunai.keyvault.crypto.EncryptionComponent;
import com.kunai.keyvault.crypto.EncryptionException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@Api(value = "Encryption services")
public class ConnectionController {
    private static final String PROXY_HEADER = "proxy-url";
    private static final String ENCRYPTION_REGEX_HEADER = "encryption-regex";
    private static final String ENCRYPTION_TYPE_HEADER = "encryption-type";
    private static final String DECRYPTION_REGEX_HEADER = "decryption-regex";
    private static final String DECRYPTION_TYPE_HEADER = "decryption-type";

    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionController.class);

    @Autowired
    EncryptionComponent encryptionComponent;

    @Autowired
    RestTemplate restTemplate;

    /**
     * Proxy a request to a remote end-point using the URL in the header PROXY_HEADER
     *
     * @param httpRequest The request to proxy with the header value set
     */
    @ApiOperation(value = "Proxy a request with encryption/decryption", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "The contents of the request to decrypt", required = true, paramType = "body", example = "<ID>some sensitive data</ID> and then some data and then <CARD>some card data</CARD> and some more data and then <CARD>some other card data</CARD> and then finally <ID>some other sensitive data</ID> and the something."),
            @ApiImplicitParam(name = "proxy-url", value = "The URL to send the request to", required = true, paramType = "header", example = "http://httpbin.org/post"),
            @ApiImplicitParam(name = "encryption-regex0", value = "A regex used to match and encrypt response data.  You can have 0 though n of them", required = false, paramType = "header", example = "encryption-regex1:(?<=<ID>).*?(?=</ID>)"),
            @ApiImplicitParam(name = "encryption-type0", value = "The type of encryption for encryption n, one of ssn, card_data, generic", required = false, paramType = "header", example = "encryption-type1:generic"),
            @ApiImplicitParam(name = "decryption-regex0", value = "A regex used to match and decrypt request data.  You can have 0 though n of them", required = false, paramType = "header", example = "decryption-regex1:(?<=<ID>).*?(?=</ID>)"),
            @ApiImplicitParam(name = "decryption-type0", value = "The type of decryption for decryption n, one of ssn, card_data, generic", required = false, paramType = "header", example = "decryption-type1:generic")
    })
    @CrossOrigin()
    @RequestMapping(value = "/proxy", method = {GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE})
    public void proxyPost(HttpServletResponse httpResponse, HttpServletRequest httpRequest) throws IOException {
        proxy(HttpMethod.resolve(httpRequest.getMethod()), httpResponse, httpRequest, httpRequest.getInputStream());
    }

    /**
     * Proxy a request to a remote end-point using the URL in the header PROXY_HEADER
     *
     * @param httpRequest The request to proxy with the header value set
     */
    private void proxy(HttpMethod type, HttpServletResponse httpResponse, HttpServletRequest httpRequest, InputStream data) {
        HashMap<Integer, CryptoOp> encryptions = new HashMap<Integer, CryptoOp>();
        HashMap<Integer, CryptoOp> decryptions = new HashMap<Integer, CryptoOp>();
        HttpHeaders headers = new HttpHeaders();
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        //extract the proxy url from the header
        String proxyUrl = httpRequest.getHeader(PROXY_HEADER);

        // Return malformed request if the header wasn't set
        if (proxyUrl == null) {
            LOGGER.warn("[{}] {} request did not contain {}", sessionId, type, PROXY_HEADER);
            httpResponse.setStatus(400);
            return;
        } else {
            LOGGER.debug("[{}] {} proxied to: {}", sessionId, type, proxyUrl);
        }

        Enumeration headerValues = httpRequest.getHeaderNames();
        List<String> headerValuesList = Collections.list(headerValues);

        //extract the crypto operations
        for (String headerName : headerValuesList) {
            if (headerName.trim().startsWith(ENCRYPTION_REGEX_HEADER)) {
                int index = Integer.parseInt(headerName.substring(ENCRYPTION_REGEX_HEADER.length()));
                encryptions.put(index, new CryptoOp(httpRequest.getHeader(headerName), encryptionComponent));
            }

            if (headerName.trim().startsWith(DECRYPTION_REGEX_HEADER)) {
                int index = Integer.parseInt(headerName.substring(DECRYPTION_REGEX_HEADER.length()));
                decryptions.put(index, new CryptoOp(httpRequest.getHeader(headerName), encryptionComponent));
            }
        }

        //extract the type operations
        for (String headerName : headerValuesList) {
            if (headerName.trim().startsWith(ENCRYPTION_TYPE_HEADER)) {
                int index = -1;
                try {
                    index = Integer.parseInt(headerName.substring(ENCRYPTION_TYPE_HEADER.length()));
                } catch (NumberFormatException e) {
                    LOGGER.error("[{}] {} should end with an index so that the regex can be matched to the type", sessionId, headerName);
                    httpResponse.setStatus(400);
                    return;
                }

                CryptoOp encryption = encryptions.get(index);
                if (encryption != null) {
                    try {
                        encryption.setType(httpRequest.getHeader(headerName));
                    } catch (NumberFormatException e) {
                        LOGGER.error("[{}] {} header value should be an integer but was {}", sessionId, headerName, httpRequest.getHeader(headerName));
                        httpResponse.setStatus(400);
                        return;
                    }
                } else {
                    LOGGER.error("[{}] Header {} not found but '{}{}' was found", sessionId, ENCRYPTION_REGEX_HEADER, index, headerName);
                    httpResponse.setStatus(400);
                    return;
                }

            }

            if (headerName.trim().startsWith(DECRYPTION_TYPE_HEADER)) {
                int index = -1;
                try {
                    index = Integer.parseInt(headerName.substring(DECRYPTION_TYPE_HEADER.length()));
                } catch (NumberFormatException e) {
                    LOGGER.error("[{}] {} should end with an index so that the regex can be matched to the type", sessionId, headerName);
                    httpResponse.setStatus(400);
                    return;
                }

                CryptoOp decryption = decryptions.get(index);
                if (decryption != null) {
                    try {
                        decryption.setType(httpRequest.getHeader(headerName));
                    } catch (NumberFormatException e) {
                        LOGGER.error("[{}] {} header value should be an integer but was {}", sessionId, headerName, httpRequest.getHeader(headerName));
                        httpResponse.setStatus(400);
                        return;
                    }
                } else {
                    LOGGER.error("[{}] Header {} not found but '{}{}' was found", sessionId, DECRYPTION_REGEX_HEADER, index, headerName);
                    httpResponse.setStatus(400);
                    return;
                }

            }

        }

        // make sure all the types where set
        Iterator it = encryptions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, CryptoOp> pair = (Map.Entry) it.next();
            if (pair.getValue().getType().equals("unset")) {
                LOGGER.error("[{}] regex {} found but there was no matching {} header", sessionId, pair.getValue().getRegex(), ENCRYPTION_TYPE_HEADER);
                httpResponse.setStatus(400);
                return;
            }
        }

        // make sure all the types where set
        it = decryptions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, CryptoOp> pair = (Map.Entry) it.next();
            if (pair.getValue().getType().equals("unset")) {
                LOGGER.error("[{}] regex {} found but there was no matching {} header", sessionId, pair.getValue().getRegex(), ENCRYPTION_TYPE_HEADER);
                httpResponse.setStatus(400);
                return;
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
                headers.add(headerName, httpRequest.getHeader(headerName));
            }
        }

        byte[] requestData = null;
        if (data != null) {
            //perform decryptions on the request body...
            try {
                // Large or small we have to pull the entire thing in here to do this.  That's going to be a limitation of the service.
                // If that limitation is too much then we are either going to have to shift to file based operation or use an offset.
                // For now I don't think it's a limitation.
                requestData = IOUtils.toByteArray(data);
            } catch (IOException e) {
                LOGGER.error("[{}] unable to read body data into a byte array: {}", sessionId, e.getMessage());
                httpResponse.setStatus(400);
                return;
            }
            // Decrypt
            it = decryptions.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, CryptoOp> pair = (Map.Entry) it.next();
                try {
                    requestData = pair.getValue().decrypt(new String(requestData)).getBytes();
                } catch (DecryptionException e) {
                    LOGGER.error("[{}] unable to decrypt {}", sessionId, e.getMessage());
                    httpResponse.setStatus(400);
                    return;
                }

                it.remove();
            }
        }

        //make the proxied request
        ResponseEntity<byte[]> proxyResponse;
        URI uri;
        try {
            uri = new URI(proxyUrl);
        } catch (URISyntaxException e) {
            LOGGER.error("[{}] Could not create the proxy request URI from {}", sessionId, proxyUrl);
            httpResponse.setStatus(400);
            return;
        }
        RequestEntity<byte[]> requestEntity = new RequestEntity<>(requestData, headers, type, uri);
        try {
            proxyResponse = restTemplate.exchange(requestEntity, byte[].class);
        } catch (RestClientException e) {
            LOGGER.warn("[{}] Exception while making the proxied request: {}", sessionId, e.getLocalizedMessage());
            httpResponse.setStatus(Integer.parseInt(e.getLocalizedMessage().substring(0, 3)));
            return;
        }


        //build the response
        if (proxyResponse.getStatusCode().isError()) {
            LOGGER.warn("[{}] {} proxied to: {} returned response status: {}", sessionId, type, proxyUrl, proxyResponse.getStatusCode());
        }
        LOGGER.debug("[{}] Response status: {}", sessionId, proxyResponse.getStatusCode());

        // perform encryption on the response body
        //Build the response...
        httpResponse.setStatus(proxyResponse.getStatusCodeValue());

        // Copy the headers...
        Set<String> proxyResponseHeaders = proxyResponse.getHeaders().keySet();
        for (String header : proxyResponseHeaders) {
            List<String> headerList = proxyResponse.getHeaders().get(header);
            if (headerList != null) {
                for (String s : headerList) {
                    httpResponse.addHeader(header, s);
                }
            }
        }

        byte[] responseData = proxyResponse.getBody();
        if (responseData != null) {
            it = encryptions.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, CryptoOp> pair = (Map.Entry) it.next();
                try {
                    responseData = pair.getValue().encrypt(new String(responseData)).getBytes();
                } catch (EncryptionException e) {
                    LOGGER.error("[{}] unable to encrypt {}", sessionId, e.getMessage());
                    httpResponse.setStatus(400);
                    return;
                }

                it.remove();
            }

        } else {
            LOGGER.debug("[{}] {} proxied to: {} had null entity data", sessionId, type, proxyUrl);
        }

        // Copy the data...
        try {
            OutputStream outputStream = httpResponse.getOutputStream();
            if (responseData != null) {
                outputStream.write(responseData);
                outputStream.flush(); // not sure if this is needed?
            }
        } catch (IOException e) {
            LOGGER.error("[{}] Exception while writing proxy response to the response: {}", sessionId, e.getLocalizedMessage());
            httpResponse.setStatus(500);
        }
    }
}
