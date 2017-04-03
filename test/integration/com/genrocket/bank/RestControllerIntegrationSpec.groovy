package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.AccountTypeTestDataLoader
import com.genrocket.bank.testDataLoader.BranchTestDataLoader
import com.genrocket.bank.testDataLoader.CardPoolTestDataLoader
import com.genrocket.bank.testDataLoader.CardTypeTestDataLoader
import com.genrocket.bank.testDataLoader.CustomerLevelTestDataLoader
import com.genrocket.bank.testDataLoader.TransactionTypeTestDataLoader
import grails.test.spock.IntegrationSpec

class RestControllerIntegrationSpec extends IntegrationSpec {
  def transactionCreatorService
  def messageSource

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

  def "test makeDeposit for TRANSACTION_COMPLETE"() {
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
    restController.request.json = [pin: "123456", cardNumber: card.cardNumber, amount: depositAmount]
    restController.makeDeposit()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("transaction.complete", null, null)
    card.customer.account.balance == balance + depositAmount
  }

  def "test makeDeposit not TRANSACTION_COMPLETE"() {
    given:
    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['savingsCard']

    Float depositAmount = 0.00

    when:
    RestController restController = new RestController()
    restController.request.json = [pin: "123456", cardNumber: card.cardNumber, amount: depositAmount]
    restController.makeDeposit()

    then:
    restController.response.json.transactionStatus == messageSource.getMessage("invalid.amount.value", null, null)
  }
}
