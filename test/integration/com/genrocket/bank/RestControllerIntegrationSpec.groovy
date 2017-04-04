package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.AccountTypeTestDataLoader
import com.genrocket.bank.testDataLoader.BranchTestDataLoader
import com.genrocket.bank.testDataLoader.CardPoolTestDataLoader
import com.genrocket.bank.testDataLoader.CardTypeTestDataLoader
import com.genrocket.bank.testDataLoader.CustomerLevelTestDataLoader
import com.genrocket.bank.testDataLoader.TransactionTypeTestDataLoader
import com.genrocket.bank.testDataLoader.UserTestDataLoader
import grails.test.spock.IntegrationSpec

class RestControllerIntegrationSpec extends IntegrationSpec {
  def transactionCreatorService
  def messageSource
  def branchTestDataService
  def accountTypeTestDataService
  def customerLevelTestDataService
  def cardPoolTestDataService
  def cardTypeTestDataService

  def "test createAccountType"() {
    given:
    List<LoaderDTO> loaderDTO = (LoaderDTO[]) AccountTypeTestDataLoader.load()

    when:
    RestController restController = new RestController()

    loaderDTO.each {
      restController.request.json = [accountType: it.object]
      restController.createAccountType()
    }

    then:
    AccountType.count() == 2
  }

  def "test createCardPool"() {
    given:
    List<LoaderDTO> loaderDTO = (LoaderDTO[]) CardPoolTestDataLoader.load(10)

    when:
    RestController restController = new RestController()

    loaderDTO.each {
      restController.request.json = [cardPool: it.object]
      restController.createCardPool()
    }

    then:
    CardPool.count() == 10
  }

  def "test createCardType"() {
    given:
    List<LoaderDTO> loaderDTO = (LoaderDTO[]) CardTypeTestDataLoader.load()

    when:
    RestController restController = new RestController()

    loaderDTO.each {
      restController.request.json = [cardType: it.object]
      restController.createCardType()
    }

    then:
    CardType.count() == 1
  }

  def "test createCustomerLevel"() {
    given:
    List<LoaderDTO> loaderDTO = (LoaderDTO[]) CustomerLevelTestDataLoader.load()

    when:
    RestController restController = new RestController()

    loaderDTO.each {
      restController.request.json = [customerLevel: it.object]
      restController.createCustomerLevel()
    }

    then:
    CustomerLevel.count() == 3
  }

  def "test createTransactionType"() {
    given:
    List<LoaderDTO> loaderDTO = (LoaderDTO[]) TransactionTypeTestDataLoader.load()

    when:
    RestController restController = new RestController()

    loaderDTO.each {
      restController.request.json = [transactionType: it.object]
      restController.createTransactionType()
    }

    then:
    TransactionType.count() == 11
  }

  def "test createBranch"() {
    given:
    List<LoaderDTO> loaderDTO = (LoaderDTO[]) BranchTestDataLoader.load(10)

    when:
    RestController restController = new RestController()

    loaderDTO.each {
      restController.request.json = [branch: it.object]
      restController.createBranch()
    }

    then:
    Branch.count() == 10
  }

  def "test makeDeposit checking TRANSACTION_COMPLETE"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Card card = (Card) fromInfo['checkingCard']

    Account account = (Account) fromInfo['checkingAccount']

    Float balance = 500.00
    Float depositAmount = 100.00

    account.balance = balance
    account.save()

    when:
    RestController restController = new RestController()
    restController.request.json = [deposit: [pin: "123456", cardNumber: card.cardNumber, amount: depositAmount]]
    restController.makeDeposit()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("transaction.complete", null, null)
    card.customer.account.balance == balance + depositAmount
  }

  def "test makeDeposit savings TRANSACTION_COMPLETE"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Account account = (Account) fromInfo['savingsAccount']
    Card card = (Card) fromInfo['savingsCard']

    Float balance = 500.00
    Float depositAmount = 100.00

    account.balance = balance
    account.save()

    when:
    RestController restController = new RestController()
    restController.request.json =  [deposit:[pin: "123456", cardNumber: card.cardNumber, amount: depositAmount]]
    restController.makeDeposit()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("transaction.complete", null, null)
    card.customer.account.balance == balance + depositAmount
  }

  def "test makeDeposit checking not TRANSACTION_COMPLETE"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    Float depositAmount = 0.00

    when:
    RestController restController = new RestController()
    restController.request.json =  [deposit:[pin: "123456", cardNumber: card.cardNumber, amount: depositAmount]]
    restController.makeDeposit()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("invalid.amount.value", null, null)
  }

  def "test makeDeposit savings not TRANSACTION_COMPLETE"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['savingsCard']

    Float depositAmount = 0.00

    when:
    RestController restController = new RestController()
    restController.request.json =  [deposit:[pin: "123456", cardNumber: card.cardNumber, amount: depositAmount]]
    restController.makeDeposit()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("invalid.amount.value", null, null)
  }

  def "test makeWithdrawal checking TRANSACTION_COMPLETE"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Account account = (Account) fromInfo['checkingAccount']
    Card card = (Card) fromInfo['checkingCard']

    Float balance = 500.00
    Float withdrawalAmount = 100.00

    account.balance = balance
    account.save()

    when:
    RestController restController = new RestController()
    restController.request.json = [withdrawal:[pin: "123456", cardNumber: card.cardNumber, amount: withdrawalAmount]]
    restController.makeWithdrawal()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("transaction.complete", null, null)
    card.customer.account.balance == balance - withdrawalAmount
  }

  def "test makeWithdrawal savings TRANSACTION_COMPLETE"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Account account = (Account) fromInfo['savingsAccount']
    Card card = (Card) fromInfo['savingsCard']

    Float balance = 500.00
    Float withdrawalAmount = 100.00

    account.balance = balance
    account.save()

    when:
    RestController restController = new RestController()
    restController.request.json = [withdrawal:[pin: "123456", cardNumber: card.cardNumber, amount: withdrawalAmount]]
    restController.makeWithdrawal()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("transaction.complete", null, null)
    card.customer.account.balance == balance - withdrawalAmount
  }

  def "test makeWithdrawal checking not TRANSACTION_COMPLETE"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Card card = (Card) fromInfo['checkingCard']

    Float withdrawalAmount = 0.00

    when:
    RestController restController = new RestController()
    restController.request.json = [withdrawal:[pin: "123456", cardNumber: card.cardNumber, amount: withdrawalAmount]]
    restController.makeWithdrawal()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("invalid.amount.value", null, null)
  }

  def "test makeWithdrawal savings not TRANSACTION_COMPLETE"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)
    Card card = (Card) fromInfo['savingsCard']

    Float withdrawalAmount = 0.00

    when:
    RestController restController = new RestController()
    restController.request.json = [withdrawal:[pin: "123456", cardNumber: card.cardNumber, amount: withdrawalAmount]]
    restController.makeWithdrawal()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("invalid.amount.value", null, null)
  }

  void "test invalid amount for transfer"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']
    Card toCard = (Card) toInfo['checkingCard']

    when:
    RestController restController = new RestController()
    restController.request.json = [transfer: [pin: "123456", fromCardNumber: card.cardNumber, toCardNumber: toCard?.cardNumber, amount: null]]
    restController.makeTransfer()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("invalid.amount.value", null, null)
  }

  void "test transfer not complete because of withdrawal limit"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    CustomerLevel customerLevel = (CustomerLevel) fromInfo['checkingCustomerLevel']
    customerLevel.dailyWithdrawalLimit = 500
    customerLevel.save()

    Account account = (Account) fromInfo['checkingAccount']
    account.balance = 5000
    account.save()

    Card toCard = (Card) toInfo['checkingCard']

    when:
    RestController restController = new RestController()
    restController.request.json = [transfer: [pin: "123456", fromCardNumber: card.cardNumber, toCardNumber: toCard.cardNumber, amount: 1000]]
    restController.makeTransfer()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("withdrawal.limit.reached", null, null)
  }

  void "test transfer from Checking to Checking"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    CustomerLevel customerLevel = (CustomerLevel) fromInfo['checkingCustomerLevel']
    customerLevel.dailyWithdrawalLimit = 2000
    customerLevel.save()

    Account account = (Account) fromInfo['checkingAccount']
    account.balance = 5000
    account.save()

    Card toCard = (Card) toInfo['checkingCard']

    when:
    RestController restController = new RestController()
    restController.request.json = [transfer: [pin: "123456", fromCardNumber: card.cardNumber, toCardNumber: toCard.cardNumber, amount: 1000]]
    restController.makeTransfer()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("transaction.complete", null, null)
  }

  void "test transfer from Checking to Savings"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    CustomerLevel customerLevel = (CustomerLevel) fromInfo['checkingCustomerLevel']
    customerLevel.dailyWithdrawalLimit = 2000
    customerLevel.save()

    Account account = (Account) fromInfo['checkingAccount']
    account.balance = 5000
    account.save()

    Card toCard = (Card) toInfo['savingsCard']

    when:
    RestController restController = new RestController()
    restController.request.json = [transfer: [pin: "123456", fromCardNumber: card.cardNumber, toCardNumber: toCard.cardNumber, amount: 1000]]
    restController.makeTransfer()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("transaction.complete", null, null)
  }

  void "test transfer from Savings to Checking"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['savingsCard']

    CustomerLevel customerLevel = (CustomerLevel) fromInfo['savingsCustomerLevel']
    customerLevel.dailyWithdrawalLimit = 2000
    customerLevel.save()

    Account account = (Account) fromInfo['savingsAccount']
    account.balance = 5000
    account.save()

    Card toCard = (Card) toInfo['checkingCard']

    when:
    RestController restController = new RestController()
    restController.request.json = [transfer: [pin: "123456", fromCardNumber: card.cardNumber, toCardNumber: toCard.cardNumber, amount: 1000]]
    restController.makeTransfer()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("transaction.complete", null, null)
  }

  void "test transfer from Savings to Savings"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['savingsCard']

    CustomerLevel customerLevel = (CustomerLevel) fromInfo['savingsCustomerLevel']
    customerLevel.dailyWithdrawalLimit = 2000
    customerLevel.save()

    Account account = (Account) fromInfo['savingsAccount']
    account.balance = 5000
    account.save()

    Card toCard = (Card) toInfo['savingsCard']

    when:
    RestController restController = new RestController()
    restController.request.json = [transfer: [pin: "123456", fromCardNumber: card.cardNumber, toCardNumber: toCard.cardNumber, amount: 1000]]
    restController.makeTransfer()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("transaction.complete", null, null)
  }

  def "test openCheckingAccount"() {
    given:
    branchTestDataService.loadData()
    accountTypeTestDataService.loadData()
    customerLevelTestDataService.loadData()
    cardPoolTestDataService.loadData(10)
    cardTypeTestDataService.loadData()

    List<LoaderDTO> loaderDTO = (LoaderDTO[]) UserTestDataLoader.load(1)
    User user = (User) loaderDTO.first()?.object

    Branch branch = Branch.first()
    CustomerLevel customerLevel = CustomerLevel.first()

    when:
    RestController restController = new RestController()
    Map openAccount = [openAccount: [pin: "123456", customerLevel: customerLevel.name, checking: 1000, branchCode: branch.branchCode]]
    openAccount["openAccount"].putAll(toMap(user))
    restController.request.json = openAccount
    restController.openAccount()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("transaction.complete", null, null)
  }

  private Map toMap(object) {
    return object?.properties.findAll { (it.key != 'class') }.collectEntries {
      it.value == null || it.value instanceof Serializable ? [it.key, it.value] : [it.key, toMap(it.value)]
    }
  }
}
