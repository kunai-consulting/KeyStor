
package com.kunai.keyvault.crypto.voltage.vibesimple;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataRange complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * {@literal <}complexType name="DataRange"{@literal >}
 *   {@literal <}complexContent{@literal >}
 *     {@literal <}restriction base="{http://www.w3.org/2001/XMLSchema}anyType"{@literal >}
 *       {@literal <}sequence{@literal >}
 *         {@literal <}element name="data1" type="{http://www.w3.org/2001/XMLSchema}string"/{@literal >}
 *         {@literal <}element name="data2" type="{http://www.w3.org/2001/XMLSchema}string"/{@literal >}
 *       {@literal <}/sequence{@literal >}
 *     {@literal <}/restriction{@literal >}
 *   {@literal <}/complexContent{@literal >}
 * {@literal <}/complexType{@literal >}
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataRange", propOrder = {
    "data1",
    "data2"
})
public class DataRange {

    @XmlElement(required = true)
    protected String data1;
    @XmlElement(required = true, nillable = true)
    protected String data2;

    /**
     * Gets the value of the data1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getData1() {
        return data1;
    }

    /**
     * Sets the value of the data1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setData1(String value) {
        this.data1 = value;
    }

    /**
     * Gets the value of the data2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getData2() {
        return data2;
    }

    /**
     * Sets the value of the data2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setData2(String value) {
        this.data2 = value;
    }

}
