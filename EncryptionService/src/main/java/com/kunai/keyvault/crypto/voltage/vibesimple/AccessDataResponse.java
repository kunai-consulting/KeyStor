
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
 *         {@literal <}element name="dataOut" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/{@literal >}
 *         {@literal <}element name="signIdentity" type="{http://www.w3.org/2001/XMLSchema}string"/{@literal >}
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
    "dataOut",
    "signIdentity"
})
@XmlRootElement(name = "AccessDataResponse")
public class AccessDataResponse {

    @XmlElement(required = true)
    protected byte[] dataOut;
    @XmlElement(required = true)
    protected String signIdentity;

    /**
     * Gets the value of the dataOut property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDataOut() {
        return dataOut;
    }

    /**
     * Sets the value of the dataOut property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDataOut(byte[] value) {
        this.dataOut = value;
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

}
