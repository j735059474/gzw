
package nc.scap.pub.sms.service.ah;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the _1._0._0._127._8080.ahgzwsms package. 
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

    private final static QName _SendMsgSvr_QNAME = new QName("http://127.0.0.1:8080/ahgzwsms", "sendMsgSvr");
    private final static QName _SendMsgSvrResponse_QNAME = new QName("http://127.0.0.1:8080/ahgzwsms", "sendMsgSvrResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: _1._0._0._127._8080.ahgzwsms
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendMsgSvr }
     * 
     */
    public SendMsgSvr createSendMsgSvr() {
        return new SendMsgSvr();
    }

    /**
     * Create an instance of {@link SendMsgSvrResponse }
     * 
     */
    public SendMsgSvrResponse createSendMsgSvrResponse() {
        return new SendMsgSvrResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMsgSvr }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://127.0.0.1:8080/ahgzwsms", name = "sendMsgSvr")
    public JAXBElement<SendMsgSvr> createSendMsgSvr(SendMsgSvr value) {
        return new JAXBElement<SendMsgSvr>(_SendMsgSvr_QNAME, SendMsgSvr.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMsgSvrResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://127.0.0.1:8080/ahgzwsms", name = "sendMsgSvrResponse")
    public JAXBElement<SendMsgSvrResponse> createSendMsgSvrResponse(SendMsgSvrResponse value) {
        return new JAXBElement<SendMsgSvrResponse>(_SendMsgSvrResponse_QNAME, SendMsgSvrResponse.class, null, value);
    }

}
