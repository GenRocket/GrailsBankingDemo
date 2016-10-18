package com.genrocket.bank

import grails.transaction.Transactional

@Transactional
class CheckingService {
  def savingsService

  void deposit(Account account, Float amount) {

  }

  void withdrawal(Account account, Float amount) {

  }

  void transfer(Account checking, Account savings, Float amount) {
    withdrawal(savings, amount)
    savingsService.deposit(checking, amount)
  }
}
