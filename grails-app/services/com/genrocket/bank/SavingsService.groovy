package com.genrocket.bank

import grails.transaction.Transactional

@Transactional
class SavingsService {
  def checkingService

  void deposit(User user, Account account, Float amount) {

  }

  void withdrawal(User user, Account account, Float amount) {

  }

  void transfer(User user, Account savings, Account checking, Float amount) {
    withdrawal(user, savings, amount)
    checkingService.deposit(user, checking, amount)
  }
}
