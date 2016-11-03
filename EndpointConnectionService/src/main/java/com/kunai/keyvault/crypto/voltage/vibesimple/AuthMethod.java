
package com.kunai.keyvault.crypto.voltage.vibesimple;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AuthMethod.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * {@literal <}simpleType name="AuthMethod"{@literal >}
 *   {@literal <}restriction base="{http://www.w3.org/2001/XMLSchema}token"{@literal >}
 *     {@literal <}enumeration value="UserPassword"/{@literal >}
 *     {@literal <}enumeration value="SharedSecret"/{@literal >}
 *     {@literal <}enumeration value="Certificate"/{@literal >}
 *     {@literal <}enumeration value="AuthToken_HMAC_SHA1"/{@literal >}
 *   {@literal <}/restriction{@literal >}
 * {@literal <}/simpleType{@literal >}
 * </pre>
 * 
 */
@XmlType(name = "AuthMethod")
@XmlEnum
public enum AuthMethod {

    @XmlEnumValue("UserPassword")
    USER_PASSWORD("UserPassword"),
    @XmlEnumValue("SharedSecret")
    SHARED_SECRET("SharedSecret"),
    @XmlEnumValue("Certificate")
    CERTIFICATE("Certificate"),
    @XmlEnumValue("AuthToken_HMAC_SHA1")
    AUTH_TOKEN_HMAC_SHA_1("AuthToken_HMAC_SHA1");
    private final String value;

    AuthMethod(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AuthMethod fromValue(String v) {
        for (AuthMethod c: AuthMethod.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
