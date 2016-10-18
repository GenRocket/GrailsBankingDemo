package com.genrocket.bank

import grails.transaction.Transactional

@Transactional
class SavingsService {
  def checkingService

  void deposit(Account account, Float amount) {

  }

  void withdrawal(Account account, Float amount) {

  }

  void transfer(Account savings, Account checking, Float amount) {
    withdrawal(savings, amount)
    checkingService.deposit(checking, amount)
  }
}
