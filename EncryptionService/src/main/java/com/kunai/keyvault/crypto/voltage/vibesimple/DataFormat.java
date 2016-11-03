
package com.kunai.keyvault.crypto.voltage.vibesimple;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataFormat.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;simpleType name="DataFormat"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="Default"/&gt;
 *     &lt;enumeration value="SecureFileV1"/&gt;
 *     &lt;enumeration value="SecureFileV2"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DataFormat")
@XmlEnum
public enum DataFormat {

    @XmlEnumValue("Default")
    DEFAULT("Default"),
    @XmlEnumValue("SecureFileV1")
    SECURE_FILE_V_1("SecureFileV1"),
    @XmlEnumValue("SecureFileV2")
    SECURE_FILE_V_2("SecureFileV2");
    private final String value;

    DataFormat(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DataFormat fromValue(String v) {
        for (DataFormat c: DataFormat.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
