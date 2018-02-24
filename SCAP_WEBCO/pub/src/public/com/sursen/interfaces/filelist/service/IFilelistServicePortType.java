
package com.sursen.interfaces.filelist.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "IFilelistServicePortType", targetNamespace = "http://service.filelist.interfaces.sursen.com")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface IFilelistServicePortType {


    /**
     * 
     * @param in2
     * @param in1
     * @param in0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "out", targetNamespace = "http://service.filelist.interfaces.sursen.com")
    @RequestWrapper(localName = "getToDoList", targetNamespace = "http://service.filelist.interfaces.sursen.com", className = "com.sursen.interfaces.filelist.service.GetToDoList")
    @ResponseWrapper(localName = "getToDoListResponse", targetNamespace = "http://service.filelist.interfaces.sursen.com", className = "com.sursen.interfaces.filelist.service.GetToDoListResponse")
    public String getToDoList(
        @WebParam(name = "in0", targetNamespace = "http://service.filelist.interfaces.sursen.com")
        String in0,
        @WebParam(name = "in1", targetNamespace = "http://service.filelist.interfaces.sursen.com")
        String in1,
        @WebParam(name = "in2", targetNamespace = "http://service.filelist.interfaces.sursen.com")
        int in2);

    /**
     * 
     * @param in1
     * @param in0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "out", targetNamespace = "http://service.filelist.interfaces.sursen.com")
    @RequestWrapper(localName = "getLoginStatus", targetNamespace = "http://service.filelist.interfaces.sursen.com", className = "com.sursen.interfaces.filelist.service.GetLoginStatus")
    @ResponseWrapper(localName = "getLoginStatusResponse", targetNamespace = "http://service.filelist.interfaces.sursen.com", className = "com.sursen.interfaces.filelist.service.GetLoginStatusResponse")
    public String getLoginStatus(
        @WebParam(name = "in0", targetNamespace = "http://service.filelist.interfaces.sursen.com")
        String in0,
        @WebParam(name = "in1", targetNamespace = "http://service.filelist.interfaces.sursen.com")
        String in1);

}