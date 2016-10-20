package com.genrocket.bank

import grails.test.spock.IntegrationSpec

import java.text.SimpleDateFormat

class CheckingServiceIntegrationSpec extends IntegrationSpec {
  def checkingService
  def accountTestDataService
  def transactionTestDataService
  def transactionTypeTestDataService

  void "test deposit INVALID_AMOUNT_VALUE"() {
    given:

    accountTestDataService.loadData(1)

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user

    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)

    when:

    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    TransactionStatus status = checkingService.deposit(user, account, null)

    then:

    status == TransactionStatus.INVALID_AMOUNT_VALUE
  }

  void "test deposit ACCOUNT_NOT_CHECKING"() {
    given:

    accountTestDataService.loadData(1)

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user

    AccountType accountType = AccountType.findByName(AccountTypes.SAVINGS.value)

    when:

    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    TransactionStatus status = checkingService.deposit(user, account, 100.0.floatValue())

    then:

    status == TransactionStatus.ACCOUNT_NOT_CHECKING
  }

  void "test deposit ACCOUNT_NOT_ENABLED"() {
    given:

    accountTestDataService.loadData(1)

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user

    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)

    when:

    account.accountType = accountType
    account.save()

    customer.enabled = false
    customer.save()

    TransactionStatus status = checkingService.deposit(user, account, 100.0.floatValue())

    then:

    status == TransactionStatus.ACCOUNT_NOT_ENABLED
  }

  void "test deposit TRANSACTION_COMPLETE"() {
    given:

    accountTestDataService.loadData(1)
    transactionTypeTestDataService.loadData()

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user

    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)
    TransactionType transactionType = TransactionType.findByName(TransactionTypes.DEPOSIT_CHECKING.value)

    SimpleDateFormat sdf = new SimpleDateFormat('yyyy/MM/dd')

    when:

    Float balance = 1000.95
    Float deposit = 150.25

    account.balance = balance
    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    TransactionStatus status = checkingService.deposit(user, account, deposit)

    Transaction transaction = Transaction.findByUserAndAccount(user, account)
    String dateCreated = sdf.parse(sdf.format(transaction.dateCreated))
    String today = sdf.parse(sdf.format(new Date()))
    account = Account.get(account.id)
    Float oldBalance = balance.round(2) + deposit.round(2)

    then:

    status == TransactionStatus.TRANSACTION_COMPLETE
    transaction != null
    transaction.transactionType == transactionType
    transaction.amount.round(2) == deposit.round(2)

    ((Float) account.balance).round(2) == oldBalance.round(2)
    dateCreated == today
  }

  void "test withdrawal INVALID_AMOUNT_VALUE"() {
    given:

    accountTestDataService.loadData(1)

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user

    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)

    when:

    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    TransactionStatus status = checkingService.withdrawal(user, account, null)

    then:

    status == TransactionStatus.INVALID_AMOUNT_VALUE
  }

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
    customerLevel.dailyWithdrawalLimit = 100.0
    customerLevel.save()

    TransactionStatus status = checkingService.withdrawal(user, account, 100.0.floatValue())

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
    customerLevel.dailyWithdrawalLimit = 100.0
    customerLevel.save()

    TransactionStatus status = checkingService.withdrawal(user, account, 100.0.floatValue())

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

    account.balance = 100.0
    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    customerLevel.overdraftAllowed = false
    customerLevel.dailyWithdrawalLimit = 100.0
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
    customerLevel.dailyWithdrawalLimit = 150.0
    customerLevel.dailyWithdrawalLimit = 100.0
    customerLevel.save()

    checkingService.withdrawal(user, account, 250.0.floatValue())

    TransactionStatus status = checkingService.withdrawal(user, account, 250.0.floatValue())

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
    Float withdrawalAmount = 250.0

    SimpleDateFormat sdf = new SimpleDateFormat('yyyy/MM/dd')

    when:

    account.balance = balance
    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    customerLevel.overdraftAllowed = false
    customerLevel.dailyWithdrawalLimit = 1000.0
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

    customerLevel.dailyWithdrawalLimit = 100.0
    customerLevel.save()

    transactions.eachWithIndex { transaction, index ->
      transaction.amount = 50.0
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

    customerLevel.dailyWithdrawalLimit = 250.0
    customerLevel.save()

    transactions.eachWithIndex { transaction, index ->
      transaction.amount = 50.0
      transaction.dateCreated = today
      transaction.transactionType = transactionType
      transaction.save()
    }

    Boolean reached = checkingService.checkDailyWithdrawalLimit(user, account, amount)

    then:

    !reached
  }
}
