package com.genrocket.bank.testUtil

import com.genrocket.bank.Account
import com.genrocket.bank.AccountType
import com.genrocket.bank.AccountTypes
import com.genrocket.bank.Branch
import com.genrocket.bank.Card
import com.genrocket.bank.CardType
import com.genrocket.bank.Customer
import com.genrocket.bank.CustomerLevel
import com.genrocket.bank.User
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

  void createCheckingAndSavingsAccount(Integer count) {
    userTestDataService.loadData(count)
    cardPoolTestDataService.loadData(100)
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
        cardService.activateCard(card, 123456)
      }
    }
  }

  Map createCheckingAndSavingsAccount() {
    createCheckingAndSavingsAccount(1)

    AccountType checkingType = AccountType.findByName(AccountTypes.CHECKING.value)
    AccountType savingsType = AccountType.findByName(AccountTypes.SAVINGS.value)
    User user = User.first()

    Account checkingAccount = Account.findByAccountType(checkingType)
    Account savingsAccount = Account.findByAccountType(savingsType)

    Customer checkingCustomer = Customer.findByUserAndAccount(user, checkingAccount)
    Customer savingsCustomer = Customer.findByUserAndAccount(user, savingsAccount)

    CustomerLevel checkingCustomerLevel = checkingCustomer.customerLevel
    CustomerLevel savingsCustomerLevel = savingsCustomer.customerLevel

    Card checkingCard = Card.findByCustomer(checkingCustomer)
    Card savingsCard = Card.findByCustomer(savingsCustomer)

    return [
      user: user,
      checkingAccount: checkingAccount,
      savingsAccount: savingsAccount,
      checkingCustomer: checkingCustomer,
      savingsCustomer: savingsCustomer,
      checkingCustomerLevel: checkingCustomerLevel,
      savingsCustomerLevel: savingsCustomerLevel,
      checkingCard: checkingCard,
      savingsCard: savingsCard
    ]
  }
}
