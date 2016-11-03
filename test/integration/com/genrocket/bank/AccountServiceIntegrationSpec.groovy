package com.genrocket.bank

import grails.test.spock.IntegrationSpec

/**
 * Created by htaylor on 10/16/16.
 */
class AccountServiceIntegrationSpec extends IntegrationSpec {
  def accountService
  def userTestDataService
  def customerLevelService
  def branchTestDataService
  def cardTypeTestDataService
  def cardPoolTestDataService
  def customerTestDataService
  def transactionCreatorService
  def accountTypeTestDataService
  def customerLevelTestDataService

  void "create account"() {
    given:

    cardPoolTestDataService.loadData(100)

    accountTypeTestDataService.loadData()
    AccountType accountType = AccountType.first()

    branchTestDataService.loadData()
    Branch branch = Branch.first()

    userTestDataService.loadData()
    User user = User.first()

    cardTypeTestDataService.loadData()
    CardType cardType = CardType.first()

    customerLevelTestDataService.loadData()
    CustomerLevel customerLevel = CustomerLevel.first()

    when:

    accountService.save(user, branch, cardType, accountType, customerLevel)

    then:

    Account account = Account.findByBranchAndAccountType(branch, accountType)

    account.id
    account.branch == branch
    account.accountType == accountType

    Customer customer = Customer.findByAccountAndUser(account, user)

    customer.id
    customer.customerLevel == customerLevel

    Card card = Card.findByCustomer(customer)

    card.id
    card.cardType == cardType
  }

  void "test accountNumber for unique"() {
    given:

    cardPoolTestDataService.loadData(100)

    accountTypeTestDataService.loadData()
    AccountType accountType = AccountType.first()

    branchTestDataService.loadData()
    Branch branch = Branch.first()

    userTestDataService.loadData()
    User user = User.first()

    cardTypeTestDataService.loadData()
    CardType cardType = CardType.first()

    customerLevelTestDataService.loadData()
    CustomerLevel customerLevel = CustomerLevel.first()

    Account account1 = accountService.save(user, branch, cardType, accountType, customerLevel)
    Account account2 = accountService.save(user, branch, cardType, accountType, customerLevel)

    when:

    account2.accountNumber = account1.accountNumber
    account2.save()

    then:

    account2.errors.getFieldError("accountNumber").code == "unique"
  }

  void "test checkOverdraftAllowed"() {
    given:

    customerTestDataService.loadData()

    Customer customer = Customer.first()
    CustomerLevel customerLevel = customer.customerLevel
    Account account = customer.account
    User user = customer.user

    when:

    customerLevel.overdraftAllowed = true
    customerLevelService.update(customerLevel)

    Boolean allowed = accountService.checkOverdraftAllowed(user, account)

    then:

    allowed
  }

  void "test not checkOverdraftAllowed"() {
    given:

    customerTestDataService.loadData()

    Customer customer = Customer.first()
    CustomerLevel customerLevel = customer.customerLevel
    Account account = customer.account
    User user = customer.user

    when:

    customerLevel.overdraftAllowed = false
    customerLevelService.update(customerLevel)

    Boolean allowed = accountService.checkOverdraftAllowed(user, account)

    then:

    !allowed
  }

  void "test findAccounts one account"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map info = transactionCreatorService.getUserAccountInformation(1)

    User user = (User) info['user']
    AccountType accountType = (AccountType) info['checkingType']
    Account account = (Account) info['checkingAccount']

    when:

    List<Account> accounts = accountService.findAccounts(user, accountType)

    then:

    accounts.size() == 1
    accounts.getAt(0) == account
  }

  void "test findAccounts multiple accounts"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map info = transactionCreatorService.getUserAccountInformation(1)

    User user = (User) info['user']
    Branch branch = (Branch) info['branch']
    CardType cardType = (CardType) info['cardType']
    AccountType savingsType = (AccountType) info['savingsType']
    AccountType checkingType = (AccountType) info['checkingType']
    CustomerLevel customerLevel = (CustomerLevel) info['customerLevel']

    accountService.save(user, branch, cardType, checkingType, customerLevel)
    accountService.save(user, branch, cardType, savingsType, customerLevel)

    accountService.save(user, branch, cardType, checkingType, customerLevel)
    accountService.save(user, branch, cardType, savingsType, customerLevel)

    when:

    List<Account> accounts = accountService.findAccounts(user, checkingType)

    then:

    accounts.size() == 3
  }

  void "test hasEnabledCustomer with enabled accounts"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map info = transactionCreatorService.getUserAccountInformation(1)
    Account account = (Account) info['checkingAccount']

    when:

    Boolean enabled = accountService.hasEnabledCustomer(account)

    then:

    enabled
  }

  void "test hasEnabledCustomer with !enabled accounts"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map info = transactionCreatorService.getUserAccountInformation(1)
    Account account = (Account) info['checkingAccount']
    Customer customer = (Customer) info['checkingCustomer']

    when:

    customer.enabled = false
    customer.save()

    Boolean enabled = accountService.hasEnabledCustomer(account)

    then:

    !enabled
  }
}
