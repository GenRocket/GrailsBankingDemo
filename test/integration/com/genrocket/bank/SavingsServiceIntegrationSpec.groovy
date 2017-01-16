package com.genrocket.bank

import grails.test.spock.IntegrationSpec

import java.text.SimpleDateFormat

/**
 * Created by htaylor on 10/22/16.
 */
class SavingsServiceIntegrationSpec extends IntegrationSpec {
  def savingsService
  def accountTestDataService
  def transactionCreatorService
  def transactionTypeTestDataService

  void "test deposit INVALID_AMOUNT_VALUE"() {
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

    TransactionStatus status = savingsService.deposit(user, account, -1)

    then:

    status == TransactionStatus.INVALID_AMOUNT_VALUE
  }

  void "test deposit ACCOUNT_NOT_SAVINGS"() {
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

    TransactionStatus status = savingsService.deposit(user, account, (float) 100.0)

    then:

    status == TransactionStatus.ACCOUNT_NOT_SAVINGS
  }

  void "test deposit ACCOUNT_NOT_ENABLED"() {
    given:

    accountTestDataService.loadData(1)

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user

    AccountType accountType = AccountType.findByName(AccountTypes.SAVINGS.value)

    when:

    account.accountType = accountType
    account.save()

    customer.enabled = false
    customer.save()

    TransactionStatus status = savingsService.deposit(user, account, (float) 100.0)

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

    AccountType accountType = AccountType.findByName(AccountTypes.SAVINGS.value)
    TransactionType transactionType = TransactionType.findByName(TransactionTypes.DEPOSIT_SAVINGS.value)

    SimpleDateFormat sdf = new SimpleDateFormat('yyyy/MM/dd')

    when:

    Float balance = 1000.95
    Float deposit = 150.25

    account.balance = balance
    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    TransactionStatus status = savingsService.deposit(user, account, deposit)

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

    AccountType accountType = AccountType.findByName(AccountTypes.SAVINGS.value)

    when:

    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    TransactionStatus status = savingsService.withdrawal(user, account, null)

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

    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)

    when:

    account.balance = 100.0
    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    customerLevel.overdraftAllowed = true
    customerLevel.dailyWithdrawalLimit = 100.0
    customerLevel.save()

    TransactionStatus status = savingsService.withdrawal(user, account, 100.0.floatValue())

    then:

    status == TransactionStatus.ACCOUNT_NOT_SAVINGS
  }

  void "test withdrawal ACCOUNT_NOT_ENABLED"() {
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

    customer.enabled = false
    customer.save()

    customerLevel.overdraftAllowed = true
    customerLevel.dailyWithdrawalLimit = 100.0
    customerLevel.save()

    TransactionStatus status = savingsService.withdrawal(user, account, 100.0.floatValue())

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

    AccountType accountType = AccountType.findByName(AccountTypes.SAVINGS.value)

    when:

    account.balance = 100.0
    account.accountType = accountType
    account.save()

    customer.enabled = true
    customer.save()

    customerLevel.overdraftAllowed = false
    customerLevel.dailyWithdrawalLimit = 100.0
    customerLevel.save()

    TransactionStatus status = savingsService.withdrawal(user, account, 101)

    then:

    status == TransactionStatus.OVERDRAFT_NOT_ALLOWED
  }

  void "test withdrawal TRANSACTION_COMPLETE"() {
    given:

    accountTestDataService.loadData(1)
    transactionTypeTestDataService.loadData()

    Account account = Account.first()
    Customer customer = Customer.findByAccount(account)
    User user = customer.user
    CustomerLevel customerLevel = customer.customerLevel

    AccountType accountType = AccountType.findByName(AccountTypes.SAVINGS.value)
    TransactionType transactionType = TransactionType.findByName(TransactionTypes.WITHDRAWAL_SAVINGS.value)

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

    TransactionStatus status = savingsService.withdrawal(user, account, withdrawalAmount)
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

  void "test transfer INVALID_AMOUNT_VALUE"() {
    given:

    Map map = transactionCreatorService.createCheckingAndSavingsAccount()

    User user = (User) map['user']
    Account checkingAccount = (Account) map['checkingAccount']
    Account savingsAccount = (Account) map['savingsAccount']

    when:

    TransactionStatus status = savingsService.transfer(user, savingsAccount, checkingAccount, null)

    then:

    status == TransactionStatus.INVALID_AMOUNT_VALUE
  }

  void "test transfer with checking ACCOUNT_NOT_ENABLED"() {
    given:

    Map map = transactionCreatorService.createCheckingAndSavingsAccount()

    User user = (User) map['user']
    Account checkingAccount = (Account) map['checkingAccount']
    Account savingsAccount = (Account) map['savingsAccount']
    Customer customer = (Customer) map['checkingCustomer']

    savingsAccount.balance = 500
    savingsAccount.save()

    customer.enabled = false
    customer.save()

    when:

    TransactionStatus status = savingsService.transfer(user, savingsAccount, checkingAccount, 100)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.ACCOUNT_NOT_ENABLED
    transactions.size() == 0
  }

  void "test transfer with savings ACCOUNT_NOT_ENABLED"() {
    given:

    Map map = transactionCreatorService.createCheckingAndSavingsAccount()

    User user = (User) map['user']
    Account checkingAccount = (Account) map['checkingAccount']
    Account savingsAccount = (Account) map['savingsAccount']
    Customer customer = (Customer) map['savingsCustomer']

    savingsAccount.balance = 500
    savingsAccount.save()

    customer.enabled = false
    customer.save()

    when:

    TransactionStatus status = savingsService.transfer(user, savingsAccount, checkingAccount, 100)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.ACCOUNT_NOT_ENABLED
    transactions.size() == 0
  }

  void "test MAX_TRANSFERS_EXCEEDED"() {
    given:

    Map map = transactionCreatorService.createCheckingAndSavingsAccount()

    User user = (User) map['user']
    Account checkingAccount = (Account) map['checkingAccount']
    Account savingsAccount = (Account) map['savingsAccount']
    CustomerLevel customerLevel = (CustomerLevel) map['savingsCustomerLevel']

    savingsAccount.balance = 5000
    savingsAccount.save()

    customerLevel.dailyWithdrawalLimit = 10000
    customerLevel.monthlyMaxTransfersAllowed = 3
    customerLevel.save()

    when:

    savingsService.transfer(user, savingsAccount, checkingAccount, 500)
    savingsService.transfer(user, savingsAccount, checkingAccount, 500)
    savingsService.transfer(user, savingsAccount, checkingAccount, 500)
    TransactionStatus status = savingsService.transfer(user, savingsAccount, checkingAccount, 500)

    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.MAX_TRANSFERS_EXCEEDED
    transactions.size() == 6
  }

  void "test transfer from savings to savings ACCOUNT_NOT_ENABLE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)

    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Map toInfo = transactionCreatorService.getUserAccountInformation(2)

    User fromUser = (User) fromInfo['user']

    Customer fromCustomer = (Customer) fromInfo['savingsCustomer']

    Account fromAccount = (Account) fromInfo['savingsAccount']
    Account toAccount = (Account) toInfo['savingsAccount']

    fromCustomer.enabled = false
    fromCustomer.save()

    fromAccount.balance = 100.00
    fromAccount.save()

    when:

    TransactionStatus status = savingsService.transferSavingsToSavings(fromUser, fromAccount, toAccount, (float) 1.0)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.ACCOUNT_NOT_ENABLED
    transactions.size() == 0
  }

  void "test transfer from savings to savings INVALID_AMOUNT_VALUE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)

    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Map toInfo = transactionCreatorService.getUserAccountInformation(2)

    User fromUser = (User) fromInfo['user']

    Account fromAccount = (Account) fromInfo['savingsAccount']
    Account toAccount = (Account) toInfo['savingsAccount']

    fromAccount.balance = 100.00
    fromAccount.save()

    when:

    TransactionStatus status = savingsService.transferSavingsToSavings(fromUser, fromAccount, toAccount, (float) 0.0)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.INVALID_AMOUNT_VALUE
    transactions.size() == 0
  }

  void "test transfer from savings to savings AMOUNT_GT_BALANCE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)

    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Map toInfo = transactionCreatorService.getUserAccountInformation(2)

    User fromUser = (User) fromInfo['user']

    Account fromAccount = (Account) fromInfo['savingsAccount']
    Account toAccount = (Account) toInfo['savingsAccount']

    fromAccount.balance = 100.00
    fromAccount.save()

    when:

    TransactionStatus status = savingsService.transferSavingsToSavings(fromUser, fromAccount, toAccount, (float) 100.01)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.AMOUNT_GT_BALANCE
    transactions.size() == 0
  }

  void "test transfer from savings to savings fromUser ACCOUNT_NOT_ENABLE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)

    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Map toInfo = transactionCreatorService.getUserAccountInformation(2)

    User fromUser = (User) fromInfo['user']

    Customer fromCustomer = (Customer) fromInfo['savingsCustomer']

    Account fromAccount = (Account) fromInfo['savingsAccount']
    Account toAccount = (Account) toInfo['savingsAccount']

    fromCustomer.enabled = false
    fromCustomer.save()

    fromAccount.balance = 100.00
    fromAccount.save()

    when:

    TransactionStatus status = savingsService.transferSavingsToSavings(fromUser, fromAccount, toAccount, (float) 1.0)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.ACCOUNT_NOT_ENABLED
    transactions.size() == 0
  }

  void "test transfer from savings to savings toUser ACCOUNT_NOT_ENABLE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)

    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Map toInfo = transactionCreatorService.getUserAccountInformation(2)

    User fromUser = (User) fromInfo['user']

    Customer toCustomer = (Customer) toInfo['savingsCustomer']

    Account fromAccount = (Account) fromInfo['savingsAccount']
    Account toAccount = (Account) toInfo['savingsAccount']

    toCustomer.enabled = false
    toCustomer.save()

    fromAccount.balance = 100.00
    fromAccount.save()

    when:

    TransactionStatus status = savingsService.transferSavingsToSavings(fromUser, fromAccount, toAccount, (float) 1.0)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.ACCOUNT_NOT_ENABLED
    transactions.size() == 0
  }

  void "test transfer from savings to savings TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)

    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Map toInfo = transactionCreatorService.getUserAccountInformation(2)

    User fromUser = (User) fromInfo['user']

    Account fromAccount = (Account) fromInfo['savingsAccount']
    Account toAccount = (Account) toInfo['savingsAccount']

    Float balance = 100.00
    Float amount = 50.00

    fromAccount.balance = balance
    fromAccount.save()

    toAccount.balance = balance
    toAccount.save()

    when:

    TransactionStatus status = savingsService.transferSavingsToSavings(fromUser, fromAccount, toAccount, amount)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.TRANSACTION_COMPLETE
    fromAccount.balance == balance - amount
    toAccount.balance == balance + amount
    transactions.size() == 2
  }
}
