package com.genrocket.bank

import grails.transaction.Transactional
import org.grails.cxf.utils.GrailsCxfEndpoint

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult

@Transactional
@GrailsCxfEndpoint
class BankService {

  @WebMethod
  @WebResult(name = "AccountType")
  AccountType createAccountType(@WebParam(name = 'name') String name) {
    AccountType.findOrSaveByName(name)
  }
}
