package com.genrocket.bank

import com.genrocket.bank.co.LoginCO
import grails.converters.JSON

class RestController {
  def branchService
  def accountService
  def savingsService
  def cardTypeService
  def cardPoolService
  def checkingService
  def accountTypeService
  def customerLevelService
  def transactionTypeService

  def createAccountType() {
    Map accountTypeMap = request.JSON as Map
    AccountType accountType = new AccountType()
    accountType.properties = accountTypeMap.accountType

    accountTypeService.save(accountType)

    render([success: true] as JSON)
  }

  def createCardPool() {
    Map cardPoolMap = request.JSON as Map
    CardPool cardPool = new CardPool()
    cardPool.properties = cardPoolMap.cardPool

    cardPoolService.save(cardPool)

    render([success: true] as JSON)
  }

  def createCardType() {
    Map cardTypeMap = request.JSON as Map
    CardType cardType = new CardType()
    cardType.properties = cardTypeMap.cardType

    cardTypeService.save(cardType)

    render([success: true] as JSON)
  }

  def createCustomerLevel() {
    Map customerLevelMap = request.JSON as Map
    CustomerLevel customerLevel = new CustomerLevel()
    customerLevel.properties = customerLevelMap.customerLevel

    customerLevelService.save(customerLevel)

    render([success: true] as JSON)
  }

  def createTransactionType() {
    Map transactionTypeMap = request.JSON as Map
    TransactionType transactionType = new TransactionType()
    transactionType.properties = transactionTypeMap.transactionType

    transactionTypeService.save(transactionType)

    render([success: true] as JSON)
  }

  def createBranch() {
    Map branchMap = request.JSON as Map
    Branch branch = new Branch()
    branch.properties = branchMap.branch

    branchService.save(branch)

    render([success: true] as JSON)
  }

  def makeDeposit() {
    Map map = request.JSON as Map

    String pin = map.pin
    String cardNumber = map.cardNumber
    Float amount = Float.parseFloat(map.amount)
    LoginCO loginCO = new LoginCO(pin: pin, cardNumber: cardNumber)

    TransactionStatus transactionStatus = TransactionStatus.INVALID_PIN_NUMBER

    if (loginCO.validate()) {
      Card card = Card.findByCardNumber(loginCO.cardNumber)
      Customer customer = card.customer
      User user = customer.user
      Account account = customer.account
      AccountType accountType = account.accountType

      if (accountType.name == AccountTypes.CHECKING.value) {
        transactionStatus = checkingService.deposit(user, account, amount)
      } else if (accountType.name == AccountTypes.SAVINGS.value) {
        transactionStatus = savingsService.deposit(user, account, amount)
      } else {
        transactionStatus = TransactionStatus.INVALID_ACCOUNT_TYPE
      }
    }

    render([trasactionStatus: transactionStatus] as JSON)
  }

  def makeWithdrawal() {
    Map map = request.JSON as Map

    String pin = map.pin
    String cardNumber = map.cardNumber
    Float amount = Float.parseFloat(map.amount)
    LoginCO loginCO = new LoginCO(pin: pin, cardNumber: cardNumber)

    TransactionStatus transactionStatus = TransactionStatus.INVALID_PIN_NUMBER

    if (loginCO.validate()) {
      Card card = Card.findByCardNumber(loginCO.cardNumber)
      Customer customer = card.customer
      User user = customer.user
      Account account = customer.account
      AccountType accountType = account.accountType

      if (accountType.name == AccountTypes.CHECKING.value) {
        transactionStatus = checkingService.withdrawal(user, account, amount)
      } else if (accountType.name == AccountTypes.SAVINGS.value) {
        transactionStatus = savingsService.withdrawal(user, account, amount)
      } else {
        transactionStatus = TransactionStatus.INVALID_ACCOUNT_TYPE
      }
    }

    render([trasactionStatus: transactionStatus] as JSON)
  }

  def makeTransfer() {

  }

  def openAccount() {
    Map map = request.JSON as Map

    String pin = map.pin

    CustomerLevel customerLevel = CustomerLevel.findByName(map.customer)
    Float checking = Float.parseFloat(map.checking)
    Float savings = Float.parseFloat(map.savings)
    Branch branch = Branch.findByBranchCode(map.branchCode)

    User user = new User()
    user.title = map.title
    user.firstName = map.firstName
    user.lastName = map.lastName
    user.middleInitial = map.middleInitial
    user.suffix = map.suffix
    user.username = map.username
    user.emailAddress = map.emailAddress
    user.phoneNumber = map.phoneNumber

    accountService.openAccountWithDeposit(user, branch, customerLevel, checking, savings, pin)

    render([success: true] as JSON)
  }
}
