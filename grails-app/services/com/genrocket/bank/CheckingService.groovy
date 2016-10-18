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

  Boolean checkDailyWithdrawalLimit(User user, Account account, Float amount) {
    if (account.accountType.name != AccountTypes.CHECKING) {
      return false
    }

    Date today = new Date()
    Float totalWithdrawal = 0
    Customer customer = Customer.findByUserAndAccount(user, account)
    CustomerLevel customerLevel = customer.customerLevel
    Integer withdrawalLimit = customerLevel.dailyWithdrawalLimit

    TransactionType transactionType =
      TransactionType.findByNameGreaterThan(TransactionTypes.WITHDRAWAL_CHECKING.value)

    List<Transaction> transactions =
      Transaction.findAllByAccountAndDateCreatedGreaterThanEquals(account, today)

    transactions.each { transaction ->
      if (transaction.transactionType == transactionType) {
        totalWithdrawal += transaction.amount
      }
    }

    return withdrawalLimit < totalWithdrawal + amount
  }
}
