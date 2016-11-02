package com.genrocket.bank

import grails.test.spock.IntegrationSpec

import java.text.SimpleDateFormat

class CheckingServiceIntegrationSpec extends IntegrationSpec {
  def checkingService
  def accountTestDataService
  def transactionCreatorService
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

  void "test transfer INVALID_AMOUNT_VALUE"() {
    given:

    Map map = transactionCreatorService.createCheckingAndSavingsAccount()

    User user = (User) map['user']
    Account checkingAccount = (Account) map['checkingAccount']
    Account savingsAccount = (Account) map['savingsAccount']

    when:

    TransactionStatus status = checkingService.transfer(user, checkingAccount, savingsAccount, null)

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

    checkingAccount.balance = 500
    checkingAccount.save()

    customer.enabled = false
    customer.save()

    when:

    TransactionStatus status = checkingService.transfer(user, checkingAccount, savingsAccount, 100)
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

    checkingAccount.balance = 500
    checkingAccount.save()

    customer.enabled = false
    customer.save()

    when:

    TransactionStatus status = checkingService.transfer(user, checkingAccount, savingsAccount, 100)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.ACCOUNT_NOT_ENABLED
    transactions.size() == 0
  }

  void "test transfer from checking to checking ACCOUNT_NOT_ENABLE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)

    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Map toInfo = transactionCreatorService.getUserAccountInformation(2)

    User fromUser = (User) fromInfo['user']
    User toUser = (User) toInfo['user']

    Customer fromCustomer = (Customer) fromInfo['checkingCustomer']

    Account fromAccount = (Account) fromInfo['checkingAccount']
    Account toAccount = (Account) toInfo['checkingAccount']

    fromCustomer.enabled = false
    fromCustomer.save()

    fromAccount.balance = 100.00
    fromAccount.save()

    when:

    TransactionStatus status = checkingService.transferCheckingToChecking(fromUser, toUser, fromAccount, toAccount, (float) 1.0)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.ACCOUNT_NOT_ENABLED
    transactions.size() == 0
  }

  void "test transfer from checking to checking INVALID_AMOUNT_VALUE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)

    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Map toInfo = transactionCreatorService.getUserAccountInformation(2)

    User fromUser = (User) fromInfo['user']
    User toUser = (User) toInfo['user']

    Account fromAccount = (Account) fromInfo['checkingAccount']
    Account toAccount = (Account) toInfo['checkingAccount']

    fromAccount.balance = 100.00
    fromAccount.save()

    when:

    TransactionStatus status = checkingService.transferCheckingToChecking(fromUser, toUser, fromAccount, toAccount, (float) 0.0)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.INVALID_AMOUNT_VALUE
    transactions.size() == 0
  }

  void "test transfer from checking to checking AMOUNT_GT_BALANCE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)

    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Map toInfo = transactionCreatorService.getUserAccountInformation(2)

    User fromUser = (User) fromInfo['user']
    User toUser = (User) toInfo['user']

    Account fromAccount = (Account) fromInfo['checkingAccount']
    Account toAccount = (Account) toInfo['checkingAccount']

    fromAccount.balance = 100.00
    fromAccount.save()

    when:

    TransactionStatus status = checkingService.transferCheckingToChecking(fromUser, toUser, fromAccount, toAccount, (float) 100.01)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.AMOUNT_GT_BALANCE
    transactions.size() == 0
  }

  void "test transfer from checking to checking fromUser ACCOUNT_NOT_ENABLE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)

    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Map toInfo = transactionCreatorService.getUserAccountInformation(2)

    User fromUser = (User) fromInfo['user']
    User toUser = (User) toInfo['user']

    Customer fromCustomer = (Customer) fromInfo['checkingCustomer']

    Account fromAccount = (Account) fromInfo['checkingAccount']
    Account toAccount = (Account) toInfo['checkingAccount']

    fromCustomer.enabled = false
    fromCustomer.save()

    fromAccount.balance = 100.00
    fromAccount.save()

    when:

    TransactionStatus status = checkingService.transferCheckingToChecking(fromUser, toUser, fromAccount, toAccount, (float) 1.0)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.ACCOUNT_NOT_ENABLED
    transactions.size() == 0
  }

  void "test transfer from checking to checking toUser ACCOUNT_NOT_ENABLE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)

    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Map toInfo = transactionCreatorService.getUserAccountInformation(2)

    User fromUser = (User) fromInfo['user']
    User toUser = (User) toInfo['user']

    Customer toCustomer = (Customer) toInfo['checkingCustomer']

    Account fromAccount = (Account) fromInfo['checkingAccount']
    Account toAccount = (Account) toInfo['checkingAccount']

    toCustomer.enabled = false
    toCustomer.save()

    fromAccount.balance = 100.00
    fromAccount.save()

    when:

    TransactionStatus status = checkingService.transferCheckingToChecking(fromUser, toUser, fromAccount, toAccount, (float) 1.0)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.ACCOUNT_NOT_ENABLED
    transactions.size() == 0
  }

  void "test transfer from checking to checking TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)

    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Map toInfo = transactionCreatorService.getUserAccountInformation(2)

    User fromUser = (User) fromInfo['user']
    User toUser = (User) toInfo['user']

    Account fromAccount = (Account) fromInfo['checkingAccount']
    Account toAccount = (Account) toInfo['checkingAccount']

    Float balance = 100.00
    Float amount = 50.00

    fromAccount.balance = balance
    fromAccount.save()

    toAccount.balance = balance
    toAccount.save()

    when:

    TransactionStatus status = checkingService.transferCheckingToChecking(fromUser, toUser, fromAccount, toAccount, amount)
    List<Transaction> transactions = Transaction.findAll()

    then:

    status == TransactionStatus.TRANSACTION_COMPLETE
    fromAccount.balance == balance - amount
    toAccount.balance == balance + amount
    transactions.size() == 2
  }
}
