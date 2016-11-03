
package com.kunai.keyvault.crypto.voltage.vibesimple;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * {@literal <}complexType{@literal >}
 *   {@literal <}complexContent{@literal >}
 *     {@literal <}restriction base="{http://www.w3.org/2001/XMLSchema}anyType"{@literal >}
 *       {@literal <}sequence{@literal >}
 *         {@literal <}element name="dataIn" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/{@literal >}
 *         {@literal <}element name="identity" type="{http://www.w3.org/2001/XMLSchema}string"/{@literal >}
 *         {@literal <}element name="maskingFormat" type="{http://www.w3.org/2001/XMLSchema}string"/{@literal >}
 *         {@literal <}element name="masked" type="{http://www.w3.org/2001/XMLSchema}boolean"/{@literal >}
 *         {@literal <}element name="authMethod" type="{http://voltage.com/vibesimple}AuthMethod"/{@literal >}
 *         {@literal <}element name="authInfo" type="{http://www.w3.org/2001/XMLSchema}string"/{@literal >}
 *       {@literal <}/sequence{@literal >}
 *     {@literal <}/restriction{@literal >}
 *   {@literal <}/complexContent{@literal >}
 * {@literal <}/complexType{@literal >}
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dataIn",
    "identity",
    "maskingFormat",
    "masked",
    "authMethod",
    "authInfo"
})
@XmlRootElement(name = "AccessGenericData")
public class AccessGenericData {

    @XmlElement(required = true)
    protected byte[] dataIn;
    @XmlElement(required = true, nillable = true)
    protected String identity;
    @XmlElement(required = true, nillable = true)
    protected String maskingFormat;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean masked;
    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected AuthMethod authMethod;
    @XmlElement(required = true)
    protected String authInfo;

    /**
     * Gets the value of the dataIn property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDataIn() {
        return dataIn;
    }

    /**
     * Sets the value of the dataIn property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDataIn(byte[] value) {
        this.dataIn = value;
    }

    /**
     * Gets the value of the identity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentity() {
        return identity;
    }

    /**
     * Sets the value of the identity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentity(String value) {
        this.identity = value;
    }

    /**
     * Gets the value of the maskingFormat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaskingFormat() {
        return maskingFormat;
    }

    /**
     * Sets the value of the maskingFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaskingFormat(String value) {
        this.maskingFormat = value;
    }

    /**
     * Gets the value of the masked property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMasked() {
        return masked;
    }

    /**
     * Sets the value of the masked property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMasked(Boolean value) {
        this.masked = value;
    }

    /**
     * Gets the value of the authMethod property.
     * 
     * @return
     *     possible object is
     *     {@link AuthMethod }
     *     
     */
    public AuthMethod getAuthMethod() {
        return authMethod;
    }

    /**
     * Sets the value of the authMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthMethod }
     *     
     */
    public void setAuthMethod(AuthMethod value) {
        this.authMethod = value;
    }

    /**
     * Gets the value of the authInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthInfo() {
        return authInfo;
    }

    /**
     * Sets the value of the authInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthInfo(String value) {
        this.authInfo = value;
    }

}
