package com.genrocket.bank.temp

import wslite.soap.SOAPClient

/**
 * Created by htaylor on 4/18/17.
 */
class Example {

  public static void main(String[] args) {

  def client = new SOAPClient("http://localhost:8080/GrailsBankingDemo/services/bank")
  def response = client.send(SOAPAction: "createAccountType",
    connectTimeout: 5000,
    readTimeout: 10000,
    useCaches: false,
    followRedirects: false,
    sslTrustAllCerts: true,
    """<xs:Envelope xmlns:xs='http://schemas.xmlsoap.org/soap/envelope/'>
        <xs:Body>
        <name>Checking</name>
      </xs:Body>
    </xs:Envelope>
    """)

    println response
  }
}
