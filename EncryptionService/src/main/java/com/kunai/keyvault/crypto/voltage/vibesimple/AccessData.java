
package com.kunai.keyvault.crypto.voltage.vibesimple;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dataIn" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="decryptIdentity" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="decryptAuthMethod" type="{http://voltage.com/vibesimple}AuthMethod"/&gt;
 *         &lt;element name="decryptAuthInfo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dataIn",
    "decryptIdentity",
    "decryptAuthMethod",
    "decryptAuthInfo"
})
@XmlRootElement(name = "AccessData")
public class AccessData {

    @XmlElement(required = true)
    protected byte[] dataIn;
    @XmlElement(required = true)
    protected String decryptIdentity;
    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected AuthMethod decryptAuthMethod;
    @XmlElement(required = true)
    protected String decryptAuthInfo;

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
     * Gets the value of the decryptIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDecryptIdentity() {
        return decryptIdentity;
    }

    /**
     * Sets the value of the decryptIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDecryptIdentity(String value) {
        this.decryptIdentity = value;
    }

    /**
     * Gets the value of the decryptAuthMethod property.
     * 
     * @return
     *     possible object is
     *     {@link AuthMethod }
     *     
     */
    public AuthMethod getDecryptAuthMethod() {
        return decryptAuthMethod;
    }

    /**
     * Sets the value of the decryptAuthMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthMethod }
     *     
     */
    public void setDecryptAuthMethod(AuthMethod value) {
        this.decryptAuthMethod = value;
    }

    /**
     * Gets the value of the decryptAuthInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDecryptAuthInfo() {
        return decryptAuthInfo;
    }

    /**
     * Sets the value of the decryptAuthInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDecryptAuthInfo(String value) {
        this.decryptAuthInfo = value;
    }

}
