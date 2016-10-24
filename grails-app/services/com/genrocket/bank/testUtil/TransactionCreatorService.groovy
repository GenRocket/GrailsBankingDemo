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
  def accountService
  def userTestDataService
  def branchTestDataService
  def cardPoolTestDataService
  def cardTypeTestDataService
  def accountTypeTestDataService
  def customerLevelTestDataService
  def transactionTypeTestDataService

  Map createCheckingAndSavingsAccount() {
    cardPoolTestDataService.loadData(100)
    transactionTypeTestDataService.loadData()

    accountTypeTestDataService.loadData()
    AccountType checkingType = AccountType.findByName(AccountTypes.CHECKING.value)
    AccountType savingsType = AccountType.findByName(AccountTypes.SAVINGS.value)

    branchTestDataService.loadData()
    Branch branch = Branch.first()

    userTestDataService.loadData()
    User user = User.first()

    cardTypeTestDataService.loadData()
    CardType cardType = CardType.first()

    customerLevelTestDataService.loadData()
    CustomerLevel customerLevel = CustomerLevel.first()

    Account checkingAccount = accountService.save(user, branch, cardType, checkingType, customerLevel)
    Account savingsAccount = accountService.save(user, branch, cardType, savingsType, customerLevel)

    Customer checkingCustomer = Customer.findByUserAndAccount(user, checkingAccount)
    Customer savingsCustomer = Customer.findByUserAndAccount(user, savingsAccount)

    CustomerLevel checkingCustomerLevel = checkingCustomer.customerLevel
    CustomerLevel savingsCustomerLevel = savingsCustomer.customerLevel

    checkingCustomer.enabled = true
    checkingCustomer.save()

    savingsCustomer.enabled = true
    savingsCustomer.save()

    Card checkingCard = Card.findByCustomer(checkingCustomer)
    Card savingsCard = Card.findByCustomer(savingsCustomer)

    checkingCustomerLevel.dailyWithdrawalLimit = 500
    checkingCustomerLevel.monthlyMaxTransfersAllowed = 3
    checkingCustomerLevel.overdraftAllowed = true
    checkingCustomerLevel.save()

    savingsCustomerLevel.dailyWithdrawalLimit = 500
    savingsCustomerLevel.monthlyMaxTransfersAllowed = 3
    savingsCustomerLevel.overdraftAllowed = true
    savingsCustomerLevel.save()

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
