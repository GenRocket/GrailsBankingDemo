package com.genrocket.bank.co

import com.genrocket.bank.Account
import com.genrocket.bank.CardType
import com.genrocket.bank.Customer
import com.genrocket.bank.CustomerLevel
import com.genrocket.bank.User
import grails.validation.Validateable

@Validateable
class AssociateAccountCO {
  Long accountNumber
  Long userId
  Long customerLevelId
  Long cardTypeId

  static constraints = {
    accountNumber(nullable: false, blank: false, validator: { value, object ->
      Account account = value ? Account.findByAccountNumber(value) : null
      if (!account) {
        return "invalid.account.number"
      } else if (object.user && Customer.findByAccountAndUser(account, object.user)) {
        return "already.associated.account"
      }
    })
    customerLevelId nullable: false
    cardTypeId nullable: false
    userId nullable: false
  }

  User getUser() {
    userId ? User.get(userId) : null
  }

  CustomerLevel getCustomerLevel() {
    customerLevelId ? CustomerLevel.get(customerLevelId) : null
  }

  CardType getCardType() {
    cardTypeId ? CardType.get(cardTypeId) : null
  }

}
