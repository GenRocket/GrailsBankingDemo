package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.AccountTestDataLoader
import grails.test.spock.IntegrationSpec

/**
 * Created by htaylor on 10/16/16.
 */
class AccountServiceCustomIntegrationSpec extends IntegrationSpec {
  def accountService
  def accountTypeTestDataService
  def branchTestDataService
  def cardTypeTestDataService
  def customerLevelTestDataService
  def userTestDataService
  def cardPoolTestDataService

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
}
