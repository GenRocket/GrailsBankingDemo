package com.genrocket.bank.co

import com.genrocket.bank.Account
import grails.validation.Validateable

@Validateable
class TransferAmountCO {
  Long accountIdTo
  Long amount

  static constraints = {
    amount(nullable: false, blank: false)
    accountIdTo(nullable: false, blank: false, validator: { value, object ->
      if (value && !Account.countById(value)) {
        return "invalid.account.id"
      }
    })
  }

  Account getAccount() {
    return accountIdTo ? Account.get(accountIdTo) : null
  }
}
