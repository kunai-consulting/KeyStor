
package com.kunai.keyvault.crypto.voltage.vibesimple;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


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
 *         {@literal <}element name="encryptIdentity" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/{@literal >}
 *         {@literal <}element name="authenticationIdentity" type="{http://www.w3.org/2001/XMLSchema}string"/{@literal >}
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
    "encryptIdentity",
    "authenticationIdentity",
    "authMethod",
    "authInfo"
})
@XmlRootElement(name = "ProtectUnsignedData")
public class ProtectUnsignedData {

    @XmlElement(required = true)
    protected byte[] dataIn;
    @XmlElement(required = true)
    protected List<String> encryptIdentity;
    @XmlElement(required = true)
    protected String authenticationIdentity;
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
     * Gets the value of the encryptIdentity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the encryptIdentity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEncryptIdentity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getEncryptIdentity() {
        if (encryptIdentity == null) {
            encryptIdentity = new ArrayList<String>();
        }
        return this.encryptIdentity;
    }

    /**
     * Gets the value of the authenticationIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationIdentity() {
        return authenticationIdentity;
    }

    /**
     * Sets the value of the authenticationIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationIdentity(String value) {
        this.authenticationIdentity = value;
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
