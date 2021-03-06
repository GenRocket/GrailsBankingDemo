package com.genrocket.bank.testUtil

import com.genrocket.bank.*
import grails.transaction.Transactional

@Transactional
class TransactionCreatorService {
  def cardService
  def accountService
  def userTestDataService
  def branchTestDataService
  def cardPoolTestDataService
  def cardTypeTestDataService
  def accountTypeTestDataService
  def customerLevelTestDataService
  def transactionTypeTestDataService

  void createCheckingAndSavingsAccounts(Integer count) {
    userTestDataService.loadData(count)
    cardPoolTestDataService.loadData(100 + count)
    transactionTypeTestDataService.loadData()

    accountTypeTestDataService.loadData()
    AccountType checkingType = AccountType.findByName(AccountTypes.CHECKING.value)
    AccountType savingsType = AccountType.findByName(AccountTypes.SAVINGS.value)

    branchTestDataService.loadData()
    Branch branch = Branch.first()

    cardTypeTestDataService.loadData()
    CardType cardType = CardType.first()

    customerLevelTestDataService.loadData()
    CustomerLevel customerLevel = CustomerLevel.first()

    List<User> users = User.findAll()

    users.each { user ->
      println "Creating accounts for User ${user.id}. ${user.firstName} ${user.lastName}"

      accountService.save(user, branch, cardType, checkingType, customerLevel)
      accountService.save(user, branch, cardType, savingsType, customerLevel)

      List<Customer> customers = Customer.findAllByUser(user) as Customer[]

      customers.each { customer ->
        customer.enabled = true
        customer.save()

        customer.customerLevel.dailyWithdrawalLimit = 500
        customer.customerLevel.monthlyMaxTransfersAllowed = 3
        customer.customerLevel.overdraftAllowed = true
        customer.customerLevel.save()

        Card card = Card.findByCustomer(customer)
        cardService.activateCard(card, '123456')
      }
    }
  }

  Map getUserAccountInformation(Integer index) {
    if (index > User.count()) {
      return null
    }

    User user = User.list().getAt(index - 1)

    Branch branch = Branch.first()
    CardType cardType = CardType.first()
    CustomerLevel customerLevel = CustomerLevel.first()

    AccountType checkingType = AccountType.findByName(AccountTypes.CHECKING.value)
    AccountType savingsType = AccountType.findByName(AccountTypes.SAVINGS.value)

    Account checkingAccount = accountService.findAccounts(user, checkingType).getAt(0)
    Account savingsAccount = accountService.findAccounts(user, savingsType).getAt(0)

    Customer checkingCustomer = Customer.findByUserAndAccount(user, checkingAccount)
    Customer savingsCustomer = Customer.findByUserAndAccount(user, savingsAccount)

    CustomerLevel checkingCustomerLevel = checkingCustomer.customerLevel
    CustomerLevel savingsCustomerLevel = savingsCustomer.customerLevel

    Card checkingCard = Card.findByCustomer(checkingCustomer)
    Card savingsCard = Card.findByCustomer(savingsCustomer)

    return [
      user                 : user,
      branch               : branch,
      cardType             : cardType,
      customerLevel        : customerLevel,
      checkingAccount      : checkingAccount,
      savingsAccount       : savingsAccount,
      checkingCustomer     : checkingCustomer,
      savingsCustomer      : savingsCustomer,
      checkingCustomerLevel: checkingCustomerLevel,
      savingsCustomerLevel : savingsCustomerLevel,
      checkingCard         : checkingCard,
      savingsCard          : savingsCard,
      checkingType         : checkingType,
      savingsType          : savingsType
    ]
  }

  Map createCheckingAndSavingsAccount() {
    createCheckingAndSavingsAccounts(1)
    getUserAccountInformation(1)
  }
}
