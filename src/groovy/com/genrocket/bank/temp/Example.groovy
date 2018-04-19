package com.genrocket.bank.temp

import com.predic8.wsdl.Definitions
import com.predic8.wsdl.WSDLParser
import com.predic8.wstool.creator.RequestTemplateCreator
import com.predic8.wstool.creator.SOARequestCreator
import groovy.xml.MarkupBuilder
import wslite.soap.SOAPClient

/**
 * Created by htaylor on 4/18/17.
 */
class Example {

  public static void main(String[] args) {
      WSDLParser parser = new WSDLParser()

      Definitions wsdl = parser.parse("/home/htaylor/Desktop/Wsdl.xml")

      StringWriter writer = new StringWriter()

      //SOAPRequestCreator constructor: SOARequestCreator(Definitions, Creator, MarkupBuilder)
      SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestTemplateCreator(), new MarkupBuilder(writer))

      //creator.createRequest(PortType name, Operation name, Binding name)
      creator.createRequest("BankService", "createAccountType", "BankServiceServiceSoapBinding")

      println(writer)
  }  
}
//  def client = new SOAPClient("http://localhost:8080/GrailsBankingDemo/services/bank")
//  def response = client.send(connectTimeout: 5000,
//    readTimeout: 10000,
//    useCaches: false,
//    followRedirects: false,
//    sslTrustAllCerts: true,
//    """<xs:Envelope xmlns:xs='http://schemas.xmlsoap.org/soap/envelope/'>
//         <xs:Body>
//           <ns1:createAccountType xmlns:ns1='http://bank.genrocket.com/'>
//             <name>Foo</name>
//           </ns1:createAccountType>
//         </xs:Body>
//       </xs:Envelope>"""
//  )
//
//    println response.text
//  }
