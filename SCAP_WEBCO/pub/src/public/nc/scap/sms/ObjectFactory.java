
package nc.scap.sms;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the nc.scap.sms.ah package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _String_QNAME = new QName("http://access.xx95.net:8886/", "string");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: nc.scap.sms.ah
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RecvMessageResponse }
     * 
     */
    public RecvMessageResponse createRecvMessageResponse() {
        return new RecvMessageResponse();
    }

    /**
     * Create an instance of {@link RecvMessageExResponse }
     * 
     */
    public RecvMessageExResponse createRecvMessageExResponse() {
        return new RecvMessageExResponse();
    }

    /**
     * Create an instance of {@link UserOverage }
     * 
     */
    public UserOverage createUserOverage() {
        return new UserOverage();
    }

    /**
     * Create an instance of {@link RecvMessageResponse.RecvMessageResult }
     * 
     */
    public RecvMessageResponse.RecvMessageResult createRecvMessageResponseRecvMessageResult() {
        return new RecvMessageResponse.RecvMessageResult();
    }

    /**
     * Create an instance of {@link UserOverageResponse }
     * 
     */
    public UserOverageResponse createUserOverageResponse() {
        return new UserOverageResponse();
    }

    /**
     * Create an instance of {@link SendSms }
     * 
     */
    public SendSms createSendSms() {
        return new SendSms();
    }

    /**
     * Create an instance of {@link ChangePass }
     * 
     */
    public ChangePass createChangePass() {
        return new ChangePass();
    }

    /**
     * Create an instance of {@link ChangePassResponse }
     * 
     */
    public ChangePassResponse createChangePassResponse() {
        return new ChangePassResponse();
    }

    /**
     * Create an instance of {@link SendSmsResponse }
     * 
     */
    public SendSmsResponse createSendSmsResponse() {
        return new SendSmsResponse();
    }

    /**
     * Create an instance of {@link RecvMessageExResponse.RecvMessageExResult }
     * 
     */
    public RecvMessageExResponse.RecvMessageExResult createRecvMessageExResponseRecvMessageExResult() {
        return new RecvMessageExResponse.RecvMessageExResult();
    }

    /**
     * Create an instance of {@link ChkBadWord }
     * 
     */
    public ChkBadWord createChkBadWord() {
        return new ChkBadWord();
    }

    /**
     * Create an instance of {@link SendSmsExResponse }
     * 
     */
    public SendSmsExResponse createSendSmsExResponse() {
        return new SendSmsExResponse();
    }

    /**
     * Create an instance of {@link RecvMessage }
     * 
     */
    public RecvMessage createRecvMessage() {
        return new RecvMessage();
    }

    /**
     * Create an instance of {@link ChkBadWordResponse }
     * 
     */
    public ChkBadWordResponse createChkBadWordResponse() {
        return new ChkBadWordResponse();
    }

    /**
     * Create an instance of {@link SendSmsEx }
     * 
     */
    public SendSmsEx createSendSmsEx() {
        return new SendSmsEx();
    }

    /**
     * Create an instance of {@link RecvMessageEx }
     * 
     */
    public RecvMessageEx createRecvMessageEx() {
        return new RecvMessageEx();
    }

    /**
     * Create an instance of {@link SendSmsEx1 }
     * 
     */
    public SendSmsEx1 createSendSmsEx1() {
        return new SendSmsEx1();
    }

    /**
     * Create an instance of {@link SendSmsEx1Response }
     * 
     */
    public SendSmsEx1Response createSendSmsEx1Response() {
        return new SendSmsEx1Response();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://access.xx95.net:8886/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

}
