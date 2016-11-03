
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
 *         &lt;element name="dataIn" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="encryptIdentity" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/&gt;
 *         &lt;element name="signIdentity" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="signAuthMethod" type="{http://voltage.com/vibesimple}AuthMethod"/&gt;
 *         &lt;element name="signAuthInfo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="outputFormat" type="{http://voltage.com/vibesimple}DataFormat"/&gt;
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
    "encryptIdentity",
    "signIdentity",
    "signAuthMethod",
    "signAuthInfo",
    "outputFormat"
})
@XmlRootElement(name = "ProtectData")
public class ProtectData {

    @XmlElement(required = true)
    protected byte[] dataIn;
    @XmlElement(required = true)
    protected List<String> encryptIdentity;
    @XmlElement(required = true)
    protected String signIdentity;
    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected AuthMethod signAuthMethod;
    @XmlElement(required = true)
    protected String signAuthInfo;
    @XmlElement(required = true)
    @XmlSchemaType(name = "token")
    protected DataFormat outputFormat;

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
     * @return an Array List of Strings representing the encrypted identity of a person.
     */
    public List<String> getEncryptIdentity() {
        if (encryptIdentity == null) {
            encryptIdentity = new ArrayList<String>();
        }
        return this.encryptIdentity;
    }

    /**
     * Gets the value of the signIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignIdentity() {
        return signIdentity;
    }

    /**
     * Sets the value of the signIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignIdentity(String value) {
        this.signIdentity = value;
    }

    /**
     * Gets the value of the signAuthMethod property.
     * 
     * @return
     *     possible object is
     *     {@link AuthMethod }
     *     
     */
    public AuthMethod getSignAuthMethod() {
        return signAuthMethod;
    }

    /**
     * Sets the value of the signAuthMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthMethod }
     *     
     */
    public void setSignAuthMethod(AuthMethod value) {
        this.signAuthMethod = value;
    }

    /**
     * Gets the value of the signAuthInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignAuthInfo() {
        return signAuthInfo;
    }

    /**
     * Sets the value of the signAuthInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignAuthInfo(String value) {
        this.signAuthInfo = value;
    }

    /**
     * Gets the value of the outputFormat property.
     * 
     * @return
     *     possible object is
     *     {@link DataFormat }
     *     
     */
    public DataFormat getOutputFormat() {
        return outputFormat;
    }

    /**
     * Sets the value of the outputFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataFormat }
     *     
     */
    public void setOutputFormat(DataFormat value) {
        this.outputFormat = value;
    }

}
