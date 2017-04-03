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
}
