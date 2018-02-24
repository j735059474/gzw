
package nc.scap.sms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="User_OverageResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "userOverageResult"
})
@XmlRootElement(name = "User_OverageResponse")
public class UserOverageResponse {

    @XmlElement(name = "User_OverageResult")
    protected String userOverageResult;

    /**
     * Gets the value of the userOverageResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserOverageResult() {
        return userOverageResult;
    }

    /**
     * Sets the value of the userOverageResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserOverageResult(String value) {
        this.userOverageResult = value;
    }

}
