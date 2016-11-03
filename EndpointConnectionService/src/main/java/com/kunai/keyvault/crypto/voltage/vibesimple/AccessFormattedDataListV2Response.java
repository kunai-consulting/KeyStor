
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
    "dataOut"
})
@XmlRootElement(name = "AccessFormattedDataListV2Response")
public class AccessFormattedDataListV2Response {

    @XmlElement(required = true)
    protected List<String> dataOut;

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
     * @return a list of strings for output data.
     */
    public List<String> getDataOut() {
        if (dataOut == null) {
            dataOut = new ArrayList<String>();
        }
        return this.dataOut;
    }

}
