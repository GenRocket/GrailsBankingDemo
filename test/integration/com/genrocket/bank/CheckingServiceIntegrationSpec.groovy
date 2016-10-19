package com.genrocket.bank

import grails.test.spock.IntegrationSpec

class CheckingServiceIntegrationSpec extends IntegrationSpec {
  def checkingService
  def accountTestDataService
  def transactionTestDataService

  void "test check daily withdrawal limit reached"() {
    given:

    accountTestDataService.loadData(1)
    transactionTestDataService.loadData(3)

    Float amount = 100.0
    Date today = new Date()

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user
    CustomerLevel customerLevel = customer.customerLevel

    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)
    TransactionType transactionType = TransactionType.findByName(TransactionTypes.WITHDRAWAL_CHECKING.value)

    List<Transaction> transactions = Transaction.findAll()

    when:

    account.accountType = accountType
    account.save()

    customerLevel.dailyWithdrawalLimit = 100
    customerLevel.save()

    transactions.eachWithIndex { transaction, index ->
      transaction.amount = 50
      transaction.dateCreated = today
      transaction.transactionType = transactionType
      transaction.save()
    }

    Boolean reached = checkingService.checkDailyWithdrawalLimit(user, account, amount)

    then:

    reached
  }

  void "test check daily withdrawal limit not reached"() {
    given:

    accountTestDataService.loadData(1)
    transactionTestDataService.loadData(3)

    Float amount = 100.0
    Date today = new Date()

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user
    CustomerLevel customerLevel = customer.customerLevel

    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)
    TransactionType transactionType = TransactionType.findByName(TransactionTypes.WITHDRAWAL_CHECKING.value)

    List<Transaction> transactions = Transaction.findAll()

    when:

    account.accountType = accountType
    account.save()

    customerLevel.dailyWithdrawalLimit = 250
    customerLevel.save()

    transactions.eachWithIndex { transaction, index ->
      transaction.amount = 50
      transaction.dateCreated = today
      transaction.transactionType = transactionType
      transaction.save()
    }

    Boolean reached = checkingService.checkDailyWithdrawalLimit(user, account, amount)

    then:

    !reached
  }
}
