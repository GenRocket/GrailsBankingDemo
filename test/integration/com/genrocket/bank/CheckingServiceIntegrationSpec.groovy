package com.genrocket.bank

import grails.test.spock.IntegrationSpec

import java.text.SimpleDateFormat

class CheckingServiceIntegrationSpec extends IntegrationSpec {
  def checkingService
  def accountTestDataService
  def transactionTestDataService
  def transactionTypeTestDataService

  void "test withdrawal ACCOUNT_NOT_CHECKING"() {
    given:

    accountTestDataService.loadData(1)

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user
    CustomerLevel customerLevel = customer.customerLevel

    AccountType accountType = AccountType.findByName(AccountTypes.SAVINGS.value)

    when:

    account.balance = 100.0
    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    customerLevel.overdraftAllowed = true
    customerLevel.dailyWithdrawalLimit = 100
    customerLevel.save()

    TransactionStatus status = checkingService.withdrawal(user, account, 100)

    then:

    status == TransactionStatus.ACCOUNT_NOT_CHECKING
  }

  void "test withdrawal ACCOUNT_NOT_ENABLED"() {
    given:

    accountTestDataService.loadData(1)

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user
    CustomerLevel customerLevel = customer.customerLevel

    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)

    when:

    account.balance = 100.0
    account.accountType = accountType
    account.save()

    customer.enabled = false
    customer.save()

    customerLevel.overdraftAllowed = true
    customerLevel.dailyWithdrawalLimit = 100
    customerLevel.save()

    TransactionStatus status = checkingService.withdrawal(user, account, 100)

    then:

    status == TransactionStatus.ACCOUNT_NOT_ENABLED
  }

  void "test withdrawal OVERDRAFT_NOT_ALLOWED"() {
    given:

    accountTestDataService.loadData(1)

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user
    CustomerLevel customerLevel = customer.customerLevel

    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)

    when:

    account.balance = 100.00
    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    customerLevel.overdraftAllowed = false
    customerLevel.dailyWithdrawalLimit = 100
    customerLevel.save()

    TransactionStatus status = checkingService.withdrawal(user, account, 101)

    then:

    status == TransactionStatus.OVERDRAFT_NOT_ALLOWED
  }

  void "test withdrawal WITHDRAWAL_LIMIT_REACHED"() {
    given:

    accountTestDataService.loadData(1)

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user
    CustomerLevel customerLevel = customer.customerLevel

    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)

    when:

    account.balance = 500.0
    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    customerLevel.overdraftAllowed = false
    customerLevel.dailyWithdrawalLimit = 150
    customerLevel.dailyWithdrawalLimit = 100
    customerLevel.save()

    checkingService.withdrawal(user, account, 250)

    TransactionStatus status = checkingService.withdrawal(user, account, 250)

    then:

    status == TransactionStatus.WITHDRAWAL_LIMIT_REACHED
  }

  void "test withdrawal TRANSACTION_COMPLETE"() {
    given:

    accountTestDataService.loadData(1)
    transactionTypeTestDataService.loadData()

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user
    CustomerLevel customerLevel = customer.customerLevel

    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)
    TransactionType transactionType = TransactionType.findByName(TransactionTypes.WITHDRAWAL_CHECKING.value)

    Float balance = 500.00
    Float withdrawalAmount = 250

    SimpleDateFormat sdf = new SimpleDateFormat('yyyy/MM/dd')

    when:

    account.balance = balance
    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    customerLevel.overdraftAllowed = false
    customerLevel.dailyWithdrawalLimit = 1000
    customerLevel.save()

    checkingService.withdrawal(user, account, withdrawalAmount)

    TransactionStatus status = checkingService.withdrawal(user, account, withdrawalAmount)
    Transaction transaction = Transaction.findByUserAndAccount(user, account)
    String dateCreated = sdf.parse(sdf.format(transaction.dateCreated))
    String today = sdf.parse(sdf.format(new Date()))

    then:

    status == TransactionStatus.TRANSACTION_COMPLETE
    transaction != null
    transaction.transactionType == transactionType
    transaction.amount.round(2) == ((Float) balance - withdrawalAmount).round(2)
    dateCreated == today
  }

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

    customer.enabled = true
    customer.save()

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

    customer.enabled = true
    customer.save()

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
