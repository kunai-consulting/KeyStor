
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
 *         &lt;element name="creditCardNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="identity" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="tweak" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="masked" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="authMethod" type="{http://voltage.com/vibesimple}AuthMethod"/&gt;
 *         &lt;element name="authInfo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "creditCardNumber",
    "identity",
    "tweak",
    "masked",
    "authMethod",
    "authInfo"
})
@XmlRootElement(name = "AccessCreditCardNumber")
public class AccessCreditCardNumber {

    @XmlElement(required = true)
    protected String creditCardNumber;
    @XmlElement(required = true)
    protected String identity;
    @XmlElement(required = true, nillable = true)
    protected String tweak;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean masked;
    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected AuthMethod authMethod;
    @XmlElement(required = true)
    protected String authInfo;

    /**
     * Gets the value of the creditCardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * Sets the value of the creditCardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreditCardNumber(String value) {
        this.creditCardNumber = value;
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
     * Gets the value of the tweak property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTweak() {
        return tweak;
    }

    /**
     * Sets the value of the tweak property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTweak(String value) {
        this.tweak = value;
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
