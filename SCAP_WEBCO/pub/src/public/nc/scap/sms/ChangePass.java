
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
 *         &lt;element name="EPID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="User_Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OldPass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NewPass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Confirm_Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "epid",
    "userName",
    "oldPass",
    "newPass",
    "confirmPassword"
})
@XmlRootElement(name = "ChangePass")
public class ChangePass {

    @XmlElement(name = "EPID")
    protected String epid;
    @XmlElement(name = "User_Name")
    protected String userName;
    @XmlElement(name = "OldPass")
    protected String oldPass;
    @XmlElement(name = "NewPass")
    protected String newPass;
    @XmlElement(name = "Confirm_Password")
    protected String confirmPassword;

    /**
     * Gets the value of the epid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEPID() {
        return epid;
    }

    /**
     * Sets the value of the epid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEPID(String value) {
        this.epid = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the oldPass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldPass() {
        return oldPass;
    }

    /**
     * Sets the value of the oldPass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldPass(String value) {
        this.oldPass = value;
    }

    /**
     * Gets the value of the newPass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewPass() {
        return newPass;
    }

    /**
     * Sets the value of the newPass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewPass(String value) {
        this.newPass = value;
    }

    /**
     * Gets the value of the confirmPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the value of the confirmPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfirmPassword(String value) {
        this.confirmPassword = value;
    }

}
