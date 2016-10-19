package com.genrocket.bank

import grails.transaction.Transactional

@Transactional
class SavingsService {
  def checkingService

  TransactionStatus deposit(User user, Account account, Float amount) {

  }

  TransactionStatus withdrawal(User user, Account account, Float amount) {

  }

  TransactionStatus transfer(User user, Account savings, Account checking, Float amount) {
    withdrawal(user, savings, amount)
    checkingService.deposit(user, checking, amount)
  }
}
