package com.genrocket.bank.temp

import wslite.soap.SOAPClient

/**
 * Created by htaylor on 4/18/17.
 */
class Example {

  public static void main(String[] args) {

  def client = new SOAPClient("http://localhost:8080/GrailsBankingDemo/services/bank")
  def response = client.send(connectTimeout: 5000,
    readTimeout: 10000,
    useCaches: false,
    followRedirects: false,
    sslTrustAllCerts: true,
    """<xs:Envelope xmlns:xs='http://schemas.xmlsoap.org/soap/envelope/'>
         <xs:Body>
           <ns1:createAccountType xmlns:ns1='http://bank.genrocket.com/'>
             <name>Foo</name>
           </ns1:createAccountType>
         </xs:Body>
       </xs:Envelope>"""
  )

    println response.text
  }
}
