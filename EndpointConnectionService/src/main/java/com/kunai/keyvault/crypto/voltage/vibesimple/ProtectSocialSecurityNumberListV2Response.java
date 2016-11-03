
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
 *         {@literal <}element name="dataOut" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/{@literal >}
 *         {@literal <}element name="fullApplicationIdentityOut" type="{http://www.w3.org/2001/XMLSchema}string"/{@literal >}
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
    "fullApplicationIdentityOut"
})
@XmlRootElement(name = "ProtectSocialSecurityNumberListV2Response")
public class ProtectSocialSecurityNumberListV2Response {

    @XmlElement(required = true)
    protected List<String> dataOut;
    @XmlElement(required = true)
    protected String fullApplicationIdentityOut;

    /**
     * Gets the value of the dataOut property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataOut property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataOut().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * @return a list of String for the output data.
     */
    public List<String> getDataOut() {
        if (dataOut == null) {
            dataOut = new ArrayList<String>();
        }
        return this.dataOut;
    }

    /**
     * Gets the value of the fullApplicationIdentityOut property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFullApplicationIdentityOut() {
        return fullApplicationIdentityOut;
    }

    /**
     * Sets the value of the fullApplicationIdentityOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFullApplicationIdentityOut(String value) {
        this.fullApplicationIdentityOut = value;
    }

}
