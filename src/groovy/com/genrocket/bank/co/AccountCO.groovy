package com.genrocket.bank.co

import com.genrocket.bank.AccountType
import com.genrocket.bank.Branch
import com.genrocket.bank.CardType
import com.genrocket.bank.CustomerLevel
import com.genrocket.bank.User
import grails.validation.Validateable

@Validateable
class AccountCO {
  Long userId
  Long branchId
  Long accountTypeId
  Long customerLevelId
  Long cardTypeId

  static constraints = {
    userId nullable: false
    branchId nullable: false
    accountTypeId nullable: false
    customerLevelId nullable: false
    cardTypeId nullable: false
  }

  User getUser() {
    userId ? User.get(userId) : null
  }

  Branch getBranch() {
    branchId ? Branch.get(branchId) : null
  }

  AccountType getAccountType() {
    accountTypeId ? AccountType.get(accountTypeId) : null
  }

  CustomerLevel getCustomerLevel() {
    customerLevelId ? CustomerLevel.get(customerLevelId) : null
  }

  CardType getCardType() {
    cardTypeId ? CardType.get(cardTypeId) : null
  }
}
