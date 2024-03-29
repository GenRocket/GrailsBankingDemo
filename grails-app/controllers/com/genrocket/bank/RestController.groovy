package com.genrocket.bank

import com.genrocket.bank.co.LoginCO
import com.genrocket.bank.co.TransferCO
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
  def userService

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

    String pin = map.deposit.pin
    String cardNumber = map.deposit.cardNumber
    Float amount = map.deposit.amount?.toFloat()
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

    render([transactionStatus: message(code: "${transactionStatus}")] as JSON)
  }

  def makeWithdrawal() {
    Map map = request.JSON as Map

    String pin = map.withdrawal.pin
    String cardNumber = map.withdrawal.cardNumber
    Float amount = map.withdrawal.amount?.toFloat()
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

    render([transactionStatus: message(code: "${transactionStatus}")] as JSON)
  }

  def makeTransfer() {
    Map map = request.JSON as Map

    String pin = map.transfer.pin
    String fromCardNumber = map.transfer.fromCardNumber
    String toCardNumber = map.transfer.toCardNumber
    Float amount = map.transfer.amount?.toFloat()
    LoginCO loginCO = new LoginCO(pin: pin, cardNumber: fromCardNumber)

    TransactionStatus transactionStatus = TransactionStatus.INVALID_PIN_NUMBER

    if (loginCO.validate()) {
      Card fromCard = Card.findByCardNumber(fromCardNumber)
      Card toCard = Card.findByCardNumber(toCardNumber)

      Customer fromCustomer = fromCard.customer
      Customer toCustomer = toCard.customer

      Account fromAccount = fromCustomer.account
      Account toAccount = toCustomer.account

      TransferCO transferCO = new TransferCO(accountNumber: toAccount?.accountNumber, currentAccountNumber: fromAccount?.accountNumber)

      if (transferCO.validate()) {
        User user = fromCustomer.user
        if (fromAccount.accountType.name == AccountTypes.CHECKING.value) {
          if (toAccount.accountType.name == AccountTypes.CHECKING.value) {
            transactionStatus = checkingService.transferCheckingToChecking(user, fromAccount, toAccount, amount)
          } else {
            transactionStatus = checkingService.transfer(user, fromAccount, toAccount, amount)
          }
        } else {
          if (toAccount.accountType.name == AccountTypes.CHECKING.value) {
            transactionStatus = savingsService.transfer(user, fromAccount, toAccount, amount)
          } else {
            transactionStatus = savingsService.transferSavingsToSavings(user, fromAccount, toAccount, amount)
          }
        }
      } else {
        transactionStatus = TransactionStatus.INVALID_ACCOUNT_NUMBER
      }
    }

    render([transactionStatus: message(code: "${transactionStatus}")] as JSON)
  }

  def openAccount() {
    Map map = request.JSON as Map

    String pin = map.openAccount.pin
    CustomerLevel customerLevel = CustomerLevel.findByName(map.openAccount.customerLevel)
    Float checking = map.openAccount.checking?.toFloat()
    Float savings = map.openAccount.savings?.toFloat()
    Branch branch = Branch.findByBranchCode(map.openAccount.branchCode)
    String suffix = map.openAccount.suffix?.trim()

    User user = new User()
    user.title = map.openAccount.title
    user.firstName = map.openAccount.firstName
    user.lastName = map.openAccount.lastName
    user.middleInitial = map.openAccount.middleInitial
    user.suffix = suffix?.size() == 0 ? null : suffix
    user.username = map.openAccount.username
    user.emailAddress = map.openAccount.emailAddress
    user.phoneNumber = map.openAccount.phoneNumber

    TransactionStatus transactionStatus = accountService.openAccountWithDeposit(user, branch, customerLevel, checking, savings, pin)

    render([transactionStatus: message(code: "${transactionStatus}")] as JSON)
  }

  def createUser() {
    Map map = request.JSON as Map

    User user = new User()
    user.title = map.title
    user.firstName = map.first_name
    user.lastName = map.last_name
    user.middleInitial = map.middle_initial
    user.suffix = map.suffix.trim() ?: null
    user.username = map.username
    user.emailAddress = map.email_address
    user.phoneNumber = map.phone_number

    userService.save(user)

    render([success: true] as JSON)
  }
}
