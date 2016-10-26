package com.genrocket.bank.co

import com.genrocket.bank.Account
import grails.validation.Validateable

@Validateable
class TransferCO {
  Long accountNumber
  Long currentAccountNumber

  static constraints = {
    accountNumber(nullable: false, blank: false, validator: { value, object ->
      if (value && !Account.countByAccountNumber(value)) {
        return "invalid.account.number"
      } else if (value == object.currentAccountNumber) {
        return "same.account.number"
      }
    })
  }
}
