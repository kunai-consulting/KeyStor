
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
 *         &lt;element name="dataIn" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "dataIn"
})
@XmlRootElement(name = "GetGenericStringDataIdentity")
public class GetGenericStringDataIdentity {

    @XmlElement(required = true)
    protected String dataIn;

    /**
     * Gets the value of the dataIn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataIn() {
        return dataIn;
    }

    /**
     * Sets the value of the dataIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataIn(String value) {
        this.dataIn = value;
    }

}
