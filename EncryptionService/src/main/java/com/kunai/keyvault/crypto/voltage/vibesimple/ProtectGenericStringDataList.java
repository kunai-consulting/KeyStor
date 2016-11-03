
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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dataIn" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/&gt;
 *         &lt;element name="identity" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="generateTweak" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="authMethod" type="{http://voltage.com/vibesimple}AuthMethod"/&gt;
 *         &lt;element name="authInfo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="algorithm" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "identity",
    "generateTweak",
    "authMethod",
    "authInfo",
    "algorithm"
})
@XmlRootElement(name = "ProtectGenericStringDataList")
public class ProtectGenericStringDataList {

    @XmlElement(required = true)
    protected List<String> dataIn;
    @XmlElement(required = true)
    protected String identity;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean generateTweak;
    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected AuthMethod authMethod;
    @XmlElement(required = true)
    protected String authInfo;
    @XmlElement(required = true, nillable = true)
    protected String algorithm;

    /**
     * Gets the value of the dataIn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataIn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataIn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * @return an array list of strings for input.
     */
    public List<String> getDataIn() {
        if (dataIn == null) {
            dataIn = new ArrayList<String>();
        }
        return this.dataIn;
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
     * Gets the value of the generateTweak property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGenerateTweak() {
        return generateTweak;
    }

    /**
     * Sets the value of the generateTweak property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGenerateTweak(Boolean value) {
        this.generateTweak = value;
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

    /**
     * Gets the value of the algorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Sets the value of the algorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlgorithm(String value) {
        this.algorithm = value;
    }

}
