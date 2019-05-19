package com.kunai.keyvault.controllers;

import com.kunai.keyvault.crypto.EncryptionComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * Proxy a request to a remote resource
 */
@Controller
@Api(value = "Encryption services")
public class EncryptionResource {
    @Autowired
    private EncryptionComponent encryptionComponent;

    public static final String FORMAT_FIRST_4_LAST_4_SST = "First 4 Last 4 SST";

    /**
     * Encrypt data
     * @param type The type of encryption. Must be one of 'generic', 'test', 'card_data', 'ssn'.  When this parameter is set to 'test' data will simply be wrapped in 'encrypted {}'
     * @param data The data to encrypt
     * @return Returns the response from the end-point or 400 (malformed request) if the header value was not set.
     */
    @ApiOperation(value="Encrypt data", response = String.class)
    @PostMapping(path = "encrypt")
    @ResponseBody
    public String encryptPost(
            @ApiParam(value="Type of data to encrypt [generic, test, card_data, ssn]")
            @RequestParam(name="type", required=false, defaultValue="generic")
                    String type,
            @ApiParam(value = "Data to encrypt")
            @RequestBody
                    String data
    ) {
        type = type.toLowerCase().trim();

        if (type.equals("generic")) {
            return encryptionComponent.encryptor.protectGenericData(data);
        }
        if (type.equals("test")) {
            return "encrypted {" + data + "}";
        }
        if (type.equals("card_data")) {
            return encryptionComponent.encryptor.protectFormattedData(data, FORMAT_FIRST_4_LAST_4_SST);
        }
        if (type.equals("ssn")) {
            return encryptionComponent.encryptor.protectSocialSecurityNumber(data);
        }


        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Encryption type " + type + " is not supported");
    }
}
